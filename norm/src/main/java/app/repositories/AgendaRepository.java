package app.repositories;


import app.model.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, Integer> {

    @Query("{'id':?0}")
    public Agenda findById(String id);
}