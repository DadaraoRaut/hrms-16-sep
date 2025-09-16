import {
  CommonModule,
  NgStyle,
  isPlatformBrowser
} from "./chunk-WNMFGVF3.js";
import {
  Chart,
  registerables
} from "./chunk-V2N2VJKM.js";
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  Inject,
  Input,
  InputFlags,
  NgModule,
  NgZone,
  Output,
  PLATFORM_ID,
  ViewEncapsulation$1,
  booleanAttribute,
  setClassMetadata,
  ɵɵInputTransformsFeature,
  ɵɵadvance,
  ɵɵattribute,
  ɵɵdefineComponent,
  ɵɵdefineInjector,
  ɵɵdefineNgModule,
  ɵɵdirectiveInject,
  ɵɵelementEnd,
  ɵɵelementStart,
  ɵɵlistener,
  ɵɵproperty,
  ɵɵpureFunction2
} from "./chunk-WIRKS3HE.js";
import "./chunk-D5PSRL4M.js";

// node_modules/chart.js/auto/auto.js
Chart.register(...registerables);
var auto_default = Chart;

// node_modules/primeng/fesm2022/primeng-chart.mjs
var _c0 = (a0, a1) => ({
  position: "relative",
  width: a0,
  height: a1
});
var _c1 = (a0, a1) => ({
  width: a0,
  height: a1
});
var UIChart = class _UIChart {
  platformId;
  el;
  zone;
  /**
   * Type of the chart.
   * @group Props
   */
  type;
  /**
   * Array of per-chart plugins to customize the chart behaviour.
   * @group Props
   */
  plugins = [];
  /**
   * Width of the chart.
   * @group Props
   */
  width;
  /**
   * Height of the chart.
   * @group Props
   */
  height;
  /**
   * Whether the chart is redrawn on screen size change.
   * @group Props
   */
  responsive = true;
  /**
   * Used to define a string that autocomplete attribute the current element.
   * @group Props
   */
  ariaLabel;
  /**
   * Establishes relationships between the component and label(s) where its value should be one or more element IDs.
   * @group Props
   */
  ariaLabelledBy;
  /**
   * Data to display.
   * @group Props
   */
  get data() {
    return this._data;
  }
  set data(val) {
    this._data = val;
    this.reinit();
  }
  /**
   * Options to customize the chart.
   * @group Props
   */
  get options() {
    return this._options;
  }
  set options(val) {
    this._options = val;
    this.reinit();
  }
  /**
   * Callback to execute when an element on chart is clicked.
   * @group Emits
   */
  onDataSelect = new EventEmitter();
  isBrowser = false;
  initialized;
  _data;
  _options = {};
  chart;
  constructor(platformId, el, zone) {
    this.platformId = platformId;
    this.el = el;
    this.zone = zone;
  }
  ngAfterViewInit() {
    this.initChart();
    this.initialized = true;
  }
  onCanvasClick(event) {
    if (this.chart) {
      const element = this.chart.getElementsAtEventForMode(event, "nearest", {
        intersect: true
      }, false);
      const dataset = this.chart.getElementsAtEventForMode(event, "dataset", {
        intersect: true
      }, false);
      if (element && element[0] && dataset) {
        this.onDataSelect.emit({
          originalEvent: event,
          element: element[0],
          dataset
        });
      }
    }
  }
  initChart() {
    if (isPlatformBrowser(this.platformId)) {
      let opts = this.options || {};
      opts.responsive = this.responsive;
      if (opts.responsive && (this.height || this.width)) {
        opts.maintainAspectRatio = false;
      }
      this.zone.runOutsideAngular(() => {
        this.chart = new auto_default(this.el.nativeElement.children[0].children[0], {
          type: this.type,
          data: this.data,
          options: this.options,
          plugins: this.plugins
        });
      });
    }
  }
  getCanvas() {
    return this.el.nativeElement.children[0].children[0];
  }
  getBase64Image() {
    return this.chart.toBase64Image();
  }
  generateLegend() {
    if (this.chart) {
      return this.chart.generateLegend();
    }
  }
  refresh() {
    if (this.chart) {
      this.chart.update();
    }
  }
  reinit() {
    if (this.chart) {
      this.chart.destroy();
      this.initChart();
    }
  }
  ngOnDestroy() {
    if (this.chart) {
      this.chart.destroy();
      this.initialized = false;
      this.chart = null;
    }
  }
  static ɵfac = function UIChart_Factory(t) {
    return new (t || _UIChart)(ɵɵdirectiveInject(PLATFORM_ID), ɵɵdirectiveInject(ElementRef), ɵɵdirectiveInject(NgZone));
  };
  static ɵcmp = ɵɵdefineComponent({
    type: _UIChart,
    selectors: [["p-chart"]],
    hostAttrs: [1, "p-element"],
    inputs: {
      type: "type",
      plugins: "plugins",
      width: "width",
      height: "height",
      responsive: [InputFlags.HasDecoratorInputTransform, "responsive", "responsive", booleanAttribute],
      ariaLabel: "ariaLabel",
      ariaLabelledBy: "ariaLabelledBy",
      data: "data",
      options: "options"
    },
    outputs: {
      onDataSelect: "onDataSelect"
    },
    features: [ɵɵInputTransformsFeature],
    decls: 2,
    vars: 10,
    consts: [[3, "ngStyle"], ["role", "img", 3, "click", "ngStyle"]],
    template: function UIChart_Template(rf, ctx) {
      if (rf & 1) {
        ɵɵelementStart(0, "div", 0)(1, "canvas", 1);
        ɵɵlistener("click", function UIChart_Template_canvas_click_1_listener($event) {
          return ctx.onCanvasClick($event);
        });
        ɵɵelementEnd()();
      }
      if (rf & 2) {
        ɵɵproperty("ngStyle", ɵɵpureFunction2(4, _c0, ctx.responsive && !ctx.width ? null : ctx.width, ctx.responsive && !ctx.height ? null : ctx.height));
        ɵɵadvance();
        ɵɵproperty("ngStyle", ɵɵpureFunction2(7, _c1, ctx.responsive && !ctx.width ? null : ctx.width, ctx.responsive && !ctx.height ? null : ctx.height));
        ɵɵattribute("aria-label", ctx.ariaLabel)("aria-labelledby", ctx.ariaLabelledBy);
      }
    },
    dependencies: [NgStyle],
    encapsulation: 2,
    changeDetection: 0
  });
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(UIChart, [{
    type: Component,
    args: [{
      selector: "p-chart",
      template: `
        <div
            [ngStyle]="{
                position: 'relative',
                width: responsive && !width ? null : width,
                height: responsive && !height ? null : height
            }"
        >
            <canvas
                role="img"
                [attr.aria-label]="ariaLabel"
                [attr.aria-labelledby]="ariaLabelledBy"
                [ngStyle]="{
                    width: responsive && !width ? null : width,
                    height: responsive && !height ? null : height
                }"
                (click)="onCanvasClick($event)"
            ></canvas>
        </div>
    `,
      changeDetection: ChangeDetectionStrategy.OnPush,
      encapsulation: ViewEncapsulation$1.None,
      host: {
        class: "p-element"
      }
    }]
  }], () => [{
    type: void 0,
    decorators: [{
      type: Inject,
      args: [PLATFORM_ID]
    }]
  }, {
    type: ElementRef
  }, {
    type: NgZone
  }], {
    type: [{
      type: Input
    }],
    plugins: [{
      type: Input
    }],
    width: [{
      type: Input
    }],
    height: [{
      type: Input
    }],
    responsive: [{
      type: Input,
      args: [{
        transform: booleanAttribute
      }]
    }],
    ariaLabel: [{
      type: Input
    }],
    ariaLabelledBy: [{
      type: Input
    }],
    data: [{
      type: Input
    }],
    options: [{
      type: Input
    }],
    onDataSelect: [{
      type: Output
    }]
  });
})();
var ChartModule = class _ChartModule {
  static ɵfac = function ChartModule_Factory(t) {
    return new (t || _ChartModule)();
  };
  static ɵmod = ɵɵdefineNgModule({
    type: _ChartModule,
    declarations: [UIChart],
    imports: [CommonModule],
    exports: [UIChart]
  });
  static ɵinj = ɵɵdefineInjector({
    imports: [CommonModule]
  });
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(ChartModule, [{
    type: NgModule,
    args: [{
      imports: [CommonModule],
      exports: [UIChart],
      declarations: [UIChart]
    }]
  }], null, null);
})();
export {
  ChartModule,
  UIChart
};
//# sourceMappingURL=primeng_chart.js.map
