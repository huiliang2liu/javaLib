package com.xiaohei.java.lib.paras;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public abstract class Paras {

    public List<NodeTree> file2list(File file) {
        return paras(file);
    }


    public NodeTree file2nodeTree(File file) {
        List<NodeTree> nodeTrees = file2list(file);
        return nodeTrees == null || nodeTrees.size() == 0 ? null : nodeTrees
                .get(0);
    }


    public List<NodeTree> uri2list(String uri) {
        return paras(uri);
    }


    public NodeTree uri2nodeTree(String uri) {
        List<NodeTree> nodeTrees = uri2list(uri);
        return nodeTrees == null || nodeTrees.size() == 0 ? null : nodeTrees
                .get(0);
    }


    public List<NodeTree> is2list(InputStream is) {
        return paras(is);
    }


    public NodeTree is2nodeTree(InputStream is) {
        List<NodeTree> nodeTrees = is2list(is);
        return nodeTrees == null || nodeTrees.size() == 0 ? null : nodeTrees
                .get(0);
    }


    abstract List<NodeTree> paras(File file);


    abstract List<NodeTree> paras(String uri);


    abstract List<NodeTree> paras(InputStream is);


    abstract String nodeTree2string(NodeTree nodeTree);


    String list2string(List<NodeTree> nodeTrees, String father_name, Map<String, String> atts) {
        if (nodeTrees == null || father_name == null)
            return "";
        NodeTree nodeTree = new NodeTree();
        nodeTree.setChildNodeTrees(nodeTrees);
        nodeTree.setName(father_name);
        nodeTree.setAttributes(atts);
        return nodeTree2string(nodeTree);
    }

    public boolean save(String savePath, String name, NodeTree nodeTree) {
        try {
            return save(pathAname2os(savePath, name), nodeTree);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(String savePath, String name, List<NodeTree> nodeTrees, String father_name, Map<String, String> atts) {
        try {
            return save(pathAname2os(savePath, name), nodeTrees, father_name, atts);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(File file, NodeTree nodeTree) {
        try {
            return save(file2os(file), nodeTree);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(File file, List<NodeTree> nodeTrees, String father_name, Map<String, String> atts) {
        try {
            return save(file2os(file), nodeTrees, father_name, atts);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    public boolean save(String path, NodeTree nodeTree) {
        try {
            return save(path2os(path), nodeTree);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    public boolean save(String path, List<NodeTree> nodeTrees, String father_name, Map<String, String> atts) {
        try {
            return save(path2os(path), nodeTrees, father_name, atts);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    public boolean save(OutputStream os, NodeTree nodeTree) {
        try {
            return save(os, nodeTree2string(nodeTree));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }


    public boolean save(OutputStream os, List<NodeTree> nodeTrees, String father_name, Map<String, String> atts) {
        try {
            return save(os, list2string(nodeTrees, father_name, atts));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }


    public OutputStream pathAname2os(String savePath, String fileName)
            throws Exception {
        if (savePath == null || savePath.isEmpty() || fileName == null
                || fileName.isEmpty())
            return null;
        File file_path = new File(savePath);
        if (!file_path.exists())
            file_path.mkdirs();
        return path2os(savePath + "/" + fileName);
    }

    public OutputStream path2os(String path) throws Exception {
        if (path == null || path.isEmpty())
            return null;
        return new FileOutputStream(path);
    }


    public OutputStream file2os(File file) throws Exception {
        if (file == null)
            return null;
        return new FileOutputStream(file);
    }


    public boolean save(OutputStream os, String s) throws Exception {
        return save(os, s, "UTF-8");
    }


    public boolean save(OutputStream os, String s, String charsetName)
            throws Exception {
        if (s == null)
            return false;
        return save(os, s.getBytes(charsetName));
    }


    public synchronized boolean save(OutputStream os, byte[] buff)
            throws Exception {
        if (os != null) {
            if (buff != null)
                os.write(buff);
            os.flush();
            os.close();
        }
        return true;
    }
}
