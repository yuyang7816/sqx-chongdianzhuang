<template>
	<div class="nf-page monthly-card-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">卡券管理</span>
			<h2 class="nf-page-hero__title">卡券管理</h2>
			<p class="nf-page-hero__desc">管理月卡与次卡配置，支持按名称、状态、类型、服务项目筛选，可添加、修改、删除及赠送卡券。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="名称">
					<el-input
						class="monthly-card-page__filter-input"
						@keydown.enter.native="phoneSelect"
						clearable
						placeholder="请输入标题名称"
						v-model="cardNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="状态">
					<el-select
						class="monthly-card-page__filter-select"
						v-model="statusT"
						placeholder="请选择状态"
						@change="phoneSelect(status)">
						<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="类型">
					<el-select
						class="monthly-card-page__filter-select"
						v-model="cardTypeT"
						placeholder="请选择类型"
						@change="phoneSelect(status)">
						<el-option v-for="item in cardTypeList" :key="item.value" :label="item.title" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="服务项目">
					<el-select
						class="monthly-card-page__filter-select"
						v-model="orderTakingId"
						filterable
						placeholder="请选择服务项目"
						@change="phoneSelect(status)">
						<el-option v-for="item in homeData" :key="item.value" :label="item.myLevel" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="monthly-card-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('monthlyCard:add')" @click="addNotice">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="cardId" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="cardType" label="类型">
					<template slot-scope="scope">
						<div v-if="scope.row.cardType==1">月卡</div>
						<div v-if="scope.row.cardType==2">次卡</div>
					</template>
				</el-table-column>
				<el-table-column prop="cardName" label="名称">
				</el-table-column>
				<el-table-column prop="num" label="数量">
				</el-table-column>
				<el-table-column prop="price" label="价格">
				</el-table-column>
				<el-table-column prop="status" label="服务项目">
					<template slot-scope="scope">
						<div v-if="scope.row.orderTakingList">
							<span v-for="(item,index) in scope.row.orderTakingList" :key="index">
								{{item?item.myLevel:''}};
							</span>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<span class="monthly-card-page__status-on" v-if="scope.row.status==1">上架</span>
						<span v-if="scope.row.status==2">下架</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="160">
					<template slot-scope="scope">
						<el-button style="margin: 3px;" :disabled="!isAuth('couponYhq:zengsong')" size="mini" type="primary"
							icon="document" @click="songCoupon(scope.row)">赠送</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('monthlyCard:update')"
							@click="amendBanner(scope.$index, scope.row)" style="margin: 3px;">修改
						</el-button>
						<el-button style="margin: 3px;" size="mini" type="danger" :disabled="!isAuth('monthlyCard:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="monthly-card-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>
		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<!-- <div style="margin-bottom: 10px;display: flex;">
    		<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
    		<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
    			<el-upload
    				class="avatar-uploader"
    				v-model="memberImg"
    				:action="$http.adornUrlUp('alioss/upload')"
    				:show-file-list="false"
    				:on-success="handleAvatarSuccess"
    				>
    				<img v-if="memberImg" :src="memberImg" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
    				<i v-else class="el-icon-plus avatar-uploader-icon"></i>
    			</el-upload>
    		</div>
    	</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
				<!-- <el-input style="width:50%;" v-model="cardType" placeholder="请输入类型"></el-input> -->
				<el-radio-group v-model="cardType" @change="cardTypebtn(cardType)">
					<el-radio :label="1">月卡</el-radio>
					<el-radio :label="2">次卡</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="cardName" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">数量：</span>
				<el-input style="width:50%;" v-model="num" type="number" placeholder="请输入数量"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">价格：</span>
				<el-input style="width:50%;" v-model="price" type="number" placeholder="请输入价格"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务项目：</span>
				<el-checkbox-group v-model="checkList" @change="onChang" style="display: inline;" :max="3">
					<el-checkbox v-for="(item,index) in homeData" :key="index" :label="item.id">
						{{item.myLevel}}
					</el-checkbox>
				</el-checkbox-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<!-- <el-input style="width:50%;" v-model="status" placeholder="请输入状态"></el-input> -->
				<el-radio-group v-model="status">
					<el-radio :label="1">上架</el-radio>
					<el-radio :label="2">下架</el-radio>
				</el-radio-group>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 赠送-->
		<el-dialog title="赠送" :visible.sync="dialogFormVisible0" center>
			<div style="margin: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
				<el-tag :key="tag" v-for="(tag,j) in userNameList" closable :disable-transitions="false"
					@close="couponIdClose1(tag,j)">
					{{tag}}
				</el-tag>
				<el-button class="button-new-tag" size="small" @click="couponIdBtn1(couponIds)">添加</el-button>
			</div>
			<div style="margin: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">赠送数量：</span>
				<el-input style="width: 150px;" clearable placeholder="请输入赠送数量" v-model="num"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible0 = false">取 消</el-button>
				<el-button type="primary" @click="songNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible5" center>
			<div style="display: inline-block;margin-top: 5px;">
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入邀请人邀请码"
					v-model="inviterCode">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入手机号"
					v-model="phone">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入昵称"
					v-model="nickName">
				</el-input>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select1">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans1">重置
				</el-button>
			</div>
			<el-button style="margin-left:15px;" :disabled="xuanzhong.length <= 0 " size="mini" type="primary"
				icon="document" @click="closes2()">确定</el-button>
			<el-table v-loading="tableDataLoading1" @selection-change="changeFun1" :data="userData.list"
				ref="multipleTable"> <!-- :row-key="(row)=>{ return row.userId}" -->
				<el-table-column :reserve-selection="true" type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="userId" label="id" width="80">
				</el-table-column>
				<el-table-column fixed prop="userName" label="昵称" width="120">
					<template slot-scope="scope">
						<span class="monthly-card-page__link"
							@click="updates(scope.row)">{{ scope.row.userName ? scope.row.userName : '未设置' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span class="monthly-card-page__link" @click="updates(scope.row)">
							{{ scope.row.phone ? scope.row.phone : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="invitation" label="邀请人邀请码">
					<template slot-scope="scope">
						<span>{{ scope.row.invitation ? scope.row.invitation : '-' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="member" label="是否是会员">
					<template slot-scope="scope">
						<span v-if="scope.row.member==1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				tableDataLoading: true,
				dialogFormVisible: false,
				tableData: [],
				cardId: '',
				cardType: '',
				cardName: '',
				num: '',
				price: '',
				orderTakingIds: [],
				memberImg: '',
				memberId: '',
				sort: '',
				status: '',
				title: '添加',
				homeData: [],
				checkList: [], //多选 选中集合
				cardNameT: '',
				statuss: [{
						id: 1,
						title: '上架'
					},
					{
						id: 2,
						title: '下架'
					},
				],
				statusT: '',
				cardTypeT: '',
				cardTypeList: [{
						id: 1,
						title: '月卡'
					},
					{
						id: 2,
						title: '次卡'
					},
				],
				orderTakingId: '',
				// 赠送优惠券
				size1: 10,
				page1: 1,
				tableDataLoading1: false,
				dialogFormVisible0: false,
				dialogFormVisible5: false,
				dialogFormVisible4: false,
				couponIds: [],
				couponIdList: [],
				userIdList: [],
				userNameList: [],
				isCoupon: 1,
				userData: [],
				nun: '',
				types: '',
				phone: '',
				nickName: '',
				member: -1,
				memberList: [{
						value: -1,
						title: '全部'
					},
					{
						value: 0,
						title: '否'
					},
					{
						value: 1,
						title: '是'
					},
				],
				inviterCode: '',
				xuanzhong: [],

			}
		},
		methods: {
			phoneSelect() {
				this.page = 1
				this.dataSelect()
			},
			cleans2() {
				this.page = 1
				this.cardTypeT = ''
				this.cardNameT = ''
				this.statusT = ''
				this.orderTakingId = ''

				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('card/selectCardList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'cardType': this.cardTypeT,
						'cardName': this.cardNameT,
						'status': this.statusT,
						'orderTakingId': this.orderTakingId,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.memberImg = file.data
			},
			addNotice() {
				this.homeSelect()
				this.cardId = ''
				this.cardName = ''
				this.num = ''
				this.price = ''
				this.status = ''
				this.cardType = ''
				this.checkList = []
				this.orderTakingIds = []
				this.title = '添加'
				this.dialogFormVisible = true
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.dataSelect1()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.dataSelect1()
			},
			// 添加banner图
			addNoticeTo() {
				if (this.cardType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}
				if (this.cardName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				// if(this.cardType==1){
				if (this.num == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入数量',
						type: 'warning'
					});
					return
				}
				// }else{
				// 	this.num =1
				// }
				if (this.price == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入价格',
						type: 'warning'
					});
					return
				}
				if (this.checkList.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择服务项目',
						type: 'warning'
					});
					return
				}
				if (this.status == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					});
					return
				}
				this.classify = 1
				if (this.title == '添加') {
					var url = 'card/insertCard'
				} else {
					var url = 'card/updateCard'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'cardType': this.cardType,
						'cardId': this.cardId,
						'cardName': this.cardName,
						'num': this.num,
						'price': this.price,
						'status': this.status,
						'orderTakingIds': this.checkList.toString(),

					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
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
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			// 修改
			amendBanner(index, rows, a) {
				this.homeSelect()
				if (a == 1) {
					this.pl = 1
				} else {
					this.pl = 0
				}
				this.dialogFormVisible = true
				this.cardId = rows.cardId
				this.num = rows.num
				this.cardName = rows.cardName
				this.price = rows.price
				this.status = rows.status
				this.cardType = rows.cardType
				var arr = []
				for (var i in rows.orderTakingIds.split(',')) {
					console.log(i, rows.orderTakingIds.split(',')[i])
					arr.push(Number(rows.orderTakingIds.split(',')[i]))
				}
				this.orderTakingIds = arr
				this.checkList = arr
				console.log('this.orderTakingIds', this.orderTakingIds)
				this.title = '修改'
			},
			//删除一级
			deletes(row) {
				let delid = row.cardId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`card/deleteCard?cardId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取服务启用列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`orderTaking/queryAllOrderTaking`),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 50,
						'userName': '',
						'gameId': '',
						'status': '',
						'city': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.homeData = data.data.list
				})
			},
			// 获取分类id
			onChang(e) {
				// this.gameId = e.toString()
				this.checkList = e
			},
			cardTypebtn(cardType) {
				if (cardType == 1) {
					this.num = ''
				} else {
					this.num = ''
				}
			},
			// 赠送优惠券
			songCoupon(row) {
				this.userIdList = []
				this.couponIds = row.cardId
				this.userNameList = []
				this.couponIdList = []
				this.types = ''
				this.num = ''
				this.dialogFormVisible0 = true
			},
			// 赠送优惠券
			songNoticeTo() {
				if (this.num == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入赠送数量',
						type: 'warning'
					});
					return
				}
				// if (this.types == 1) {
				if (this.userIdList.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					});
					return
				}
				// }
				// if (this.couponIds.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择卡券',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				var userIdList = this.userIdList.toString()
				// var couponIds = this.couponIds.toString()
				this.$http({
					url: this.$http.adornUrl('card/giveUserCard'),
					method: 'post',
					params: this.$http.adornParams({
						'userIds': userIdList,
						'cardId': this.couponIds,
						'num': this.num
					})
				}).then(({
					data
				}) => {
					if (data.code == 0 || data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.userIdList = []
								this.couponIds = []
								this.userNameList = []
								this.couponIdList = []
								this.types = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible0 = false
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
			// 获取用户数据列表
			dataSelect1() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						// 'userType': this.userType,
						'phone': this.phone,
						'userName': this.nickName,
						'inviterCode': this.inviterCode,
						'platform': '',
						'member': this.member
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			select1() {
				this.page1 = 1
				this.dataSelect1()
			},
			// 重置
			cleans1() {
				this.page1 = 1
				this.phone = ''
				this.member = -1
				this.nickName = ''
				this.inviterCode = ''
				this.dataSelect1()
			},
			// 确定选中
			queding(row) {
				this.userName = row.userName
				this.userId = row.userId
				this.dialogFormVisible2 = false
			},
			// 打开用户列表
			couponIdBtn1(rows) {
				this.page1 = 1
				this.phone = ''
				this.member = -1
				this.nickName = ''
				this.inviterCode = ''
				this.dataSelect1()
				this.dialogFormVisible5 = true
			},
			// 多选用户
			changeFun1(val) {
				console.log('多选', val)
				this.xuanzhong = val
				// var userIdList = []
				// var userNameList = []
				// for (var i in val) {
				// 	userIdList.push(val[i].userId)
				// 	userNameList.push(val[i].userName)
				// }
				// this.userIdList = userIdList
				// this.userNameList = userNameList
			},
			// 批量开启
			closes2(id) {
				this.isCoupon = 1
				var userIdList = []
				var userNameList = []
				for (var i in this.xuanzhong) {
					if (this.userIdList.length > 0) {
						this.userIdList.push(this.xuanzhong[i].userId)
					}
					if (this.userIdList.length > 0) {
						this.userNameList.push(this.xuanzhong[i].userName)
					}
					userIdList.push(this.xuanzhong[i].userId)
					userNameList.push(this.xuanzhong[i].userName)
				}
				if (this.userIdList.length == 0) {
					this.userIdList = userIdList
					this.userNameList = userNameList
				}
				this.dialogFormVisible5 = false
				this.$refs.multipleTable.clearSelection();
			},
			// 删除用户
			couponIdClose1(ietm, index) {
				console.log('index', index, 'ietm', ietm, this.userIdList.length)
				if (this.userIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一个用户',
						type: 'warning'
					});
					return
				} else {
					this.userIdList.splice(index, 1)
					this.userNameList.splice(index, 1)
				}
			},
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
			this.homeSelect()
		}
	}
</script>

<style scoped>
.monthly-card-page__filter-input {
	width: 200px;
}

.monthly-card-page__filter-select {
	width: 150px;
}

.monthly-card-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.monthly-card-page__status-on {
	color: #5cc477;
}

.monthly-card-page__link {
	color: #5cc477;
	cursor: pointer;
}

.monthly-card-page__link:hover {
	color: #3fae5c;
}

.monthly-card-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>