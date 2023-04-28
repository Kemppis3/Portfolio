package oy.tol.tra;

public class Person implements Comparable<Person>{
    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        Person anotherPerson = (Person) obj;
        if(getFullName() == null){
            if(anotherPerson.getFullName() != null){
                return false;
            }
        }
        else if(!getFullName().equals(anotherPerson.getFullName())){
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(Person person){
        return getFullName().compareTo(person.getFullName());
    }

    @Override
    public int hashCode(){
        int hash = 2777;
        String personName = getFullName();
        for(int c = 0; c < personName.length(); c++){
            hash = hash + (hash << 6) + personName.charAt(c);
        }
        return hash;
    }


}
