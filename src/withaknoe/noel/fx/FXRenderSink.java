package withaknoe.noel.fx;

import withaknoe.noel.core.Primitive;
import withaknoe.noel.core.RenderSink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// what does fxrendersink do...
//   converts primitives and their normalized coordinates to pixels/coordinates.
//     ie FxVisitor walks the tree and sends renderSink.draw(new LinePrimitive(start, end))
//       then, FxRenderSink converts 0.20 to 20% * canvas height, to get offset from origin.
//              the same will be done with centers/radius/angles
//   will need things like: fontHeightPx, fontWidthPx, baselineY, ascenderY, descenderY, leftMargin, topMargin
//                          and helpers: toPixelX, toPixelY
public class FXRenderSink implements RenderSink {
    private final List<Primitive> primitives = new ArrayList<>();

    public FXRenderSink() {

    }

    // will eventually become the main entry point for this visitor, and dispatch the primitive to the
    //      appropriate add* function.
    @Override
    public void add(Primitive p) {
        primitives.add(p);
    }
    @Override
    public List<Primitive> primitives() {
        return Collections.unmodifiableList(primitives);
    }

    @Override
    public void clear() {
        if (!(primitives == null)) { this.primitives.clear(); }
    }

}