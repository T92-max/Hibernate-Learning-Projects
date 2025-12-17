import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class Main {
    public static void main(String[] args) {
      //Student st =new Student();
//        st.setRollNo(4);
//        st.setName("radha");
//       st.setAge(20);

        //Student su=null;

/*
        Configuration con=new Configuration();
        con.addAnnotatedClass(Student.class);
        con.configure("hibernate.con.xml");
*/

        //Session session;
        SessionFactory sf = new Configuration()
                .addAnnotatedClass(Student.class)
                .configure("hibernate.con.xml")
                .buildSessionFactory();                //con.buildSessionFactory();
        Session session = sf.openSession();
        /*
         su=session.get(Student.class,3);
        Transaction transaction = session.beginTransaction();
         session.persist(st);
         transaction.commit();
        */
       // st=session.get(Student.class,7);
       // Transaction transaction=session.beginTransaction();
//        session.persist(st);
        //session.merge(st);
       // session.remove(st);
       // transaction.commit();
        //HQL from Student where age=23
        Query query=session.createQuery("from Student where name Like 'Rose'",Student.class);
        List<Student> students=query.getResultList();

       // Student st=session.get(Student.class,3);
        System.out.println(students);



        session.close();
        sf.close();
        //System.out.println(su);
       // System.out.println(st);

    }
}
