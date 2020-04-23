package com.org.miuv;

import com.org.models.Apartado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alsorc
 */
public class DaoApartados implements IDao<Apartado>{

    private transient  Connection driverPostgres;
    private boolean successQuery = false;
    private transient  PreparedStatement preQuery;

    public DaoApartados() {
        driverPostgres = ConnectionToDb.getInstance().getDriver();
    }
    
    private String getStatement(int statementOption){
        String[] statements = new String[]{
                            "INSERT INTO apartados (matricula, id_equipo, id_lugar, grupo, fecha, hora_inicio, hora_final) VALUES (?,?,?,?,?,?,?);",
                            "DELETE FROM apartados WHERE (id_apartado = ?);",
                            "UPDATE apartados SET matricula = ?, id_equipo = ?, id_lugar = ?, grupo = ?,"
                        + "fecha =?, hora_inicio = ?, hora_final = ?  WHERE (id_apartado = ?);",
                            "SELECT * FROM apartados WHERE (id_apartado = ?);",
                            "SELECT * FROM apartados;"};
            return statements[statementOption];
     }
    
    public boolean updateTable(String statement,int statementOption, String[] values) {
        try {
            preQuery = driverPostgres.prepareStatement(statement);
            switch(statementOption){
                case 0:
                    preQuery.setString(1, values[0]);
                    preQuery.setString(2, values[1]);
                    preQuery.setString(3, values[2]);
                    preQuery.setString(4, values[3]);
                    preQuery.setString(5, values[4]);
                    preQuery.setString(6, values[5]);
                    preQuery.setString(7, values[6]);
                    //preQuery.setString(8, values[7]);
                    break;
                case 1:
                    preQuery.setString(1, values[0]);
                    break;
                case 2:
                    preQuery.setString(1,values[1]);
                    preQuery.setString(2,values[2]);
                    preQuery.setString(3,values[3]);
                    preQuery.setString(4,values[4]);
                    preQuery.setString(5,values[5]);
                    preQuery.setString(6,values[6]);
                    preQuery.setString(7,values[7]);
                    preQuery.setInt(8,Integer.parseInt(values[0]));
                    break;
                default:
                    System.err.println("No elegiste una opción válida");
            }
            if(preQuery.executeUpdate()>0)
                successQuery = true;

        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return successQuery;
    }
    
     public ResultSet getData(String statement, int statementOption,int id) {
        ResultSet data = null;
        try {
            preQuery = driverPostgres.prepareStatement(statement);
            
            if ( statementOption == 3)
                preQuery.setInt(1, id);
            
            data = preQuery.executeQuery();            
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionToDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    
    
    @Override
    public boolean insertRecord(Apartado t) {
        String values [] = { t.getMatricula(), t.getIdEquipo(), t.getIdLugar(), t.getGrupo(), t.getFecha(), t.getHoraInicio(), t.getHoraFinal() };
        return updateTable(getStatement(0), 0, values);
    }

    @Override
    public boolean deleteRecord(Apartado t) {
        String values [] = {String.valueOf(t.getIdApartado())};
        return updateTable(getStatement(1), 1, values);
    }

    @Override
    public boolean updateRecord(Apartado t) {
        String values [] = {String.valueOf(t.getIdApartado()), t.getMatricula(), t.getIdEquipo(), t.getIdLugar(), t.getGrupo(), t.getFecha(), t.getHoraInicio(), t.getHoraFinal() };
        return updateTable(getStatement(2), 2, values);
    }

    @Override
    public List<Apartado> getRecords() {
        List<Apartado> listaApartados = new ArrayList();
        ResultSet data = getData(getStatement(4), 4, 0);
        try {
            while(data.next()){
                Apartado apartado = new Apartado();
                apartado.setIdApartado(data.getInt(1));
                apartado.setMatricula(data.getString(2));
                apartado.setIdEquipo(data.getString(3));
                apartado.setIdLugar(data.getString(4));
                apartado.setGrupo(data.getString(5));
                apartado.setFecha(data.getString(6));
                apartado.setHoraInicio(data.getString(7));
                apartado.setHoraFinal(data.getString(8));
                listaApartados.add(apartado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoApartados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaApartados;
    }

    @Override
    public Apartado getOneRecord(Apartado t) {
        ResultSet data = getData(getStatement(3), 3, t.getIdApartado());
        try {
            if(data.next()){
                 t.setIdApartado(data.getInt(1));
                t.setMatricula(data.getString(2));
                t.setIdEquipo(data.getString(3));
                t.setIdLugar(data.getString(4));
                t.setGrupo(data.getString(5));
                t.setFecha(data.getString(6));
                t.setHoraInicio(data.getString(7));
                t.setHoraFinal(data.getString(8));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoApartados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }
    
}