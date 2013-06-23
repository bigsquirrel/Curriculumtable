package com.zxl.Timetable.entity;

import java.util.HashMap;

public class GridViewItem {


		private String nickName;
		private String place;
		private String roomNum;
		public static HashMap<Integer, GridViewItem> itemCache;
		
		
		
		public GridViewItem() {
			super();
		}

		public GridViewItem(String nickName, String place, String roomNum) {
			super();
			this.nickName = nickName;
			this.place = place;
			this.roomNum = roomNum;
		} 
		
		
		public static void putToMap(int position, GridViewItem item) {
			if (itemCache.containsKey(position)) {
				return ;
			}
			itemCache.put(position, item);
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public String getRoomNum() {
			return roomNum;
		}

		public void setRoomNum(String roomNum) {
			this.roomNum = roomNum;
		}

}