/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 *
 * @author Pantoufle
 */


public class Model implements IModel {
    private final Session session;
    
    public Model(){
       SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
       this.session = sessionFactory.openSession(); 
    }
    
    public void die(){
        this.session.close();
    }
    
}
