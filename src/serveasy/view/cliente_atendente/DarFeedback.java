package serveasy.view.cliente_atendente;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import serveasy.control.FeedbackDAO;
import serveasy.control.MesasDAO;
import serveasy.view.*;

public class DarFeedback extends javax.swing.JFrame {

    private int num_mesa;

    public DarFeedback() {
        initComponents();
    }

    public DarFeedback(int i) {
        initComponents();
        this.num_mesa = i;
    }

    private boolean validarFeedback() {
        if (txtFeedback.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Seu feedback não pode estar vazio.");
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblinstrucao = new javax.swing.JLabel();
        lblPagina = new javax.swing.JLabel();
        btnEnviar = new javax.swing.JButton();
        lblVersao = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtFeedback = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        btnCardapio = new javax.swing.JButton();
        btnPedidos = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();
        btnFeedback = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ServEasy");

        jPanel2.setBackground(new java.awt.Color(55, 58, 71));

        lblinstrucao.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblinstrucao.setForeground(new java.awt.Color(204, 204, 204));
        lblinstrucao.setText("Por gentileza, deixe seu feedback aqui:");
        lblinstrucao.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                lblinstrucaoAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblPagina.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblPagina.setForeground(new java.awt.Color(204, 204, 204));
        lblPagina.setText("Dar Feedback");
        lblPagina.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                lblPaginaAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btnEnviar.setBackground(new java.awt.Color(55, 58, 71));
        btnEnviar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEnviar.setForeground(new java.awt.Color(255, 255, 255));
        btnEnviar.setText("Enviar Feedback");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        lblVersao.setForeground(new java.awt.Color(204, 204, 204));
        lblVersao.setText("ServEasy Version 1.0.0");

        txtFeedback.setBackground(new java.awt.Color(225, 165, 0));
        txtFeedback.setColumns(20);
        txtFeedback.setRows(5);
        jScrollPane1.setViewportView(txtFeedback);

        jPanel1.setBackground(new java.awt.Color(225, 165, 0));

        lblTitle.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 36)); // NOI18N
        lblTitle.setText("ServEasy");

        lblSubtitle.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        lblSubtitle.setText("Services.");

        btnCardapio.setBackground(new java.awt.Color(225, 165, 0));
        btnCardapio.setText("Cardápio");
        btnCardapio.setFocusable(false);
        btnCardapio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCardapioActionPerformed(evt);
            }
        });

        btnPedidos.setBackground(new java.awt.Color(225, 165, 0));
        btnPedidos.setText("Seus Pedidos");
        btnPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPedidosActionPerformed(evt);
            }
        });

        btnPagar.setBackground(new java.awt.Color(225, 165, 0));
        btnPagar.setText("Pagar Conta");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        btnFeedback.setBackground(new java.awt.Color(225, 165, 0));
        btnFeedback.setText("Dar Feedback");
        btnFeedback.setSelected(true);

        btnSair.setBackground(new java.awt.Color(55, 58, 71));
        btnSair.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 18)); // NOI18N
        btnSair.setForeground(new java.awt.Color(255, 255, 255));
        btnSair.setText("< Sair  ");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCardapio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPagar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFeedback, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSubtitle)
                            .addComponent(lblTitle))
                        .addGap(0, 29, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitle)
                .addGap(66, 66, 66)
                .addComponent(btnCardapio)
                .addGap(18, 18, 18)
                .addComponent(btnPedidos)
                .addGap(18, 18, 18)
                .addComponent(btnPagar)
                .addGap(18, 18, 18)
                .addComponent(btnFeedback)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblinstrucao)
                                .addComponent(lblPagina)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblVersao)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lblPagina)
                .addGap(18, 18, 18)
                .addComponent(lblinstrucao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEnviar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblVersao)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblPaginaAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblPaginaAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_lblPaginaAncestorMoved

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        if (num_mesa != 0) {
            new LoginScreen().setVisible(true);
            try {
                new MesasDAO().setOcupada(num_mesa, false);
                dispose();
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }

        } else {
            new LoginScreen().setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnSairActionPerformed

    private void lblinstrucaoAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblinstrucaoAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_lblinstrucaoAncestorMoved

    private void btnCardapioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCardapioActionPerformed
        if (num_mesa != 0) {
            new Cardapio(num_mesa).setVisible(true);
            dispose();
        } else {
            new Cardapio().setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnCardapioActionPerformed

    private void btnPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPedidosActionPerformed
        if (num_mesa != 0) {
            try {
                new SeusPedidos(num_mesa).setVisible(true);
                dispose();
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }

        } else {
            new EscolherMesa(2, 0).setVisible(true);
        }
    }//GEN-LAST:event_btnPedidosActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        if (num_mesa != 0) {
            new PagarConta(num_mesa).setVisible(true);
            dispose();
        } else {
            new PagarConta().setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        if (validarFeedback()) {
            try {
                new FeedbackDAO().addFeedback(txtFeedback.getText());
                JOptionPane.showMessageDialog(rootPane, "Feedback enviado com sucesso!");
                txtFeedback.setText("");
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }
        }
    }//GEN-LAST:event_btnEnviarActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DarFeedback.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DarFeedback.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DarFeedback.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DarFeedback.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new DarFeedback().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCardapio;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnFeedback;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnPedidos;
    private javax.swing.JButton btnSair;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVersao;
    private javax.swing.JLabel lblinstrucao;
    private javax.swing.JTextArea txtFeedback;
    // End of variables declaration//GEN-END:variables
}
