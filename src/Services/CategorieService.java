package Services;

import Entities.Categorie;
import tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategorieService {

    public void save(Categorie categorie) {
        try{
            String request = "INSERT INTO categorie (nom, description)" +
                    " VALUES (?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            pst.setString(1, categorie.getNom());
            pst.setString(2, categorie.getDescription());
            pst.executeUpdate();
            System.out.println("Categorie Saved!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public int delete(int id) {
        try {
            String request = "Delete FROM categorie where id = " + id;
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
            return pst.executeUpdate(request);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    public void update(Categorie categorie) {
        System.out.println("IDDDDDD"+categorie.getId());
        Categorie categorie1 = findById(categorie.getId());
        System.out.println("CHECKPOINT1"+categorie1);
        if(categorie1 != null){
            try {
                String request = "UPDATE categorie set nom = ?, description = ? WHERE id = ?";
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(request);
                pst.setString(1, categorie.getNom());
                pst.setString(2, categorie.getDescription());
                pst.setInt(3, categorie1.getId());
                if(pst.executeUpdate() == 1){
                    System.out.println("categorie Updated !");
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public Categorie findById(int id){
        Categorie categorie = null;
        String req = "SELECT * FROM categorie WHERE id LIKE '"+id+"'";
        try {
            Statement statement = MyConnection.getInstance().getCnx().createStatement();
            ResultSet resultSet = statement.executeQuery(req);
            if(resultSet.next()){
                categorie = new Categorie();
                categorie.setId(resultSet.getInt("id"));
                categorie.setNom(resultSet.getString("nom"));
                categorie.setDescription(resultSet.getString("description"));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            return  null;
        }
        return categorie;
    }

    public Categorie findByNom(String nom){
        try {
            String req = "SELECT * FROM categorie  WHERE nom LIKE "+nom;
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            System.out.println(pst);
            ResultSet resultSet = pst.executeQuery(req);
            Categorie categorie = new Categorie();
            while (resultSet.next()) {
                categorie.setId(resultSet.getInt("id"));
                categorie.setNom(resultSet.getString("nom"));
                categorie.setDescription(resultSet.getString("description"));
            }
            System.out.println(categorie);
            return categorie;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            return  null;
        }
    }


    public List<Categorie> findAll(){
        List<Categorie> myList = new ArrayList<>();

        try {
            String request = "SELECT * FROM Categorie";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet resultSet = st.executeQuery(request);
            while(resultSet.next()) {
                Categorie categorie = new Categorie();
                categorie.setId(resultSet.getInt("id"));
                categorie.setNom(resultSet.getString("nom"));
                categorie.setDescription(resultSet.getString("description"));
                myList.add(categorie);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return myList;
    }
}
