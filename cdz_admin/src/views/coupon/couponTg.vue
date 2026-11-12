<template>
	<div class="nf-page coupon-tg-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">兑换列表</span>
			<h2 class="nf-page-hero__title">兑换列表</h2>
			<p class="nf-page-hero__desc">查看用户兑换记录，支持按用户昵称、手机号、兑换码、平台筛选，可查询、重置与手动兑换。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="用户昵称">
					<el-input
						class="coupon-tg-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="coupon-tg-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="phoneT">
					</el-input>
				</el-form-item>
				<el-form-item label="兑换码">
					<el-input
						class="coupon-tg-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入兑换码"
						v-model="contentT">
					</el-input>
				</el-form-item>
				<el-form-item label="平台">
					<el-select
						class="coupon-tg-page__filter-select"
						v-model="classify"
						placeholder="请选择平台"
						@change="select()">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="coupon-tg-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" plain :disabled="!isAuth('couponTg:yanquan')" @click="duihuan()">兑换</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="shopData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<span class="coupon-tg-page__link" @click="updates(scope.row.userId)">{{scope.row.userName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="image" label="用户头像">
					<template slot-scope="scope">
						<img v-if="scope.row.image" :src="scope.row.image" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="type" label="平台">
					<template slot-scope="scope">
						<span v-if="scope.row.type==1">美团</span>
						<span v-if="scope.row.type==2">抖音</span>
						<span v-if="scope.row.type==3">快手</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="兑换码">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div class="coupon-tg-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="shopData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog title="兑换信息确认" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">用户昵称：</span>
				<el-input style="width: 50%;" v-model="nickName1" @focus="userselect()" placeholder="请选择用户"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">兑换码：</span>
				<el-input style="width: 50%;" v-model="expressNumber" placeholder="请输入兑换码"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="duihuanNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="70%">
			<div style="margin:5px;display: inline-block;">
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' type="primary" icon="el-icon-search" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' type="primary" icon="el-icon-refresh-left" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
				<el-table-column prop="userId" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="userName"  label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime"  label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				type: 1,
				title: '',
				userNameT:'',
				phoneT:'',
				contentT:'',
				classify: '',
				statusmain: [{
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
				couponId: '',
				tableDataLoading: false,
				dialogFormVisible: false,
				shopData: {},
				titles: '',
				expressNumber:'',
				phone1:'',
				store_id: '',
				goods_list: [],
				goods_ids: [],
				chaguanData: [],
				dialogFormVisible3: false,
				tableDataLoading4:false,
				userIdss: '', //用户id
				nickName: '',
				nickName1: '',
				nickName2: '',
				userIds: {}, //用户列表
				size1: 10,
				page1: 1,
			}
		},

		methods: {
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
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.shopSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.userClass()
			},
			//搜索
			select() {
				this.page = 1
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.classify = ''
				this.userNameT = ''
				this.phoneT = ''
				this.contentT = ''
				this.shopSelect()
			},
			// 获取商品数据
			shopSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'type': this.classify,
						'state':100,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'content': this.contentT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.shopData = returnData
				})
			},
			duihuan() {
				this.goods_ids = []
				this.goods_list = []
				this.store_id = ''
				this.nickName1 = ''
				this.userIdss = ''
				this.type = 1
				this.expressNumber = ''
				this.chaguanSelect()
				this.dialogFormVisible = true
			},
			duihuanNoticeTo() {
				if (this.userIdss === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					});
					return
				}
				if (this.expressNumber == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入兑换码',
						type: 'warning'
					});
					return
				}
				
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/adminConversionCoupon'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': this.userIdss,
						'key': this.expressNumber,
					})
				}).then(({
					data
				}) => {
					if(data.code==1){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
						this.dialogFormVisible = false
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
			},
			// 获取用户列表弹框
			userselect() {
				this.page1 = 1
				this.phone1 = ''
				this.nickName2 = ''
				
				this.dialogFormVisible3 = true
				
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'userName': this.nickName2,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication':'',
						'phone': this.phone1
					})
					
					
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page1 = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.page1 = 1
				this.phone1 = ''
				this.nickName2 = ''
				this.tableDataLoading4 = true
			
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phone = row.mophonebile
				this.dialogFormVisible3 = false
			},
			// 选择台球馆
			shopClick() {
				console.log('-----------', this.store_id)
				this.$http({
					
					url: this.$http.adornUrl('goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 100,
						'goodsName': '',
						'status': '',
						'tearoomId': this.store_id,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.list;
					this.goods_list = returnData
				})
			},
			// 获取台球馆数据
			chaguanSelect() {
			
				this.tableDataLoading1 = true
				this.$http({
					
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 1000,
						'tearoomName': '',
						'city':'',
						'status':'',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.chaguanData = returnData.list
				})
			},
		},
		mounted() {
			this.shopSelect()
		}
	};
</script>

<style scoped>
.coupon-tg-page__filter-input {
	width: 150px;
}

.coupon-tg-page__filter-select {
	width: 150px;
}

.coupon-tg-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.coupon-tg-page__link {
	color: #5cc477;
	cursor: pointer;
}

.coupon-tg-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
