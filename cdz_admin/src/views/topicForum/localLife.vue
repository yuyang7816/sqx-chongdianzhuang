<template>
	<div class="nf-page local-life-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">话题论坛</span>
			<h2 class="nf-page-hero__title">本地生活</h2>
			<p class="nf-page-hero__desc">查看与管理本地生活资讯，支持按台球馆名称、资讯名称筛选查询，可添加、修改与删除资讯内容。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="台球馆名称">
					<el-input
						class="local-life-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入台球馆名称"
						v-model="storeNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="资讯名称">
					<el-input
						class="local-life-page__filter-input"
						@keydown.enter.native="selectList"
						clearable
						placeholder="请输入资讯名称"
						v-model="tearoomPhoneT">
					</el-input>
				</el-form-item>
				<el-form-item class="local-life-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="selectList">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" @click="addtea()">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.records">
			<el-table-column fixed="left" prop="informationId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="tearoomName" fixed="left" label="台球馆名称" width="120">
			</el-table-column>
			<el-table-column prop="phone" label="电话">
			</el-table-column>
			<el-table-column prop="informationName" label="资讯名称">
			</el-table-column>
			<el-table-column prop="informationCoverImage" label="封面图">
				<template slot-scope="scope">
					<span v-if="scope.row.informationCoverImage == null">暂无图片</span>
					<img v-else :src="scope.row.informationCoverImage" alt="" width="40" height="40">
				</template>
			</el-table-column>
			<el-table-column prop="informationImage" label="详情图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.informationImage">
						<span v-for="(item,index) in scope.row.informationImage.split(',')">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: 300px" :src="item" alt="">
							</el-popover>
						</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="informationLabel" label="标签">
			</el-table-column>
			<el-table-column prop="informationPrice" label="价格">
			</el-table-column>
			<el-table-column prop="store_address" label="地址" width="200">
				<template slot-scope="scope">
					<div>
						{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}
					</div>
					<div>经度：{{scope.row.longitude ? scope.row.longitude : '未绑定'}}</div>
					<div>纬度：{{scope.row.latitude ? scope.row.latitude : '未绑定'}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="informationDetails" label="详情">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.informationDetails}}</div>
						<div style="width: 300px;">{{scope.row.informationDetails}}</div>
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="status" fixed="right" label="状态" width="80">
				<template slot-scope="scope">
					<span class="local-life-page__status--brand" v-if="scope.row.status == 1">正常</span>
					<span class="local-life-page__status--danger" v-if="scope.row.status == 2">下架</span>
					<!-- <el-switch v-model="scope.row.ban_status" @change="change(scope.row.ban_status,scope.row)"
						:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
						inactive-color="#ff4949">
					</el-switch> -->
				</template>
			</el-table-column>

			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" width="200" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary"
						:disabled="!isAuth('localLife:update')" @click="addtea(scope.row)">修改</el-button>
					<el-button size="mini" type="danger" v-if="scope.row.status ==1"
						:disabled="!isAuth('localLife:delete')" @click="deblocking(scope.row)">删除
					</el-button>

				</template>
			</el-table-column>
		</el-table>
		<div class="local-life-page__pagination">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next" :total="tableData.total">
			</el-pagination>
		</div>
		</div>

		<!-- 添加弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible1" center width="80%">
			<el-form>
				<div class="shop_item">
					<div class="shop_content" style="max-width: 800px;margin: auto;">
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">选择台球馆：</span>
							<el-select v-model="tearoomId" placeholder="请选择台球馆" style="width:50%;">
								<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
									:value="item.tearoomId">
								</el-option>
							</el-select>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">电话:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入电话" v-model="phone"
								autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">资讯名称:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入资讯名称"
								v-model="informationName" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<div style="display: flex;align-items: center;margin: 2% 0;">
								<span style="display: inline-block;text-align: left;width: 120px;">封面图：</span>
								<div
									style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
									<el-upload class="avatar-uploader" v-model="informationCoverImage"
										:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
										:on-success="handleAvatarSuccess3">
										<img v-if="informationCoverImage" :src="informationCoverImage" class="avatar"
											style="width: 148px;height: 148px;border-radius:6px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											style="font-size: 28px;color: #8c939d"></i>
									</el-upload>
								</div>
							</div>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<div style="display: flex;align-items: center;margin: 2% 0;">
								<span style="display: inline-block;text-align: left;width: 120px;">详情图：</span>
								<div style="display: flex;">
									<div v-for="(item,index) of informationImage" :key="index"
										style="display: inline-block;">
										<div style="position: relative;margin: 5px;">
											<img :src="item" class="avatar"
												style="width:148px;height: 148px;border-radius: 6px;" />
											<div @click="clear(index)" class="divhove" v-if="informationImage">
												<i class="el-icon-delete"
													style="margin: 0;font-size: 18px;color: #fff;"></i>
											</div>
										</div>
									</div>
									<el-upload class="avatar-uploader"
										style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
										:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
										:on-success="handleUploadSuccess" :on-change="handleChange">
										<i class="el-icon-plus"></i>
									</el-upload>
								</div>
							</div>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="width: 120px;text-align: left;display:inline-block;">价格:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入价格" v-model="informationPrice"
								autosize></el-input>
						</div>
						<div class="content_item" style="position: relative;margin: 10px 0;">
							<span style="display: inline-block;text-align: left;width: 120px;">状态:</span>
							<div class="margin15" style="position: absolute;top: 0;">
								<el-radio-group v-model="status" style="margin-left: 123px;">
									<el-radio :label="1">正常</el-radio>
									<el-radio :label="2">下架</el-radio>
								</el-radio-group>
							</div>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">详情:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入详情"
								v-model="informationDetails" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">资讯标签:</span>
							<div style="display: inline-block;width: auto;">
								<span v-if="informationLabel" style="display: inline-block;width: auto;">
									<el-tag v-for="(item,index) of informationLabel" :key="index" type="info" closable
										@close="bqClose(index)">{{item}}</el-tag>
								</span>
								<el-input style="width:133px;" class="margin15" placeholder="请输入标签" v-model="bq"
									autosize @keydown.enter.native="addBq"></el-input>
								<el-button type="primary" plain @click="addBq()">确定</el-button>
							</div>
						</div>

						<div class="content_item" style="margin-bottom: 10px;">
							<span span style="display: inline-block;text-align: left;width: 120px;">城市：</span>
							<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr"
								v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55"
								:value="storeAddre">
							</el-cascader>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">详细地址:</span>
							<el-input style="width:50%;" class="margin15" placeholder="请输入详细地址" v-model="address"
								autosize @keydown.enter.native="selectDz"></el-input>
							<el-button type="info" plain @click="selectDz()">获取经纬度</el-button>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">经度:</span>
							<el-input style="width:50%;" class="margin15" disabled placeholder="请输入经度"
								v-model="longitude" autosize></el-input>
						</div>
						<div class="content_item" style="margin-bottom: 10px;">
							<span style="display: inline-block;text-align: left;width: 120px;">纬度:</span>
							<el-input style="width:50%;" class="margin15" disabled placeholder="请输入纬度"
								v-model="latitude" autosize></el-input>

						</div>
						<div>
							<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
						</div>
					</div>
				</div>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="addteaclassroom()">确 定</el-button>
			</div>
		</el-dialog>
		
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
	var geocoder, map, markersArray = [];
	export default {
		data() {
			return {
				tearoomAddress: '',
				tearoomLatitude: '',
				tearoomLongitude: '',
				tearoomProvince: '',
				options: regionData,
				tearoomCity: '',
				tearoomDistrict: '',
				storeAddress: [],
				storeAddre: '请选择城市',
				storephone: '',
				tearoomTitleImage: '',
				tearoomCoverImage: [],
				storeNameT: '',
				tearoomPhoneT: '',
				shipAddress: '',
				margin_charge: '',
				banstatus: '',
				tearoomLabel: [],
				bq: '',
				img: '',
				store_time: '',
				startTime: '',
				endTime: '',
				size: 10,
				page: 1,
				tearoomName: '',
				moneyScope: '',
				isReserve: '',
				isFaceRecognition: '',
				isMusic: 1,
				isWelfare: '', //是否开启爱心活动
				welfareContent: '', //爱心活动介绍

				userNameT: '',
				form: {
					storeId: '',
					merchantId: '',
					tearoomName: '',
					tearoomTitleImage: '',
					shipAddress: '',
					refundAddress: '',
					refundContact: '',
					refundMobile: '',
					createTime: ''
				},
				isshop: true,
				formLabelWidth: '200px',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: [],
				openValue: 1,
				closeValue: 2,

				userId: '',
				phone1: '',
				nickName: '',
				nickName1: '', //门店用户
				nickName2: '',
				nickName3: '', //团长用户
				userIds: {}, //用户列表
				size1: 10,
				page1: 1,
				dialogFormVisible3: false,
				tableDataLoading4: false,

				cashPledge: '',
				hourPrice: '',
				minutePrice: '',
				subscription: '',
				keyCode: '',

				tearoomId: '',
				tableDataKt: {},
				tableDataLoadingKt: false,
				dialogFormVisibleKt2: false,
				openValue: 1,
				closeValue: 2,

				roomId: '',
				roomType: '',
				roomName: '',
				roomNo: '',
				isOpen: 2,
				token: '',
				id: '',
				title: '',
				limit: 10,
				yyToken: '',
				yyId: '',
				shopPoiId: '',
				wifiNo: '',
				wifiPwd: '',
				rate: '',
				broadcastContent: '',
				cameraId: '',
				qunImg: '',
				wxCode: '',
				welfareImg: '',
				isOpen: '',
				shopType: 1, //店铺类型 1自营 2三方
				isVipPrivilege: 1, //是否享受会员权益  1是 2否
				shopTypeT: '',
				isVipPrivilegeT: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '自营'
					},
					{
						value: 2,
						label: '三方'
					},
				],
				isVipPrivilegenum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					},
				],
				shopData: [],
				titles: '',

				informationId: 1,
				informationName: "", //资讯名称
				informationLabel: "", //资讯标签
				informationPrice: "", //价格
				informationCoverImage: "", //封面图
				informationImage: "", //详情图
				informationDetails: "", //详情
				province: "", //省
				city: "", //市
				district: "", //区
				address: "", //详细地址
				longitude: "", //经度
				latitude: "", //纬度
				phone: "", //联系方式
				tearoomId: 1, //台球门店id
				status: 1, //状态 1正常 2下架

			}
		},
		methods: {
			//上传成功
			handleAvatarSuccess3(file, fileList) {
				console.log(file)
				this.informationCoverImage = file.data
			},
			
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.informationImage.push(file.data)
			},
			handleChange(e) {
				console.log(e)
				// this.hideUpload = fileList.length >= this.limit;
			},
			
			// 删除
			clear(index) {
				this.informationImage.splice(index, 1);
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
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			
			// 查询
			selectList() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.storeNameT = ''
				this.tearoomPhoneT = ''
				this.userNameT = ''
				this.shopTypeT = ''
				this.isVipPrivilegeT = ''


				this.dataSelect()
			},
			
			// 删除
			deblocking(row, index) {
				
				this.$confirm(`确定删除此数据?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('information/deleteInformation'),
						method: 'post',
						params: this.$http.adornParams({
						// data: this.$http.adornParams({
							'informationId': row.informationId,
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
						}
					})
				}).catch(() => {})
			},

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('information/selectInformationByList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'tearoomName': this.storeNameT,
						'city': '',
						'informationName': this.tearoomPhoneT,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 修改状态
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/storecomplain/status_store'),
					method: 'post',
					params: this.$http.adornParams({
						'store_id': row.store_id,
						'status': row.status
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
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			// 添加标签
			addBq() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.informationLabel.push(this.bq)
					this.bq = ''
				}
			},
			// 删除标签
			bqClose(index) {
				this.informationLabel.splice(index, 1);
			},
			// 获取省市区
			handleChange55(value) {

				console.log(this.$refs['cascaderAddr'].currentLabels)
				value = this.$refs['cascaderAddr'].currentLabels
				if (value) {

					this.province = value[0]
					this.city = value[1]
					this.district = value[2]
					// this.form.city = value[1]
					if (this.city == '市辖区') {
						this.city = this.province
					}
				}
			},
			addtea(row) {
				if (row) {
					this.titles = '修改'
					this.informationId = row.informationId
					this.informationName = row.informationName
					if (row.informationLabel) {
						this.informationLabel = row.informationLabel.split(',')
					} else {
						this.informationLabel = []
					}

					this.informationPrice = row.informationPrice
					this.informationCoverImage = row.informationCoverImage
					if (row.informationImage) {
						this.informationImage = row.informationImage.split(',')
					} else {
						this.informationImage = []
					}
					this.informationDetails = row.informationDetails
					this.province = row.province
					this.city = row.city
					this.district = row.district
					this.address = row.address
					this.longitude = row.longitude
					this.latitude = row.latitude
					this.phone = row.phone
					this.tearoomId = row.tearoomId
					this.status = row.status

					this.storeAddress = row.province + '/' + row.city + '/' + row.district
					this.storeAddre = '请选择城市'
				} else {
					this.titles = '添加'
					this.informationId = ''
					this.informationName = ''
					this.informationLabel = []
					this.informationPrice = ''
					this.informationCoverImage = ''
					this.informationImage = []
					this.informationDetails = ''
					this.province = ''
					this.city = ''
					this.district = ''
					this.address = ''
					this.longitude = ''
					this.latitude = ''
					this.phone = ''
					this.tearoomId = ''
					this.status = 1

					this.storeAddress = []
					this.storeAddre = '请选择城市'
				}
				this.getMyLocation();
				this.dialogFormVisible1 = true
			},
			addteaclassroom() {
				if (this.tearoomId == '') {
					this.$notify({
						message: '请选择台球馆',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.phone == '') {
					this.$notify({
						message: '请填写电话',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.informationName == '') {
					this.$notify({
						message: '请填写资讯名称',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.address == '') {
					this.$notify({
						message: '请完善位置信息,经纬度进行选择',
						type: 'error',
						duration: 1500
					})
					return
				}

				// if (this.tearoomLabel == '') {
				// 	this.$notify({
				// 		message: '请填写台球馆标签',
				// 		type: 'warning',
				// 		duration: 1500
				// 	})
				// 	return
				// }

				if (this.titles == '添加') {
					var url = 'information/insertInformation'
				} else {
					var url = 'information/updateInformation'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornParams({
						'informationId': this.informationId,
						'informationName': this.informationName,
						'informationLabel': this.informationLabel.toString(),
						'informationPrice': this.informationPrice,
						'informationCoverImage': this.informationCoverImage,
						'informationImage': this.informationImage.toString(),
						'informationDetails': this.informationDetails,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'address': this.address,
						'longitude': this.longitude,
						'latitude': this.latitude,
						'phone': this.phone,
						'tearoomId': this.tearoomId,
						'status': this.status,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible1 = false
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
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ", "充电桩");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}
				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng
						.getLng() + '&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response)
						that.address = response.result.formatted_addresses.recommend
						that.province = response.result.ad_info.province
						that.city = response.result.ad_info.city
						that.district = response.result.ad_info.district





					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					var marker = new qq.maps.Marker({
						map: map,
						position: event.latLng
					});
					markersArray.push(marker);
				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			selectDz() {

				console.log('搜索地址：', this.province + this.city + this.district + this.address)
				var addr = this.province + this.city + this.district + this.address //
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
			// 获取门店数据
			shopSelect() {
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
					let returnData = data.data.list;
					this.shopData = returnData
				})
			},
		},
		mounted() {
			this.dataSelect()
			this.shopSelect()
		}
	}
</script>

<style scoped="scoped">
.local-life-page__filter-input {
	width: 160px;
}

.local-life-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.local-life-page__status--brand {
	color: #5cc477;
}

.local-life-page__status--danger {
	color: #f56c6c;
}

.local-life-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

	.divhove {
		position: absolute;
		width: 100%;
		height: 100%;
		left: 0;
		top: 0;
		opacity: 0;
		cursor: default;
		text-align: center;
		padding-top: 50%;
		border-radius: 6px;
		background-color: rgba(0, 0, 0, .5);
		-webkit-transition: opacity .3s;
		transition: opacity .3s;
	}

	.el-tag--medium {
		margin-right: 10px;
	}

	.divhove:hover {
		opacity: 0.8;
	}

	.text_color span {
		margin-right: 5px;
	}

	.shop_item {
		/* margin-top: 60px; */
		padding-left: 20px;
	}

	/* 规格 */
	.customWidth {
		width: 60% !important;
	}

	.ruleitem {
		padding-left: 10px;
		margin-top: 15px;
	}

	.ruleitem .ruleicon {
		position: relative;
		top: 3px;
		left: 2px;
		width: 1.2em;
		height: 1.2em;
	}

	.el-tag+.el-tag {
		margin-left: 10px;
	}

	.button-new-tag {
		height: 32px;
		line-height: 31px;
		padding-top: 0;
		padding-bottom: 0;
		border-radius: 4px;
		border-top-left-radius: 0;
		border-bottom-left-radius: 0;
		background-color: #f5f7fa;
		position: relative;
		left: -5px;
		border-color: #dcdfe6;
		border: 1px solid #dcdfe6;
	}

	.input-new-tag {
		width: 90px;
		margin-left: 10px;
		vertical-align: bottom;
	}

	.el-input--small .el-input__inner {
		border-top-right-radius: 0;
		border-bottom-right-radius: 0;
	}

	/* switch按钮样式 */
	::v-deep .switchClass .el-switch__label {
		position: absolute;
		display: none;
		color: #fff !important;
	}

	/*打开时文字位置设置*/
	::v-deep .switchClass .el-switch__label--right {
		z-index: 1;
	}

	/* 调整打开时文字的显示位子 */
	::v-deep .switchClass .el-switch__label--right span {
		margin-right: 13px;
		font-size: 12px;

	}

	/*关闭时文字位置设置*/
	::v-deep .switchClass .el-switch__label--left {
		z-index: 1;
	}

	/* 调整关闭时文字的显示位子 */
	::v-deep .switchClass .el-switch__label--left span {
		margin-left: 17px;
		font-size: 12px;
	}

	/*显示文字*/
	::v-deep .switchClass .el-switch__label.is-active {
		display: block;
	}

	/* 调整按钮的宽度 */
	::v-deep .switchClass.el-switch .el-switch__core,
	.el-switch .el-switch__label {
		width: 42px !important;
		margin: 0;
	}
</style>