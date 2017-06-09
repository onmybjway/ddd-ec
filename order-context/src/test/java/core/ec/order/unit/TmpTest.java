package core.ec.order.unit;

import org.junit.Test;

import java.util.*;

public class TmpTest {

    class User {
        private String name;

        User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void name() throws Exception {

        Set<User> orders = new HashSet<>();

        orders.add(new User("tom"));
        orders.add(new User("jerry"));

        Iterator<User> iterator = orders.iterator();

        Enumeration<User> o = Collections.enumeration(orders);


  /*      while (iterator.hasNext()) {
           User next = iterator.next();
           iterator.remove();
           System.out.println(next.getName());
       }*/

        while (o.hasMoreElements()) {
            User next = o.nextElement();

            System.out.println(next.getName());
        }


  /*      for (User order : orders) {
            System.out.println(order.getName());
        }*/


    }

    @Test
    public void name2() throws Exception {

    }
}
