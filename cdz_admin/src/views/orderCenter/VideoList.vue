<template>
	<div class="nf-page video-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">订单中心</span>
			<h2 class="nf-page-hero__title">视频列表</h2>
			<p class="nf-page-hero__desc">查看与管理订单关联视频，支持按类型、用户、台球桌及订单编号筛选，并可修改视频关联订单。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="视频类型">
					<el-select
						class="video-list-page__filter-select"
						v-model="classifyT"
						placeholder="请选择视频类型"
						@change="select()">
						<el-option v-for="item in homeData1" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="订单用户昵称">
					<el-input
						class="video-list-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入订单用户昵称"
						v-model="minMoney">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="video-list-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入手机号"
						v-model="maxMoney">
					</el-input>
				</el-form-item>
				<el-form-item label="台球桌名称">
					<el-input
						class="video-list-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入台球桌名称"
						v-model="couponName">
					</el-input>
				</el-form-item>
				<el-form-item label="桌号">
					<el-input
						class="video-list-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入桌号"
						v-model="goodsNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="订单编号">
					<el-input
						class="video-list-page__filter-input"
						@keydown.enter.native="select"
						placeholder="请输入订单编号"
						v-model="ordersNoT">
					</el-input>
				</el-form-item>
				<el-form-item class="video-list-page__filter-actions">
					<el-button type="primary" icon="el-icon-refresh" @click="select">刷新</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.records">
			<el-table-column prop="ordersVideoId" label="编号" width="80">
			</el-table-column>
			<el-table-column label="视频类型" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.classify==1" class="video-list-page__tag video-list-page__tag--danger">原视频</span>
					<span v-if="scope.row.classify==2" class="video-list-page__tag video-list-page__tag--brand">剪辑视频</span>
				</template>
			</el-table-column>
			<el-table-column prop="userName" label="订单用户昵称">
				<template slot-scope="scope">
					<span
						v-if="scope.row.userName"
						class="video-list-page__user-link"
						@click="updatesvideo(scope.row.userId)">
						{{ scope.row.userName ? scope.row.userName : '未绑定' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone"  label="手机号">
			</el-table-column>
			<el-table-column prop="tearoomName" label="台球馆名称">
			</el-table-column>
			<el-table-column prop="goodsName" label="桌号">
			</el-table-column>
			<el-table-column prop="ordersNo" label="订单编号" width="170">
				<template slot-scope="scope">
					{{scope.row.ordersNo}}
					<el-button size="mini" style="margin: 2px;" type="primary" plain :disabled="!isAuth('VideoList:update')"  @click="updateOrder(scope.row)">修改</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="videoUrl" label="视频" width="210">
				<template slot-scope="scope">
					<div>
						<video style="width: 180px;height: 100px;" v-if="scope.row.videoUrl" :src="scope.row.videoUrl"
							type="audio/wav" controls="controls"></video>
			
						<span v-else>暂无视频</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="videoTime" label="视频时间" width="160">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			
			<!-- <el-table-column label="状态" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.status==1" style="color: #f56c6c;">已报名</span>
					<span v-if="scope.row.status==2" style="color: #5cc477;" >游戏中</span>
					<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
					<span v-if="scope.row.status==4" style="color: chocolate;">已签到</span>
					<span v-if="scope.row.status==5" style="color: #999;" >已取消</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" fixed="right" label="操作" width="100">
				<template slot-scope="scope">
					<el-button size="mini" style="margin: 2px;" type="primary" plain :disabled="!isAuth('matching:quxiao')"
						v-if="scope.row.status==1||scope.row.status==2||scope.row.status==4" @click="cancelOrder(scope.row,2)">取消</el-button>
					<el-button size="mini" style="margin: 2px;" type="warning" :disabled="!isAuth('matching:delete')"
						@click="deleteOrder(scope.row)" v-if="scope.row.status==3||scope.row.status==5">删除</el-button>
				</template>
			</el-table-column> -->
		</el-table>
		<div class="video-list-page__pagination">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
			</el-pagination>
		</div>
		</div>
		<!-- 任务拒绝弹框 -->
		<!-- <el-dialog title="任务拒绝" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog> -->
		<!-- 修改转单 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center width="70%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择台球馆：</span>
				<el-select v-model="store_id" placeholder="请选择台球馆" style="width:50%;" @change="shopClick()">
					<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
						:value="item.tearoomId">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择桌台：</span>
				<el-radio-group v-model="goods_id"  @change="goodsIdBtn()" style="margin-left: 200px;">
					<el-radio v-for="(item,index) in goods_list" :key="index" style="margin: 5px;"
						:label="item.goodsId">{{item.goodsName}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">订单编号：</span>
				<el-input style="width:50%;" v-model="ordersNo" type="text" :disabled='!show'  @focus="orderNumBtn()" :placeholder="placeholderTrxt">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 订单列表 -->
		<el-dialog title="订单列表" :visible.sync="dialogFormVisibleOr" center width="70%">
			<div style="margin:5px;display: inline-block;">
				<span>订单状态：</span>
				<el-select v-model="statusorder" placeholder="请选择订单状态" style="width:150px;"
					@change="select1(statusorder)">
					<el-option v-for="item in statusmain1" :key="item.value" :label="item.label"
						:value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>订单号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入订单号"
					v-model="orderNum"></el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>预约人电话：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入预约人电话"
					v-model="orderType"></el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>预约人姓名：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入预约人姓名"
					v-model="userName"></el-input>&nbsp;&nbsp;
			</div>
			<!-- <div style="position: relative;display: inline-block;margin:5px;">
				<span>桌台名称：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入桌台名称"
					v-model="ordersUserName"></el-input>&nbsp;&nbsp;
			</div> -->
			<!-- </div> -->
			<div style="margin:5px;display: inline-block;">
				<span>开始时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:5px;display: inline-block;">
				<span>截止时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
				</el-date-picker>
			</div>
			<div style="display: inline-block;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select1">查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans1">重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading1" :data="orderData.list">
				<el-table-column fixed prop="ordersId" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="tearoomName" label="台球馆" width="160">
				</el-table-column>
				<el-table-column fixed prop="tearoomTitleImage" label="台球馆图片">
					<template slot-scope="scope">
						<div v-if="scope.row.tearoomTitleImage">
							<span v-for="(item,index) in scope.row.tearoomTitleImage.split(',')">
								<el-popover placement="top-start" title="" trigger="hover" v-if="index==0">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px; height: 300px" :src="item" alt="">
								</el-popover>
							</span>
						</div>
						<div v-else>暂无图片</div>
					</template>
				</el-table-column>
				<el-table-column fixed prop="goodsName" label="桌台名称">
					<template slot-scope="scope">
						<span>{{scope.row.goodsName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单号" width="200">
				</el-table-column>
				<el-table-column prop="ordersType" label="订单类型" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.ordersType==1">开台</span>
						<span v-else-if="scope.row.ordersType==2">-</span>
						<span v-else-if="scope.row.ordersType==3">预约</span>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column prop="nickname" label="用户昵称" width="120">
					<template slot-scope="scope">
						<span style="color: #5cc477;">{{scope.row.payUserName ? scope.row.payUserName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="vip.vipName" label="会员等级" width="80">
				</el-table-column>
				<el-table-column prop="hourPrice" label="每小时价格">
					<template slot-scope="scope">
						<span style="color: deepskyblue;"
							v-if="scope.row.tearoom">{{scope.row.tearoom.hourPrice}}元</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="小时数">
					<template slot-scope="scope">
						<span v-if="scope.row.productId">-</span>
						<span v-else>{{scope.row.hour}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="platformType" label="优惠券平台">
					<template slot-scope="scope">
						<span v-if="scope.row.platformType==1">美团</span>
						<span v-else-if="scope.row.platformType==2">抖音</span>
						<span v-else-if="scope.row.platformType==3">快手</span>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称" width="120">
				</el-table-column>
				<el-table-column prop="couponMoney" label="优惠券金额">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;cursor: pointer;">{{scope.row.couponMoney?scope.row.couponMoney:'0' }}元</span>
					</template>
				</el-table-column>
				<el-table-column prop="vipMoney" label="会员优惠金额">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;cursor: pointer;">{{scope.row.vipMoney?scope.row.vipMoney:'0' }}元</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额">
					<template slot-scope="scope">
						<span
							style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney ?scope.row.payMoney:0}}元</span>
					</template>
				</el-table-column>
				<el-table-column prop="minuteNum" label="超出时间(分钟)">
				</el-table-column>
				<el-table-column prop="minuteMoney" label="超出费用">
				</el-table-column>
				<el-table-column prop="cashPledge" label="押金">
				</el-table-column>
				<el-table-column prop="shopMoney" label="商家总收益">
				</el-table-column>
				<el-table-column prop="payWay" label="支付方式">
					<template slot-scope="scope">
						<span style="color: #5cc477;" v-if='scope.row.payWay == 1'>微信</span>
						<span style="color: #5cc477;" v-if='scope.row.payWay == 2'>支付宝</span>
						<span style="color: #5cc477;" v-if='scope.row.payWay == 3'>零钱</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="订单状态">
					<template slot-scope="scope">
						<span style="color: #5cc477;" v-if="scope.row.status === 1 ">待支付</span>
						<span style="color: #5cc477;" v-if="scope.row.status === 2 ">待使用 </span>
						<span style="color: #999999;" v-if="scope.row.status === 3 ">使用中 </span>
						<span style="color: #999999;" v-if="scope.row.status === 4 ">已完成</span>
						<span style="color: #999999;" v-if="scope.row.status === 5 ">已取消</span>
						<span style="color: #5cc477;" v-if="scope.row.status === 6 ">待评价</span>
						<span style="color: #999999;" v-if="scope.row.status === 7 ">已退款</span>
						<span style="color: #999999;" v-if="scope.row.status === 8 ">已失效</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="payTime" label="付款时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" style="margin: 2px;" type="warning"
							@click="querenOrder(scope.row)" >选择</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
					:page-sizes="[5,10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next" :total="orderData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				content: '',
				status: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '审核成功'
					}, {
						value: 2,
						label: '拒绝'
					}
				],
				userName: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				couponName: '',
				homeData1: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '原视频'
					},
					{
						id: 2,
						name: '剪辑视频'
					},
				],
				status: 0,
				maxMoney: '',
				minMoney: '',
				classifyT:'',
				times: '',
				sex: 0, //1男 2女 不限0
				sexList: [{
						value: 0,
						label: '不限'
					},
					{
						value: 1,
						label: '男'
					}, {
						value: 2,
						label: '女'
					}
				],
				goodsNameT:'',
				ordersNoT:'',
				ordersVideoId:'',
				shopData: [],
				goods_list: [],
				goods_ids: [],
				goodsIds: '',
				store_id:'',
				goods_id:'',
				ordersNo:'',
				ordersId:'',
				dialogFormVisibleOr:false,
				tableDataLoading1:false,
				size1:5,
				page1:1,
				orderData:{},
				statusmain1: [{
						value: 'all',
						label: '全部'
					},
					{
						value: 1,
						label: '待支付'
					},
					{
						value: 2,
						label: '待使用'
					},
					{
						value: 3,
						label: '使用中'
					},
					{
						value: 4,
						label: '已完成'
					},
					{
						value: 5,
						label: '已取消'
					},
					{
						value: 6,
						label: '待评价'
					},
					{
						value: 7,
						label: '已退款'
					},
					{
						value: 8,
						label: '已失效'
					},
				],
				statusorder: 'all',
				orderNum: '',
				orderType: '',
				userName:'',
				ordersUserName:'',
				startTime:'',
				endTime:'',
				show:true,
				placeholderTrxt:'请选择订单',
			}
		},
		methods: {
			// 详情跳转
			updatesvideo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},
			
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			//订单
			handleSizeChange3(val) {
				this.size1 = val
				this.orderBtn()
			},
			handleCurrentChange3(val) {
				this.page1 = val
				this.orderBtn()
			},
			
			
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.couponName = ''
				this.maxMoney = ''
				this.minMoney = ''
				this.goodsNameT = ''
				this.ordersNoT = ''
				this.classifyT = ''
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('ordersVideo/selectOrdersVideoList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.minMoney,
						'tearoomName': this.couponName,
						'phone': this.maxMoney,
						'goodsName':this.goodsNameT,
						'ordersNo': this.ordersNoT,
						'classify':this.classifyT
					})
					
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			
			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},
			
			cancelOrder(row, status) {
				let content = '确定取消此订单'

				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activityApply/cancelActivityApply'),
						method: 'post',
						params: this.$http.adornParams({
							'activityApplyId': row.activityApplyId
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}
						
					})
				})
			},
			// 删除
			deleteOrder(row){
				this.$confirm('确定删除此订单', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activityApply/deleteActivityApply'),
						method: 'post',
						params: this.$http.adornParams({
							'activityApplyId': row.activityApplyId
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}
						
					})
				})
			},
			// 修改转单
			updateOrder(row){
				this.ordersVideoId = row.ordersVideoId
				this.store_id = ''
				this.goods_id = ''
				this.ordersNo = ''
				this.ordersId = ''
				this.goods_list = []
				this.show = true
				this.placeholderTrxt = '请选择订单'
				this.shopSelect()
				this.dialogFormVisible = true
			},
			// 选择台球馆
			shopSelect() {
			
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 1000,
						'tearoomName': '',
						'city': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.list;
					this.shopData = returnData
				})
			},
			// 选择桌台
			shopClick() {
				// for (var i in this.shopData) {
			
				this.$http({
					url: this.$http.adornUrl('goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 100,
						'goodsName': '',
						'status': '',
						'tearoomId': this.store_id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.goods_list = returnData.list
				})
			},
			 goodsIdBtn(){
				 for(var i in this.goods_list){
					 if(this.goods_id==this.goods_list[i].goodsId){
					 	this.ordersUserName = this.goods_list[i].goodsName
						this.page1 = 1
						this.orderType = ''
						this.orderNum = ''
						// this.ordersUserName = ''
						this.statusorder = ''
						this.startTime = ''
						this.endTime = ''
						this.userName = ''
						this.orderBtn()
					 }
				 }
				
			},
			orderNumBtn(){
				this.page1 = 1
				this.orderType = ''
				this.orderNum = ''
				// this.ordersUserName = ''
				this.statusorder = ''
				this.startTime = ''
				this.endTime = ''
				this.userName = ''
				
				this.orderBtn()
				this.dialogFormVisibleOr = true
			},
			// 获取订单数据
			orderBtn(){
					let storeId = this.store_id
					this.tableDataLoading1 = true
					this.$http({
						url: this.$http.adornUrl('orders/selectOrdersList'),
						method: 'get',
						params: this.$http.adornParams({
							'page': this.page1,
							'limit': this.size1,
							'phone': this.orderType,
							'userName': this.userName,
							'goodsName': this.ordersUserName,
							'ordersNo': this.orderNum,
							'status': this.statusorder,
							'startTime': this.startTime,
							'endTime': this.endTime,
							'tearoomId': storeId
						})
					}).then(({
						data
					}) => {
						this.tableDataLoading1 = false
						let returnData = data.data;
						this.orderData = returnData
						if(this.orderData.list.length==0){
							this.show = false
							this.placeholderTrxt = '暂无订单'
						}else{
							this.show = true
							this.placeholderTrxt = '请选择订单'
						}
					})
			},
			//搜索
			select1() {
				this.page1 = 1
				this.size1 = 10
				this.orderBtn()
			},
			// 重置
			cleans1() {
				this.page1 = 1
				this.orderType = ''
				this.orderNum = ''
				// this.ordersUserName = ''
				this.statusorder = ''
				this.startTime = ''
				this.endTime = ''
				this.userName = ''
				this.orderBtn()
			},
			// 确认选择
			querenOrder(row){
				this.ordersNo = row.ordersNo
				this.ordersId = row.ordersId
				this.dialogFormVisibleOr = false
			},
			// 确认修改视频信息
			refuseto(){
				if (this.store_id === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择台球馆',
						type: 'warning'
					});
					return
				}
				if (this.goods_id === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择桌台',
						type: 'warning'
					});
					return
				}
				if (this.ordersId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择订单',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('ordersVideo/updateOrdersVideo'),
					method: 'post',
					data: this.$http.adornData({
						'ordersId':this.ordersId,
						'tearoomId':this.store_id,
						'goodsId':this.goods_id,
						'ordersVideoId':this.ordersVideoId
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
								this.dialogFormVisible = false
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
					
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.video-list-page__filter-select {
	width: 150px;
}

.video-list-page__filter-input {
	width: 200px;
}

.video-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.video-list-page__user-link {
	color: #5cc477;
	cursor: pointer;
}

.video-list-page__user-link:hover {
	color: #3fae5c;
}

.video-list-page__tag--brand {
	color: #5cc477;
}

.video-list-page__tag--danger {
	color: #f56c6c;
}

.video-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
