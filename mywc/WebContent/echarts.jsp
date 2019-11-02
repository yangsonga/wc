<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@include file="commons.jsp" %>
<title>报表展示</title>
</head>
<body>
 <div id="main" style="width:100%;height:400px;"></div>
 <br/>
 <hr/>
 <div id="main2" style="width:100%;height:400px;"></div>
 <!-- 引入echarts文件 -->
<script type="text/javascript" src="/mywc/statics/js/echarts.js"></script>
 <script type="text/javascript">
     $.ajax({
    	 type:'post',
    	 url:'account',
    	 data:{
    		'actionName':'queryAccountList' 
    	 },
    	 dataType:'json',
    	 success:function(data){
    		 var accountNames = new Array();
    		 var moneyArr = new Array();
    		 for(var i=0;i<data.length;i++){
    			 accountNames.push(data[i].accountName);
    			 moneyArr.push(data[i].money);
    		 }
    		 loadAccountEcharts(accountNames,moneyArr);
    	 }
     });
     $.ajax({
    	 type:'post',
    	 url:'payout',
    	 data:{
    		'actionName':'queryPayOutByUid' 
    	 },
    	 dataType:'json',
    	 success:function(data){
    		 var typeNameArr = [];
    		 var dataArr = [];
    		 for(var i=0;i<data.length;i++){
    			 typeNameArr.push(data[i].typeName);
    			 var str = {value:data[i].money,name:data[i].typeName};
    			 dataArr.push(str);
    		 }
    		 loadPayOutEcharts(typeNameArr,dataArr);
    		 }
    	 });
        function loadAccountEcharts(accountNames,moneyArr){
        	// 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '账户金额报表图',
                    left:'center'
                },
                tooltip: {},
                legend: {
                    data:['金额'],
                    left:'right'
                },
                xAxis: {
                    data: accountNames
                },
                yAxis: {},
                series: [{
                    name: '金额',
                    type: 'bar',
                    data: moneyArr
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
        
        //加载支出类型占比图
     // 基于准备好的dom，初始化echarts实例
        function loadPayOutEcharts(typeNameArr,dataArr){
        	var myChart = echarts.init(document.getElementById('main2'));
            option = {
            	    title : {
            	        text: '支出类型占比图',
            	        x:'center'
            	    },
            	    tooltip : {
            	        trigger: 'item',
            	        formatter: "{a} <br/>{b} : {c} ({d}%)"
            	    },
            	    legend: {
            	        orient: 'vertical',
            	        left: 'left',
            	        data: typeNameArr
            	    },
            	    series : [
            	        {
            	            name: '支出金额',
            	            type: 'pie',
            	            radius : '55%',
            	            center: ['50%', '60%'],
            	            data:dataArr,
            	            /*
            	             [
            	                {value:335, name:'直接访问'},
            	                {value:310, name:'邮件营销'},
            	                {value:234, name:'联盟广告'},
            	                {value:135, name:'视频广告'},
            	                {value:1548, name:'搜索引擎'}
            	            ]
            	            */
            	            itemStyle: {
            	                emphasis: {
            	                    shadowBlur: 10,
            	                    shadowOffsetX: 0,
            	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
            	                }
            	            }
            	        }
            	    ]
            	};

            myChart.setOption(option);
        }
    </script>
</body>
</html>