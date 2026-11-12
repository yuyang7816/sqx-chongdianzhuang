<template>
	<div class="nf-page couponuser-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">优惠券领取记录</span>
			<h2 class="nf-page-hero__title">优惠券领取记录</h2>
			<p class="nf-page-hero__desc">查看用户优惠券领取与使用记录，支持按平台、类型、名称、用户、充电站及状态筛选，可查询、重置、作废与核销。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="优惠券平台">
					<el-select
						class="couponuser-page__filter-select"
						v-model="platformTypeT"
						placeholder="请选择平台"
						@change="refresh()">
						<el-option v-for="item in platformnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="优惠券类型">
					<el-select
						class="couponuser-page__filter-select"
						v-model="typeT"
						placeholder="请选择类型"
						@change="refresh()">
						<el-option v-for="item in typesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="优惠券名称">
					<el-input
						class="couponuser-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入优惠券名称"
						v-model="couponName">
					</el-input>
				</el-form-item>
				<el-form-item label="用户昵称">
					<el-input
						class="couponuser-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="nick_name">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="couponuser-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</el-form-item>
				<el-form-item label="充电站名称">
					<el-select
						class="couponuser-page__filter-select couponuser-page__filter-select--wide"
						v-model="tearoomNameT"
						placeholder="请选择充电站"
						@change="select()"
						filterable>
						<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
							:value="item.tearoomId">
						</el-option>
					</el-select>
				</el-form-item>
				<!-- <el-form-item label="优惠劵编号">
					<el-input
						class="couponuser-page__filter-input"
						@keydown.enter.native="refresh"
						placeholder="请输入优惠劵编号"
						v-model="ordersIdT">
					</el-input>
				</el-form-item> -->
				<el-form-item label="状态">
					<el-select
						class="couponuser-page__filter-select"
						v-model="payWayT"
						placeholder="请选择状态"
						@change="refresh()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="couponuser-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="couponuser-page__tabs">

			<el-tab-pane label="领取列表" name="second">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponUserId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="platformType" label="优惠券平台" >
						<template slot-scope="scope">
							<span v-if="scope.row.platformType==1">美团</span>
							<span v-else-if="scope.row.platformType==2">抖音</span>
							<span v-else-if="scope.row.platformType==3">快手</span>
							<span v-else>-</span>
						</template>
					</el-table-column>
					<el-table-column prop="type" label="优惠券类型">
						<template slot-scope="scope">
							<!-- <span v-if="scope.row.type == 1">团购券</span> -->
							<span v-if="scope.row.type == 2">优惠券</span>
							<span v-if="scope.row.type == 3">实物</span>
							<span v-if="scope.row.type == 4">定时券</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="充电站名称" width="160">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称" width="150">
						<template slot-scope="scope">
							<span class="couponuser-page__link"
								@click="updates(scope.row)">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="160">
					</el-table-column>
					<el-table-column prop="platform" label="来源">
						<template slot-scope="scope">
							<div v-if="scope.row.platform==1">新用户领取</div>
							<div v-if="scope.row.platform==2">邀请新用户</div>
							<div v-if="scope.row.platform==3">邀请用户下单</div>
							<div v-if="scope.row.platform==4">购买</div>
							<div v-if="scope.row.platform==5">兑换</div>
							<div v-if="scope.row.platform==6">商家赠送</div>
							<div v-if="scope.row.platform==null||scope.row.platform==''">后台赠送</div>
						</template>
					</el-table-column>
					<el-table-column prop="payWay" label="支付类型">
						<template slot-scope="scope">
							<div v-if="scope.row.payWay==1">微信app</div>
							<div v-if="scope.row.payWay==2">微信公众号</div>
							<div v-if="scope.row.payWay==3">微信小程序</div>
							<div v-if="scope.row.payWay==4">支付宝app</div>
							<div v-if="scope.row.payWay==5">支付宝H5</div>
							<div v-if="scope.row.payWay==6">零钱</div>
							<div v-if="scope.row.payWay==null">-</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponId" label="优惠劵编号" width="160">
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"  slot="reference">
									{{scope.row.buyRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.buyRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="lessMoney" label="面值">
					</el-table-column>
					<el-table-column prop="minMoney" label="最低消费金额">
					</el-table-column> -->
					
					<el-table-column prop="cdkCode" label="核销码">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长(小时)">
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
					</el-table-column>
					<el-table-column prop="failureTime" label="有效期限" width="150">
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span class="couponuser-page__status-active" v-if="scope.row.status == 1">未使用</span>
							<span v-if="scope.row.status == 2">已使用</span>
							<span v-if="scope.row.status == 3">已过期</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" v-if="scope.row.status == 1" type="primary" plain
								:disabled="!isAuth('couponuser:zuofei')" @click="amendTk(scope.row)"
								style="margin: 3px;">作废
							</el-button>
							<el-button size="mini" type="warning" plain
								:disabled="!isAuth('couponuser:hexiao')" v-if="scope.row.status == 1" @click="amendHx(scope.row)"
								style="margin: 3px;">核销
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="couponuser-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="新人券" name="three">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponUserId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="type" label="优惠券类型">
						<template slot-scope="scope">
							<span v-if="scope.row.type == 1">团购券</span>
							<span v-if="scope.row.type == 2">积分兑换券</span>
							<span v-if="scope.row.type == 3">新人券</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="充电站名称">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								@click="updates(scope.row)">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="platform" label="来源">
						<template slot-scope="scope">
							<div v-if="scope.row.platform==1">新用户领取</div>
							<div v-if="scope.row.platform==2">邀请新用户</div>
							<div v-if="scope.row.platform==3">邀请用户下单</div>
							<div v-if="scope.row.platform==4">购买</div>
							<div v-if="scope.row.platform==5">兑换</div>
							<div v-if="scope.row.platform==6">商家赠送</div>
						</template>
					</el-table-column>
					<el-table-column prop="payWay" label="支付类型">
						<template slot-scope="scope">
							<div v-if="scope.row.payWay==1">微信app</div>
							<div v-if="scope.row.payWay==2">微信公众号</div>
							<div v-if="scope.row.payWay==3">微信小程序</div>
							<div v-if="scope.row.payWay==4">支付宝app</div>
							<div v-if="scope.row.payWay==5">支付宝H5</div>
							<div v-if="scope.row.payWay==6">零钱</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponId" label="订单编号">
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"  slot="reference">
									{{scope.row.buyRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.buyRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="cdkCode" label="核销码">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长(小时)">
					</el-table-column>
					<el-table-column prop="failureTime" label="有效期限(天)">
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status == 1">未使用</span>
							<span v-if="scope.row.status == 2">已使用</span>
							<span v-if="scope.row.status == 3">已过期</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" v-if="scope.row.status == 1" type="primary" plain
								:disabled="!isAuth('orderAdmin:refund')" @click="amendTk(scope.row)"
								style="margin: 3px;">退款
							</el-button>
							<el-button size="mini" type="warning" plain v-if="scope.row.status == 1" 
								:disabled="!isAuth('couponuser:hexiao')" @click="amendHx(scope.row)"
								style="margin: 3px;">核销
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="团购券" name="one">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponUserId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="type" label="优惠券类型">
						<template slot-scope="scope">
							<span v-if="scope.row.type == 1">团购券</span>
							<span v-if="scope.row.type == 2">积分兑换券</span>
							<span v-if="scope.row.type == 3">新人券</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="充电站名称">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;"
								@click="updates(scope.row)">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="platform" label="来源">
						<template slot-scope="scope">
							<div v-if="scope.row.platform==1">新用户领取</div>
							<div v-if="scope.row.platform==2">邀请新用户</div>
							<div v-if="scope.row.platform==3">邀请用户下单</div>
							<div v-if="scope.row.platform==4">购买</div>
							<div v-if="scope.row.platform==5">兑换</div>
							<div v-if="scope.row.platform==6">商家赠送</div>
						</template>
					</el-table-column>
					<el-table-column prop="payWay" label="支付类型">
						<template slot-scope="scope">
							<div v-if="scope.row.payWay==1">微信app</div>
							<div v-if="scope.row.payWay==2">微信公众号</div>
							<div v-if="scope.row.payWay==3">微信小程序</div>
							<div v-if="scope.row.payWay==4">支付宝app</div>
							<div v-if="scope.row.payWay==5">支付宝H5</div>
							<div v-if="scope.row.payWay==6">零钱</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponId" label="订单编号">
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="200">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"  slot="reference">
									{{scope.row.buyRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.buyRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="lessMoney" label="面值">
					</el-table-column>
					<el-table-column prop="minMoney" label="最低消费金额">
					</el-table-column>
					
					<el-table-column prop="cdkCode" label="核销码">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长(小时)">
					</el-table-column>
					<el-table-column prop="failureTime" label="有效期限(天)">
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status == 1">未使用</span>
							<span v-if="scope.row.status == 2">已使用</span>
							<span v-if="scope.row.status == 3">已过期</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" v-if="scope.row.status == 1" type="primary" plain
								:disabled="!isAuth('orderAdmin:refund')" @click="amendTk(scope.row)"
								style="margin: 3px;">退款
							</el-button>
							<el-button size="mini" type="warning" plain v-if="scope.row.status == 1" 
								:disabled="!isAuth('couponuser:hexiao')" @click="amendHx(scope.row)"
								style="margin: 3px;">核销
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane> -->
		</el-tabs>
	</div>
</template>


<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				couponName: '',
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				activeName: 'second',
				typeT: '',
				
				nick_name: '',
				phone:'',
				ordersIdT: '',
				tearoomNameT:'',
				payWayT: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '未使用'
					},
					{
						value: 2,
						label: '已使用'
					},
					{
						value: 3,
						label: '已过期'
					},
					
				],
				typesnum:[
					{
							value: '',
							label: '全部'
						},
						{
							value: 2,
							label: '优惠券'
						},
						{
							value: 3,
							label: '实物'
						},
				],
				platformTypeT:'',
				platformnum:[
					{
							value: '',
							label: '全部'
						},
						{
							value: 1,
							label: '美团'
						},
						{
							value: 2,
							label: '抖音'
						},
						{
							value: 3,
							label: '快手'
						},
				],
				shopData: [],

			}
		},

		methods: {
			handleClick(tab, event) {
				this.platformTypeT = ''
				if (tab._props.label == '团购券') {
					this.page = 1
					this.size = 10
					this.typeT = 1
					this.dataSelect()
				}
				if (tab._props.label == '领取列表') {
					this.page = 1
					this.size = 10
					this.dataSelect()
				}
				if (tab._props.label == '新人券') {
					this.page = 1
					this.size = 10
					this.typeT = 3
					this.dataSelect()
				}
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.couponName = ''
				this.nick_name = ''
				this.ordersIdT = ''
				this.tearoomNameT = ''
				this.payWayT = ''
				this.phone = ''
				this.typeT = ''
				this.platformTypeT = ''
				this.dataSelect()
			},
			// 刷新
			refresh() {
				this.page = 1
				this.dataSelect()
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			//获取已发布优惠券数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'couponName': this.couponName,
						'type': this.typeT,
						'nickName': this.nick_name,
						'ordersId': this.ordersIdT,
						'status': this.payWayT,
						'phone':this.phone,
						'platformType':this.platformTypeT,
						'tearoomId':this.tearoomNameT,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 退款
			amendTk(row) {
				this.$confirm(`确定要作废?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('selfCouponUser/updateStatus'),
						method: 'post',
						// params: this.$http.adornParams({
						data: this.$http.adornData({
							'couponUserId': row.couponUserId,
							'status':3
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 核销
			amendHx(row){
				this.$confirm(`确定要核销?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('selfCouponUser/verificationCoupon'),
						method: 'post',
						params: this.$http.adornParams({
						// data: this.$http.adornData({
							'userId': 0,
							'cdkCode':row.cdkCode
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 获取门店数据
			shopSelect() {
			
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 1000,
						'tearoomName': '',
						// 'search_goods_cate': this.typeId,
						// 'status': status,
						'city': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data.list;
					this.shopData = returnData
				})
			},
		},
		mounted() {
			this.dataSelect()
			this.shopSelect()
		}
	};
</script>

<style scoped>
.couponuser-page__filter-select {
	width: 150px;
}

.couponuser-page__filter-select--wide {
	width: 200px;
}

.couponuser-page__filter-input {
	width: 160px;
}

.couponuser-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.couponuser-page__tabs {
	margin-bottom: 0;
}

.couponuser-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.couponuser-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.couponuser-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.couponuser-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.couponuser-page__link {
	color: #5cc477;
	cursor: pointer;
}

.couponuser-page__status-active {
	color: #5cc477;
}

.couponuser-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>