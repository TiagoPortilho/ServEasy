package serveasy.view.cliente_atendente;

import serveasy.control.MesasDAO;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import serveasy.control.DonoDAO;
import serveasy.control.PedidoDAO;
import serveasy.view.*;

public class PagarConta extends javax.swing.JFrame {

    private int num_mesa;

    public PagarConta() {
        initComponents();
    }

    public PagarConta(int i) {
        initComponents();
        this.num_mesa = i;
    }

    public void abrirComMesa(int numeroMesa) {
        this.num_mesa = numeroMesa;
        setVisible(true);
        toFront();
        requestFocus();
    }

    private void confirmarFecharConta() {
        if (num_mesa != 0) {
            String[] options = {"Sim", "Não"};
            int continuar = JOptionPane.showOptionDialog(rootPane,
                    "Tem certeza que fechar a conta?",
                    "Confirmar Ação",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (continuar == 0) {
                try {
                    PedidoDAO pedidoDAO = new PedidoDAO();
                    double total = pedidoDAO.fecharContaPorNumeroMesa(num_mesa);

                    if (total == 0.0) {
                        JOptionPane.showMessageDialog(this, "Não há pedidos para esta mesa.");
                        return;
                    }

                    new DonoDAO().adicionarGanhoHoje(total);
                    new MesasDAO().setPago(num_mesa, true);

                    JOptionPane.showMessageDialog(this, String.format("Conta fechada. Total: R$ %.2f", total));
                } catch (SQLException ex) {
                    new TelaErro(ex.getMessage()).setVisible(true);
                }
            }

        } else {
            new EscolherMesa(3, 0).setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblinstrucao = new javax.swing.JLabel();
        lblPagina = new javax.swing.JLabel();
        btnPix = new javax.swing.JButton();
        lblVersao = new javax.swing.JLabel();
        btnCartao = new javax.swing.JButton();
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
        lblinstrucao.setText("Selecione a forma de pagamento:");
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
        lblPagina.setText("Pagar Conta");
        lblPagina.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                lblPaginaAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btnPix.setBackground(new java.awt.Color(51, 255, 0));
        btnPix.setText("PIX");
        btnPix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPixActionPerformed(evt);
            }
        });

        lblVersao.setForeground(new java.awt.Color(204, 204, 204));
        lblVersao.setText("ServEasy Version 1.0.0");

        btnCartao.setBackground(new java.awt.Color(51, 102, 255));
        btnCartao.setText("Crédito ou Débito");
        btnCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCartaoActionPerformed(evt);
            }
        });

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
        btnPagar.setSelected(true);

        btnFeedback.setBackground(new java.awt.Color(225, 165, 0));
        btnFeedback.setText("Dar Feedback");
        btnFeedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFeedbackActionPerformed(evt);
            }
        });

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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblPagina))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(145, 145, 145)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnPix, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblinstrucao)
                                    .addComponent(btnCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 182, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblVersao)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lblPagina)
                .addGap(53, 53, 53)
                .addComponent(lblinstrucao)
                .addGap(18, 18, 18)
                .addComponent(btnPix)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCartao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblVersao)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        try {
            if (num_mesa != 0) {
                new MesasDAO().setOcupada(num_mesa, false);
            }
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        } finally {
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
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }
            dispose();
        } else {
            new EscolherMesa(2, 0).setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnPedidosActionPerformed

    private void btnFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFeedbackActionPerformed
        if (num_mesa != 0) {
            new DarFeedback(num_mesa).setVisible(true);
            dispose();
        } else {
            new DarFeedback().setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnFeedbackActionPerformed

    private void btnPixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPixActionPerformed
        confirmarFecharConta();
    }//GEN-LAST:event_btnPixActionPerformed

    private void btnCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCartaoActionPerformed
        confirmarFecharConta();
    }//GEN-LAST:event_btnCartaoActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(PagarConta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PagarConta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PagarConta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PagarConta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new PagarConta().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCardapio;
    private javax.swing.JButton btnCartao;
    private javax.swing.JButton btnFeedback;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnPedidos;
    private javax.swing.JButton btnPix;
    private javax.swing.JButton btnSair;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVersao;
    private javax.swing.JLabel lblinstrucao;
    // End of variables declaration//GEN-END:variables
}
