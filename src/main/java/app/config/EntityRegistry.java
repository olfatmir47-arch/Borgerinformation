package app.config;

import entities.*;
import org.hibernate.cfg.Configuration;


public class EntityRegistry {
    private EntityRegistry(){}
    static void registerEntities(Configuration configuration){
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(City.class);
        configuration.addAnnotatedClass(Event.class);
        configuration.addAnnotatedClass(Incident.class);
        configuration.addAnnotatedClass(User.class);
    }
}
