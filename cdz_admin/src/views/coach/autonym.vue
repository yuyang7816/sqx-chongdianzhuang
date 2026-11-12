<template>
	<div class="nf-page autonym-jl-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练模块</span>
			<h2 class="nf-page-hero__title">教练认证</h2>
			<p class="nf-page-hero__desc">查看与管理教练实名认证申请，支持按状态、姓名、电话筛选，可审核通过/拒绝或修改认证信息。</p>
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
						class="autonym-jl-page__filter-select"
						v-model="status"
						@change="select()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<!-- <el-form-item label="项目类型">
					<el-select
						class="autonym-jl-page__filter-select"
						v-model="classifyId"
						@change="select()">
						<el-option v-for="item in fwData" :key="item.id" :label="item.value" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item label="姓名">
					<el-input
						class="autonym-jl-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入姓名"
						v-model="userName">
					</el-input>
				</el-form-item>
				<el-form-item label="电话">
					<el-input
						class="autonym-jl-page__filter-input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入电话"
						v-model="phoneT">
					</el-input>
				</el-form-item>
				<el-form-item class="autonym-jl-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" class="autonym-jl-page__tabs" @tab-click="handleClick">
			<el-tab-pane label="认证列表" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<!-- <el-table-column prop="classifyName" label="项目类型" width="150">
					</el-table-column> -->
					<el-table-column prop="name" label="姓名" width="100">
						<template slot-scope="scope">
							<span
								class="autonym-jl-page__user-link"
								@click="updates(scope.row)">{{scope.row.name ? scope.row.name : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="cardFront" label="头像" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别" width="50">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-if="scope.row.sex==2">女</span>
						</template>
					</el-table-column>
					<el-table-column prop="birthdate" label="生日" width="100">
					</el-table-column>
					<el-table-column prop="phone" label="电话" width="150">
					</el-table-column>
					<el-table-column prop="idNumber" label="身份证号码" width="200">
					</el-table-column>
					<el-table-column prop="cardFront" label="身份证正面/营业执照" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.front == null || scope.row.front == ''">
								暂无图片
							</div>
							<div v-if="scope.row.front" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.front" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.front" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="cardBack" label="身份证反面" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.back == null || scope.row.back == ''">
								暂无图片
							</div>
							<div v-if="scope.row.back" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.back" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.back" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="certification" label="资格证书" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.certification == null || scope.row.certification == ''">
								暂无图片
							</div>
							<div v-for="(item,index) in scope.row.certification" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="imagePhoto" label="形象照" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.imagePhoto == null || scope.row.imagePhoto == ''">
								暂无图片
							</div>
							<div v-for="(item,index) in scope.row.imagePhoto" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="address" label="地址" width="160">
					</el-table-column>
					<el-table-column prop="individualResume" label="简介" width="160">
					</el-table-column>
					<el-table-column prop="remek" label="审核内容" width="160">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed='right' prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 0">待审核</span>
							<span v-if="scope.row.status == 1">审核成功</span>
							<span v-if="scope.row.status == 2">已拒绝</span>
						</template>
					</el-table-column>
					<el-table-column fixed='right' label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary"
								:disabled="scope.row.status !== 0 || !isAuth('autonymJl:jujue')"
								@click="refuseClick(scope.row)" style="margin: 5px;">
								拒绝
							</el-button>
							<el-button size="mini" type="primary"
								:disabled="scope.row.status !== 0 || !isAuth('autonymJl:tongguo')"
								@click="passClicks(scope.row)" style="margin: 5px;">通过
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('autonymJl:tongguo')"
								@click="updateClicks(scope.row)" style="margin: 5px;">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="pinglundelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="autonym-jl-page__pagination">
					<el-pagination
						@size-change="handleSizeChange"
						@current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]"
						:page-size="limit"
						:current-page="page"
						layout="total,sizes, prev, pager, next,jumper"
						:total="tableData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
		</el-tabs>

		<!-- 任务拒绝弹框 -->
		<el-dialog title="审核拒绝" :visible.sync="dialogFormVisible" center>
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
			</el-dialog>
			<el-dialog title="修改认证信息" :visible.sync="dialogFormVisibleR" center width="80%">
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">项目类型：</span>
					<el-select v-model="classifyIds" style="width:50%;margin-left: 10px;">
						<el-option v-for="item in fwData" :key="item.id" :label="item.value" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
					<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入姓名"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">电话：</span>
					<el-input style="width:50%;" v-model="phone" type="text" placeholder="请输入电话"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">性别：</span>
					<el-radio-group v-model="sex">
						<el-radio :label="1">男</el-radio>
						<el-radio :label="2">女</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">生日：</span>
					<el-date-picker style="width: 50%;margin-left: 10px;" v-model="birthdate" align="right"
						type="date"  format="yyyy年MM月dd日" value-format="yyyy年MM月dd日" placeholder="选择日期">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">地址：</span>
					<el-input style="width:50%;" v-model="address" type="text" placeholder="请输入地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">身份证号：</span>
					<el-input style="width:50%;" v-model="idNumber" type="text" placeholder="请输入身份证号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">简介：</span>
					<el-input style="width:50%;" v-model="individualResume" type="text" placeholder="请输入简介"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="avatar"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess5">
							<img v-if="avatar" :src="avatar" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">身份证正面图：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="front"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="front" :src="front" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">身份证反面图：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="back"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="back" :src="back" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">资格证书：</span>
					<div class="imgs" v-for="(item,index) in certification" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear1(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload  :action="$http.adornUrlUp('alioss/upload')"
							list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess3"
							:on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">形象照：</span>
					<div class="imgs" v-for="(item,index) in imagePhoto" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrlUp('alioss/upload')"
							list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess4"
							:on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleR = false">取 消</el-button>
					<el-button type="primary" @click="refusetoUp()">确 定</el-button>
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
				classify: 1,
				classlyList: [{
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '个人'
				}, {
					value: 2,
					label: '企业'
				}],
				userName: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				ids: '',
				phoneT: '',
				fwData: [],
				classifyId: '',
				
				// ````
				birthdate:'',
				classifyIds:'',
				phone: '',
				name: '',
				sex:'',
				idNumber: '',
				front: '',
				back: '',
				avatar:'',
				address:'',
				individualResume:'',
				userId: '',
				dialogFormVisibleR: false,
				userIds:'',
				percentage1: 0,
				certification:[],
				imagePhoto:[],
				info: {
					stockDate: this.getNowTime(), //日期
				},
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '认证列表') {
					this.classify = 1
					this.dataSelect()
				}
				if (tab._props.label == '企业认证') {
					this.classify = 2
					this.dataSelect()
				}
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
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
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			//任务通过
			passClick(id) {
				this.status = 1
				this.content = '同意'
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl(
						`student/auditStudentAuthentication/${ids}/${this.status}/${this.content}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					}
				})
			},
			//单条任务通过
			passClicks(row) {
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('admin/realname/auditRealName'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'id': row.id,
						'remek': '通过'
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
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					}
				})
			},
			//任务拒绝
			refuseClick(rows) {
				this.status = 2
				this.helpTakeId = rows.userId
				this.ids = rows.id
				this.content = ''
				this.dialogFormVisible = true
			},
			refuseto() {
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/realname/auditRealName'),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'id': this.ids,
						'remek': this.content
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.content = ''
							this.status = ''
							this.dataSelect()
						}
					})
				})
			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.status = ''
				this.userName = ''
				this.phoneT = ''
				this.classifyId = ''
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/realname/selectRealNameList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.status,
						'userName': this.userName,
						'phone': this.phoneT,
						'classifyId': this.classifyId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					for (var i in this.tableData.list) {
						if (this.tableData.list[i].imagePhoto) {
							this.tableData.list[i].imagePhoto = this.tableData.list[i].imagePhoto.split(',')
						}
						if (this.tableData.list[i].certification) {
							this.tableData.list[i].certification = this.tableData.list[i].certification.split(',')
						}
					}
				})
			},
			// 项目类型
			fwSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '服务类型',
						'status':1
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.fwData = returnData
				})
			},
			handleAvatarSuccess1(file, fileList) {
				this.front = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.back = file.data
			},
			handleAvatarSuccess5(file, fileList) {
				this.avatar = file.data
			},
			
			//上传成功
			handleUploadSuccess3(file, fileList) {
				this.certification.push(file.data)
				console.log('this.contentImg', this.certification)
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			//上传成功
			handleUploadSuccess4(file, fileList) {
				this.imagePhoto.push(file.data)
				console.log('this.contentImg', this.imagePhoto)
			},
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			// 删除
			clear1(index) {
				this.certification.splice(index, 1);
			},
			clear2(index) {
				this.imagePhoto.splice(index, 1);
			},
			updateClicks(row) {
				this.birthdate = row.birthdate
				this.ids = row.id
				this.classifyIds = row.classifyId
				this.name = row.name
				this.sex = row.sex
				this.address = row.address
				this.individualResume = row.individualResume
				this.percentage1 = 0
				this.certification = row.certification
				this.imagePhoto = row.imagePhoto
				this.phone = row.phone
				this.idNumber = row.idNumber
				this.front = row.front
				this.avatar = row.avatar
				this.back = row.back
				this.userId = row.userId
				this.dialogFormVisibleR = true
			},
			refusetoUp() {
				this.$http({
					url: this.$http.adornUrl('admin/realname/updateRealName'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.userId,
						'classifyId': this.classifyIds,
						'name': this.name,
						'sex':this.sex,
						'address':this.address,
						'individualResume':this.individualResume,
						'certification':this.certification.toString(),
						'imagePhoto':this.imagePhoto.toString(),
						'phone':this.phone,
						'sex':this.sex,
						'idNumber': this.idNumber,
						'front': this.front,
						'avatar':this.avatar,
						'back': this.back,
						'birthdate': this.birthdate,
						'id':this.ids
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
								this.dialogFormVisibleR = false
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
			// 删除
			pinglundelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/realname/deleteRealName?id='+delid),
						method: 'post',
						params: this.$http.adornData({})
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
		},
		mounted() {
			// this.fwSelect()
			this.dataSelect()
		}
	};
</script>

<style scoped>
.autonym-jl-page__filter-select {
	width: 150px;
}

.autonym-jl-page__filter-input {
	width: 200px;
}

.autonym-jl-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.autonym-jl-page__tabs {
	margin-bottom: 0;
}

.autonym-jl-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.autonym-jl-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.autonym-jl-page__user-link {
	color: #5cc477;
	cursor: pointer;
}

.autonym-jl-page__user-link:hover {
	color: #3fae5c;
}

.autonym-jl-page__pagination {
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
</style>
