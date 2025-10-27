package com.rushabh.Mini_blockchain.repository;

import com.rushabh.Mini_blockchain.blockchain.Block;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface blockRepository extends MongoRepository<Block , String> {
}
