jquery.map
-----------------------------------  
  ģ��java�����е�Map���󣬷������ڴ洢key-value���ݣ�ʹ�÷������£� 

###  
	<script src="jquery.map.min.js"></script>

	<script type="text/javascript">
		$(function() {
			$.Map.put('A', 'CC');
			$.Map.put('B', 1);
			$.Map.put('C', true);
	
			alert("get������" + $.Map.get('B'));
			alert("containsKey������" + $.Map.containsKey('C'));
			alert("containsValue������" + $.Map.containsValue(2));
			alert("remove������" + $.Map.remove('C'));
			alert("size������" + $.Map.size());
			
			alert("keys������" + $.Map.keys());
			alert("values������" + $.Map.values());
			alert("���ǰ����isEmpty������" + $.Map.isEmpty());
			alert("clear������" + $.Map.clear());
			alert("��պ����isEmpty������" + $.Map.isEmpty());
		});
	</script>

###

����˵��
-----------------------------------  
* $.Map.put ��Map������ֵ
* $.Map.get ����key��Map�л�ȡֵ
* $.Map.size ��ȡMap��Ԫ�ظ��� 
* $.Map.remove ����keyɾ��Map��Ԫ��
* $.Map.isEmpty �ж�map�Ƿ�Ϊ�գ����Ϊ���򷵻�true�����򷵻�false
* $.Map.containsKey �ж�map���Ƿ����ָ����key��������ڷ���true�����򷵻�false
* $.Map.containsValue �ж�map���Ƿ����ָ����value��������ڷ���true�����򷵻�false
* $.Map.clear ���map������Ԫ��
* $.Map.keys ����map������key����������key��ɵ�����
* $.Map.values ����map������value����������value��ɵ�����
