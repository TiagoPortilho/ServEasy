package serveasy.view.dono;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import serveasy.control.FeedbackDAO;
import serveasy.model.Feedback;
import serveasy.view.*;

public class Feedbacks extends javax.swing.JFrame {

    public Feedbacks() {
        initComponents();
        this.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                try {
                    fillTable(new FeedbackDAO().listarFeedbacks());
                } catch (SQLException ex) {
                    new TelaErro(ex.getMessage()).setVisible(true);
                }
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent evt) {

            }
        });
    }

    private void fillTable(List<Feedback> listaFB) {

        DefaultTableModel tablePedidos = (DefaultTableModel) tblFeedbacks.getModel();

        tablePedidos.setRowCount(0);

        for (Feedback m : listaFB) {
            Object[] obj = new Object[]{
                m.getId(),
                m.getComentario()

            };
            tablePedidos.addRow(obj);
        }

    }

    private int IdValorSelecionado() {
        int row = tblFeedbacks.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um feedback para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) tblFeedbacks.getValueAt(row, 0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        btnInicio = new javax.swing.JButton();
        btnCardapio = new javax.swing.JButton();
        btnMesas = new javax.swing.JButton();
        btnFeedbaks = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        lblVersao = new javax.swing.JLabel();
        btnExcluir = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblFeedbacks = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ServEasy");

        jPanel2.setBackground(new java.awt.Color(55, 58, 71));

        jPanel1.setBackground(new java.awt.Color(225, 165, 0));

        lblTitle.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 0, 0));
        lblTitle.setText("ServEasy");

        lblSubtitle.setFont(new java.awt.Font("Microsoft JhengHei UI Light", 0, 14)); // NOI18N
        lblSubtitle.setForeground(new java.awt.Color(0, 0, 0));
        lblSubtitle.setText("Services.");

        btnInicio.setBackground(new java.awt.Color(225, 165, 0));
        btnInicio.setForeground(new java.awt.Color(0, 0, 0));
        btnInicio.setText("Início");
        btnInicio.setFocusable(false);
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

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

        btnFeedbaks.setBackground(new java.awt.Color(225, 165, 0));
        btnFeedbaks.setForeground(new java.awt.Color(0, 0, 0));
        btnFeedbaks.setText("Feedbacks");
        btnFeedbaks.setSelected(true);

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
                            .addComponent(btnInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCardapio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnMesas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFeedbaks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addComponent(btnInicio)
                .addGap(18, 18, 18)
                .addComponent(btnCardapio)
                .addGap(18, 18, 18)
                .addComponent(btnMesas)
                .addGap(18, 18, 18)
                .addComponent(btnFeedbaks)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );

        lblVersao.setForeground(new java.awt.Color(204, 204, 204));
        lblVersao.setText("ServEasy Version 1.0.0");

        btnExcluir.setBackground(new java.awt.Color(120, 22, 22));
        btnExcluir.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnExcluir.setForeground(new java.awt.Color(255, 255, 255));
        btnExcluir.setText("Excluir Feedback");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        lblPagina.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblPagina.setForeground(new java.awt.Color(204, 204, 204));
        lblPagina.setText("FeedBacks");
        lblPagina.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                lblPaginaAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        tblFeedbacks.setBackground(new java.awt.Color(225, 165, 0));
        tblFeedbacks.setForeground(new java.awt.Color(0, 0, 0));
        tblFeedbacks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id", "Feedback"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblFeedbacks);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblVersao)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblPagina)
                                .addGap(457, 457, 457))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 20, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lblPagina)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        int idSelecionado = IdValorSelecionado();
        if (idSelecionado == -1) {
            return; // Nenhuma seleção, sai da ação
        }

        int continuar = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja deletar o feedback selecionado?",
                "Confirmar Deleção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (continuar == JOptionPane.YES_OPTION) {
            try {
                new FeedbackDAO().delFeedback(idSelecionado);
                fillTable(new FeedbackDAO().listarFeedbacks());
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void lblPaginaAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblPaginaAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_lblPaginaAncestorMoved

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        new LoginScreen().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        new Inicial().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnCardapioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCardapioActionPerformed
        new CardapioDono().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnCardapioActionPerformed

    private void btnMesasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMesasActionPerformed
        new Mesas().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnMesasActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Feedbacks().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCardapio;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFeedbaks;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnMesas;
    private javax.swing.JButton btnSair;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblVersao;
    private javax.swing.JTable tblFeedbacks;
    // End of variables declaration//GEN-END:variables
}
