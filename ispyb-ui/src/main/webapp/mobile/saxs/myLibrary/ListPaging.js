Ext.define("Ext.plugin.ListPaging",{extend:"Ext.Component",alias:"plugin.listpaging",config:{autoPaging:false,loadMoreText:"Load More...",noMoreRecordsText:"No More Records",loadTpl:['<div class="{cssPrefix}loading-spinner" style="font-size: 180%; margin: 10px auto;">','<span class="{cssPrefix}loading-top"></span>','<span class="{cssPrefix}loading-right"></span>','<span class="{cssPrefix}loading-bottom"></span>','<span class="{cssPrefix}loading-left"></span>',"</div>",'<div class="{cssPrefix}list-paging-msg">{message}</div>'].join(""),loadMoreCmp:{xtype:"component",baseCls:Ext.baseCSSPrefix+"list-paging",scrollDock:"bottom",hidden:true},loadMoreCmpAdded:false,loadingCls:Ext.baseCSSPrefix+"loading",list:null,scroller:null,loading:false},init:function(c){var a=c.getScrollable().getScroller(),b=c.getStore();this.setList(c);this.setScroller(a);this.bindStore(c.getStore());this.addLoadMoreCmp();if(b){this.disableDataViewMask(b)}c.updateStore=Ext.Function.createInterceptor(c.updateStore,this.bindStore,this);if(this.getAutoPaging()){a.on({scrollend:this.onScrollEnd,scope:this})}},bindStore:function(a,b){if(b){b.un({beforeload:this.onStoreBeforeLoad,load:this.onStoreLoad,scope:this})}if(a){a.on({beforeload:this.onStoreBeforeLoad,load:this.onStoreLoad,scope:this})}},disableDataViewMask:function(a){var b=this.getList();if(a.isAutoLoading()){b.setLoadingText(null)}else{a.on({load:{single:true,fn:function(){b.setLoadingText(null)}}})}},applyLoadTpl:function(a){return(Ext.isObject(a)&&a.isTemplate)?a:new Ext.XTemplate(a)},applyLoadMoreCmp:function(a){a=Ext.merge(a,{html:this.getLoadTpl().apply({cssPrefix:Ext.baseCSSPrefix,message:this.getLoadMoreText()}),scrollDock:"bottom",listeners:{tap:{fn:this.loadNextPage,scope:this,element:"element"}}});return Ext.factory(a,Ext.Component,this.getLoadMoreCmp())},onScrollEnd:function(b,a,d){var c=this.getList();if(!this.getLoading()&&d>=b.maxPosition.y){this.currentScrollToTopOnRefresh=c.getScrollToTopOnRefresh();c.setScrollToTopOnRefresh(false);this.loadNextPage()}},updateLoading:function(a){var b=this.getLoadMoreCmp(),c=this.getLoadingCls();if(a){b.addCls(c)}else{b.removeCls(c)}},onStoreBeforeLoad:function(a){if(a.getCount()===0){this.getLoadMoreCmp().hide()}},onStoreLoad:function(a){var d=this.getLoadMoreCmp(),b=this.getLoadTpl(),c=this.storeFullyLoaded()?this.getNoMoreRecordsText():this.getLoadMoreText();if(a.getCount()){d.show()}this.setLoading(false);d.setHtml(b.apply({cssPrefix:Ext.baseCSSPrefix,message:c}));if(this.currentScrollToTopOnRefresh!==undefined){this.getList().setScrollToTopOnRefresh(this.currentScrollToTopOnRefresh);delete this.currentScrollToTopOnRefresh}},addLoadMoreCmp:function(){var b=this.getList(),a=this.getLoadMoreCmp();if(!this.getLoadMoreCmpAdded()){b.add(a);b.fireEvent("loadmorecmpadded",this,b);this.setLoadMoreCmpAdded(true)}return a},storeFullyLoaded:function(){var a=this.getList().getStore(),b=a.getTotalCount();return b!==null?a.getTotalCount()<=(a.currentPage*a.getPageSize()):false},loadNextPage:function(){var a=this;if(!a.storeFullyLoaded()){a.setLoading(true);a.getList().getStore().nextPage({addRecords:true})}}});