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
        showSpoiler: false
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
      }
    }
  });

  window.VueApp = vue;

})(window);
