<template>
	<div class="nf-page banner-list-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">商城配置</span>
			<h2 class="nf-page-hero__title">轮播图管理</h2>
			<p class="nf-page-hero__desc">管理首页轮播图与邀请背景图，支持添加、修改、启用/禁用与删除操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation nf-filter-card__head-icon"></i>
				<span>快捷操作</span>
			</div>
			<div class="banner-list-page__filter-actions">
				<el-button type="primary" icon="el-icon-refresh" @click="dataSelect">刷新</el-button>
			</div>
		</div>

		<el-tabs v-model="activeName" class="banner-list-page__tabs" @tab-click="handleClick">
		<el-tab-pane label="轮播图" name="first">
			<div class="nf-table-card">
				<div class="banner-list-page__toolbar">
					<el-button :disabled="!isAuth('bannerList:add')" type="primary"
						icon="document" @click="addNotice">添加轮播图</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="轮播图名称"></el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
				<div class="banner-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</div>
		</el-tab-pane>
		<el-tab-pane label="邀请背景图" name="fourthly">
			<div class="nf-table-card">
				<div class="banner-list-page__toolbar">
					<el-button :disabled="!isAuth('bannerList:add')" type="primary"
						icon="document" @click="addNotice5">添加背景图</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="背景图名称">
				</el-table-column>
				<el-table-column prop="describes" label="备注">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#5cc477"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
				<div class="banner-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="我的页面底部轮播图" name="firstDb">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加轮播图</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="轮播图名称"></el-table-column>
				<el-table-column prop="describes" label="备注">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="排行榜轮播图" name="firstPhb">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加轮播图</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="轮播图名称"></el-table-column>
				<el-table-column prop="describes" label="备注">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="用户主页轮播图" name="five">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加轮播图</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="轮播图名称"></el-table-column>
				<el-table-column prop="describes" label="备注">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="活动赛事轮播图" name="six">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加轮播图</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="轮播图名称"></el-table-column>
				<el-table-column prop="describes" label="备注">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:disabled="!isAuth('bannerList:update')" :active-value="openValue"
							:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="首页分类" name="second">
			<div style="float: right;margin-right:2%;">
				<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
					icon="document" @click="addNotice2">添加任务分类</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="50">
				</el-table-column>
				<el-table-column prop="imageUrl" label="图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.imageUrl" width="60" height="60" />
					</template>
				</el-table-column>
				<el-table-column prop="name" label="分类名称">
				</el-table-column>
				<el-table-column prop="describes" label="备注">
				</el-table-column>
				<el-table-column prop="url" label="链接" width="180">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.state == 1">显示</span>
						<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
					</template>
				</el-table-column>
				<el-table-column prop="state" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#5cc477"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
							@click="amendBanner(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			<el-dialog title="添加" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;display: flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="imageUrl" :src="imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
					<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
					<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
				</div>
				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
					<el-input style="width:50%;" v-model="describes" placeholder="请输入备注"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="addNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane> -->
		</el-tabs>
		<!-- 动态轮播图、首页、我的页面、约玩页面等配置 添加弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible5" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imageUrl" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="describes" placeholder="请输入备注"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible5 = false">取 消</el-button>
				<el-button type="primary" v-if="!tableDataLoadingT" @click="addNoticeTo5()">确 定</el-button>
				<el-button type="primary" v-if="tableDataLoadingT" v-loading="tableDataLoadingT">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imageUrl" :action="$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="describes" placeholder="请输入备注"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" v-if="!tableDataLoadingT" @click="addNoticeTo()">确 定</el-button>
				<el-button type="primary" v-if="tableDataLoadingT" v-loading="tableDataLoadingT">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="图片：" :label-width="formLabelWidth" v-if="classify != 2">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form.imageUrl" :src="form.imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="名称：" :label-width="formLabelWidth">
					<el-input v-model="form.name" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="链接：" :label-width="formLabelWidth">
					<el-input v-model="form.url" style="width:65%;"></el-input>
				</el-form-item>
				
				<el-form-item label="备注：" :label-width="formLabelWidth">
					<el-input v-model="form.describes" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" v-if="!tableDataLoadingT" @click="amendNoticeTo()">确 定</el-button>
				<el-button type="primary" v-if="tableDataLoadingT" v-loading="tableDataLoadingT">确 定</el-button>
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
				classify: 1,
				openValue: 1,
				closeValue: 2,
				name: '',
				imageUrl: '',
				url: '',
				state: -1,
				describes: '',
				hideUpload: false,
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				tableData: [],
				choicenData: [],
				choicenData2: [],
				checkBoxData: [], //多选框选择的值
				keyword: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				states: [{
						label: '显示',
						value: 1
					},
					{
						label: '隐藏',
						value: 2
					}
				],
				form: {
					id: '',
					name: '',
					imageUrl: '',
					url: '',
					classify: '',
					createTime: '',
					sort: '',
					state: '',
					describes: ''

				},
				tableDataLoadingT: false,
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '轮播图') {
					this.page = 1
					this.limit = 10
					this.classify = 1
					this.dataSelect()
				}
				if (tab._props.label == '首页分类') {
					this.page = 1
					this.limit = 10
					this.classify = 2
					this.dataSelect()
				}

				if (tab._props.label == '邀请背景图') {
					this.page = 1
					this.limit = 10
					this.classify = 5
					this.dataSelect()
				}
				if (tab._props.label == '我的页面底部轮播图') {
					this.page = 1
					this.limit = 10
					this.classify = 6
					this.dataSelect()
				}
				if (tab._props.label == '排行榜轮播图') {
					this.page = 1
					this.limit = 10
					this.classify = 7
					this.dataSelect()
				}
				if (tab._props.label == '用户主页轮播图') {
					this.page = 1
					this.limit = 10
					this.classify = 30
					this.dataSelect()
				}
				if (tab._props.label == '活动赛事轮播图') {
					this.page = 1
					this.limit = 10
					this.classify = 40
					this.dataSelect()
				}

			},
			//上传成功
			handleAvatarSuccess(file) {
				this.imageUrl = file.data
			},
			handleAvatarSuccess2(file2) {
				this.form.imageUrl = file2.data
				console.log('file2.data', file2.data)
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.choicenSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.choicenSelect()
			},
			// 是否启用
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl(`banner/updateBannerStateById?id=${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
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
								this.dataSelect()
							}
						})
					}
					
				})
			},
			//添加banner图
			addNotice() {
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.dialogFormVisible = true
			},
			// 添加banner图
			addNoticeTo() {
				this.tableDataLoadingT = true
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return this.tableDataLoadingT = false
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return this.tableDataLoadingT = false
				}

				// this.classify = 1
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible = false
						this.tableDataLoadingT = false
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
			},
			// 修改
			amendBanner(index, rows) {
				this.dialogFormVisible1 = true
				this.form.id = rows.id
				this.form.imageUrl = rows.imageUrl
				this.form.url = rows.url
				this.form.sort = rows.sort
				this.form.state = rows.state
				this.form.name = rows.name
				this.form.describes = rows.describes
			},
			// 修改
			amendNoticeTo() {
				this.tableDataLoadingT = true
				this.$http({
					url: this.$http.adornUrl(
						'banner/updateBannerById'
					),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'createTime': this.info.stockDate,
						'imageUrl': this.form.imageUrl,
						'url': this.form.url,
						'state': this.form.state,
						'sort': this.form.sort,
						'name': this.form.name,
						'describes': this.form.describes
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible1 = false
						this.tableDataLoadingT = false
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
			},
			//添加任务分类
			addNotice2() {
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.dialogFormVisible2 = true
			},
			// 添加添加任务分类
			addNoticeTo2() {
				// if (this.imageUrl == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传图片',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				this.classify = 2
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible2 = false
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
			},
			
			//添加活动背景图
			addNotice5() {
				if (this.tableData.length == 1) {
					this.$message({
						title: '提示',
						duration: 1800,
						message: '活动背景图只能添加一张',
						type: 'warning'
					})
				} else {
					this.imageUrl = ''
					this.state = ''
					this.url = ''
					this.name = ''
					this.describes = ''
					this.dialogFormVisible5 = true
				}
			},
			// 添加背景图
			addNoticeTo5() {
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						// type: 'success',
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				this.classify = 5
				this.tableDataLoadingT = true
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible5 = false
						this.tableDataLoadingT = false
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
			},
			// 删除banner图
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`banner/deleteBannerById?ids=${delid}`),
						method: 'get',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
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
				}).catch(() => {})

			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('banner/selectBannerPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'classify': this.classify,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
					}

				})
			},
			
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.banner-list-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.banner-list-page__tabs {
	margin-bottom: 0;
}

.banner-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.banner-list-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.banner-list-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.banner-list-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.banner-list-page__toolbar {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 12px;
}

.banner-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.customWidth {
	width: 80% !important;
}
</style>