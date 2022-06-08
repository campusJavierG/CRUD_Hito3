import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;


public class Productos {
    private JButton btnAdd;
    private JPanel Main;
    private JLabel lblProductos;
    private JTextField txtIdProducto;
    private JTextField txtNombre;
    private JTextField txtFechaEnvasado;
    private JTextField txtUnidades;
    private JTextField txtPrecio;
    private JCheckBox cbxDisponibilidad;
    private JTable table1;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btbSearch;
    private JTextField txtSearch;
    private JScrollPane table_1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Productos");
        frame.setContentPane(new Productos().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    Connection con;
    PreparedStatement pst;

    public void connect(){
      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hito3Cuestion3","root","");
          System.out.println("Success");
      } catch (ClassNotFoundException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      }
    }





    void table_load(){
        try
        {
            pst = con.prepareStatement("select * from productos");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }








    public Productos() {
        connect();
        table_load();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String nombre,fechaEnvasado;
            int idProducto,unidades;
            float precio;
            int disponible;


            nombre= txtNombre.getText();
            fechaEnvasado= txtFechaEnvasado.getText();
            idProducto= Integer.parseInt(txtIdProducto.getText());
            unidades= Integer.parseInt(txtUnidades.getText());
            precio= Float.parseFloat(txtPrecio.getText());

            if (cbxDisponibilidad.isSelected()){
                disponible=1;
                System.out.println("Seleccionado");
            }else{
                disponible=0;
            }

            try{
                pst= con.prepareStatement("insert into  Productos (idProducto, nombre, fechaEnvasado, unidades, precio, disponible) VALUES (?,?,?,?,?,?)");
                //pst= con.prepareStatement("INSERT INTO `Productos` (`idProducto`, `nombre`, `fechaEnvasado`, `unidades`, `precio`, `disponible`) VALUES (?,?,?,?,?,?);");
                pst.setInt(1,idProducto);
                pst.setString(2,nombre);
                pst.setString(3,fechaEnvasado);

                pst.setInt(4,unidades);
                pst.setFloat(5,precio);
                pst.setInt(6,1);
                pst.executeUpdate();
                table_load();
                JOptionPane.showMessageDialog(null,"Datos introducidos correctamente");
                txtNombre.setText("");
                txtIdProducto.setText("");
                txtPrecio.setText("");
                txtSearch.setText("");
                txtUnidades.setText("");
                txtFechaEnvasado.setText("");
                cbxDisponibilidad.setSelected(false);
                txtNombre.requestFocus();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            }
        });
        btbSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int idProducto = Integer.parseInt(txtSearch.getText());

                    pst = con.prepareStatement("select idProducto, nombre, fechaEnvasado, unidades, precio, disponible from productos where idProducto = ?");
                    pst.setInt(1, idProducto);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true)
                    {
                        int idProducto2=rs.getInt(1);
                        String nombre = rs.getString(2);
                        String fechaEnvasado = rs.getString(3);
                        int unidades = rs.getInt(4);
                        float precio=rs.getFloat(5);
                        int disponible=rs.getInt(6);
                        txtIdProducto.setText(String.valueOf(idProducto2));
                        txtNombre.setText(nombre);
                        txtFechaEnvasado.setText(fechaEnvasado);
                        txtUnidades.setText(String.valueOf(unidades));
                        txtPrecio.setText(String.valueOf(precio));
                        if (disponible==1){
                            cbxDisponibilidad.setSelected(true);
                        }else {
                            cbxDisponibilidad.setSelected(false);
                        }


                    }
                    else
                    {
                        txtNombre.setText("");
                        txtFechaEnvasado.setText("");
                        txtUnidades.setText(String.valueOf(""));
                        txtPrecio.setText(String.valueOf(""));
                        cbxDisponibilidad.setSelected(false);
                        JOptionPane.showMessageDialog(null,"idProducto no válido");

                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }

        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre,fechaEnvasado;
                int idProducto,unidades;
                float precio;
                int disponible;
                nombre = txtNombre.getText();
                fechaEnvasado = txtFechaEnvasado.getText();
                idProducto = Integer.parseInt(txtSearch.getText());
                unidades= Integer.parseInt(txtUnidades.getText());
                precio= Float.parseFloat(txtPrecio.getText());
                if(cbxDisponibilidad.isSelected()){
                    disponible=1;
                }else {
                    disponible=0;
                }
                idProducto= Integer.parseInt(txtIdProducto.getText());

                try {
                    pst = con.prepareStatement("UPDATE Productos SET nombre=?,fechaEnvasado=?,unidades=?,precio=?,disponible=? WHERE idProducto=?");
                    pst.setString(1, nombre);
                    pst.setString(2, fechaEnvasado);
                    pst.setInt(3, unidades);
                    pst.setFloat(4, precio);
                    pst.setInt(5, disponible);
                    pst.setInt(6, idProducto);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Update ejecutado correctamente");
                    table_load();
                    txtNombre.setText("");
                    txtIdProducto.setText("");
                    txtPrecio.setText("");
                    txtSearch.setText("");
                    txtUnidades.setText("");
                    txtFechaEnvasado.setText("");
                    cbxDisponibilidad.setSelected(false);
                    txtNombre.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }







        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProducto;
                idProducto = Integer.parseInt(txtSearch.getText());

                try {
                    pst = con.prepareStatement("delete from productos  where idProducto = ?");

                    pst.setInt(1, idProducto);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Borrado correctamente");
                    table_load();
                    txtNombre.setText("");
                    txtIdProducto.setText("");
                    txtPrecio.setText("");
                    txtSearch.setText("");
                    txtUnidades.setText("");
                    txtFechaEnvasado.setText("");
                    cbxDisponibilidad.setSelected(false);
                    txtNombre.requestFocus();
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }
            }
        });
    }
}
