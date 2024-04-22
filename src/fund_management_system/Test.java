package fund_management_system;
private void displayhistory() {
    transactionalhistory history = new transactionalhistory();
    // Clear existing components in tab3
    jTabbedPane4.setComponentAt(2, null);
    
    // Add the contents of transactionalhistory.java to tab3
    jTabbedPane4.setComponentAt(2, history.getContentPane());
    
    // Refresh the tab3 content
    jTabbedPane4.validate();
    jTabbedPane4.repaint();
}
