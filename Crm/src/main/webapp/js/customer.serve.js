$(function () {
    loadCustomerServeType()
})
function loadCustomerServeType() {
    $.ajax({
        type:'post',
        url:ctx+"/customer_serve/queryCustomerServeType",
        dataType:'json',
        success:function (data) {
            if(data.code==200){
                var datas=data.datas;
                var types=data.types;
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));
                // 指定图表的配置项和数据
                option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        //['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
                        data:types
                    },
                    series: [
                        {
                            name:'访问来源',
                            type:'pie',
                            radius: ['50%', '70%'],
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            //{value:335, name:'直接访问'},{value:310, name:'邮件营销'},
                            data:datas
                        }
                    ]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }else {
                $.messager.alert("来自crm","暂无数据","info");
            }
        }
    });
}