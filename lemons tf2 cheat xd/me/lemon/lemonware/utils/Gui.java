// 
// Decompiled by Procyon v0.5.36
// 

package me.lemon.lemonware.utils;

import java.util.Iterator;
import me.lemon.lemonware.feature.Feature;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import me.lemon.lemonware.Main;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.LayoutManager;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JFrame;

public class Gui
{
    private JFrame externalGui;
    public JSlider fov;
    public JComboBox aimkey;
    public JComboBox bone;
    public JCheckBox smooth;
    public JCheckBox autoShoot;
    public JSlider speed;
    
    public Gui() {
        this.initialize();
    }
    
    private void initialize() {
        (this.externalGui = new JFrame()).setResizable(false);
        this.externalGui.setTitle("lemon tf2 cheat Lol XDD");
        this.externalGui.setDefaultCloseOperation(3);
        this.externalGui.setBounds(100, 100, 415, 242);
        final JTabbedPane tabbedPane = new JTabbedPane(1);
        final GroupLayout groupLayout = new GroupLayout(this.externalGui.getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabbedPane, -1, 946, 32767));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabbedPane, -1, 555, 32767));
        final JPanel panel = new JPanel();
        tabbedPane.addTab("aimbot", null, panel, null);
        panel.setLayout(null);
        final JCheckBox chckbxEnabled = new JCheckBox("enabled");
        chckbxEnabled.setBounds(10, 7, 97, 23);
        chckbxEnabled.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                Main.INSTANCE.getFeatureManager().getFeature("aimbot").setEnabled(((JCheckBox)e.getSource()).isSelected());
            }
        });
        panel.add(chckbxEnabled);
        final JLabel lblFov = new JLabel("fov:");
        lblFov.setHorizontalAlignment(2);
        lblFov.setBounds(14, 32, 400, 14);
        panel.add(lblFov);
        (this.fov = new JSlider()).setMaximum(360);
        this.fov.setBounds(3, 45, 395, 50);
        this.fov.setMajorTickSpacing(45);
        this.fov.setMinorTickSpacing(5);
        this.fov.setPaintTicks(true);
        this.fov.setPaintLabels(true);
        this.fov.setValue(90);
        panel.add(this.fov);
        final JLabel lblKey = new JLabel("key:");
        lblKey.setBounds(14, 99, 46, 14);
        panel.add(lblKey);
        (this.aimkey = new JComboBox()).setModel(new DefaultComboBoxModel<String>(new String[] { "Mouse1", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        this.aimkey.setEditable(true);
        this.aimkey.setSelectedIndex(3);
        this.aimkey.setBounds(12, 117, 80, 20);
        panel.add(this.aimkey);
        (this.autoShoot = new JCheckBox("autoshoot")).setBounds(110, 11, 106, 14);
        panel.add(this.autoShoot);
        final JLabel lblbn = new JLabel("bone:");
        lblbn.setBounds(14, 99, 46, 14);
        (this.bone = new JComboBox()).setModel(new DefaultComboBoxModel<EnumBone>(EnumBone.values()));
        this.bone.setEditable(true);
        this.bone.setSelectedIndex(0);
        this.bone.setBounds(12, 117, 80, 20);
        final JPanel panel_1 = new JPanel();
        tabbedPane.addTab("visuals", null, panel_1, null);
        panel_1.setLayout(null);
        int Lol = 7;
        for (final Feature feature : Main.INSTANCE.getFeatureManager().getFeatures()) {
            if (feature.getType() == Feature.FeatureType.VISUAL) {
                final JCheckBox checkbox = new JCheckBox(feature.getName());
                checkbox.setBounds(10, Lol, 427, 23);
                checkbox.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(final ChangeEvent e) {
                        feature.setEnabled(((JCheckBox)e.getSource()).isSelected());
                    }
                });
                Lol += 24;
                panel_1.add(checkbox);
            }
        }
        final JPanel panel_2 = new JPanel();
        tabbedPane.addTab("other", null, panel_2, null);
        panel_2.setLayout(null);
        int Lol2 = 7;
        for (final Feature feature2 : Main.INSTANCE.getFeatureManager().getFeatures()) {
            if (feature2.getType() == Feature.FeatureType.OTHER) {
                final JCheckBox checkbox2 = new JCheckBox(feature2.getName());
                checkbox2.setBounds(10, Lol2, 427, 23);
                checkbox2.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(final ChangeEvent e) {
                        feature2.setEnabled(((JCheckBox)e.getSource()).isSelected());
                    }
                });
                Lol2 += 24;
                panel_2.add(checkbox2);
            }
        }
        this.externalGui.getContentPane().setLayout(groupLayout);
        this.externalGui.setVisible(true);
    }
}
