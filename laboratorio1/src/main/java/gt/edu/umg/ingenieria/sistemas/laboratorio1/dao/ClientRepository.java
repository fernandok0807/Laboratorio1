package gt.edu.umg.ingenieria.sistemas.laboratorio1.dao;

import gt.edu.umg.ingenieria.sistemas.laboratorio1.model.Client;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Josué Barillas (jbarillas)
 */

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer>{
    
    public Client findClientByNit(String numero);
    public Client findClientById(long id);
    public List<Client> findClientByFirstNameAndLastName(String nombre, String apellido);
    public List<Client> findClientByLastName(String nombre);
    public List<Client> findClientByFirstName(String nombre);
    
    
    
}
