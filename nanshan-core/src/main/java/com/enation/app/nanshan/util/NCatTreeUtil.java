package com.enation.app.nanshan.util;

import java.util.ArrayList;
import java.util.List;

import com.enation.app.nanshan.vo.NCatVo;

import net.sf.json.JSONArray;




/**
*
* @Description:
* @author luyanfen
* @date 2017年12月21日 上午10:53:18
*
*/
public class NCatTreeUtil {
	 List<NCatVo> nodes = new ArrayList<NCatVo>();	 
     public NCatTreeUtil(List<NCatVo> nodes) {
               super();
               this.nodes= nodes;

     }  
     /**
      * 构建JSON树形结构
      * @return
      */
     public String buildJSONTree() {
           List<NCatVo> nodeTree = buildTree();
           JSONArray jsonArray = JSONArray.fromObject(nodeTree);
           return jsonArray.toString();

     }  
     /**
      * 构建树形结构
      * @return
      */
     public List<NCatVo> buildTree() {
           List<NCatVo> treeNodes = new ArrayList<NCatVo>();

           List<NCatVo> rootNodes = getRootNodes();

           for (NCatVo rootNode : rootNodes) {
                    buildChildNodes(rootNode);
                    treeNodes.add(rootNode);
           }
           return treeNodes;
     }
     /**
      * 递归子节点
      * @param node
      */
     public void buildChildNodes(NCatVo node) {
    	 NCatVo p=this.getCatParent(node.getParentId());
		   List<NCatVo> children = getChildNodes(node); 
		   if (!children.isEmpty()) {
		        for(NCatVo child : children) {
		                 buildChildNodes(child);
		        } 
		        node.setLeafs(children);
		        node.setParent(p);
		   }
     }

     /**

      * 获取父节点下所有的子节点

      * @param nodes  

      * @return

      */

     public List<NCatVo> getChildNodes(NCatVo node) {
           List<NCatVo> childNodes = new ArrayList<NCatVo>();
           for (NCatVo n : nodes){
                if (node.getId()==n.getParentId()) {
                         childNodes.add(n);
                }
           }
           return childNodes;
     }

    

     /**
      * 判断是否为根节点
      * @return
      */

     public boolean rootNode(NCatVo node) {
           boolean isRootNode = true;
           for (NCatVo n : nodes){
                    if (node.getParentId()==n.getId()) {
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
     public List<NCatVo> getRootNodes() {
           List<NCatVo> rootNodes = new ArrayList<NCatVo>();
           NCatVo vo=new NCatVo();
           
           for (NCatVo n : nodes){
                    if (rootNode(n)) {
                             rootNodes.add(n);
                    }
           }
           return rootNodes;

     }
     public NCatVo getCatParent(long x){  

         for(int i = 0; i<nodes.size();i++){  
             if(nodes.get(i).getId()==x){
            	 return nodes.get(i);
             }
         }  
         return null;  
     }
    

   

    

     public static void main(String[] args) {              

               List<NCatVo> nodes = new ArrayList <NCatVo>();

               NCatTreeUtil treeBuilder = new NCatTreeUtil(nodes);

               System.out.println(treeBuilder.buildJSONTree());

     }

}
