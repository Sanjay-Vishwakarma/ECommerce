package com.sj.ecommerce.utils;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service
public class SequenceGeneratorService {

    private MongoTemplate mongoTemplate;

    public SequenceGeneratorService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Async
    public CompletableFuture<Integer> getNextSequence(String entityName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Query query = new Query(Criteria.where("_id").is("sequence_collection"));
                Update update = new Update().inc("sequences." + entityName, 1);

                SequenceCollection sequenceCollection = mongoTemplate.findOne(query, SequenceCollection.class);

                if (sequenceCollection != null && sequenceCollection.getSequences().containsKey(entityName)) {
                    sequenceCollection = mongoTemplate.findAndModify(query, update, SequenceCollection.class);
                    if (sequenceCollection != null) {
                        return sequenceCollection.getSequences().get(entityName) + 1;
                    } else {
                        // Fallback in case findAndModify returned null
                        sequenceCollection = mongoTemplate.findOne(query, SequenceCollection.class);
                        return sequenceCollection.getSequences().get(entityName);
                    }
                } else {
                    if (sequenceCollection == null) {
                        sequenceCollection = new SequenceCollection();
                        sequenceCollection.setId("sequence_collection");
                        sequenceCollection.setSequences(new HashMap<>());
                    }
                    sequenceCollection.getSequences().put(entityName, 1);
                    mongoTemplate.save(sequenceCollection);
                    return 1; // Return 1 as the initial value
                }
            } catch (Exception e) {
                e.printStackTrace();
                return -1; // Return a default value in case of an error
            }
        });
    }
}
