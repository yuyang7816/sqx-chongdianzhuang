<template>
	<div class="nf-page table-yoncan-page">
		<div class="table-yoncan-page__back">
			<a href="#" @click.prevent="prev" class="table-yoncan-page__back-link">
				<icon-svg name="jiantou" class="table-yoncan-page__back-icon"></icon-svg>
				返回
			</a>
		</div>

		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">门店餐饮</span>
			<h2 class="nf-page-hero__title">用餐管理</h2>
			<p class="nf-page-hero__desc">管理门店使用中桌位，支持按桌位名称筛选查询，可查看桌码、用餐详情、加餐与结账。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="桌位名称">
					<el-input
						class="table-yoncan-page__filter-input"
						@keydown.enter.native="phoneSelect"
						clearable
						placeholder="请输入桌位名称"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item class="table-yoncan-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" @click="getBooksList()">开桌</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="table-yoncan-page__tabs">
			<el-tab-pane label="用餐列表" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.records">
					<el-table-column fixed prop="seatId" label="编号" width="80"></el-table-column>

					<el-table-column prop="seatName" label="桌位名称"></el-table-column>
					<el-table-column prop="sort" label="排序"></el-table-column>

					<el-table-column prop="isEnable" label="是否启用">
						<template slot-scope="scope">
							<span v-if="scope.row.isEnable === 1">启用</span>
							<span v-if="scope.row.isEnable === 0">不启用</span>
						</template>
					</el-table-column>

					<el-table-column fixed="right" prop="status" label="状态">
						<template slot-scope="scope">
							<span class="table-yoncan-page__status-active" v-if="scope.row.status === 1">使用中</span>
							<span class="table-yoncan-page__status-idle" v-if="scope.row.status === 2">空闲中</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="100"></el-table-column>
					<el-table-column label="桌码" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="erweima(scope.row)" style="margin: 5px;">查看
							</el-button>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="180">
						<template slot-scope="scope">
							<el-button type="primary" size="mini" :disabled="!isAuth('mission:qucan')"
								@click="compile(scope.row)">
								详情</el-button>
							<el-button size="mini" type="danger" @click="closeBooks(scope.row)"
								:disabled="!isAuth('mission:delete')">
								结账
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="table-yoncan-page__pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- 添加/修改弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible7" center width="80%">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="addGoods()">加餐
				</el-button>
				<el-table :data="orderGoodsList" v-loading="orderLoding">
					<el-table-column prop="date" label="商品图片" width="100">
						<template slot-scope="scope">
							<img :src="scope.row.goodsPicture.split(',')[0]"
								style="width:80px;height:80px;border-radius: 10px;" alt="" srcset="">
						</template>
					</el-table-column>
					<el-table-column prop="goodsName" label="商品名称" width="180">
					</el-table-column>
					<el-table-column prop="skuMessage" label="商品规格" width="180">
						<template slot-scope="scope">
							{{ scope.row.skuMessage ? scope.row.skuMessage : '单规格商品' }}
						</template>
					</el-table-column>
					<el-table-column prop="goodsNum" label="点餐数量" width="250">
						<template slot-scope="scope">
							<!-- scope.$index -->
							<el-input-number v-model="scope.row.goodsNum"
								@change="handleChange(scope.row, scope.$index)" :min='0' label="点餐数量"></el-input-number>
						</template>
					</el-table-column>
					<el-table-column prop="goodsPrice" label="单价">
						<template slot-scope="scope">
							¥{{ scope.row.goodsPrice ? scope.row.goodsPrice : 0 }}
						</template>
					</el-table-column>
					<el-table-column prop="goodRemarks" label="商品备注" width="180">
						<template slot-scope="scope">
							{{scope.row.goodRemarks}}
							<el-button type="primary" size="mini" plain :disabled="!isAuth('mission:update')"
								@click="selectSpBtn(scope.row)">修改</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="goodsPrice" label="合计">
						<template slot-scope="scope">
							¥{{ (scope.row.goodsPrice * scope.row.goodsNum).toFixed(2) }}
						</template>
					</el-table-column>
				</el-table>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible7 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">结 账</el-button>
				</div>
			</el-dialog>
			<!-- 店铺二维码 -->
			<el-dialog :visible.sync="dialogFormVisible2" center title="桌码">
				<div>
					<img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
					<div style="text-align: center;margin-top: 20px;">
						<el-button type="primary" @click="chakan()">打开窗口查看</el-button>
					</div>
				</div>
			</el-dialog>
			<!-- 加餐弹窗 -->
			<el-dialog title="加餐" :visible.sync="dialogVisibleAdd" center width="70%">
				<div style="display: inline-block;">
					<div style="position: relative;display: inline-block;margin: 10px 0px;">
						<span>商品名称:</span>
						<el-input style="width: 200px;" @keydown.enter.native="getGoodsList" placeholder="请输入商品名称"
							v-model="goodsName">
						</el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 10px 0px;">
						<span>商品分类:</span>
						<el-select v-model="classift" @change="getGoodsList()" placeholder="请选择商品分类">
							<el-option v-for="item in classifyList" :key="item.classifyId" :label="item.classifyName"
								:value="item.classifyId">
							</el-option>
						</el-select>&nbsp;&nbsp;
					</div>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
						@click="getGoodsList">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table :data="goodsList.list" v-loading="goodsLoding">
					<el-table-column prop="date" label="商品图片" width="180">
						<template slot-scope="scope">
							<img :src="scope.row.goodsPicture.split(',')[0]"
								style="width:100px;height:100px;border-radius: 10px;" alt="" srcset="">
						</template>
					</el-table-column>
					<el-table-column prop="goodsName" label="商品名称">
					</el-table-column>
					<el-table-column prop="goodsMoney" label="单价">
						<template slot-scope="scope">
							¥{{ scope.row.goodsMoney ? scope.row.goodsMoney : 0 }}
						</template>
					</el-table-column>
					<el-table-column prop="goodsMoney" label="操作">
						<template slot-scope="scope">
							<el-button type="primary" size="mini" @click="selectGgList(scope.row)"
								:disabled="!isAuth('mission:qucan')">
								选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="goodsSize" @current-change="goodsCurrent"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="goodsPage"
						layout="total,sizes, prev, pager, next" :total="goodsList.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 桌位选择弹窗 -->
			<el-dialog title="选择桌位" :visible.sync="dialogVisibleBook" width="41%">
				<div style="width:100%;display: flex;flex-wrap: wrap;">
					<div v-for="(item, index) in booksList" :style="current == index ? 'border:1px solid #5cc477' : ''"
						@click="current = index" :key="index"
						style="width:100px;height:100px;border-radius: 8px;border: 1px solid #000;display: flex;justify-content: center;align-items: center;margin-right: 10px;cursor: pointer;margin-bottom: 10px;">
						{{ item.seatName }}
					</div>
				</div>
				<span slot="footer" class="dialog-footer">
					<el-button @click="dialogVisibleBook = false">取 消</el-button>
					<el-button type="primary" @click="openBooks()">确 定</el-button>
				</span>
			</el-dialog>
			<!-- 规格选择弹窗 -->
			<el-dialog title="规格选择" :visible.sync="dialogVisibleGg" width="60%">
				<el-table :data="guigeList">
					<el-table-column prop="skuImg" label="商品图片" width="180">
						<template slot-scope="scope">
							<img :src="scope.row.skuImg" style="width:100px;height:100px;border-radius: 10px;" alt=""
								srcset="">
						</template>
					</el-table-column>
					<el-table-column prop="detailJson" label="商品规格">
						<template slot-scope="scope">
							{{ scope.row.detailJson ? scope.row.detailJson : '单规格商品' }}
						</template>
					</el-table-column>
					<el-table-column prop="skuPrice" label="价格">
						<template slot-scope="scope">
							¥{{ scope.row.skuPrice ? scope.row.skuPrice : 0 }}
						</template>
					</el-table-column>
					<el-table-column prop="goodsMoney" label="操作">
						<template slot-scope="scope">
							<el-button type="primary" size="mini" @click="selectGgBtn(scope.row)"
								:disabled="!isAuth('mission:qucan')">
								选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-dialog>
			<!-- 商品备注 -->
			<el-dialog title="商品备注" :visible.sync="dialogVisibleGgBz" width="60%">
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
					<el-input style="width:50%;" v-model="content" type="textarea" :rows="4"
						placeholder="请输入备注"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogVisibleGgBz = false">取 消</el-button>
					<el-button type="primary" @click="selectGg()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 订单备注 -->
			<el-dialog title="商品备注" :visible.sync="dialogVisibleGgSp" width="60%">
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
					<el-input style="width:50%;" v-model="content" type="textarea" :rows="4"
						placeholder="请输入备注"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogVisibleGgBz = false">取 消</el-button>
					<el-button type="primary" @click="selectSp()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				selectGoods: [], //选择的商品
				guigeList: [], //规格列表
				dialogVisibleGg: false,
				current: 0,
				dialogVisibleBook: false,
				booksList: [], //可用桌位列表
				goodsPage: 1,
				goodsLimit: 10,
				goodsLoding: false,
				goodsName: '', //商品名称
				classift: '', //选择的分类
				classifyList: [], //商品分类列表
				goodsList: {}, //商品对象
				dialogVisibleAdd: false,
				limit: 10,
				page: 1,
				shopId: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible2: false,
				dialogFormVisible7: false,
				tableData2: {},
				userName: '',
				titles: '添加',
				seatId: '',
				erweimaImg: '',
				orderGoodsList: [], //订单商品列表
				orderGoodsList2: [], //订单商品列表
				orderLoding: false, //订单加载状态
				orderId: '', //订单id
				dialogVisibleGgBz: false,
				content: '',
				selectGgData: {},
				dialogVisibleGgSp: false,
				goodsId: '',

			}
		},
		methods: {

			selectGgBtn(info) {
				this.selectGgData = info
				this.content = ''
				this.dialogVisibleGgBz = true

			},
			//选择规格后加入订单中
			selectGg() {
				var info = this.selectGgData
				// console.log(info)
				let datas = {
					shopId: this.shopId,
					seatId: this.seatId,
					orderType: 3,
					ordersGoodsVoList: [{
						goodsId: info.goodsId,
						skuId: info.id,
						skuMessage: info.detailJson,
						num: 1,
						goodRemarks: this.content
					}]
				}

				this.$confirm(`确认添加该商品吗？`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/order/insertOrder'),
						method: 'post',
						data: this.$http.adornData(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogVisibleGg = false
									this.dialogVisibleAdd = false
									this.dialogVisibleGgBz = false
									//添加成功后刷新数据
									this.getorderInfo()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {
					this.getorderInfo()
					console.log('取消')
				})

			},
			//选择规格列表
			selectGgList(info) {
				console.log(info)
				this.guigeList = info.sku
				this.dialogVisibleGg = true
			},
			//列表管理员结账
			closeBooks(info) {
				console.log(info)
				this.seatId = info.seatId
				this.getorderInfo('jz')

			},
			//开桌
			openBooks() {
				let datas = {
					seatId: this.booksList[this.current].seatId,
					status: 1,
				}
				this.$http({
					url: this.$http.adornUrl('admin/shopSeat/insertSeat'),
					method: 'post',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '已开桌',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogVisibleBook = false
								this.taskdataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			//根据店铺id获取空闲中的桌位
			getBooksList() {
				let datas = {
					shopId: this.shopId,
					isEnable: 1, //桌位是否启用：1、启用中 0、不启用
					status: 2, //桌位状态：1、正在使用中，2、空闲中
				}
				this.$http({
					url: this.$http.adornUrl('admin/shopSeat/getShopSeatList'),
					method: 'get',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					this.goodsLoding = false
					if (data.code == 0) {
						this.booksList = data.data.records
						this.dialogVisibleBook = true
					} else {
						this.booksList = []
						this.$message({
							message: '暂无空闲中的桌位',
							type: 'error',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			//重置筛选条件
			cleans() {
				this.goodsName = ''
				this.classift = ''
				this.getGoodsList()
			},
			//商品分页
			goodsCurrent(val) {
				this.goodsPage = val
				this.getGoodsList()
			},
			goodsSize(val) {
				this.goodsLimit = val
				this.getGoodsList()
			},
			//根绝店铺id与分类获取商品
			getGoodsList() {
				let datas = {
					status: 0,
					classifyId: this.classift, //分类id
					goodsName: this.goodsName, //商品名称
					shopId: this.shopId,
					page: this.goodsPage,
					limit: this.goodsLimit
				}
				this.goodsLoding = true
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/selectGoodsByShopId'),
					method: 'get',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					this.goodsLoding = false
					if (data.code == 0) {
						this.goodsList = data.data
					} else {
						this.goodsList = {}
					}
				})
			},
			//获取店铺的商品分类
			getClassIfyList() {
				let datas = {
					shopId: this.shopId
				}
				this.$http({
					url: this.$http.adornUrl('admin/takeoutGoods/selectAllClassify'),
					method: 'get',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.classifyList = data.data
					} else {
						this.classifyList = []
					}
				})
			},
			//加餐弹窗
			addGoods() {
				this.getClassIfyList()
				this.getGoodsList()
				this.dialogVisibleAdd = true
			},
			//返回
			prev() {
				this.$router.back()
			},
			//修改商品数量
			handleChange(info, index) {
				let num = 0;
				let type = 1;
				if (info.goodsNum > this.orderGoodsList2[index].goodsNum) { //大于则是增加了
					num = info.goodsNum - this.orderGoodsList2[index].goodsNum
					type = 1
				} else { //减少了
					num = this.orderGoodsList2[index].goodsNum - info.goodsNum
					type = 2
				}
				let datas = {
					orderGoodsId: info.id,
					type: type, //1:加 2:减
					num: num,
					shopId: this.shopId
				}
				this.$confirm(type == 1 ? `确定要增加该商品数量吗?` : `确定要减少该商品数量吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/order/updateGoodsNum'),
						method: 'get',
						params: this.$http.adornParams(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.getorderInfo()
									this.taskdataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {
					this.getorderInfo()
					console.log('取消')
				})
			},

			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},
			// tabs切换
			handleClick(tab, event) {

				if (tab._props.label == '桌码列表') {
					this.page = 1
					this.limit = 10
					this.taskdataSelect()
				}
			},


			// 筛选
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.userName = ''
				this.page = 1
				this.taskdataSelect()
			},

			// 获取接单数据列表
			taskdataSelect() {
				this.shopId = this.$route.query.shopId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/shopSeat/getShopSeatList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'seatName': this.userName,
						'status': 1,
						'shopId': this.shopId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
				})
			},
			// 添加弹框
			add() {
				this.titles = '添加'
				this.seatId = ''
				this.dialogFormVisible7 = true
			},
			// 用餐详情
			compile(rows) {
				console.log(rows, '==========')
				this.titles = '用餐详情'
				this.seatId = rows.seatId
				this.getorderInfo()

			},
			//根据桌位id获取订单详情
			getorderInfo(type) {
				this.orderLoding = true
				this.$http({
					url: this.$http.adornUrl('admin/order/getSeatOrder'),
					method: 'get',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'seatId': this.seatId,
						'shopId': this.shopId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data) {
							this.orderId = data.data.orderId
							this.orderGoodsList = data.data.orderGoodsList
							this.orderGoodsList2 = JSON.parse(JSON.stringify(data.data.orderGoodsList))
						} else {
							this.orderGoodsList = []
							this.orderGoodsList2 = []
						}
						if (!type) {
							this.orderLoding = false
							this.dialogFormVisible7 = true
						} else {
							this.refuseto()
						}


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
			// 管理员结账
			refuseto() {
				this.$confirm(`确认后将完成订单，确认吗？`, '提示', {
					confirmButtonText: '确认',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					if (this.orderId) { //是否有订单id，如果有则走订单的完成罗姐
						this.$http({
							url: this.$http.adornUrl('admin/order/adminCompleteOrder'),
							method: 'post',
							params: this.$http.adornParams({
								'orderId': this.orderId
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
										this.dialogFormVisible7 = false
										this.taskdataSelect()
									}
								})
							} else {
								this.$message({
									message: data.msg,
									type: 'error',
									duration: 1500,
									onClose: () => {}
								})
							}
						})
					} else { //如果没有订单id，则调用修改桌位的状态接口，把桌位状态修改为空闲中
						this.$http({
							url: this.$http.adornUrl('admin/shopSeat/insertSeat'),
							method: 'post',
							params: this.$http.adornParams({
								seatId: this.seatId,
								status: 2,
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
										this.dialogFormVisible7 = false
										this.taskdataSelect()
									}
								})
							} else {
								this.$message({
									message: data.msg,
									type: 'error',
									duration: 1500,
									onClose: () => {}
								})
							}
						})
					}

				}).catch(() => {
					console.log('取消')
				})
			},
			// 查看二维码
			erweima(row) {

				console.log('row.shopId', row.shopId)

				this.$http({
					url: this.$http.adornUrl('invite/shopSeatQr'),
					method: 'get',
					params: this.$http.adornParams({
						'seatId': row.seatId,
						'shopId': row.shopId
					})
				}).then(({
					data
				}) => {
					if (data) {
						this.erweimaImg = data
						this.dialogFormVisible2 = true
						console.log(data, '获取二维码')
					} else {
						this.$message({
							message: '生成二维码失败，请重试',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			chakan() {
				window.open(this.erweimaImg, '_blank');
			},
			// 修改备注弹框
			selectSpBtn(info) {
				this.goodsId = info.id
				this.content = ''
				this.dialogVisibleGgSp = true

			},
			selectSp(info) {

				let datas = {
					id: this.goodsId,
					goodRemarks: this.content,
				}
				this.$confirm(`确认修改该订单的备注吗？`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/order/updateGoodRemarks'),
						method: 'post',
						// data: this.$http.adornData(datas)
						params: this.$http.adornParams(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogVisibleGgSp = false
									//添加成功后刷新数据
									this.getorderInfo()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {
					this.getorderInfo()
					console.log('取消')
				})

			},
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.taskdataSelect()

		},
		watch: {
			'$route': 'taskdataSelect'
		}
	}
</script>

<style scoped>
.table-yoncan-page__back {
	margin-bottom: 12px;
}

.table-yoncan-page__back-link {
	display: inline-flex;
	align-items: center;
	font-size: 14px;
	color: #606266;
	text-decoration: none;
}

.table-yoncan-page__back-link:hover {
	color: #5cc477;
}

.table-yoncan-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	margin-right: 4px;
}

.table-yoncan-page__filter-input {
	width: 200px;
}

.table-yoncan-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.table-yoncan-page__tabs {
	margin-bottom: 0;
}

.table-yoncan-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.table-yoncan-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.table-yoncan-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.table-yoncan-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.table-yoncan-page__status-active {
	color: #5cc477;
}

.table-yoncan-page__status-idle {
	color: #999;
}

.table-yoncan-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>