package com.enation.app.shop.snapshot.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.vo.GoodsParamsList;
import com.enation.app.shop.snapshot.model.po.Snapshot;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

/**
 * 
 * 商品快照VO类
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年10月27日 下午2:40:10
 */
public class SnapshotVo {
		
		/**快照*/
		private Snapshot snapshot;
	
		/**参数list*/
		private List<GoodsParamsList> params_list ;
		
		/**相册list*/
		private List<GoodsGallery> gallery_list ;
		
		/**规格list*/
		private List<SpecValue> spec_list ;
		
		public SnapshotVo() {
			
		}
		
		public SnapshotVo(Snapshot snapshot) {
			Gson gson = new Gson();
			List<GoodsParamsList> paramsLists = gson.fromJson(snapshot.getParams_json(), new TypeToken<List<GoodsParamsList>>() {}.getType());
			List<GoodsGallery> imgLists = gson.fromJson(snapshot.getImg_json(), new TypeToken<List<GoodsGallery>>() {}.getType());
			
			this.snapshot = snapshot;
			this.params_list = paramsLists;
			this.gallery_list = imgLists;
		}

		public List<GoodsParamsList> getParams_list() {
			return params_list;
		}

		public void setParams_list(List<GoodsParamsList> params_list) {
			this.params_list = params_list;
		}

		public List<GoodsGallery> getGallery_list() {
			return gallery_list;
		}

		public void setGallery_list(List<GoodsGallery> gallery_list) {
			this.gallery_list = gallery_list;
		}

		public List<SpecValue> getSpec_list() {
			return spec_list;
		}

		public void setSpec_list(List<SpecValue> spec_list) {
			this.spec_list = spec_list;
		}

		public Snapshot getSnapshot() {
			return snapshot;
		}

		public void setSnapshot(Snapshot snapshot) {
			this.snapshot = snapshot;
		}
		
		
}
