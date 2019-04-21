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
public class ReportService {

    
   
    @Autowired
    private ClientRepository clientRepository;    
  
     public List<Client> buscarTodos() {
        return (List<Client>) this.clientRepository.findAll();
    }
}
