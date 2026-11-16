<template>
	<div class="nf-page about-the-ball-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">话题论坛</span>
			<h2 class="nf-page-hero__title">约球记录</h2>
			<p class="nf-page-hero__desc">查看与管理约球记录，支持按状态、用户、台球馆、对局类型等条件筛选查询。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="状态">
					<el-select
						class="about-the-ball-page__filter-select"
						v-model="statusT"
						@change="select()">
						<el-option v-for="item in homeData1" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="用户昵称">
					<el-input
						class="about-the-ball-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入用户昵称"
						v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="手机号">
					<el-input
						class="about-the-ball-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="phoneT">
					</el-input>
				</el-form-item>
				<el-form-item label="台球馆名称">
					<el-input
						class="about-the-ball-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入台球馆名称"
						v-model="tearoomNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="对局类型">
					<el-select
						class="about-the-ball-page__filter-select"
						v-model="dateBallClassifyT"
						@change="select()">
						<el-option v-for="item in homeData" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="约球类型">
					<el-select
						class="about-the-ball-page__filter-select"
						v-model="ballTypeT"
						@change="select()">
						<el-option v-for="(item,index) in ballTypeList" :key="index" :label="item" :value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="about-the-ball-page__filter-actions">
					<el-button type="primary" icon="el-icon-refresh" @click="select">刷新</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button
						type="primary"
						icon="el-icon-plus"
						:disabled="!isAuth('aboutTheBall:add')"
						@click="addsBtn()">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="dateBallId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="发起用户">
					<template slot-scope="scope">
						<span
							v-if="scope.row.userName"
							class="about-the-ball-page__user-link"
							@click="updatesvideo(scope.row.userId)">
							{{ scope.row.userName ? scope.row.userName : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="联系电话">
				</el-table-column>
				<el-table-column prop="tearoomName" label="台球馆名称">
				</el-table-column>
				<el-table-column prop="dateBallClassify" label="对局类型">
					<template slot-scope="scope">
						<span v-if="scope.row.dateBallClassify==1" class="about-the-ball-page__status--danger">公开</span>
						<span v-if="scope.row.dateBallClassify==2" class="about-the-ball-page__status--brand">单约</span>
					</template>
				</el-table-column>
				<el-table-column prop="ballType" label="约球类型">
				</el-table-column>
				<el-table-column prop="ballNum" label="约球局数">
				</el-table-column>
				<el-table-column prop="payType" label="付费方式">
				</el-table-column>
				<el-table-column prop="ballTime" label="约球时间">
				</el-table-column>
				<el-table-column prop="peopleNum" label="邀请人数">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1" class="about-the-ball-page__status--danger">待开始</span>
						<span v-if="scope.row.status==2" class="about-the-ball-page__status--brand">进行中</span>
						<span v-if="scope.row.status==3" class="about-the-ball-page__status--danger">已结束</span>
						<span v-if="scope.row.status==4" class="about-the-ball-page__status--muted">已取消</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="warning" @click="chengyuanBtn(scope.row)">约球成员</el-button>
						<el-button size="mini" type="primary"
							:disabled="!isAuth('aboutTheBall:update')" @click="addsBtn(scope.row)">修改</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('aboutTheBall:delete')"
							@click="deleteOrder(scope.row)">删除</el-button>
						<el-button size="mini" type="primary" plain
							:disabled="!isAuth('aboutTheBall:quxiao')" v-if="scope.row.status==1"
							@click="cancelOrder(scope.row,2)">取消</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="about-the-ball-page__pagination">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]"
					:page-size="limit"
					:current-page="page"
					layout="total,sizes, prev, pager, next,jumper"
					:total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>
		<el-dialog title="约球成员" :visible.sync="dialogFormVisibleYq" center width="80%">
			<!-- <div style="margin:5px;display: inline-block;">
				<span>用户昵称：</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectYy" clearable placeholder="请输入用户昵称"
					v-model="userNameU"></el-input>
				&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:5px;display: inline-block;">
				<span>手机号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectYy" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>
				&nbsp;&nbsp;&nbsp;&nbsp;
			</div> -->
			<div style="margin:10px;display: inline-block;">
				<el-button size="mini" type="primary" @click="selectYy()">刷新</el-button>
				<!-- <el-button size="mini" type="primary" @click="clearYy()">重置</el-button> -->
			</div>
			<el-table v-loading="tableDataLoadingPt" :data="tableDataUder.list">
				<el-table-column  prop="dateBallId" label="编号" width="80"></el-table-column>
				<el-table-column  prop="userName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null||scope.row.avatar==''" src="../../assets/img/avatar.png"
							alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<!-- <el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="age" label="年龄">
				</el-table-column> -->
				<el-table-column prop="createTime" label="创建时间"> 
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1" style="color: #f56c6c;">待签到</span>
						<span v-if="scope.row.status==2" style="color: #5cc477;">已签到</span>
						<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
						<span v-if="scope.row.status==4" style="color: #999;">已取消</span>
					</template>
				</el-table-column>
				<!-- <el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="xuanze(scope.row)">选择</el-button>
					</template>
				</el-table-column> -->
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeYy" @current-change="handleCurrentChangeYy"
					:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataUder.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 添加、修改 弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">选择用户：</span>
				<el-input style="width:50%;" v-model="userNames" @focus="aiteBtn()" type="text"
					placeholder="请选择选择用户"></el-input>
			</div>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">对局类型：</span>
				<el-radio-group v-model="dateBallClassify">
					<el-radio :label="1">公开</el-radio>
					<el-radio :label="2">单约</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">约球类型：</span>
				<el-radio-group v-model="ballType">
					<el-radio v-for="(item,index) in ballTypeList" :key="index" :label="item">{{item}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">约球局数：</span>
				<el-radio-group v-model="ballNum">
					<el-radio v-for="(item,index) in ballNumList" :key="index" :label="item">{{item}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">付费方式：</span>
				<el-radio-group v-model="payType">
					<el-radio v-for="(item,index) in payTypeList" :key="index" :label="item">{{item}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">约球时间：</span>
				<el-date-picker style="width: 50%;" v-model="ballTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择自动发布时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">台球馆：</span>
				<el-select v-model="tearoomId" placeholder="请选择台球馆" style="width:50%;" @change="shopClick()">
					<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
						:value="item.tearoomId">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">联系电话：</span>
				<el-input v-model="phone" type="text" style="width:50%;" placeholder="请输入联系电话"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">邀请人数：</span>
				<el-input v-model="peopleNum" type="text" style="width:50%;" placeholder="请输入邀请人数"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="用户列表" :visible.sync="dialogFormVisibleUser" center width="75%">
			<div style="margin:5px;display: inline-block;">
				<span>用户昵称：</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectUser" clearable placeholder="请输入用户昵称"
					v-model="userNameU"></el-input>
				&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:5px;display: inline-block;">
				<span>手机号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectUser" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>
				&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:10px;display: inline-block;">
				<el-button size="mini" type="primary" @click="selectUser()">查询</el-button>
				<el-button size="mini" type="primary" @click="clearUser()">重置</el-button>
			</div>
			<el-table v-loading="tableDataLoadingPt" :data="tableDataUder.list" height="600px">
				<el-table-column fixed prop="userId" label="编号"></el-table-column>
				<el-table-column fixed prop="userName" label="昵称" width="150">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null||scope.row.avatar==''" src="../../assets/img/avatar.png"
							alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="sex" label="性别">
					<template slot-scope="scope">
						<span v-if="scope.row.sex==1">男</span>
						<span v-else-if="scope.row.sex==2">女</span>
						<span v-else>未设置</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span style="color: #5cc477;cursor: pointer;">
							{{ scope.row.phone ? scope.row.phone : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="xuanze(scope.row)">选择</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
					:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataUder.totalCount">
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
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				tearoomNameT: '',
				userNameT: '',
				phoneT: '',
				statusT: '',
				ballTypeT: '',
				dateBallClassifyT: '',
				homeData1: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '待开始'
					},
					{
						id: 2,
						name: '进行中'
					},
					{
						id: 3,
						name: '已结束'
					},
					{
						id: 4,
						name: '已取消'
					}
				],
				homeData: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '公开'
					},
					{
						id: 2,
						name: '单约'
					},
				],
				dialogFormVisibleYq: false,
				tableDataLoadingPt: false,
				tableDataUder: {},
				limitU: 10,
				pageU: 1,
				userNameU: '',
				phoneU: '',
				dateBallId: '',
				dialogFormVisibleUser: false,
				titles: '添加',
				userNames: '',
				userId: '',
				tearoomName: '', //台球馆
				tearoomId: '',
				status: 1,
				phone: '', //联系电话
				peopleNum: '', //邀请人数
				dateBallId: '',
				dateBallClassify: '', //对局类型 1公开 2单约
				ballType: '', //约球类型
				ballTime: '', //约球时间
				ballNum: '', //约球局数
				payType:'',
				shopData: [], //台球馆列表
				ballTypeList: [],
				ballNumList: [],
				payTypeList: [],

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

			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.tearoomNameT = ''
				this.userNameT = ''
				this.phoneT = ''
				this.statusT = ''
				this.ballTypeT = ''
				this.dateBallClassifyT = ''
				this.dataSelect()
				this.xianshi()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('dateBall/selectDateBallList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.statusT,
						'ballType': this.ballTypeT,
						'tearoomName': this.tearoomNameT,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'dateBallClassify': this.dateBallClassifyT,
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
						url: this.$http.adornUrl('dateBall/updateDateBall'),
						method: 'post',
						data: this.$http.adornData({
							'dateBallId': row.dateBallId,
							'status': 4,
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
				})
			},
			// 删除
			deleteOrder(row) {
				this.$confirm('确定删除此数据', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('dateBall/deleteDateBall'),
						method: 'post',
						params: this.$http.adornParams({
							'dateBallId': row.dateBallId
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
				})
			},
			// 约球成员 弹框
			chengyuanBtn(row) {
				this.dateBallId = row.dateBallId
				this.pageU = 1
				this.userNameU = ''
				this.phoneU = ''
				this.dataSelectYy()
				this.dialogFormVisibleYq = true
			},
			// 获取约球成员
			dataSelectYy() {
				this.tableDataLoadingPt = true
				this.$http({
					url: this.$http.adornUrl('dateBall/selectDateBallPeopleList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'dateBallId': this.dateBallId,
						'userName': this.userNameU,
						'phone': this.phoneU,
						'userId': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUder = returnData
					this.tableDataLoadingPt = false
				})
			},
			selectYy() {
				this.pageU = 1
				this.dataSelectYy()
			},
			clearYy() {
				this.pageU = 1
				this.userNameU = ''
				this.phoneU = ''
				this.dataSelectYy()
			},
			handleSizeChangeYy(val) {
				this.limitU = val;
				this.dataSelectYy()
			},
			handleCurrentChangeYy(val) {
				this.pageU = val;
				this.dataSelectYy()
			},
			// 获取用户列表
			aiteBtn() {
				this.userNameU = ''
				this.phoneU = ''
				this.pageU = 1
				this.dataSelectUser()
				this.dialogFormVisibleUser = true
			},
			selectUser() {
				this.pageU = 1
				this.dataSelectUser()
			},
			clearUser() {
				this.pageU = 1
				this.userNameU = ''
				this.phoneU = ''
				this.dataSelectUser()
			},
			// 获取平台用户
			dataSelectUser() {
				
				
				this.tableDataLoadingPt = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'isVip': '',
						'userName': this.userNameU,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': '',
						'phone': this.phoneU,
						'status': '',
						'isRate': '',
						'moneyUser': '',
						'vipIndex': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUder = returnData
					this.tableDataLoadingPt = false
				})
			},
			handleSizeChangeU(val) {
				this.limitU = val;
				this.dataSelectUser()
			},
			handleCurrentChangeU(val) {
				this.pageU = val;
				this.dataSelectUser()
			},
			// 选择用户
			xuanze(row) {
				this.userNames = row.userName
				this.userId = row.userId
				this.dialogFormVisibleUser = false
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
			xianshi() {
				// 约球类型
				this.$http({
					url: this.$http.adornUrl('common/type/922'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {

						if (data.data.value) {
							this.ballTypeList = data.data.value.split(',')
						} else {
							this.ballTypeList = []
						}
					}
				})
				// 约球局数	933
				this.$http({
					url: this.$http.adornUrl('common/type/933'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.value) {
							this.ballNumList = data.data.value.split(',')
						} else {
							this.ballNumList = []
						}
					}
				})
				// 付费方式 939
				this.$http({
					url: this.$http.adornUrl('common/type/939'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.value) {
							this.payTypeList = data.data.value.split(',')
						} else {
							this.payTypeList = []
						}
					}
				})
			},
			// 添加、修改 弹框
			addsBtn(row) {
				if (row) {
					this.titles = '修改'
					this.userNames = row.userName
					this.userId = row.userId
					this.tearoomName = row.tearoomName
					this.tearoomId = row.tearoomId
					this.status = row.status
					this.phone = row.phone
					this.peopleNum = row.peopleNum
					this.dateBallId = row.dateBallId
					this.dateBallClassify = row.dateBallClassify
					this.ballType = row.ballType
					this.ballTime = row.ballTime
					this.ballNum = row.ballNum
					this.payType = row.payType
				} else {
					this.titles = '添加'
					this.userNames = ''
					this.userId = ''
					this.tearoomName = ''
					this.tearoomId = ''
					this.status = 1
					this.phone = ''
					this.peopleNum = ''
					this.dateBallId = ''
					this.dateBallClassify = 1
					this.ballType = ''
					this.ballTime = ''
					this.ballNum = ''
					this.payType = ''
				}
				this.shopSelect()
				this.dialogFormVisible = true
			},
			// 确认
			refuseto() {
				if (this.userId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					});
					return
				}
				if (this.phone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入联系电话',
						type: 'warning'
					});
					return
				}
				if (this.peopleNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选输入邀请人数',
						type: 'warning'
					});
					return
				}
				if (this.ballType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择约球类型',
						type: 'warning'
					});
					return
				}
				if (this.ballNum == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择约球局数',
						type: 'warning'
					});
					return
				}
				if (this.ballTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择约球时间',
						type: 'warning'
					});
					return
				}
				if (this.tearoomId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择台球馆',
						type: 'warning'
					});
					return
				}
				if (this.titles == '添加') {
					var url = 'dateBall/insertDateBall'
				} else {
					var url = 'dateBall/updateDateBall'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						'tearoomId': this.tearoomId,
						'status': this.status,
						'phone': this.phone,
						'peopleNum': this.peopleNum,
						'dateBallId': this.dateBallId,
						'dateBallClassify': this.dateBallClassify,
						'ballType': this.ballType,
						'ballTime': this.ballTime,
						'ballNum': this.ballNum,
						'payType':this.payType,
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
								this.dialogFormVisible = false
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
		},
		mounted() {
			this.dataSelect()
			this.xianshi()
		}
	};
</script>

<style scoped>
.about-the-ball-page__filter-select {
	width: 150px;
}

.about-the-ball-page__filter-input {
	width: 200px;
}

.about-the-ball-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.about-the-ball-page__user-link {
	color: #5cc477;
	cursor: pointer;
}

.about-the-ball-page__user-link:hover {
	color: #3fae5c;
}

.about-the-ball-page__status--brand {
	color: #5cc477;
}

.about-the-ball-page__status--danger {
	color: #f56c6c;
}

.about-the-ball-page__status--muted {
	color: #999;
}

.about-the-ball-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>