// spoiler-page.js
(function (window) {

  Vue = window.Vue;

  Vue.config.delimiters = [
    '<%', '%>'
  ];
  Vue.config.unsafeDelimiters = [
    '<%!', '!%>'
  ];

  vue = new Vue({
    el: '#spoiler-vue-app',
    data: {
      spoiler: window.Spoiler,
      state: {
        showSpoiler: false,
        showRawData: false
      }
    },
    methods: {
      showSpoiler: function() {
        console.log('>> show spoiler');
        this.state.showSpoiler = true;
      },
      directLink: function() {
        var link = window.location.toString();
        return link;
      },
      markdownLink: function() {
        var link = this.directLink();
        var maskText = this.spoiler.maskText;
        return "[" + maskText + "](" + link + ")";
      },
      bbcodeLink: function() {
        var link = this.directLink();
        var maskText = this.spoiler.maskText;
        return "[url=" + link + "]" + maskText + "[/url]";
      },
      toggleRawData: function() {
        this.state.showRawData = !this.state.showRawData;
      },
      rawJson: function() {
        return JSON.stringify(this.spoiler, null, 2);
      }
    }
  });

  window.VueApp = vue;

})(window);
