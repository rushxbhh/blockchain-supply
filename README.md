# Blockchain Supply Chain

A real-time distributed blockchain network built with Spring Boot, MongoDB, and WebSockets. This implementation enables multiple nodes to maintain a synchronized blockchain through peer-to-peer communication, with support for cryptocurrency-style transactions and wallet management.

## Features

- **Real-Time Node Synchronization**: WebSocket-based P2P communication for instant block propagation across the network
- **Distributed Consensus**: Multiple nodes maintain synchronized copies of the blockchain
- **Wallet System**: Built-in cryptographic wallet functionality for secure transactions
- **Transaction Pool**: Pending transactions are collected and mined into blocks
- **Chain Validation**: Automatic validation and repair mechanisms to ensure blockchain integrity
- **MongoDB Persistence**: Reliable storage and retrieval of blockchain data
- **RESTful API**: Simple HTTP endpoints for blockchain operations

## Tech Stack

- **Backend**: Spring Boot (Java)
- **Database**: MongoDB
- **Real-Time Communication**: WebSockets (Native Spring WebSocket)
- **Build Tool**: Maven

## Prerequisites

Before running this application, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+
- Git

## Installation

1. Clone the repository:
```bash
git clone https://github.com/rushxbhh/blockchain-supply.git
cd blockchain-supply
```

2. Configure MongoDB connection in `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/blockchain_db
```

3. Build the project:
```bash
./mvnw clean install
```

4. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Blockchain Operations

#### Add Transaction
```http
POST /api/blockchain/transaction?receiverPublicKey={publicKey}&amount={amount}
```
Creates a new transaction from the node's wallet to a receiver.

**Parameters:**
- `receiverPublicKey` - Public key of the receiver
- `amount` - Amount to transfer (float)

**Response:**
```
Transaction added successfully!
```

#### Mine Block
```http
GET /api/blockchain/mine
```
Mines all pending transactions into a new block and broadcasts it to all connected nodes.

**Response:**
```
Block mined successfully!
```

#### Validate Chain
```http
GET /api/blockchain/validate
```
Validates the integrity of the entire blockchain.

**Response:**
```
✅ Blockchain is valid!
```
or
```
❌ Blockchain is invalid!
```

#### Get Full Chain
```http
GET /api/blockchain/chain
```
Returns the complete blockchain with all blocks and transactions.

**Response:**
```json
{
  "chain": [...],
  "difficulty": 4,
  "pendingTransactions": [...]
}
```

#### Repair Chain
```http
GET /api/blockchain/repair
```
Attempts to repair the blockchain if validation fails.

**Response:**
```
chain is successfully repaired
```

### Node Operations

#### Register Nodes
```http
POST /api/nodes/register
Content-Type: application/json

[
  "ws://localhost:8081/ws",
  "ws://localhost:8082/ws"
]
```
Registers and connects to other nodes in the network for real-time synchronization.

**Response:**
```
Nodes registered & connected successfully: [...]
```

## How It Works

### Transaction Flow
1. **Create Transaction**: A transaction is created using the `/transaction` endpoint with sender wallet, receiver, and amount
2. **Add to Pool**: Transaction is added to the pending transactions pool
3. **Mining**: When `/mine` is called, all pending transactions are validated and included in a new block
4. **Proof of Work**: The new block goes through proof-of-work to satisfy the difficulty requirement
5. **Broadcast**: Once mined, the block is automatically broadcast to all connected nodes via WebSocket
6. **Synchronization**: Connected nodes receive and validate the new block, adding it to their local chain

### Node Communication
- Nodes connect to each other via WebSocket
- When a block is mined, it's broadcast to all connected nodes
- Receiving nodes validate and add external blocks to maintain consensus
- Multiple instances can run simultaneously to form a distributed network

## Architecture

```
┌──────────────┐    WebSocket    ┌──────────────┐    WebSocket    ┌──────────────┐
│   Node 1     │◄───────────────►│   Node 2     │◄───────────────►│   Node 3     │
│ :8080        │                 │ :8081        │                 │ :8082        │
└──────┬───────┘                 └──────┬───────┘                 └──────┬───────┘
       │                                │                                │
       │                                │                                │
    ┌──▼────────────────────────────────▼────────────────────────────────▼──┐
    │                          MongoDB Instances                            │
    │                     (Blockchain Persistence)                          │
    └───────────────────────────────────────────────────────────────────────┘
```

## Running Multiple Nodes

To create a distributed network, run multiple instances on different ports:

**Node 1 (Port 8080):**
```bash
./mvnw spring-boot:run
```

**Node 2 (Port 8081):**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

**Node 3 (Port 8082):**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

Then register nodes with each other:
```bash
# From Node 1
curl -X POST http://localhost:8080/api/nodes/register \
  -H "Content-Type: application/json" \
  -d '["ws://localhost:8081/ws", "ws://localhost:8082/ws"]'
```

## Configuration

Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=8080

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/blockchain_db
spring.data.mongodb.database=blockchain_db

# WebSocket Configuration
spring.websocket.allowed-origins=*

# Blockchain Configuration (adjust in code)
blockchain.difficulty=4
blockchain.mining-reward=50
```

## Example Usage

### 1. Start the node
```bash
./mvnw spring-boot:run
```

### 2. Add a transaction
```bash
curl "http://localhost:8080/api/blockchain/transaction?receiverPublicKey=ABC123&amount=100"
```

### 3. Mine the block
```bash
curl "http://localhost:8080/api/blockchain/mine"
```

### 4. View the blockchain
```bash
curl "http://localhost:8080/api/blockchain/chain"
```

### 5. Validate the chain
```bash
curl "http://localhost:8080/api/blockchain/validate"
```

## Testing

Run the test suite:
```bash
./mvnw test
```

## Use Cases

- **Cryptocurrency Implementation**: Basic wallet and transaction system
- **Distributed Ledger**: Maintain synchronized records across multiple nodes
- **Supply Chain Tracking**: Track assets through a transparent, immutable chain
- **Audit Trails**: Create tamper-proof records of transactions
- **Educational Purpose**: Learn blockchain fundamentals and distributed systems

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Future Enhancements

- [ ] Enhanced consensus algorithms (Proof of Stake)
- [ ] Smart contract functionality
- [ ] Frontend dashboard for blockchain visualization
- [ ] Advanced wallet management with key storage
- [ ] Network discovery and automatic node registration
- [ ] Transaction fee mechanisms
- [ ] Block explorer interface

## Troubleshooting

**Chain validation fails:**
- Use the `/repair` endpoint to attempt automatic repair
- Check MongoDB for corrupted data
- Ensure all nodes are synchronized

**Nodes not connecting:**
- Verify WebSocket URLs are correct
- Check firewall settings
- Ensure ports are not blocked

**Transactions not mining:**
- Verify wallet has sufficient balance
- Check pending transactions pool
- Call `/mine` endpoint explicitly

## License

This project is open source and available under the [MIT License](LICENSE).

## Contact

For questions or support, please open an issue on GitHub or contact [@rushxbhh](https://github.com/rushxbhh).

---

**Note**: This is a demonstration project for educational purposes. For production use, additional security measures, proper key management, and thorough testing are essential.
