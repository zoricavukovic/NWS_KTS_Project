export function swapColorsOfRoutes(indexOfSelectedPath: number, drawPolylineList: google.maps.Polyline[]): void {
  drawPolylineList.forEach(p => {
    p.setOptions({
      strokeColor: '#cdd1d3',
      strokeWeight: 7,
    });
  });
  drawPolylineList.at(indexOfSelectedPath).setOptions({
    strokeColor: '#283b50',
    strokeWeight: 9,
  });
}
