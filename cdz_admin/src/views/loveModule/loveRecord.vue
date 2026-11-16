<template>
	<div class="nf-page love-record-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">爱心钱包</span>
			<h2 class="nf-page-hero__title">爱心钱包排行榜</h2>
			<p class="nf-page-hero__desc">查看各门店爱心钱包余额与已捐款排名，支持按门店名称筛选查询。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="门店名称">
					<el-input
						class="love-record-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入门店名称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item class="love-record-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" class="love-record-page__tabs">
			<el-tab-pane label="排行榜" name="first">
				<div class="nf-table-card">
					<el-table v-loading="tableDataLoading" :data="shopData.list">
						<el-table-column prop="id" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="tearoomName" label="门店名称">
						</el-table-column>
						<el-table-column prop="money" label="爱心钱包">
							<template slot-scope="scope">
								<span class="love-record-page__money">{{ scope.row.money }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="donationMoney" label="已捐款">
							<template slot-scope="scope">
								<span class="love-record-page__money">{{ scope.row.donationMoney }}</span>
							</template>
						</el-table-column>
					</el-table>
					<div class="love-record-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="shopData.totalCount">
						</el-pagination>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				activeName: 'first',
				size: 10,
				page: 1,
				type: 1,
				title: '',
				userNameT: '',
				phoneT: '',
				contentT: '',
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
				expressNumber: '',
				phone1: '',
				store_id: '',
				goods_list: [],
				goods_ids: [],
				chaguanData: [],
				dialogFormVisible3: false,
				tableDataLoading4: false,
				userIdss: '', //用户id
				nickName: '',
				nickName1: '',
				nickName2: '',
				userIds: {}, //用户列表
				size1: 10,
				page1: 1,

				withdrawData: {},
				flag: 1,
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
			}
		},

		methods: {
			//处理默认选中当前日期
			getNowTime1() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 详情跳转
			updates(userId) {
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
			// 获取数据
			shopSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('welfareMoney/selectWelfareCountList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'tearoomName': this.userNameT,
						
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
				// if (this.store_id == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择台球馆',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.goods_ids.leng == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择台球馆',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
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
						// 'tearoomId': this.store_id,
						// 'goodsIds': this.goods_ids.toString(),
						'key': this.expressNumber,
						// 'classify':this.type
					})
				}).then(({
					data
				}) => {
					if (data.code == 1) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
						this.dialogFormVisible = false
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
						'isAuthentication': '',
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
				// if (this.nickName == '' || this.nickName == null) {
				// 	this.nickName = row.phone
				// }
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
						// 'goods_cate': this.typeId,
						'status': '',
						'tearoomId': this.store_id,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.list;
					this.goods_list = returnData
				})

				// for (var i in this.chaguanData) {
				// 	if (this.store_id == this.chaguanData[i].store_id) {
				// 		this.goods_list = this.chaguanData[i].goods_list
				// 	}
				// }

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
						// 'search_goods_cate': this.typeId,
						// 'status': status,
						'city': '',
						'status': '',
					})

					// url: this.$http.adornUrl('goods/selectGoodsList'),
					// method: 'get',
					// params: this.$http.adornParams({
					// 	'page': 1,
					// 	'limit': 100,
					// 	'goodsName': '',
					// 	// 'goods_cate': this.typeId,
					// 	'status': '',
					// 	'tearoomId': this.store_id,
					// })
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.chaguanData = returnData.list
				})
			},
			// 爱心钱包统计
			withdrawSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/statisticsCashMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			animeSelect2() {
				this.withdrawSelect()
			},
		},
		mounted() {
			this.shopSelect()
		}
	};
</script>

<style scoped>
.love-record-page__filter-input {
	width: 160px;
}

.love-record-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.love-record-page__tabs {
	margin-bottom: 0;
	margin-top: 12px;
}

.love-record-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.love-record-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.love-record-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.love-record-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.love-record-page__money {
	color: #5cc477;
	font-weight: 500;
}

.love-record-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>
