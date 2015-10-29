package cuchaz.enigma.gui.components;

import cuchaz.enigma.analysis.EntryReference;
import cuchaz.enigma.gui.ClassSelectorClassNode;
import cuchaz.enigma.gui.ClassSelectorPackageNode;
import cuchaz.enigma.gui.GuiController;
import cuchaz.enigma.mapping.IllegalNameException;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author caellian
 */
public class ClassSelectorEditMenu extends JPopupMenu {
    TreePath m_selection;
    GuiController m_controller;

    JMenuItem rename;
    JMenuItem clearMappings;

    public ClassSelectorEditMenu(GuiController controller, TreePath selection) {
        this.m_selection = selection;
        this.m_controller = controller;
        populate();
    }

    private void populate() {
        {
            rename = new JMenuItem("Rename...");
            rename.addActionListener(al -> {
                if (m_selection != null) {
                    if (m_selection.getLastPathComponent() instanceof ClassSelectorPackageNode) {
                        ClassSelectorPackageNode packageNode = (ClassSelectorPackageNode) m_selection.getLastPathComponent();

                        final String oldName = packageNode.getPackageName();
                        final String newName = JOptionPane.showInputDialog("Enter a new name:", packageNode.getPackageName());

                        renamePackage(packageNode, oldName, newName);
                    } else if (m_selection.getLastPathComponent() instanceof ClassSelectorClassNode) {

                        ClassSelectorClassNode classNode = (ClassSelectorClassNode) m_selection.getLastPathComponent();
                        EntryReference entryReference = new EntryReference<>(classNode.getClassEntry(), classNode.getClassEntry().getClassName());

                        try {
                            m_controller.rename(entryReference, JOptionPane.showInputDialog("Enter a new name:", classNode.getClassEntry().getClassName()));
                        } catch (IllegalNameException ex) {
                            m_controller.removeMapping(entryReference);
                        } catch (NullPointerException npe) {
                            //Ignore - rename canceled
                        }
                    }
                }
            });
            this.add(rename);
        }
        {
            clearMappings = new JMenuItem("Clear Mappings...");
            clearMappings.addActionListener(al -> {
                if (m_selection != null) {
                    if (m_selection.getLastPathComponent() instanceof ClassSelectorPackageNode) {
                        final int accept = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear selected mappings.\nThis can not be undone!", "Clear Mappings", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (accept == JOptionPane.YES_OPTION) {
                            ClassSelectorPackageNode packageNode = (ClassSelectorPackageNode) m_selection.getLastPathComponent();
                            reobfuscatePackage(packageNode);
                        }
                    } else if (m_selection.getLastPathComponent() instanceof ClassSelectorClassNode) {
                        final int accept = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear selected mappings.\nThis can not be undone!", "Clear Mappings", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (accept == JOptionPane.YES_OPTION) {
                            ClassSelectorClassNode classNode = (ClassSelectorClassNode) m_selection.getLastPathComponent();
                            EntryReference entryReference = new EntryReference<>(classNode.getClassEntry(), classNode.getClassEntry().getClassName());
                            m_controller.removeMapping(entryReference);
                        }
                    }
                }
            });
            this.add(clearMappings);
        }
    }

    private void reobfuscatePackage(ClassSelectorPackageNode packageNode) {
        for (int childIndex = 0; childIndex < packageNode.getChildCount(); childIndex++) {
            TreeNode child = packageNode.getChildAt(childIndex);
            if (child != null && child instanceof ClassSelectorPackageNode) {
                reobfuscatePackage((ClassSelectorPackageNode) child);
            } else if (child != null && child instanceof ClassSelectorClassNode) {
                ClassSelectorClassNode classNode = (ClassSelectorClassNode) child;
                EntryReference entryReference = new EntryReference<>(classNode.getClassEntry(), classNode.getClassEntry().getClassName());
                m_controller.removeMapping(entryReference);
            }
        }
    }

    private void renamePackage(ClassSelectorPackageNode packageNode, String oldName, String newName) {
        for (int childIndex = 0; childIndex < packageNode.getChildCount(); childIndex++) {
            TreeNode child = packageNode.getChildAt(childIndex);
            if (child != null && child instanceof ClassSelectorPackageNode) {
                renamePackage((ClassSelectorPackageNode) child, oldName, newName);
            } else if (child != null && child instanceof ClassSelectorClassNode) {
                ClassSelectorClassNode classNode = (ClassSelectorClassNode) child;
                EntryReference entryReference = new EntryReference<>(classNode.getClassEntry(), classNode.getClassEntry().getClassName());

                try {
                    m_controller.rename(entryReference, classNode.getClassEntry().getClassName().replace(oldName, newName));
                } catch (IllegalNameException ex) {
                    m_controller.removeMapping(entryReference);
                } catch (NullPointerException npe) {
                    //Ignore - rename canceled
                }
            }
        }
    }
}
