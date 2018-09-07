package app.repositories;


import app.model.Associate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends MongoRepository<Associate, Integer> {
    public Associate findById(String id);
}