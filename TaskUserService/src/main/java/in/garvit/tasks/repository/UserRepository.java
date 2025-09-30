package in.garvit.tasks.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import in.garvit.tasks.usermodel.User;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    @Query("{email :?0}")
    User findByEmail(String email);
    

}
