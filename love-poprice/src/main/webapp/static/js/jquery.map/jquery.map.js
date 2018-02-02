/**
 * 模拟java集合中Map的对象，方便用于存储key-value数据，使用方法如：
 * 
 * $(function() {
 * 	$.Map.put('key', 'value');
 * 	alert($.Map.get('key'));
 * });
 * 
 * 
 * @author lianshisheng@126.com
 * @date 2015-02-03
 */
jQuery.Map = {
	// 数据存储对象
	datamap : {},

	// 根据key从Map中获取值
	get : function(key) {
		return this.datamap[key];
	},

	// 向Map中设置值
	put : function(key, value) {
		this.datamap[key] = value;
	},

	// 获取Map中元素个数
	size : function() {
		return this.keys().length;
	},
	
	//根据key删除Map中元素
	remove : function(key) {
		delete this.datamap[key];
	},

	//判断map是否为空，如果为空则返回true，否则返回false
	isEmpty : function() {
		return this.size() == 0;
	},

	//判断map中是否存在指定的key，如果存在返回true，否则返回false
	containsKey : function(key) {
		return key in this.datamap;
	},

	//判断map中是否存在指定的value，如果存在返回true，否则返回false
	containsValue : function(value) {
		return this.values().indexOf(value) > -1;
	},

	//清空map中所有元素
	clear : function() {
		for (key in this.datamap) {
			delete this.datamap[key];
		}
	},

	//遍历map中所有key，返回所有key组成的数组
	keys : function() {
		keys = [];
		for (key in this.datamap) {
			keys.push(key);
		}
		return keys;
	},

	//遍历map中所有value，返回所有value组成的数组
	values : function() {
		values = [];
		for (key in this.datamap) {
			values.push(this.datamap[key]);
		}
		return values;
	}
};
