package gt.edu.umg.ingenieria.sistemas.laboratorio1.service;

import gt.edu.umg.ingenieria.sistemas.laboratorio1.dao.ClientRepository;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.model.Client;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author Josué Barillas (jbarillas)
 */
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;    
  
     public List<Client> buscarTodos() {
        return (List<Client>) this.clientRepository.findAll();
    }
    
    
    public Client buscarNit(String criterio) {
        return this.clientRepository.findClientByNit(criterio);
    }
    
    public Client buscarId(long id) {
        return this.clientRepository.findClientById(id);
    }
    
    public List<Client> buscarNombres(String nombre, String apellido) {
        return (List<Client>) this.clientRepository.findClientByFirstNameAndLastName(nombre, apellido);
    }
    
    public List<Client> buscarApellido(String apellido) {
        return (List<Client>) this.clientRepository.findClientByLastName(apellido);
    }
    
    public List<Client> buscarNombre(String nombre) {
        return (List<Client>) this.clientRepository.findClientByFirstName(nombre);
    }
    
   
    public Client nuevo(Client cliente) {               
        return this.clientRepository.save(cliente);
    }
    
    public Client actualizar(Client cliente) {
        return this.clientRepository.save(cliente);        
    }    
    
    
    

}
