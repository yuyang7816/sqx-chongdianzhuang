<template>
	<div class="nf-page comments-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">动态评论库</span>
			<h2 class="nf-page-hero__title">动态评论库</h2>
			<p class="nf-page-hero__desc">管理虚拟评论分类与评论内容，支持分类维护、按分类筛选评论，可刷新、查询、添加、编辑与删除。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card" v-show="activeName === 'first'">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="comments-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="select">刷新</el-button>
				<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('comments:add')" @click="addNotice">添加分类</el-button>
			</div>
		</div>

		<div class="nf-filter-card" v-show="activeName === 'second'">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="评论分类">
					<el-select
						class="comments-page__filter-select"
						v-model="classifyId"
						placeholder="请选择类型"
						@change="articleSelect(classifyId)">
						<el-option v-for="(item,index) in classifyIds" :key="item.index"
							:label="item.virtualCommentClassifyName" :value="item.virtualCommentClassifyId">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="comments-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="selectW">查询</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('comments:add')" @click="addNoticewz">添加评论</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="comments-page__tabs">
		<el-tab-pane label="分类管理" name="first">
			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableDataF.list">
				<el-table-column fixed prop="virtualCommentClassifyId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="virtualCommentClassifyName" label="分类名称">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('comments:update')"
							@click="updates(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('comments:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			</div>
			<!-- 添加弹框 -->
			<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
					<el-input style="width:50%;" v-model="helpClassifyName" placeholder="请输入分类名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input-number size="medium" v-model="sort"></el-input-number>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改弹框 -->
			<el-dialog title="修改分类" :visible.sync="dialogFormVisible1" center>
				<el-form :model="form">
					<el-form-item label="分类名称：" :label-width="formLabelWidth">
						<el-input v-model="form.helpClassifyName" style="width:65%;"></el-input>
					</el-form-item>
				</el-form>
				<el-form :model="form">
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input-number size="medium" v-model="form.sort"></el-input-number>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="评论列表" name="second">
			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="virtualCommentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="virtualCommentContent" label="内容">
					<template slot-scope="scope">
						<div
							style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
							{{scope.row.virtualCommentContent}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="virtualCommentImg" label="图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.virtualCommentImg == null || scope.row.virtualCommentImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.virtualCommentImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.virtualCommentImg" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.virtualCommentImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="sort" label="排序" width="140">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="160">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" plain @click="talkAboutEr(scope.row)" style="margin: 5px;">回复列表
            </el-button> -->

						<el-button size="mini" type="primary" :disabled="!isAuth('comments:update')"
							@click="updateswz(scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('comments:delete')"
							@click="deleteswz(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="comments-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			</div>
			<!-- 添加弹框 -->
			<el-dialog title="添加评论" :visible.sync="dialogFormVisible2" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">评论分类：</span>
					<el-select v-model="classifyIdFl" placeholder="请选择类型" style="width: 80%;">
						<el-option v-for="(item,index) in classifyIds" :key="item.index"
							:label="item.virtualCommentClassifyName" :value="item.virtualCommentClassifyId">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">排序：</span>
					<el-input-number size="medium" v-model="sort"></el-input-number>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">评论内容：</span>
					<el-input style="width:80%;" v-model="helpWordContent" type="textarea" :rows="5"
						placeholder="请输入评论内容"></el-input>
					<!-- <quill-editor ref="myTextEditor" v-model="helpWordContent" :options="quillOption"
						style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
					</quill-editor> -->
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">图片：</span>
					<div
						style=" display: inline-block;width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="picture" :action="$http.adornUrlUp('alioss/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess">
							<img v-if="picture" :src="picture" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改弹框 -->
			<el-dialog title="修改评论" :visible.sync="dialogFormVisible3" center width="80%">
				<el-form :model="form">
					<el-form-item label="评论分类：" :label-width="formLabelWidth">
						<el-select v-model="formwz.helpClassifyId" placeholder="请选择类型" style="width:80%;">
							<el-option v-for="(item,index) in classifyIds" :key="item.index"
								:label="item.virtualCommentClassifyName" :value="item.virtualCommentClassifyId">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input-number size="medium" v-model="formwz.sort"></el-input-number>
					</el-form-item>
					<el-form-item label="评论内容：" :label-width="formLabelWidth">
						<el-input style="width:80%;" v-model="formwz.helpWordContent" type="textarea" :rows="5"
							placeholder="请输入评论内容"></el-input>
						<!-- <quill-editor ref="myTextEditor" v-model="formwz.helpWordContent" :options="quillOption"
							style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
						</quill-editor> -->
					</el-form-item>
					<el-form-item label="图片：" :label-width="formLabelWidth">
						<div
							style=" display: inline-block;width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="picture"
								:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
								:on-success="handleAvatarSuccess">
								<img v-if="picture" :src="picture" class="avatar" style="width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"
									style="font-size: 28px;color: #8c939d"></i>
							</el-upload>
						</div>
					</el-form-item>

				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTowz()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<!-- 回复列表 -->
		<el-dialog title="回复列表" :visible.sync="dialogFormVisibleHf" center width="80%">
			<el-button size="mini" type="primary" :disabled="!isAuth('comments:update')" @click="updatesHf()">回复
			</el-button>
			<el-table v-loading="tableDataLoadingHf" :data="pinglunDataHf.list">
				<el-table-column fixed prop="virtualCommentId" label="编号" width="80"></el-table-column>
				<!-- <el-table-column prop="byNickName" label="回复用户" width="100">
        </el-table-column>
        <el-table-column prop="imageUrl" label="用户头像" width="100">
          <template slot-scope="scope">
            <img v-if="scope.row.byImageUrl" :src="scope.row.imageUrl" alt="" width="80px" height="80">
          </template>
        </el-table-column> -->
				<el-table-column prop="virtualCommentContent" label="回复内容">
				</el-table-column>
				<el-table-column prop="virtualCommentImg" label="图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.virtualCommentImg == null || scope.row.virtualCommentImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.virtualCommentImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.virtualCommentImg" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.virtualCommentImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" :disabled="!isAuth('comments:update')"
              @click="updatesHf(scope.row)">回复
            </el-button> -->
						<el-button size="mini" type="danger" :disabled="!isAuth('comments:delete')"
							@click="pldeletesEr(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeHf" @current-change="handleCurrentChangeHf"
					:page-sizes="[10, 20, 30, 40]" :page-size="sizeHf" :current-page="pageHf"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunDataHf.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 回复 -->
		<el-dialog title="回复" :visible.sync="dialogFormVisibleHf2" center width="70%">
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">回复内容：</span>
				<el-input style="width:50%;" v-model="content" @input="checkAtSymbol" type="textarea" :rows="4"
					placeholder="请输入回复内容">
				</el-input><br />
				<div style="color: #999;cursor: pointer;margin-left: 200px;" @click="aiteBtn()">@用户</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" display: inline-block;width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="picture" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="picture" :src="picture" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleHf2 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 回复 -->
		<el-dialog title="平台用户" :visible.sync="dialogFormVisiblePt" center width="75%">
			<el-table v-loading="tableDataLoadingPt" :data="tableDataUder.list" height="600px">
				<el-table-column fixed prop="userId" label="编号"></el-table-column>
				<el-table-column fixed prop="userName" label="昵称" width="150">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
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
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
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

	</el-tabs>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../selfShop/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				limit: 10,
				page: 1,
				state: 1,
				sort: 0,
				remark: '',
				createTime: '',
				helpClassifyName: '',
				helpClassifyId: '',
				classifyId: 1,
				classifyIds: [],
				form: {
					helpClassifyId: '',
					state: '',
					sort: '',
					helpClassifyName: '',
					createTime: '',
					remark: ''
				},
				helpWordTitle: '',
				helpWordContent: '',
				classifyIdFl: '',
				formwz: {
					virtualCommentId: '',
					picture: '',
					articleUrl: '',
					helpWordContent: '',
					helpWordTitle: '',
					helpClassifyId: '',
					createTime: '',
					sort: 0,
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				picture: '',
				picture2: '',
				articleUrl: '',

				title: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: {},
				tableDataF: {},
				parentIdnum: '',
				helpClassifyNameT: '',
				classnum: [{
						value: '0',
						label: '一级分类'
					},
					{
						value: '1',
						label: '二级分类'
					}
				],
				value: [],
				quillOption: quillConfig,
				helpWordTitleT: '',

				dialogFormVisibleHf: false,
				tableDataLoadingHf: false,
				pinglunDataHf: {},
				pageHf: 1,
				sizeHf: 10,
				dialogFormVisibleHf2: false,
				content: '',
				tableDataUder: {},
				limitU: 10,
				pageU: 1,
				userName: '',
				dialogFormVisiblePt: false,
				tableDataLoadingPt: false,
				userId: '',

			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
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
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '分类管理') {
					this.dataSelect()
				}
				if (tab._props.label == '评论列表') {
					// this.classifyId = 1
					this.dataSelectW()
					this.articleSelect()
				}
			},
			handleSizeChange(val) {
				this.limit = val;
				this.articleSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.articleSelect()
			},
			handleSizeChangeHf(val) {
				this.sizeHf = val
				this.pinglunSelectHf()
			},
			handleCurrentChangeHf(val) {
				this.pageHf = val
				this.pinglunSelectHf()
			},
			handleSizeChangeU(val) {
				this.limitU = val;
				this.dataSelectUser()
			},
			handleCurrentChangeU(val) {
				this.pageU = val;
				this.dataSelectUser()
			},
			handleAvatarSuccess(file) {
				this.picture = file.data;
			},
			handleAvatarSuccess2(file2) {
				this.formwz.picture = file2.data;
			},
			// 添加分类弹框
			addNotice() {
				this.helpClassifyName = ''
				this.sort = 0
				this.picture = ''
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo() {
				if (this.helpClassifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('virtualComment/insertVirtualCommentClassify'),
					method: 'post',
					data: this.$http.adornData({
						'sort': this.sort,
						'virtualCommentClassifyName': this.helpClassifyName,

						'parentId': 0,
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
			// 编辑分类弹框
			updates(index, rows) {
				// this.form.state = rows.state;
				this.form.sort = rows.sort;
				this.dialogFormVisible1 = true;
				this.form.helpClassifyId = rows.virtualCommentClassifyId;
				this.form.helpClassifyName = rows.virtualCommentClassifyName;
				// this.form.remark = rows.remark;
				this.form.createTime = rows.createTime;
			},
			// 修改类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('virtualComment/updateVirtualCommentClassify'),
					method: 'post',
					data: this.$http.adornData({
						'virtualCommentClassifyId': this.form.helpClassifyId,
						'virtualCommentClassifyName': this.form.helpClassifyName,
						'sort': this.form.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible1 = false
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
			// 删除分类
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							`virtualComment/deleteVirtualCommentClassify?virtualCommentClassifyId=${row.virtualCommentClassifyId}`
						),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
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
				}).catch(() => {})
			},
			// 添加评论弹框
			addNoticewz() {
				this.helpWordTitle = ''
				this.articleUrl = ''
				this.helpWordContent = ''
				this.picture = ''
				this.sort = 0
				this.dialogFormVisible2 = true
			},
			// 添加评论
			releasNoticeTowz() {
				if (this.classifyIdFl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择评论分类',
						type: 'warning'
					});
					return
				}

				if (this.helpWordContent == '' && this.picture == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '评论内容和图片不能同时为空！',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('virtualComment/insertVirtualComment'),
					method: 'post',
					data: this.$http.adornData({
						'virtualCommentClassifyId': this.classifyIdFl,
						'virtualCommentContent': this.helpWordContent,
						'sort': this.sort,
						'virtualCommentImg': this.picture,
						'parentId': 0,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {

								this.articleSelect()
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
			// 编辑评论弹框
			updateswz(rows) {
				this.dialogFormVisible3 = true;
				this.formwz.virtualCommentId = rows.virtualCommentId;
				this.formwz.helpClassifyId = rows.virtualCommentClassifyId
				this.formwz.sort = rows.sort;
				this.formwz.helpWordContent = rows.virtualCommentContent;
				this.picture = rows.picture
			},
			// 修改评论
			amendNoticeTowz() {
				this.$http({
					url: this.$http.adornUrl('virtualComment/updateVirtualComment'),
					method: 'post',
					data: this.$http.adornData({
						'virtualCommentId': this.formwz.virtualCommentId,
						'virtualCommentContent': this.formwz.helpWordContent,
						'virtualCommentClassifyId': this.formwz.helpClassifyId,
						'sort': this.formwz.sort,
						'picture': this.picture,
						'parentId': 0,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
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
			// 删除评论
			deleteswz(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('virtualComment/deleteVirtualComment'),
						method: 'post',
						params: this.$http.adornParams({
							'virtualCommentId': row.virtualCommentId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.name = '';
									this.articleSelect()
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
				}).catch(() => {})
			},
			// 获取分类管理数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('virtualComment/selectVirtualCommentClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': 100,
						// 'helpClassifyName': this.helpClassifyNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataF = returnData
				})
			},
			// 分类无分页
			dataSelectW() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('virtualComment/selectVirtualCommentClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'helpClassifyName': this.helpClassifyNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classifyId = data.data[0].virtualCommentClassifyId
					this.classifyIds = returnData
					this.articleSelect()
				})
			},
			// 获取评论管理数据
			articleSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('virtualComment/selectVirtualCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'virtualCommentClassifyId': this.classifyId,
						// 'helpWordTitle': this.helpWordTitleT,
						'page': this.page,
						'limit': this.limit,
						'parentId': 0,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.parentId = element
				})
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.helpClassifyNameT = ''
				this.page = 1
				this.dataSelect()
			},
			// 查询
			selectW() {
				this.page = 1
				this.articleSelect()
			},
			// 重置
			cleansW() {
				this.helpWordTitleT = ''
				this.page = 1
				this.articleSelect()
			},

			// 回复列表弹框
			talkAboutEr(row) {
				// this.talkId = row.talkId
				this.virtualCommentId = row.virtualCommentId
				this.pinglunSelectHf()
				this.dialogFormVisibleHf = true
			},
			// 获取回复列表数据
			pinglunSelectHf() {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoadingHf = true
				this.$http({
					url: this.$http.adornUrl('virtualComment/selectVirtualCommentList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageHf,
						'limit': this.sizeHf,
						'parentId': this.virtualCommentId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingHf = false
					let returnData = data.data
					this.pinglunDataHf = returnData
				})
			},
			// 删除回复
			pldeletesEr(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCommentById`),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
						})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.pinglunSelectHf()
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
			// 回复弹框
			updatesHf(row) {
				this.picture = ''
				this.content = ''
				this.userId = ''
				this.dialogFormVisibleHf2 = true

			},
			// 监测@符号
			checkAtSymbol(event) {
				console.log('监测@:', this.content.includes('@'))
				if (!this.content.includes('@')) {
					this.userId = ''
				}
			},
			// @用户
			aiteBtn() {
				if (this.userId != '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '只能@一位用户！',
						type: 'warning'
					});
					return
				}
				// this.content = this.content+ '@'
				this.userName = ''
				this.pageU = 1
				this.dataSelectUser()
				this.dialogFormVisiblePt = true
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
						'member': -1,
						'userName': this.userName,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': -1,
						'phone': '',
						'sex': '',
						'isAgent': '',
						'sysUser': 1
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataUder = returnData
					this.tableDataLoadingPt = false
				})
			},
			// 选择用户
			xuanze(row) {
				this.content = this.content + '@' + row.userName + ' '
				this.userId = row.userId
				this.dialogFormVisiblePt = false
			},
			//提交回复
			refuseto() {

				if (this.content == '' && this.picture == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '回复内容和图片不能同时为空！',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('virtualComment/insertVirtualComment'),
					method: 'post',
					data: this.$http.adornData({
						// 'virtualCommentClassifyId': this.classifyIdFl,
						'virtualCommentContent': this.content,
						'userIdStr': this.userId,
						'virtualCommentImg': this.picture,
						'parentId': this.virtualCommentId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleHf2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.pinglunSelectHf()
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
			// 删除回复
			pldeletesEr(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('virtualComment/deleteVirtualComment'),
						method: 'post',
						params: this.$http.adornParams({
							'virtualCommentId': row.virtualCommentId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.pinglunSelectHf()
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
				}).catch(() => {})
			},
		},
		mounted() {
			this.dataSelect()
			this.dataSelectW()
		}
	};
</script>

<style scoped>
.comments-page__filter-select {
	width: 160px;
}

.comments-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.comments-page__tabs {
	margin-bottom: 0;
}

.comments-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.comments-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.comments-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.comments-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.comments-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>