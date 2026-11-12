<template>
	<div class="nf-page shifu-scheduling-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练管理</span>
			<h2 class="nf-page-hero__title">教练地图</h2>
			<p class="nf-page-hero__desc">在地图上查看教练分布与待服务数量，支持按类型、城市、教练昵称筛选；点击标注可查看待服务订单列表。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="类型">
					<el-select
						class="shifu-scheduling-page__filter-select"
						v-model="type"
						placeholder="请选择类型"
						@change="handleChange55(type)">
						<el-option v-for="item in typeNum" :key="item.id" :label="item.value" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="城市">
					<el-input
						class="shifu-scheduling-page__filter-input"
						@keydown.enter.native="handleChange55"
						clearable
						placeholder="请输入城市"
						v-model="city">
					</el-input>
				</el-form-item>
				<el-form-item label="教练昵称">
					<el-input
						class="shifu-scheduling-page__filter-input"
						@keydown.enter.native="handleChange55"
						clearable
						placeholder="请输入教练昵称"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item class="shifu-scheduling-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="handleChange55">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card shifu-scheduling-page__map-card">
			<div id="container" class="shifu-scheduling-page__map"></div>
		</div>

		<div>
			<!-- 待服务订单 --> 
			<el-dialog title="待服务订单" :visible.sync="dialogFormVisible2" center width="70%" style="z-index: 1000;">
				<!-- <div style="display: inline-block;margin: 3px;">
					<div style="position: relative;display: inline-block;margin: 3px;">
						<span>订单类型：</span>
						<el-select v-model="type" style="width:150px;margin-left: 10px;" @change="animeDat(type)">
							<el-option v-for="item in typeNum" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 3px;">
						<span>下单人用户名：</span>
						<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入下单人用户名"
							v-model="userName"></el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 3px;">
						<span>订单编号：</span>
						<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单编号"
							v-model="ordersNo"></el-input>&nbsp;&nbsp;
					</div>
				</div> -->
				<div class="shifu-scheduling-page__dialog-toolbar">
					<el-button type="primary" icon="el-icon-refresh" @click="animeDat">刷新</el-button>
					<!-- <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansDet">
						重置
					</el-button> -->

				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="ordersId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="shifu-scheduling-page__link"
								@click="updates(scope.row.userId)">{{ scope.row.userName?scope.row.userName:'未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersType" label="订单类型">
						<template slot-scope="scope">
							<span v-if="scope.row.ordersType==1">上门订单</span>
							<span v-if="scope.row.ordersType==2">到店订单</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单编号">
					</el-table-column>
					<el-table-column prop="price" label="项目价格">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠券价格">
					</el-table-column>
					<el-table-column prop="artificerMoney" label="教练收益">
					</el-table-column>
					<el-table-column prop="payMoney" label="支付金额">
					</el-table-column>
					<el-table-column prop="taxiMoney" label="打车费">
					</el-table-column>
					<el-table-column prop="addMoney" label="加钟价格">
					</el-table-column>
					<el-table-column prop="addTime" label="加钟时长">
					</el-table-column>
					<el-table-column prop="addNum" label="加钟次数">
					</el-table-column>
					<el-table-column prop="addArtificerMoney" label=" 加钟教练收益">
					</el-table-column>
					<el-table-column prop="sumArtificerMoney" label="教练总收益">
					</el-table-column>
					<el-table-column prop="sumMoney" label="订单总金额">
					</el-table-column>
					<el-table-column prop="payWay" label="支付方式">
						<template slot-scope="scope">
							<span v-if="scope.row.payWay==1">app微信</span>
							<span v-if="scope.row.payWay==2">微信公众号</span>
							<span v-if="scope.row.payWay==3">微信小程序</span>
							<span v-if="scope.row.payWay==4">支付宝</span>
							<span v-if="scope.row.payWay==5">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="联系电话">
					</el-table-column>
					<el-table-column prop="serveTime" label="服务时间" width="100">
					</el-table-column>
					<el-table-column prop="startTime" label="开始服务时间">
					</el-table-column>
					
					<el-table-column prop="endTime" label="服务地址" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.address}}</span>
						</template>
					</el-table-column>
					
					<el-table-column prop="remark" label="备注">
					</el-table-column>
					<el-table-column prop="status" label="是否加钟" fixed="right">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.status==1">是</span>
							<span style="color: #999;" v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="订单状态" fixed="right">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.status==1">待支付</span>
							<span style="color: #E87D0D;" v-if="scope.row.status==2">待服务</span>
							<span style="color: #009900;" v-if="scope.row.status==3">待评价</span>
							<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
							<span style="color: #999;" v-if="scope.row.status==5">已完成</span>
							<span style="color: red;" v-if="scope.row.status==6">服务中</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="createTime" fixed="right" label="操作" width="160">
						<template slot-scope="scope">
							<el-button type="primary" @click="zhuandan(scope.row,'pd')">加钟记录</el-button>
							
						</template>
					</el-table-column>
				</el-table>
				<div class="shifu-scheduling-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 加钟记录 -->
			<el-dialog title="加钟记录" :visible.sync="dialogFormVisible" center>
				
				<el-table v-loading="tableDataLoadingU" :data="userData.list">
					<el-table-column prop="ordersId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="ordersMassageList" label="加钟名称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.ordersMassageList.length>0">{{scope.row.ordersMassageList[0].massageType?scope.row.ordersMassageList[0].massageType.title:''}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="payMoney" label="加钟金额">
					</el-table-column>
					<el-table-column prop="artificerMoney" label="教练收益">
					</el-table-column>
					<el-table-column prop="oneArtificerMoney" label="推广教练佣金">
					</el-table-column>
					<el-table-column prop="shopMoney" label=" 商家收益">
						<template slot-scope="scope">
							<span style="color: #B94A48;" >{{ scope.row.shopMoney?scope.row.shopMoney:'0' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="pingMoney" label="平台收益">
						<template slot-scope="scope">
							<span style="color: #B94A48;" >{{ scope.row.pingMoney?scope.row.pingMoney:'0' }}</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="cityAgencyMoney" label="城市代理收益">
						<template slot-scope="scope">
							<span style="color: #B94A48;" >{{ scope.row.cityAgencyMoney?scope.row.cityAgencyMoney:'0' }}</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="duration" label="加钟时长">
						<template slot-scope="scope">
							<span v-if="scope.row.ordersMassageList.length>0">{{scope.row.ordersMassageList[0].massageType?scope.row.ordersMassageList[0].massageType.duration:''}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
			</el-dialog>
			<!-- 信息详情 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleDel" center width="80%">
				<el-row>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">就诊人姓名</div>
								<div class="text_color"><span style="cursor: pointer;color: #5cc477;" v-if="detaDatle.patientInfo" @click="updates(detaDatle.patientInfo.userId)">{{detaDatle.patientInfo.realName}}</span>
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">就诊人电话</div>
								<div class="text_color"><span v-if="detaDatle.patientInfo">{{detaDatle.patientInfo.phone}}</span></div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">身份证号</div>
								<div class="text_color"><span v-if="detaDatle.patientInfo">{{detaDatle.patientInfo.idNumber}}</span></div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">性别</div>
								<div class="text_color"><span v-if="detaDatle.patientInfo">{{detaDatle.patientInfo.sex==1?'男':'女'}}</span></div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">是否满18岁</div>
								<div class="text_color"><span v-if="detaDatle.patientInfo">{{detaDatle.patientInfo.isUnderAge==1?'是':'否'}}</span></div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">与下单人的关系</div>
								<div class="text_color"><span v-if="detaDatle.patientInfo">{{detaDatle.patientInfo.relationship}}</span></div>
							</div>
						</div>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">床号</div>
								<div class="text_color">
									<span >{{detaDatle.badNo}}</span>
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">症状</div>
								<div class="text_color"><span>{{detaDatle.symptom}}</span>
								</div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">自理能力</div>
								<div class="text_color"><span >{{detaDatle.selfAbility}}</span></div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">护理需求</div>
								<div class="text_color"><span>{{detaDatle.nursingNeeds}}</span></div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">护理天数</div>
								<div class="text_color"><span>{{detaDatle.serviceNum}}</span></div>
							</div>
						</div>
					</el-col>
					<el-col :span="8" class="cards">
						<div class="box">
							<div class="box_num">
								<div class="box_color">备注</div>
								<div class="text_color"><span>{{detaDatle.remarks}}</span></div>
							</div>
						</div>
					</el-col>
				</el-row>
			
			
			</el-dialog>
		</div>
	</div>
</template>

<script>
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var geocoder, map, markersArray, infoWindow = [];
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				shopName1: '',
				shopId: '',
				shopName: '',
				openTime: '',
				closeTime: '',
				detailedAddress: '',
				shopLng: '',
				shopLat: '',
				percentage1: 0, //进度条
				img: [], //任务轮播图
				// img: '',
				classn: '',
				value: [],
				parentId: 0,
				parentIdnum: '',

				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible: false,
				typeDatas: {},
				typeDatas1: {},
				titles: '添加',
				// 地图相关
				longitude: '', //经度
				latitude: '', //维度
				loadingMap: true,
				BMap: '',
				map: '',
				geoc: '',
				keyword: '',
				showMap: false,
				pointLngLat: '',
				center: {
					lng: 108.948024,
					lat: 34.263161
				},
				lng: 108.948024,
				lat: 34.263161,
				storeAddress: [],
				storeAddre: '请选择城市',
				form: {
					province: '',
					city: '', //市
					district: '', //区
				},
				dialogImageUrl: [],
				dialogVisible: false,
				erweimaImg: '',
				activeName: 'first',
				shopTypeName: '',
				shopTypId: '',
				titles1: '',
				labels: [], //标签集合
				bq: '', //输入的标签名
				province: '',
				city: '', //市
				district: '', //区
				options: regionData,
				shopCover: '',
				shopTypenum: [],
				typeDatas2: [],
				shopType: '',
				geometries: [],
				center: '',
				map: '',
				lists: [],
				index1: 0,
				tableData: {},
				hospitalId: '',
				ordersNo: '',
				userName: '',
				type: '',
				marker: '',
				infoWindowList:[],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '陪诊订单'
					},
					{
						value: 2,
						label: '陪护订单'
					},
				],
				userData:[],
				tableDataLoadingU:false,
				dialogFormVisible:false,
				limit1: 10,
				page1: 1,
				phone:'',
				campus:'',
				ordersId:'',
				pd:'',
				titles:'信息详情',
				dialogFormVisibleDel:false,
				detaDatle:{},
				datleIndex:1,
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val
				this.dataSelectoder()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelectoder()
			},
			handleSizeChangeU(val) {
				this.limit1 = val;
				this.dataSelectUser()()
			},
			handleCurrentChangeU(val) {
				this.page1 = val;
				this.dataSelectUser()
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ", "充电桩");
				geolocation.getIpLocation(this.showPosition, this.showErr);
			},
			showPosition(position) {
				this.setMap();
			},
			showErr(e) {
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				var center = new TMap.LatLng(this.latitude, this.longitude);
				if(map){
					map.destroy();
				}
				map=[]
				infoWindow = []
				// 初始化地图
				map = new TMap.Map('container', {
					zoom: 13, // 设置地图缩放级别
					pitch: 30,
					center: center, // 设置地图中心点坐标
					// mapStyleId: "style1", //个性化样式
					baseMap: {
						type: 'vector',
						// features: ['base', 'building3d'], // 隐藏矢量文字
					},
				});
				console.log('this.geometries', this.geometries)
				// marker文字在图片内
				/* if (this.marker) {
					this.marker.setMap(null);
				}else{
					
				} */
				
				this.marker = new TMap.MultiMarker({
					map: map,
					styles: {
						default: new TMap.MarkerStyle({
							width: 66, // 宽度
							height: 66, // 高度
							anchor: {
								x: 46,
								y: 46
							}, // 锚点位置
							src: 'https://mapapi.qq.com/web/lbs/visualizationApi/demo/img/circle.png', // 标注点图片url或base64地址
							color: '#fff', // 标注点文本颜色
							size: 14, // 标注点文本文字大小
							offset: {
								x: 0,
								y: 16
							}, // 标注点文本文字基于direction方位的偏移属性
						}),
					},
					geometries: this.geometries
				});
				/* if(this.infoWindowList && this.infoWindowList.length>0){
					for(var i in this.infoWindowList){
						var infoWindow=this.infoWindowList[i];
						infoWindow.close()
					}
				} */
				

				//初始化infoWindow
				console.log('this.geometries', this.geometries.length)
				// infoWindow.clear();
				for (var i in this.geometries) {
					infoWindow = new TMap.InfoWindow({
						map: map,
						position: new TMap.LatLng(this.geometries[i].datas.latitude, this.geometries[i].datas
							.longitude),
						offset: {
							x: 0,
							y: -32
						} //设置信息窗相对position偏移像素
					});
					// infoWindow.clears();//关闭信息窗
					 // infoWindow.setContent('待服务数量：1'); //设置信息窗内容
					infoWindow.setContent('待服务数量：' + this.geometries[i].datas.serviceCount); //设置信息窗内容
					infoWindow.open(); //初始信息窗开启
					//this.infoWindowList.push(infoWindow);
					// infoWindow.close(); //关闭信息窗
				}
				let that = this
				this.marker.on("click", function(evt) {
					that.hospitalId = evt.geometry.datas.artificerId
					that.dataSelectoder()
					that.dialogFormVisible2 = true
				})

			},
			// 地图定位
			select() {
				console.log(this.detailedAddress)
				// this.address = this.province+ this.city+this.district+this.address
				var addr = this.province + this.city + this.district + this.address
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
					'&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.longitude = response.result.location.lng; // 经度
					that.latitude = response.result.location.lat; // 纬度
					// that.address = response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			// 医院点击事件
			bindClick() {
				this.dialogFormVisible2 = true
				this.dataSelectoder()
			},
			phoneSelect() {
				this.select()
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 获取省市区
			handleChange5(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}
				this.dataSelect()
			},
			
			handleChange55(value) {
				
				this.dataSelect()
			},
			handleChange() {
				this.$router.push({
					path: '/riderScheduling',
				})
			},
			dataSelect() {
				this.$http({
					url: this.$http.adornUrl('artificer/selectArtificer'),
					method: 'get',
					params: this.$http.adornParams({
						'classifyId':this.type,
						'city':this.city,
						'artificerName':this.userName
					})
				}).then(({
					data
				}) => {
					console.log(data)
					var arr = []
					var arr1 = []
					if (data.data.length > 0) {

						for (var i in data.data) {
							if (data.data[i].latitude && data.data[i].longitude) {
								var obj = {}
								obj.position = new TMap.LatLng(data.data[i].latitude, data.data[i].longitude)
								obj.datas = data.data[i]
								obj.img = data.data[i].artificerImg
								obj.content = data.data[i].artificerName
								// obj['position'] = new TMap.LatLng(data.data.records[i].latitude, data.data
								// 	.records[i].hospitalLng)
								// obj['datas'] = data.data.records[i]
								// obj['img'] = data.data.records[i].icon
								// obj['content'] = data.data.records[i].artificerName
								// arr.push(new TMap.LatLng(data.data.records[i].latitude, data.data.records[i]
								// 	.hospitalLng))
								arr.push(obj)
								arr1.push(obj)
								this.longitude = data.data[0].longitude
								this.latitude = data.data[0].latitude
							}else{
								this.longitude = this.lng
								this.latitude = this.lat
							}

						}
						
						this.geometries = arr
						this.lists = arr1
						this.setMap();

					} else {
						this.longitude = this.lng
						this.latitude = this.lat
						this.$message({
							message: '暂无数据',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
						this.setMap();
					}

				})
			},
			// 重置
			cleans() {
				this.province = ''
				this.city = ''
				this.district = ''
				this.storeAddress = []
				this.storeAddre = '请选择城市'
				this.userName = ''
				this.type = ''
				this.dataSelect()
			},
			// 详情跳转
			updates(userId) {
				if(userId){
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelectoder()
			},
			cleansDet() {
				this.page = 1
				this.type = ''
				this.userName = ''
				this.ordersNo = ''
				this.dataSelectoder()
			},

			// 获取骑手订单
			dataSelectoder() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('artificer/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': 2,
						'artificerId': this.hospitalId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 转单弹框
			zhuandan(row,pd){
				this.ordersId = row.ordersId
				if(pd){
					this.pd = pd
				}else{
					this.pd = ''
				}
			
				this.dataSelectUser()
				this.dialogFormVisible = true
			},
			// 确认选择
			userBtns(row){
				let url = ''
				if(this.pd){
					url='orders/distributionOrder'
				}else{
					url='orders/giveOrdersUser'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'orderId': this.ordersId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelectoder()
								this.dataSelect()
								this.dialogFormVisible = false
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
			
			
				})
			},
			// 获取数据列表
			dataSelectUser() {
				this.tableDataLoadingU = true
				this.$http({
					url: this.$http.adornUrl('artificer/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'phone': '',
						'name': '',
						'status': '',
						'city': '',
						'parentId':this.ordersId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingU = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			selectU() {
				this.page = 1
				this.dataSelectUser()
			},
			// 重置
			cleansU() {
				this.page1 = 1
				this.campus = ''
				this.phone = ''
				this.dataSelectUser()
			},
			dataDel(row,index){
				console.log(row)
				this.titles = '就诊信息'
				this.datleIndex = index
				this.detaDatle = row
				this.dialogFormVisibleDel = true
			},
			// 项目类型
			fwSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '服务类型'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.typeNum = returnData
				})
			},
		},
		created() {

		},
		mounted() {
			let data = {
				key: "ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", //申请的密钥
				output: "jsonp",
			};
			let url = "https://apis.map.qq.com/ws/location/v1/ip"
			this.$jsonp(url, data).then(res => {
				console.log('获取Ip定位成功----')
				
				this.lng = res.result.location.lng
				this.lat = res.result.location.lat
			
			}).catch(err => {
				this.$toast('获取Ip定位失败--', err.message)
			})
			// this.setMap();
			// this.getMyLocation()
			this.dataSelect()
			this.fwSelect()

		}
	}
</script>

<style scoped>
.shifu-scheduling-page__filter-select {
	width: 150px;
}

.shifu-scheduling-page__filter-input {
	width: 200px;
}

.shifu-scheduling-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.shifu-scheduling-page__map-card {
	padding: 12px;
}

.shifu-scheduling-page__map {
	width: 100%;
	height: calc(80vh - 32px);
	min-height: 480px;
	border-radius: 6px;
	overflow: hidden;
}

.shifu-scheduling-page__link {
	color: #5cc477;
	cursor: pointer;
}

.shifu-scheduling-page__link:hover {
	color: #3fae5c;
}

.shifu-scheduling-page__dialog-toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.shifu-scheduling-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>

<style>
	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}

	.csssprite {
		width: 100% !important;
		height: 100% !important;
		border-radius: 50% !important;
		min-width: 100% !important;
		max-width: 100% !important;
		min-height: 100% !important;
		max-height: 100% !important;
		border: 3px solid red !important;
	}
</style>
