package com.enation.app.nanshan.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.enation.app.nanshan.model.NanShanArticleCatVo;


public class NanShanArticleCatTreeUtil {
	 List<NanShanArticleCatVo> nodes = new ArrayList<NanShanArticleCatVo>();	 
     public NanShanArticleCatTreeUtil(List<NanShanArticleCatVo> nodes) {
               super();
               this.nodes= nodes;

     }   

     /**
      * 构建JSON树形结构
      * @return
      */
     public String buildJSONTree() {
           List<NanShanArticleCatVo> nodeTree = buildTree();
           JSONArray jsonArray = JSONArray.fromObject(nodeTree);
           return jsonArray.toString();

     }  
     /**
      * 构建树形结构
      * @return
      */

     public List<NanShanArticleCatVo> buildTree() {

               List<NanShanArticleCatVo> treeNodes = new ArrayList<NanShanArticleCatVo>();

               List<NanShanArticleCatVo> rootNodes = getRootNodes();

               for (NanShanArticleCatVo rootNode : rootNodes) {

                        buildChildNodes(rootNode);

                        treeNodes.add(rootNode);

               }
               System.out.println(JSONArray.fromObject(treeNodes).toString());
               return treeNodes;
               
              

     }
     /**
      * 递归子节点
      * @param node
      */
     public void buildChildNodes(NanShanArticleCatVo node) {
		   List<NanShanArticleCatVo> children = getChildNodes(node); 
		   if (!children.isEmpty()) {
		        for(NanShanArticleCatVo child : children) {
		                 buildChildNodes(child);
		        } 
		        node.setNotes(children);
		        
		   }
     }

     /**

      * 获取父节点下所有的子节点

      * @param nodes  

      * @return

      */

     public List<NanShanArticleCatVo> getChildNodes(NanShanArticleCatVo node) {

               List<NanShanArticleCatVo> childNodes = new ArrayList<NanShanArticleCatVo>();

               for (NanShanArticleCatVo n : nodes){

                        if (node.getCat_id()==n.getParent_id()) {

                                 childNodes.add(n);
                        }
               }
               return childNodes;

     }

    

     /**
      * 判断是否为根节点
      * @return
      */

     public boolean rootNode(NanShanArticleCatVo node) {
           boolean isRootNode = true;
           for (NanShanArticleCatVo n : nodes){
                    if (node.getParent_id()==n.getCat_id()) {
                             isRootNode= false;
                             break;
                    }

           }
           return isRootNode;

     }

    

     /**

      * 获取集合中所有的根节点

      * @param nodes

      * @return

      */

     public List<NanShanArticleCatVo> getRootNodes() {
           List<NanShanArticleCatVo> rootNodes = new ArrayList<NanShanArticleCatVo>();
           NanShanArticleCatVo vo=new NanShanArticleCatVo();
           
           for (NanShanArticleCatVo n : nodes){
                    if (rootNode(n)) {
                             rootNodes.add(n);
                    }
           }
           return rootNodes;

     }

    

   

    

     public static void main(String[] args) {

              

               List<NanShanArticleCatVo> nodes = new ArrayList <NanShanArticleCatVo>();

               NanShanArticleCatTreeUtil treeBuilder = new NanShanArticleCatTreeUtil(nodes);

               System.out.println(treeBuilder.buildJSONTree());

     }

}
