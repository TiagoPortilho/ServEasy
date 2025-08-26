package serveasy.view.dono;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import serveasy.control.DonoDAO;
import serveasy.view.*;

public class Inicial extends javax.swing.JFrame {

    public Inicial() {
        initComponents();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent evt) {
                atualizarDados();
            }
        });
    }

    private void atualizarDados() {
        try {
            lblGanhoT.setText(String.valueOf(new DonoDAO().getTotalGanho()));
            lblGanhoH.setText(String.valueOf(new DonoDAO().getGanhoHoje()));
            txtAreaNotas.setText(new DonoDAO().getNota());
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaNotas = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubTitle = new javax.swing.JLabel();
        btnInicio = new javax.swing.JButton();
        btnCardapio = new javax.swing.JButton();
        btnMesas = new javax.swing.JButton();
        btnFeedbacks = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();
        lblNotas = new javax.swing.JLabel();
        lblVersao = new javax.swing.JLabel();
        btnSalvarNotas = new javax.swing.JButton();
        pnlGanhos = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblGanhoH = new javax.swing.JLabel();
        lblGanhoT = new javax.swing.JLabel();
        btnAbrirRest = new javax.swing.JButton();
        btnFecharRest = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ServEasy");

        jPanel11.setBackground(new java.awt.Color(55, 58, 71));
        jPanel11.setForeground(new java.awt.Color(55, 58, 71));

        txtAreaNotas.setBackground(new java.awt.Color(225, 165, 0));
        txtAreaNotas.setColumns(20);
        txtAreaNotas.setRows(5);
        jScrollPane1.setViewportView(txtAreaNotas);

        jPanel4.setBackground(new java.awt.Color(225, 165, 0));

        lblTitle.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 0, 0));
        lblTitle.setText("ServEasy");

        lblSubTitle.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        lblSubTitle.setForeground(new java.awt.Color(0, 0, 0));
        lblSubTitle.setText("Services.");

        btnInicio.setBackground(new java.awt.Color(225, 165, 0));
        btnInicio.setForeground(new java.awt.Color(0, 0, 0));
        btnInicio.setText("Início");
        btnInicio.setSelected(true);

        btnCardapio.setBackground(new java.awt.Color(225, 165, 0));
        btnCardapio.setForeground(new java.awt.Color(0, 0, 0));
        btnCardapio.setText("Cardápio");
        btnCardapio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCardapioActionPerformed(evt);
            }
        });

        btnMesas.setBackground(new java.awt.Color(225, 165, 0));
        btnMesas.setForeground(new java.awt.Color(0, 0, 0));
        btnMesas.setText("Mesas");
        btnMesas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMesasActionPerformed(evt);
            }
        });

        btnFeedbacks.setBackground(new java.awt.Color(225, 165, 0));
        btnFeedbacks.setForeground(new java.awt.Color(0, 0, 0));
        btnFeedbacks.setText("Feedbacks");
        btnFeedbacks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFeedbacksActionPerformed(evt);
            }
        });

        btnSair.setBackground(new java.awt.Color(55, 58, 71));
        btnSair.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 18)); // NOI18N
        btnSair.setForeground(new java.awt.Color(255, 255, 255));
        btnSair.setText("< Sair  ");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCardapio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMesas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFeedbacks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSubTitle)
                            .addComponent(lblTitle))
                        .addGap(0, 29, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubTitle)
                .addGap(66, 66, 66)
                .addComponent(btnInicio)
                .addGap(18, 18, 18)
                .addComponent(btnCardapio)
                .addGap(18, 18, 18)
                .addComponent(btnMesas)
                .addGap(18, 18, 18)
                .addComponent(btnFeedbacks)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );

        lblPagina.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblPagina.setForeground(new java.awt.Color(204, 204, 204));
        lblPagina.setText("Bem vindo(a) ao ServEasy!");
        lblPagina.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                lblPaginaAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblNotas.setForeground(new java.awt.Color(204, 204, 204));
        lblNotas.setText("Notas:");

        lblVersao.setForeground(new java.awt.Color(204, 204, 204));
        lblVersao.setText("ServEasy Version 1.0.0");

        btnSalvarNotas.setText("Salvar");
        btnSalvarNotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarNotasActionPerformed(evt);
            }
        });

        pnlGanhos.setBackground(new java.awt.Color(225, 165, 0));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Ganhos Totais: R$");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Ganho hoje: R$");

        lblGanhoH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblGanhoH.setForeground(new java.awt.Color(0, 0, 0));
        lblGanhoH.setText("00,00");

        lblGanhoT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblGanhoT.setForeground(new java.awt.Color(0, 0, 0));
        lblGanhoT.setText("00,00");

        javax.swing.GroupLayout pnlGanhosLayout = new javax.swing.GroupLayout(pnlGanhos);
        pnlGanhos.setLayout(pnlGanhosLayout);
        pnlGanhosLayout.setHorizontalGroup(
            pnlGanhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGanhosLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlGanhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGanhosLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGanhoH))
                    .addGroup(pnlGanhosLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGanhoT)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlGanhosLayout.setVerticalGroup(
            pnlGanhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGanhosLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlGanhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblGanhoT))
                .addGap(18, 18, 18)
                .addGroup(pnlGanhosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblGanhoH))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btnAbrirRest.setBackground(new java.awt.Color(51, 178, 4));
        btnAbrirRest.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAbrirRest.setForeground(new java.awt.Color(255, 255, 255));
        btnAbrirRest.setText("Abrir Restaurante");
        btnAbrirRest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirRestActionPerformed(evt);
            }
        });

        btnFecharRest.setBackground(new java.awt.Color(55, 58, 71));
        btnFecharRest.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnFecharRest.setForeground(new java.awt.Color(255, 255, 255));
        btnFecharRest.setText("Fechar Restaurante");
        btnFecharRest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharRestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblVersao)
                        .addContainerGap())
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPagina)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(btnAbrirRest)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnFecharRest))
                                    .addComponent(pnlGanhos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnSalvarNotas)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblNotas)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 31, Short.MAX_VALUE))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(lblNotas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblPagina)
                        .addGap(26, 26, 26)
                        .addComponent(pnlGanhos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAbrirRest, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFecharRest, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalvarNotas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(lblVersao)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharRestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharRestActionPerformed
        try {
            if (new DonoDAO().getEstadoRestaurante().equals("aberto")) {
                int continuar = JOptionPane.showConfirmDialog(rootPane,
                        "Tem certeza que deseja FECHAR?",
                        "Confirmar Ação",
                        JOptionPane.YES_NO_OPTION);

                if (continuar == JOptionPane.YES_OPTION) {
                    new DonoDAO().setEstadoRestaurante("fechado");
                    new DonoDAO().adicionarGanhoTotal();
                    JOptionPane.showMessageDialog(rootPane, "O restaurante foi FECHADO!");
                    atualizarDados();
                }
            }
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        }
    }//GEN-LAST:event_btnFecharRestActionPerformed

    private void btnAbrirRestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirRestActionPerformed
        try {
            if (new DonoDAO().getEstadoRestaurante().equals("fechado")) {
                new DonoDAO().setEstadoRestaurante("aberto");
                JOptionPane.showMessageDialog(rootPane, "O restaurante foi ABERTO!");
                atualizarDados();
            }
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        }
    }//GEN-LAST:event_btnAbrirRestActionPerformed

    private void lblPaginaAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblPaginaAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_lblPaginaAncestorMoved

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new LoginScreen().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnCardapioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCardapioActionPerformed
        new CardapioDono().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnCardapioActionPerformed

    private void btnMesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesasActionPerformed
        new Mesas().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnMesasActionPerformed

    private void btnFeedbacksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFeedbacksActionPerformed
        new Feedbacks().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnFeedbacksActionPerformed

    private void btnSalvarNotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarNotasActionPerformed
        try {
            String s = txtAreaNotas.getText();
            new DonoDAO().setNota(s);
            JOptionPane.showMessageDialog(rootPane, "Suas notas foram ATUALIZADAS!");
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        }
    }//GEN-LAST:event_btnSalvarNotasActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Inicial().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirRest;
    private javax.swing.JButton btnCardapio;
    private javax.swing.JButton btnFecharRest;
    private javax.swing.JButton btnFeedbacks;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnMesas;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnSalvarNotas;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGanhoH;
    private javax.swing.JLabel lblGanhoT;
    private javax.swing.JLabel lblNotas;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JLabel lblSubTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVersao;
    private javax.swing.JPanel pnlGanhos;
    private javax.swing.JTextArea txtAreaNotas;
    // End of variables declaration//GEN-END:variables
}
