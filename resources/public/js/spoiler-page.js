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
    el: '#spoiler-panel',
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
      }
    }
  });

  window.VueApp = vue;

})(window);
