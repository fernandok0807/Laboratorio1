package gt.edu.umg.ingenieria.sistemas.laboratorio1.controller;

import com.google.gson.Gson;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.model.Client;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.service.ClientService;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.service.ReportService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Josué Barillas (jbarillas)
 */
@RestController
@RequestMapping("/clientes")
public class ClientController {

    @Autowired
    private ReportService reportService;
 
    @Autowired
    private ClientService clientService;
    
    @GetMapping(value="/test") 
    public Response test(){
        Response res = null;
        return res;
    }
    
    
    @GetMapping("/buscarTodos")
    public List<Client> getAll() {
            return this.clientService.buscarTodos();
    }
    
    @GetMapping("/buscarPorNit")
    public Client getByNit(@RequestParam(name = "nit", required = true) String nit) {
            
            int guion = nit.indexOf("-");
            if(guion>0){
                nit = nit.replace("-", "");
            }
        
            return this.clientService.buscarNit(nit);
    }
    
    @GetMapping("/buscarPorNombreApellido")
    public List<Client> getByName(@RequestParam(name = "query", required = true) String query) {
        
        List<Client> respuesta= new ArrayList<Client>();
        List<Client> aux= null;
        int separador=query.indexOf(" ");
        if(separador>0){
            respuesta = this.clientService.buscarNombres(query.substring(0,separador),query.substring(separador+1));
        }
        else{
            int comodin=query.indexOf("*");
            if(comodin>=0){
                if(comodin==0){
                    respuesta = this.clientService.buscarApellido(query.substring(1));
                }
                else{
                    if(comodin==(query.length()-1)){
                        respuesta = this.clientService.buscarNombre(query.substring(0,query.length()-1));
                    }
                    else{
                        aux = this.clientService.buscarTodos();
                    String cadena="";
                    for (int i=0; i<aux.size();i++){
                        cadena = aux.get(i).getFirstName()+aux.get(i).getLastName();
                        //System.out.println(query.substring(0,comodin)+"--"+cadena.substring(0,comodin));
                        //System.out.println(query.substring(comodin+1)+"--"+cadena.substring(comodin));
                        if(cadena.startsWith(query.substring(0,comodin)) && cadena.endsWith(query.substring(comodin+1))){
                            respuesta.add(aux.get(i));
                        }
                    }
                    }
                }
            }
            else{
                aux = this.clientService.buscarTodos();
                String cadena="";
                for (int i=0; i<aux.size();i++){
                        if(aux.get(i).getFirstName().isEmpty()==true){
                            cadena = aux.get(i).getLastName();
                        }
                        else if(aux.get(i).getLastName().isEmpty()==true){
                            cadena = aux.get(i).getFirstName();
                        }
                        else{
                            cadena = aux.get(i).getFirstName()+aux.get(i).getLastName();
                        }
                    if(cadena.equals(query)){
                        respuesta.add(aux.get(i));
                    }
                }
            }
        }
        return respuesta;
    }
    
    
    
    @PostMapping("/crearClientes")
    public String setNews(@RequestBody(required = true) List<Client> nuevo) {
        String respuesta = "";
        for (int i=0; i<nuevo.size();i++){
            respuesta = respuesta +" "+this.setNew(nuevo.get(i)) +"\n";
        }
        return respuesta;
    }
    
    
    @PostMapping("/crearCliente")
    public String setNew(@RequestBody(required = true) Client nuevo) {
        
        Client client;
        String respuesta = "";
        
        int guion = nuevo.getNit().indexOf("-");
        if(guion>0 || nuevo.getNit().length()>10){
            respuesta ="NIT Invalido";
        }
        else if(nuevo.getAge()< 18){
            respuesta ="Lo sentimos. El sistema solo permite el registro de usuarios mayores de edad.";
        }
        else{
            
            if(nuevo.getFirstName().length()>0){
                nuevo.setFirstName(nuevo.getFirstName().substring(0,1).toUpperCase()+nuevo.getFirstName().substring(1).toLowerCase());
            }
            if(nuevo.getLastName().length()>0){
                nuevo.setLastName(nuevo.getLastName().substring(0,1).toUpperCase()+nuevo.getLastName().substring(1).toLowerCase());
            }
            
            client = this.clientService.nuevo(nuevo);
            Gson json = new Gson();
            respuesta = json.toJson(client);
            
        }
        return respuesta;
    }

    
    @PutMapping("/editarCliente")
    public String setUpdate(@RequestBody(required = true) Client nuevo) {
        
        Client client;
        String respuesta = "";
        
        int guion = nuevo.getNit().indexOf("-");
        if(guion>0 || nuevo.getNit().length()>10){
            respuesta ="NIT Invalido";
        }
        else if(nuevo.getAge()< 18){
            respuesta ="Lo sentimos. El sistema solo permite el registro de usuarios mayores de edad.";
        }
        else{
            if(nuevo.getFirstName().length()>0){
                nuevo.setFirstName(nuevo.getFirstName().substring(0,1).toUpperCase()+nuevo.getFirstName().substring(1).toLowerCase());
            }
            if(nuevo.getLastName().length()>0){
                nuevo.setLastName(nuevo.getLastName().substring(0,1).toUpperCase()+nuevo.getLastName().substring(1).toLowerCase());
            }
            client = this.clientService.actualizar(nuevo);
            Gson json = new Gson();
            respuesta = json.toJson(client);
        }
        return respuesta;
    }
    
    @PutMapping("/editarCliente/{id}/{nit}")
    public Client setUpdateNit(@PathVariable(required = true) long id,@PathVariable(required = true) String nit) {
        
        Client respuesta= new Client();
        int guion = nit.indexOf("-");
        if(guion>0){
            nit = nit.replace("-", "");
        }
        respuesta = this.clientService.buscarId(id);
        respuesta.setNit(nit);
    
        return this.clientService.actualizar(respuesta);
    }
    
    @PutMapping("/editarCliente/{id}/{nombre}/{apellido}")
    public Client setUpdateNombre(@PathVariable(required = true) long id,@PathVariable(required = true) String nombre,@PathVariable(required = true) String apellido) {
        
        Client respuesta= new Client();
        
        respuesta = this.clientService.buscarId(id);
        
        if(nombre.length()>0){
            respuesta.setFirstName(nombre.substring(0,1).toUpperCase()+nombre.substring(1).toLowerCase());
        }
        if(apellido.length()>0){
            respuesta.setLastName(apellido.substring(0,1).toUpperCase()+apellido.substring(1).toLowerCase());
        }
        
        return this.clientService.actualizar(respuesta);
    }
    
    @GetMapping("/generarReporteClientes")
    public String reporte() {
        
        String html = "<h1>Reporte Clientes<h1>";
        
        String sDirectorio = "/var/www/";
        File f = new File(sDirectorio);
        File[] ficheros = f.listFiles();
        int contador=1;
        for (int x=0;x<ficheros.length;x++){
          
            if(ficheros[x].getName().endsWith(".html")){
                contador++;
                System.out.println(ficheros[x].getName());
            }
            
        }
        System.out.println(contador);
        File rep = new File("/var/www/Clientes_"+String.valueOf(contador)+".html");
        List<Client> respuesta = this.reportService.buscarTodos();
        
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(rep));
            
            html = html+" <table border style=\"width:100%\">\n" +
            "  <tr>\n" +
            "    <th>Id</th>\n" +
            "    <th>Firstname</th>\n" +
            "    <th>Lastname</th>\n" +
            "    <th>Nit</th>\n" +
            "    <th>Phone</th>\n" +
            "    <th>Address</th>\n" +                    
            "    <th>Age</th>\n" +
            "  </tr>\n";
            
        for(int i=0; i<respuesta.size();i++){
            html= html+"  <tr>\n" +
            "    <td>"+respuesta.get(i).getId()+"</td>\n" +
            "    <td>"+respuesta.get(i).getFirstName()+"</td>\n" +
            "    <td>"+respuesta.get(i).getLastName()+"</td>\n" +
            "    <td>"+respuesta.get(i).getNit()+"</td>\n" +
            "    <td>"+respuesta.get(i).getPhone()+"</td>\n" +
            "    <td>"+respuesta.get(i).getAddress()+"</td>\n" +
            "    <td>"+respuesta.get(i).getAge()+"</td>\n" +
            "  </tr>\n" ;
        }
        html = html+"</table> ";
        bw.write(html);
        bw.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return "El reporte <a href=file:///var/www/Clientes_"+String.valueOf(contador)+".html> "
                + " file:///var/www/Clientes_"+String.valueOf(contador)+".html </a> ha sido generado.";
    }
    
    
}
