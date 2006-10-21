/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import java.nio.*;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

/**
 * OpenGL standard constants and methods. These static constants and methods 
 * wrap those provided by JOGL. The reason for this wrapper is to make OpenGL 
 * programming in Java more like that in C or C++. For example, where with
 * JOGL we might write
 * <pre><code>
   gl.glClear(GL.GL_COLOR_BUFFER_BIT);
 * </code></pre>
 * with this wrapper we can write
 * <pre><code>
 * glClear(GL_COLOR_BUFFER_BIT);
 * </code></pre>
 * The static methods in this class use the OpenGL context that is current
 * for the current thread. This class is stateless, and can be used with any
 * JOGL class.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
public class Gl {

  public static final int GL_FALSE
    = GL.GL_FALSE;

  public static final int GL_TRUE
    = GL.GL_TRUE;

  public static final int GL_BYTE
    = GL.GL_BYTE;

  public static final int GL_UNSIGNED_BYTE
    = GL.GL_UNSIGNED_BYTE;

  public static final int GL_SHORT
    = GL.GL_SHORT;

  public static final int GL_UNSIGNED_SHORT
    = GL.GL_UNSIGNED_SHORT;

  public static final int GL_INT
    = GL.GL_INT;

  public static final int GL_UNSIGNED_INT
    = GL.GL_UNSIGNED_INT;

  public static final int GL_FLOAT
    = GL.GL_FLOAT;

  public static final int GL_DOUBLE
    = GL.GL_DOUBLE;

  public static final int GL_2_BYTES
    = GL.GL_2_BYTES;

  public static final int GL_3_BYTES
    = GL.GL_3_BYTES;

  public static final int GL_4_BYTES
    = GL.GL_4_BYTES;

  public static final int GL_POINTS
    = GL.GL_POINTS;

  public static final int GL_LINES
    = GL.GL_LINES;

  public static final int GL_LINE_LOOP
    = GL.GL_LINE_LOOP;

  public static final int GL_LINE_STRIP
    = GL.GL_LINE_STRIP;

  public static final int GL_TRIANGLES
    = GL.GL_TRIANGLES;

  public static final int GL_TRIANGLE_STRIP
    = GL.GL_TRIANGLE_STRIP;

  public static final int GL_TRIANGLE_FAN
    = GL.GL_TRIANGLE_FAN;

  public static final int GL_QUADS
    = GL.GL_QUADS;

  public static final int GL_QUAD_STRIP
    = GL.GL_QUAD_STRIP;

  public static final int GL_POLYGON
    = GL.GL_POLYGON;

  public static final int GL_MATRIX_MODE
    = GL.GL_MATRIX_MODE;

  public static final int GL_MODELVIEW
    = GL.GL_MODELVIEW;

  public static final int GL_PROJECTION
    = GL.GL_PROJECTION;

  public static final int GL_TEXTURE
    = GL.GL_TEXTURE;

  public static final int GL_POINT_SMOOTH
    = GL.GL_POINT_SMOOTH;

  public static final int GL_POINT_SIZE
    = GL.GL_POINT_SIZE;

  public static final int GL_POINT_SIZE_GRANULARITY
    = GL.GL_POINT_SIZE_GRANULARITY;

  public static final int GL_POINT_SIZE_RANGE
    = GL.GL_POINT_SIZE_RANGE;

  public static final int GL_LINE_SMOOTH
    = GL.GL_LINE_SMOOTH;

  public static final int GL_LINE_STIPPLE
    = GL.GL_LINE_STIPPLE;

  public static final int GL_LINE_STIPPLE_PATTERN
    = GL.GL_LINE_STIPPLE_PATTERN;

  public static final int GL_LINE_STIPPLE_REPEAT
    = GL.GL_LINE_STIPPLE_REPEAT;

  public static final int GL_LINE_WIDTH
    = GL.GL_LINE_WIDTH;

  public static final int GL_LINE_WIDTH_GRANULARITY
    = GL.GL_LINE_WIDTH_GRANULARITY;

  public static final int GL_LINE_WIDTH_RANGE
    = GL.GL_LINE_WIDTH_RANGE;

  public static final int GL_POINT
    = GL.GL_POINT;

  public static final int GL_LINE
    = GL.GL_LINE;

  public static final int GL_FILL
    = GL.GL_FILL;

  public static final int GL_CW
    = GL.GL_CW;

  public static final int GL_CCW
    = GL.GL_CCW;

  public static final int GL_FRONT
    = GL.GL_FRONT;

  public static final int GL_BACK
    = GL.GL_BACK;

  public static final int GL_POLYGON_MODE
    = GL.GL_POLYGON_MODE;

  public static final int GL_POLYGON_SMOOTH
    = GL.GL_POLYGON_SMOOTH;

  public static final int GL_POLYGON_STIPPLE
    = GL.GL_POLYGON_STIPPLE;

  public static final int GL_EDGE_FLAG
    = GL.GL_EDGE_FLAG;

  public static final int GL_CULL_FACE
    = GL.GL_CULL_FACE;

  public static final int GL_CULL_FACE_MODE
    = GL.GL_CULL_FACE_MODE;

  public static final int GL_FRONT_FACE
    = GL.GL_FRONT_FACE;

  public static final int GL_POLYGON_OFFSET_FACTOR
    = GL.GL_POLYGON_OFFSET_FACTOR;

  public static final int GL_POLYGON_OFFSET_UNITS
    = GL.GL_POLYGON_OFFSET_UNITS;

  public static final int GL_POLYGON_OFFSET_POINT
    = GL.GL_POLYGON_OFFSET_POINT;

  public static final int GL_POLYGON_OFFSET_LINE
    = GL.GL_POLYGON_OFFSET_LINE;

  public static final int GL_POLYGON_OFFSET_FILL
    = GL.GL_POLYGON_OFFSET_FILL;

  public static final int GL_COMPILE
    = GL.GL_COMPILE;

  public static final int GL_COMPILE_AND_EXECUTE
    = GL.GL_COMPILE_AND_EXECUTE;

  public static final int GL_LIST_BASE
    = GL.GL_LIST_BASE;

  public static final int GL_LIST_INDEX
    = GL.GL_LIST_INDEX;

  public static final int GL_LIST_MODE
    = GL.GL_LIST_MODE;

  public static final int GL_NEVER
    = GL.GL_NEVER;

  public static final int GL_LESS
    = GL.GL_LESS;

  public static final int GL_EQUAL
    = GL.GL_EQUAL;

  public static final int GL_LEQUAL
    = GL.GL_LEQUAL;

  public static final int GL_GREATER
    = GL.GL_GREATER;

  public static final int GL_NOTEQUAL
    = GL.GL_NOTEQUAL;

  public static final int GL_GEQUAL
    = GL.GL_GEQUAL;

  public static final int GL_ALWAYS
    = GL.GL_ALWAYS;

  public static final int GL_DEPTH_TEST
    = GL.GL_DEPTH_TEST;

  public static final int GL_DEPTH_BITS
    = GL.GL_DEPTH_BITS;

  public static final int GL_DEPTH_CLEAR_VALUE
    = GL.GL_DEPTH_CLEAR_VALUE;

  public static final int GL_DEPTH_FUNC
    = GL.GL_DEPTH_FUNC;

  public static final int GL_DEPTH_RANGE
    = GL.GL_DEPTH_RANGE;

  public static final int GL_DEPTH_WRITEMASK
    = GL.GL_DEPTH_WRITEMASK;

  public static final int GL_DEPTH_COMPONENT
    = GL.GL_DEPTH_COMPONENT;

  public static final int GL_LIGHTING
    = GL.GL_LIGHTING;

  public static final int GL_LIGHT0
    = GL.GL_LIGHT0;

  public static final int GL_LIGHT1
    = GL.GL_LIGHT1;

  public static final int GL_LIGHT2
    = GL.GL_LIGHT2;

  public static final int GL_LIGHT3
    = GL.GL_LIGHT3;

  public static final int GL_LIGHT4
    = GL.GL_LIGHT4;

  public static final int GL_LIGHT5
    = GL.GL_LIGHT5;

  public static final int GL_LIGHT6
    = GL.GL_LIGHT6;

  public static final int GL_LIGHT7
    = GL.GL_LIGHT7;

  public static final int GL_SPOT_EXPONENT
    = GL.GL_SPOT_EXPONENT;

  public static final int GL_SPOT_CUTOFF
    = GL.GL_SPOT_CUTOFF;

  public static final int GL_CONSTANT_ATTENUATION
    = GL.GL_CONSTANT_ATTENUATION;

  public static final int GL_LINEAR_ATTENUATION
    = GL.GL_LINEAR_ATTENUATION;

  public static final int GL_QUADRATIC_ATTENUATION
    = GL.GL_QUADRATIC_ATTENUATION;

  public static final int GL_AMBIENT
    = GL.GL_AMBIENT;

  public static final int GL_DIFFUSE
    = GL.GL_DIFFUSE;

  public static final int GL_SPECULAR
    = GL.GL_SPECULAR;

  public static final int GL_SHININESS
    = GL.GL_SHININESS;

  public static final int GL_EMISSION
    = GL.GL_EMISSION;

  public static final int GL_POSITION
    = GL.GL_POSITION;

  public static final int GL_SPOT_DIRECTION
    = GL.GL_SPOT_DIRECTION;

  public static final int GL_AMBIENT_AND_DIFFUSE
    = GL.GL_AMBIENT_AND_DIFFUSE;

  public static final int GL_COLOR_INDEXES
    = GL.GL_COLOR_INDEXES;

  public static final int GL_LIGHT_MODEL_TWO_SIDE
    = GL.GL_LIGHT_MODEL_TWO_SIDE;

  public static final int GL_LIGHT_MODEL_LOCAL_VIEWER
    = GL.GL_LIGHT_MODEL_LOCAL_VIEWER;

  public static final int GL_LIGHT_MODEL_AMBIENT
    = GL.GL_LIGHT_MODEL_AMBIENT;

  public static final int GL_FRONT_AND_BACK
    = GL.GL_FRONT_AND_BACK;

  public static final int GL_SHADE_MODEL
    = GL.GL_SHADE_MODEL;

  public static final int GL_FLAT
    = GL.GL_FLAT;

  public static final int GL_SMOOTH
    = GL.GL_SMOOTH;

  public static final int GL_COLOR_MATERIAL
    = GL.GL_COLOR_MATERIAL;

  public static final int GL_COLOR_MATERIAL_FACE
    = GL.GL_COLOR_MATERIAL_FACE;

  public static final int GL_COLOR_MATERIAL_PARAMETER
    = GL.GL_COLOR_MATERIAL_PARAMETER;

  public static final int GL_NORMALIZE
    = GL.GL_NORMALIZE;

  public static final int GL_CLIP_PLANE0
    = GL.GL_CLIP_PLANE0;

  public static final int GL_CLIP_PLANE1
    = GL.GL_CLIP_PLANE1;

  public static final int GL_CLIP_PLANE2
    = GL.GL_CLIP_PLANE2;

  public static final int GL_CLIP_PLANE3
    = GL.GL_CLIP_PLANE3;

  public static final int GL_CLIP_PLANE4
    = GL.GL_CLIP_PLANE4;

  public static final int GL_CLIP_PLANE5
    = GL.GL_CLIP_PLANE5;

  public static final int GL_ACCUM_RED_BITS
    = GL.GL_ACCUM_RED_BITS;

  public static final int GL_ACCUM_GREEN_BITS
    = GL.GL_ACCUM_GREEN_BITS;

  public static final int GL_ACCUM_BLUE_BITS
    = GL.GL_ACCUM_BLUE_BITS;

  public static final int GL_ACCUM_ALPHA_BITS
    = GL.GL_ACCUM_ALPHA_BITS;

  public static final int GL_ACCUM_CLEAR_VALUE
    = GL.GL_ACCUM_CLEAR_VALUE;

  public static final int GL_ACCUM
    = GL.GL_ACCUM;

  public static final int GL_ADD
    = GL.GL_ADD;

  public static final int GL_LOAD
    = GL.GL_LOAD;

  public static final int GL_MULT
    = GL.GL_MULT;

  public static final int GL_RETURN
    = GL.GL_RETURN;

  public static final int GL_ALPHA_TEST
    = GL.GL_ALPHA_TEST;

  public static final int GL_ALPHA_TEST_REF
    = GL.GL_ALPHA_TEST_REF;

  public static final int GL_ALPHA_TEST_FUNC
    = GL.GL_ALPHA_TEST_FUNC;

  public static final int GL_BLEND
    = GL.GL_BLEND;

  public static final int GL_BLEND_SRC
    = GL.GL_BLEND_SRC;

  public static final int GL_BLEND_DST
    = GL.GL_BLEND_DST;

  public static final int GL_ZERO
    = GL.GL_ZERO;

  public static final int GL_ONE
    = GL.GL_ONE;

  public static final int GL_SRC_COLOR
    = GL.GL_SRC_COLOR;

  public static final int GL_ONE_MINUS_SRC_COLOR
    = GL.GL_ONE_MINUS_SRC_COLOR;

  public static final int GL_SRC_ALPHA
    = GL.GL_SRC_ALPHA;

  public static final int GL_ONE_MINUS_SRC_ALPHA
    = GL.GL_ONE_MINUS_SRC_ALPHA;

  public static final int GL_DST_ALPHA
    = GL.GL_DST_ALPHA;

  public static final int GL_ONE_MINUS_DST_ALPHA
    = GL.GL_ONE_MINUS_DST_ALPHA;

  public static final int GL_DST_COLOR
    = GL.GL_DST_COLOR;

  public static final int GL_ONE_MINUS_DST_COLOR
    = GL.GL_ONE_MINUS_DST_COLOR;

  public static final int GL_SRC_ALPHA_SATURATE
    = GL.GL_SRC_ALPHA_SATURATE;

  public static final int GL_FEEDBACK
    = GL.GL_FEEDBACK;

  public static final int GL_RENDER
    = GL.GL_RENDER;

  public static final int GL_SELECT
    = GL.GL_SELECT;

  public static final int GL_2D
    = GL.GL_2D;

  public static final int GL_3D
    = GL.GL_3D;

  public static final int GL_3D_COLOR
    = GL.GL_3D_COLOR;

  public static final int GL_3D_COLOR_TEXTURE
    = GL.GL_3D_COLOR_TEXTURE;

  public static final int GL_4D_COLOR_TEXTURE
    = GL.GL_4D_COLOR_TEXTURE;

  public static final int GL_POINT_TOKEN
    = GL.GL_POINT_TOKEN;

  public static final int GL_LINE_TOKEN
    = GL.GL_LINE_TOKEN;

  public static final int GL_LINE_RESET_TOKEN
    = GL.GL_LINE_RESET_TOKEN;

  public static final int GL_POLYGON_TOKEN
    = GL.GL_POLYGON_TOKEN;

  public static final int GL_BITMAP_TOKEN
    = GL.GL_BITMAP_TOKEN;

  public static final int GL_DRAW_PIXEL_TOKEN
    = GL.GL_DRAW_PIXEL_TOKEN;

  public static final int GL_COPY_PIXEL_TOKEN
    = GL.GL_COPY_PIXEL_TOKEN;

  public static final int GL_PASS_THROUGH_TOKEN
    = GL.GL_PASS_THROUGH_TOKEN;

  public static final int GL_FEEDBACK_BUFFER_POINTER
    = GL.GL_FEEDBACK_BUFFER_POINTER;

  public static final int GL_FEEDBACK_BUFFER_SIZE
    = GL.GL_FEEDBACK_BUFFER_SIZE;

  public static final int GL_FEEDBACK_BUFFER_TYPE
    = GL.GL_FEEDBACK_BUFFER_TYPE;

  public static final int GL_SELECTION_BUFFER_POINTER
    = GL.GL_SELECTION_BUFFER_POINTER;

  public static final int GL_SELECTION_BUFFER_SIZE
    = GL.GL_SELECTION_BUFFER_SIZE;

  public static final int GL_FOG
    = GL.GL_FOG;

  public static final int GL_FOG_MODE
    = GL.GL_FOG_MODE;

  public static final int GL_FOG_DENSITY
    = GL.GL_FOG_DENSITY;

  public static final int GL_FOG_COLOR
    = GL.GL_FOG_COLOR;

  public static final int GL_FOG_INDEX
    = GL.GL_FOG_INDEX;

  public static final int GL_FOG_START
    = GL.GL_FOG_START;

  public static final int GL_FOG_END
    = GL.GL_FOG_END;

  public static final int GL_LINEAR
    = GL.GL_LINEAR;

  public static final int GL_EXP
    = GL.GL_EXP;

  public static final int GL_EXP2
    = GL.GL_EXP2;

  public static final int GL_LOGIC_OP
    = GL.GL_LOGIC_OP;

  public static final int GL_INDEX_LOGIC_OP
    = GL.GL_INDEX_LOGIC_OP;

  public static final int GL_COLOR_LOGIC_OP
    = GL.GL_COLOR_LOGIC_OP;

  public static final int GL_LOGIC_OP_MODE
    = GL.GL_LOGIC_OP_MODE;

  public static final int GL_CLEAR
    = GL.GL_CLEAR;

  public static final int GL_SET
    = GL.GL_SET;

  public static final int GL_COPY
    = GL.GL_COPY;

  public static final int GL_COPY_INVERTED
    = GL.GL_COPY_INVERTED;

  public static final int GL_NOOP
    = GL.GL_NOOP;

  public static final int GL_INVERT
    = GL.GL_INVERT;

  public static final int GL_AND
    = GL.GL_AND;

  public static final int GL_NAND
    = GL.GL_NAND;

  public static final int GL_OR
    = GL.GL_OR;

  public static final int GL_NOR
    = GL.GL_NOR;

  public static final int GL_XOR
    = GL.GL_XOR;

  public static final int GL_EQUIV
    = GL.GL_EQUIV;

  public static final int GL_AND_REVERSE
    = GL.GL_AND_REVERSE;

  public static final int GL_AND_INVERTED
    = GL.GL_AND_INVERTED;

  public static final int GL_OR_REVERSE
    = GL.GL_OR_REVERSE;

  public static final int GL_OR_INVERTED
    = GL.GL_OR_INVERTED;

  public static final int GL_STENCIL_TEST
    = GL.GL_STENCIL_TEST;

  public static final int GL_STENCIL_WRITEMASK
    = GL.GL_STENCIL_WRITEMASK;

  public static final int GL_STENCIL_BITS
    = GL.GL_STENCIL_BITS;

  public static final int GL_STENCIL_FUNC
    = GL.GL_STENCIL_FUNC;

  public static final int GL_STENCIL_VALUE_MASK
    = GL.GL_STENCIL_VALUE_MASK;

  public static final int GL_STENCIL_REF
    = GL.GL_STENCIL_REF;

  public static final int GL_STENCIL_FAIL
    = GL.GL_STENCIL_FAIL;

  public static final int GL_STENCIL_PASS_DEPTH_PASS
    = GL.GL_STENCIL_PASS_DEPTH_PASS;

  public static final int GL_STENCIL_PASS_DEPTH_FAIL
    = GL.GL_STENCIL_PASS_DEPTH_FAIL;

  public static final int GL_STENCIL_CLEAR_VALUE
    = GL.GL_STENCIL_CLEAR_VALUE;

  public static final int GL_STENCIL_INDEX
    = GL.GL_STENCIL_INDEX;

  public static final int GL_KEEP
    = GL.GL_KEEP;

  public static final int GL_REPLACE
    = GL.GL_REPLACE;

  public static final int GL_INCR
    = GL.GL_INCR;

  public static final int GL_DECR
    = GL.GL_DECR;

  public static final int GL_NONE
    = GL.GL_NONE;

  public static final int GL_LEFT
    = GL.GL_LEFT;

  public static final int GL_RIGHT
    = GL.GL_RIGHT;

  public static final int GL_FRONT_LEFT
    = GL.GL_FRONT_LEFT;

  public static final int GL_FRONT_RIGHT
    = GL.GL_FRONT_RIGHT;

  public static final int GL_BACK_LEFT
    = GL.GL_BACK_LEFT;

  public static final int GL_BACK_RIGHT
    = GL.GL_BACK_RIGHT;

  public static final int GL_AUX0
    = GL.GL_AUX0;

  public static final int GL_AUX1
    = GL.GL_AUX1;

  public static final int GL_AUX2
    = GL.GL_AUX2;

  public static final int GL_AUX3
    = GL.GL_AUX3;

  public static final int GL_COLOR_INDEX
    = GL.GL_COLOR_INDEX;

  public static final int GL_RED
    = GL.GL_RED;

  public static final int GL_GREEN
    = GL.GL_GREEN;

  public static final int GL_BLUE
    = GL.GL_BLUE;

  public static final int GL_ALPHA
    = GL.GL_ALPHA;

  public static final int GL_LUMINANCE
    = GL.GL_LUMINANCE;

  public static final int GL_LUMINANCE_ALPHA
    = GL.GL_LUMINANCE_ALPHA;

  public static final int GL_ALPHA_BITS
    = GL.GL_ALPHA_BITS;

  public static final int GL_RED_BITS
    = GL.GL_RED_BITS;

  public static final int GL_GREEN_BITS
    = GL.GL_GREEN_BITS;

  public static final int GL_BLUE_BITS
    = GL.GL_BLUE_BITS;

  public static final int GL_INDEX_BITS
    = GL.GL_INDEX_BITS;

  public static final int GL_SUBPIXEL_BITS
    = GL.GL_SUBPIXEL_BITS;

  public static final int GL_AUX_BUFFERS
    = GL.GL_AUX_BUFFERS;

  public static final int GL_READ_BUFFER
    = GL.GL_READ_BUFFER;

  public static final int GL_DRAW_BUFFER
    = GL.GL_DRAW_BUFFER;

  public static final int GL_DOUBLEBUFFER
    = GL.GL_DOUBLEBUFFER;

  public static final int GL_STEREO
    = GL.GL_STEREO;

  public static final int GL_BITMAP
    = GL.GL_BITMAP;

  public static final int GL_COLOR
    = GL.GL_COLOR;

  public static final int GL_DEPTH
    = GL.GL_DEPTH;

  public static final int GL_STENCIL
    = GL.GL_STENCIL;

  public static final int GL_DITHER
    = GL.GL_DITHER;

  public static final int GL_RGB
    = GL.GL_RGB;

  public static final int GL_RGBA
    = GL.GL_RGBA;

  public static final int GL_MAX_LIST_NESTING
    = GL.GL_MAX_LIST_NESTING;

  public static final int GL_MAX_ATTRIB_STACK_DEPTH
    = GL.GL_MAX_ATTRIB_STACK_DEPTH;

  public static final int GL_MAX_MODELVIEW_STACK_DEPTH
    = GL.GL_MAX_MODELVIEW_STACK_DEPTH;

  public static final int GL_MAX_NAME_STACK_DEPTH
    = GL.GL_MAX_NAME_STACK_DEPTH;

  public static final int GL_MAX_PROJECTION_STACK_DEPTH
    = GL.GL_MAX_PROJECTION_STACK_DEPTH;

  public static final int GL_MAX_TEXTURE_STACK_DEPTH
    = GL.GL_MAX_TEXTURE_STACK_DEPTH;

  public static final int GL_MAX_EVAL_ORDER
    = GL.GL_MAX_EVAL_ORDER;

  public static final int GL_MAX_LIGHTS
    = GL.GL_MAX_LIGHTS;

  public static final int GL_MAX_CLIP_PLANES
    = GL.GL_MAX_CLIP_PLANES;

  public static final int GL_MAX_TEXTURE_SIZE
    = GL.GL_MAX_TEXTURE_SIZE;

  public static final int GL_MAX_PIXEL_MAP_TABLE
    = GL.GL_MAX_PIXEL_MAP_TABLE;

  public static final int GL_MAX_VIEWPORT_DIMS
    = GL.GL_MAX_VIEWPORT_DIMS;

  public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH
    = GL.GL_MAX_CLIENT_ATTRIB_STACK_DEPTH;

  public static final int GL_ATTRIB_STACK_DEPTH
    = GL.GL_ATTRIB_STACK_DEPTH;

  public static final int GL_CLIENT_ATTRIB_STACK_DEPTH
    = GL.GL_CLIENT_ATTRIB_STACK_DEPTH;

  public static final int GL_COLOR_CLEAR_VALUE
    = GL.GL_COLOR_CLEAR_VALUE;

  public static final int GL_COLOR_WRITEMASK
    = GL.GL_COLOR_WRITEMASK;

  public static final int GL_CURRENT_INDEX
    = GL.GL_CURRENT_INDEX;

  public static final int GL_CURRENT_COLOR
    = GL.GL_CURRENT_COLOR;

  public static final int GL_CURRENT_NORMAL
    = GL.GL_CURRENT_NORMAL;

  public static final int GL_CURRENT_RASTER_COLOR
    = GL.GL_CURRENT_RASTER_COLOR;

  public static final int GL_CURRENT_RASTER_DISTANCE
    = GL.GL_CURRENT_RASTER_DISTANCE;

  public static final int GL_CURRENT_RASTER_INDEX
    = GL.GL_CURRENT_RASTER_INDEX;

  public static final int GL_CURRENT_RASTER_POSITION
    = GL.GL_CURRENT_RASTER_POSITION;

  public static final int GL_CURRENT_RASTER_TEXTURE_COORDS
    = GL.GL_CURRENT_RASTER_TEXTURE_COORDS;

  public static final int GL_CURRENT_RASTER_POSITION_VALID
    = GL.GL_CURRENT_RASTER_POSITION_VALID;

  public static final int GL_CURRENT_TEXTURE_COORDS
    = GL.GL_CURRENT_TEXTURE_COORDS;

  public static final int GL_INDEX_CLEAR_VALUE
    = GL.GL_INDEX_CLEAR_VALUE;

  public static final int GL_INDEX_MODE
    = GL.GL_INDEX_MODE;

  public static final int GL_INDEX_WRITEMASK
    = GL.GL_INDEX_WRITEMASK;

  public static final int GL_MODELVIEW_MATRIX
    = GL.GL_MODELVIEW_MATRIX;

  public static final int GL_MODELVIEW_STACK_DEPTH
    = GL.GL_MODELVIEW_STACK_DEPTH;

  public static final int GL_NAME_STACK_DEPTH
    = GL.GL_NAME_STACK_DEPTH;

  public static final int GL_PROJECTION_MATRIX
    = GL.GL_PROJECTION_MATRIX;

  public static final int GL_PROJECTION_STACK_DEPTH
    = GL.GL_PROJECTION_STACK_DEPTH;

  public static final int GL_RENDER_MODE
    = GL.GL_RENDER_MODE;

  public static final int GL_RGBA_MODE
    = GL.GL_RGBA_MODE;

  public static final int GL_TEXTURE_MATRIX
    = GL.GL_TEXTURE_MATRIX;

  public static final int GL_TEXTURE_STACK_DEPTH
    = GL.GL_TEXTURE_STACK_DEPTH;

  public static final int GL_VIEWPORT
    = GL.GL_VIEWPORT;

  public static final int GL_AUTO_NORMAL
    = GL.GL_AUTO_NORMAL;

  public static final int GL_MAP1_COLOR_4
    = GL.GL_MAP1_COLOR_4;

  public static final int GL_MAP1_INDEX
    = GL.GL_MAP1_INDEX;

  public static final int GL_MAP1_NORMAL
    = GL.GL_MAP1_NORMAL;

  public static final int GL_MAP1_TEXTURE_COORD_1
    = GL.GL_MAP1_TEXTURE_COORD_1;

  public static final int GL_MAP1_TEXTURE_COORD_2
    = GL.GL_MAP1_TEXTURE_COORD_2;

  public static final int GL_MAP1_TEXTURE_COORD_3
    = GL.GL_MAP1_TEXTURE_COORD_3;

  public static final int GL_MAP1_TEXTURE_COORD_4
    = GL.GL_MAP1_TEXTURE_COORD_4;

  public static final int GL_MAP1_VERTEX_3
    = GL.GL_MAP1_VERTEX_3;

  public static final int GL_MAP1_VERTEX_4
    = GL.GL_MAP1_VERTEX_4;

  public static final int GL_MAP2_COLOR_4
    = GL.GL_MAP2_COLOR_4;

  public static final int GL_MAP2_INDEX
    = GL.GL_MAP2_INDEX;

  public static final int GL_MAP2_NORMAL
    = GL.GL_MAP2_NORMAL;

  public static final int GL_MAP2_TEXTURE_COORD_1
    = GL.GL_MAP2_TEXTURE_COORD_1;

  public static final int GL_MAP2_TEXTURE_COORD_2
    = GL.GL_MAP2_TEXTURE_COORD_2;

  public static final int GL_MAP2_TEXTURE_COORD_3
    = GL.GL_MAP2_TEXTURE_COORD_3;

  public static final int GL_MAP2_TEXTURE_COORD_4
    = GL.GL_MAP2_TEXTURE_COORD_4;

  public static final int GL_MAP2_VERTEX_3
    = GL.GL_MAP2_VERTEX_3;

  public static final int GL_MAP2_VERTEX_4
    = GL.GL_MAP2_VERTEX_4;

  public static final int GL_MAP1_GRID_DOMAIN
    = GL.GL_MAP1_GRID_DOMAIN;

  public static final int GL_MAP1_GRID_SEGMENTS
    = GL.GL_MAP1_GRID_SEGMENTS;

  public static final int GL_MAP2_GRID_DOMAIN
    = GL.GL_MAP2_GRID_DOMAIN;

  public static final int GL_MAP2_GRID_SEGMENTS
    = GL.GL_MAP2_GRID_SEGMENTS;

  public static final int GL_COEFF
    = GL.GL_COEFF;

  public static final int GL_DOMAIN
    = GL.GL_DOMAIN;

  public static final int GL_ORDER
    = GL.GL_ORDER;

  public static final int GL_FOG_HINT
    = GL.GL_FOG_HINT;

  public static final int GL_LINE_SMOOTH_HINT
    = GL.GL_LINE_SMOOTH_HINT;

  public static final int GL_PERSPECTIVE_CORRECTION_HINT
    = GL.GL_PERSPECTIVE_CORRECTION_HINT;

  public static final int GL_POINT_SMOOTH_HINT
    = GL.GL_POINT_SMOOTH_HINT;

  public static final int GL_POLYGON_SMOOTH_HINT
    = GL.GL_POLYGON_SMOOTH_HINT;

  public static final int GL_DONT_CARE
    = GL.GL_DONT_CARE;

  public static final int GL_FASTEST
    = GL.GL_FASTEST;

  public static final int GL_NICEST
    = GL.GL_NICEST;

  public static final int GL_SCISSOR_TEST
    = GL.GL_SCISSOR_TEST;

  public static final int GL_SCISSOR_BOX
    = GL.GL_SCISSOR_BOX;

  public static final int GL_MAP_COLOR
    = GL.GL_MAP_COLOR;

  public static final int GL_MAP_STENCIL
    = GL.GL_MAP_STENCIL;

  public static final int GL_INDEX_SHIFT
    = GL.GL_INDEX_SHIFT;

  public static final int GL_INDEX_OFFSET
    = GL.GL_INDEX_OFFSET;

  public static final int GL_RED_SCALE
    = GL.GL_RED_SCALE;

  public static final int GL_RED_BIAS
    = GL.GL_RED_BIAS;

  public static final int GL_GREEN_SCALE
    = GL.GL_GREEN_SCALE;

  public static final int GL_GREEN_BIAS
    = GL.GL_GREEN_BIAS;

  public static final int GL_BLUE_SCALE
    = GL.GL_BLUE_SCALE;

  public static final int GL_BLUE_BIAS
    = GL.GL_BLUE_BIAS;

  public static final int GL_ALPHA_SCALE
    = GL.GL_ALPHA_SCALE;

  public static final int GL_ALPHA_BIAS
    = GL.GL_ALPHA_BIAS;

  public static final int GL_DEPTH_SCALE
    = GL.GL_DEPTH_SCALE;

  public static final int GL_DEPTH_BIAS
    = GL.GL_DEPTH_BIAS;

  public static final int GL_PIXEL_MAP_S_TO_S_SIZE
    = GL.GL_PIXEL_MAP_S_TO_S_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_I_SIZE
    = GL.GL_PIXEL_MAP_I_TO_I_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_R_SIZE
    = GL.GL_PIXEL_MAP_I_TO_R_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_G_SIZE
    = GL.GL_PIXEL_MAP_I_TO_G_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_B_SIZE
    = GL.GL_PIXEL_MAP_I_TO_B_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_A_SIZE
    = GL.GL_PIXEL_MAP_I_TO_A_SIZE;

  public static final int GL_PIXEL_MAP_R_TO_R_SIZE
    = GL.GL_PIXEL_MAP_R_TO_R_SIZE;

  public static final int GL_PIXEL_MAP_G_TO_G_SIZE
    = GL.GL_PIXEL_MAP_G_TO_G_SIZE;

  public static final int GL_PIXEL_MAP_B_TO_B_SIZE
    = GL.GL_PIXEL_MAP_B_TO_B_SIZE;

  public static final int GL_PIXEL_MAP_A_TO_A_SIZE
    = GL.GL_PIXEL_MAP_A_TO_A_SIZE;

  public static final int GL_PIXEL_MAP_S_TO_S
    = GL.GL_PIXEL_MAP_S_TO_S;

  public static final int GL_PIXEL_MAP_I_TO_I
    = GL.GL_PIXEL_MAP_I_TO_I;

  public static final int GL_PIXEL_MAP_I_TO_R
    = GL.GL_PIXEL_MAP_I_TO_R;

  public static final int GL_PIXEL_MAP_I_TO_G
    = GL.GL_PIXEL_MAP_I_TO_G;

  public static final int GL_PIXEL_MAP_I_TO_B
    = GL.GL_PIXEL_MAP_I_TO_B;

  public static final int GL_PIXEL_MAP_I_TO_A
    = GL.GL_PIXEL_MAP_I_TO_A;

  public static final int GL_PIXEL_MAP_R_TO_R
    = GL.GL_PIXEL_MAP_R_TO_R;

  public static final int GL_PIXEL_MAP_G_TO_G
    = GL.GL_PIXEL_MAP_G_TO_G;

  public static final int GL_PIXEL_MAP_B_TO_B
    = GL.GL_PIXEL_MAP_B_TO_B;

  public static final int GL_PIXEL_MAP_A_TO_A
    = GL.GL_PIXEL_MAP_A_TO_A;

  public static final int GL_PACK_ALIGNMENT
    = GL.GL_PACK_ALIGNMENT;

  public static final int GL_PACK_LSB_FIRST
    = GL.GL_PACK_LSB_FIRST;

  public static final int GL_PACK_ROW_LENGTH
    = GL.GL_PACK_ROW_LENGTH;

  public static final int GL_PACK_SKIP_PIXELS
    = GL.GL_PACK_SKIP_PIXELS;

  public static final int GL_PACK_SKIP_ROWS
    = GL.GL_PACK_SKIP_ROWS;

  public static final int GL_PACK_SWAP_BYTES
    = GL.GL_PACK_SWAP_BYTES;

  public static final int GL_UNPACK_ALIGNMENT
    = GL.GL_UNPACK_ALIGNMENT;

  public static final int GL_UNPACK_LSB_FIRST
    = GL.GL_UNPACK_LSB_FIRST;

  public static final int GL_UNPACK_ROW_LENGTH
    = GL.GL_UNPACK_ROW_LENGTH;

  public static final int GL_UNPACK_SKIP_PIXELS
    = GL.GL_UNPACK_SKIP_PIXELS;

  public static final int GL_UNPACK_SKIP_ROWS
    = GL.GL_UNPACK_SKIP_ROWS;

  public static final int GL_UNPACK_SWAP_BYTES
    = GL.GL_UNPACK_SWAP_BYTES;

  public static final int GL_ZOOM_X
    = GL.GL_ZOOM_X;

  public static final int GL_ZOOM_Y
    = GL.GL_ZOOM_Y;

  public static final int GL_TEXTURE_ENV
    = GL.GL_TEXTURE_ENV;

  public static final int GL_TEXTURE_ENV_MODE
    = GL.GL_TEXTURE_ENV_MODE;

  public static final int GL_TEXTURE_1D
    = GL.GL_TEXTURE_1D;

  public static final int GL_TEXTURE_2D
    = GL.GL_TEXTURE_2D;

  public static final int GL_TEXTURE_WRAP_S
    = GL.GL_TEXTURE_WRAP_S;

  public static final int GL_TEXTURE_WRAP_T
    = GL.GL_TEXTURE_WRAP_T;

  public static final int GL_TEXTURE_MAG_FILTER
    = GL.GL_TEXTURE_MAG_FILTER;

  public static final int GL_TEXTURE_MIN_FILTER
    = GL.GL_TEXTURE_MIN_FILTER;

  public static final int GL_TEXTURE_ENV_COLOR
    = GL.GL_TEXTURE_ENV_COLOR;

  public static final int GL_TEXTURE_GEN_S
    = GL.GL_TEXTURE_GEN_S;

  public static final int GL_TEXTURE_GEN_T
    = GL.GL_TEXTURE_GEN_T;

  public static final int GL_TEXTURE_GEN_MODE
    = GL.GL_TEXTURE_GEN_MODE;

  public static final int GL_TEXTURE_BORDER_COLOR
    = GL.GL_TEXTURE_BORDER_COLOR;

  public static final int GL_TEXTURE_WIDTH
    = GL.GL_TEXTURE_WIDTH;

  public static final int GL_TEXTURE_HEIGHT
    = GL.GL_TEXTURE_HEIGHT;

  public static final int GL_TEXTURE_BORDER
    = GL.GL_TEXTURE_BORDER;

  public static final int GL_TEXTURE_COMPONENTS
    = GL.GL_TEXTURE_COMPONENTS;

  public static final int GL_TEXTURE_RED_SIZE
    = GL.GL_TEXTURE_RED_SIZE;

  public static final int GL_TEXTURE_GREEN_SIZE
    = GL.GL_TEXTURE_GREEN_SIZE;

  public static final int GL_TEXTURE_BLUE_SIZE
    = GL.GL_TEXTURE_BLUE_SIZE;

  public static final int GL_TEXTURE_ALPHA_SIZE
    = GL.GL_TEXTURE_ALPHA_SIZE;

  public static final int GL_TEXTURE_LUMINANCE_SIZE
    = GL.GL_TEXTURE_LUMINANCE_SIZE;

  public static final int GL_TEXTURE_INTENSITY_SIZE
    = GL.GL_TEXTURE_INTENSITY_SIZE;

  public static final int GL_NEAREST_MIPMAP_NEAREST
    = GL.GL_NEAREST_MIPMAP_NEAREST;

  public static final int GL_NEAREST_MIPMAP_LINEAR
    = GL.GL_NEAREST_MIPMAP_LINEAR;

  public static final int GL_LINEAR_MIPMAP_NEAREST
    = GL.GL_LINEAR_MIPMAP_NEAREST;

  public static final int GL_LINEAR_MIPMAP_LINEAR
    = GL.GL_LINEAR_MIPMAP_LINEAR;

  public static final int GL_OBJECT_LINEAR
    = GL.GL_OBJECT_LINEAR;

  public static final int GL_OBJECT_PLANE
    = GL.GL_OBJECT_PLANE;

  public static final int GL_EYE_LINEAR
    = GL.GL_EYE_LINEAR;

  public static final int GL_EYE_PLANE
    = GL.GL_EYE_PLANE;

  public static final int GL_SPHERE_MAP
    = GL.GL_SPHERE_MAP;

  public static final int GL_DECAL
    = GL.GL_DECAL;

  public static final int GL_MODULATE
    = GL.GL_MODULATE;

  public static final int GL_NEAREST
    = GL.GL_NEAREST;

  public static final int GL_REPEAT
    = GL.GL_REPEAT;

  public static final int GL_CLAMP
    = GL.GL_CLAMP;

  public static final int GL_S
    = GL.GL_S;

  public static final int GL_T
    = GL.GL_T;

  public static final int GL_R
    = GL.GL_R;

  public static final int GL_Q
    = GL.GL_Q;

  public static final int GL_TEXTURE_GEN_R
    = GL.GL_TEXTURE_GEN_R;

  public static final int GL_TEXTURE_GEN_Q
    = GL.GL_TEXTURE_GEN_Q;

  public static final int GL_VENDOR
    = GL.GL_VENDOR;

  public static final int GL_RENDERER
    = GL.GL_RENDERER;

  public static final int GL_VERSION
    = GL.GL_VERSION;

  public static final int GL_EXTENSIONS
    = GL.GL_EXTENSIONS;

  public static final int GL_NO_ERROR
    = GL.GL_NO_ERROR;

  public static final int GL_INVALID_VALUE
    = GL.GL_INVALID_VALUE;

  public static final int GL_INVALID_ENUM
    = GL.GL_INVALID_ENUM;

  public static final int GL_INVALID_OPERATION
    = GL.GL_INVALID_OPERATION;

  public static final int GL_STACK_OVERFLOW
    = GL.GL_STACK_OVERFLOW;

  public static final int GL_STACK_UNDERFLOW
    = GL.GL_STACK_UNDERFLOW;

  public static final int GL_OUT_OF_MEMORY
    = GL.GL_OUT_OF_MEMORY;

  public static final int GL_CURRENT_BIT
    = GL.GL_CURRENT_BIT;

  public static final int GL_POINT_BIT
    = GL.GL_POINT_BIT;

  public static final int GL_LINE_BIT
    = GL.GL_LINE_BIT;

  public static final int GL_POLYGON_BIT
    = GL.GL_POLYGON_BIT;

  public static final int GL_POLYGON_STIPPLE_BIT
    = GL.GL_POLYGON_STIPPLE_BIT;

  public static final int GL_PIXEL_MODE_BIT
    = GL.GL_PIXEL_MODE_BIT;

  public static final int GL_LIGHTING_BIT
    = GL.GL_LIGHTING_BIT;

  public static final int GL_FOG_BIT
    = GL.GL_FOG_BIT;

  public static final int GL_DEPTH_BUFFER_BIT
    = GL.GL_DEPTH_BUFFER_BIT;

  public static final int GL_ACCUM_BUFFER_BIT
    = GL.GL_ACCUM_BUFFER_BIT;

  public static final int GL_STENCIL_BUFFER_BIT
    = GL.GL_STENCIL_BUFFER_BIT;

  public static final int GL_VIEWPORT_BIT
    = GL.GL_VIEWPORT_BIT;

  public static final int GL_TRANSFORM_BIT
    = GL.GL_TRANSFORM_BIT;

  public static final int GL_ENABLE_BIT
    = GL.GL_ENABLE_BIT;

  public static final int GL_COLOR_BUFFER_BIT
    = GL.GL_COLOR_BUFFER_BIT;

  public static final int GL_HINT_BIT
    = GL.GL_HINT_BIT;

  public static final int GL_EVAL_BIT
    = GL.GL_EVAL_BIT;

  public static final int GL_LIST_BIT
    = GL.GL_LIST_BIT;

  public static final int GL_TEXTURE_BIT
    = GL.GL_TEXTURE_BIT;

  public static final int GL_SCISSOR_BIT
    = GL.GL_SCISSOR_BIT;

  public static final int GL_ALL_ATTRIB_BITS
    = GL.GL_ALL_ATTRIB_BITS;

  public static final int GL_PROXY_TEXTURE_1D
    = GL.GL_PROXY_TEXTURE_1D;

  public static final int GL_PROXY_TEXTURE_2D
    = GL.GL_PROXY_TEXTURE_2D;

  public static final int GL_TEXTURE_PRIORITY
    = GL.GL_TEXTURE_PRIORITY;

  public static final int GL_TEXTURE_RESIDENT
    = GL.GL_TEXTURE_RESIDENT;

  public static final int GL_TEXTURE_BINDING_1D
    = GL.GL_TEXTURE_BINDING_1D;

  public static final int GL_TEXTURE_BINDING_2D
    = GL.GL_TEXTURE_BINDING_2D;

  public static final int GL_TEXTURE_INTERNAL_FORMAT
    = GL.GL_TEXTURE_INTERNAL_FORMAT;

  public static final int GL_ALPHA4
    = GL.GL_ALPHA4;

  public static final int GL_ALPHA8
    = GL.GL_ALPHA8;

  public static final int GL_ALPHA12
    = GL.GL_ALPHA12;

  public static final int GL_ALPHA16
    = GL.GL_ALPHA16;

  public static final int GL_LUMINANCE4
    = GL.GL_LUMINANCE4;

  public static final int GL_LUMINANCE8
    = GL.GL_LUMINANCE8;

  public static final int GL_LUMINANCE12
    = GL.GL_LUMINANCE12;

  public static final int GL_LUMINANCE16
    = GL.GL_LUMINANCE16;

  public static final int GL_LUMINANCE4_ALPHA4
    = GL.GL_LUMINANCE4_ALPHA4;

  public static final int GL_LUMINANCE6_ALPHA2
    = GL.GL_LUMINANCE6_ALPHA2;

  public static final int GL_LUMINANCE8_ALPHA8
    = GL.GL_LUMINANCE8_ALPHA8;

  public static final int GL_LUMINANCE12_ALPHA4
    = GL.GL_LUMINANCE12_ALPHA4;

  public static final int GL_LUMINANCE12_ALPHA12
    = GL.GL_LUMINANCE12_ALPHA12;

  public static final int GL_LUMINANCE16_ALPHA16
    = GL.GL_LUMINANCE16_ALPHA16;

  public static final int GL_INTENSITY
    = GL.GL_INTENSITY;

  public static final int GL_INTENSITY4
    = GL.GL_INTENSITY4;

  public static final int GL_INTENSITY8
    = GL.GL_INTENSITY8;

  public static final int GL_INTENSITY12
    = GL.GL_INTENSITY12;

  public static final int GL_INTENSITY16
    = GL.GL_INTENSITY16;

  public static final int GL_R3_G3_B2
    = GL.GL_R3_G3_B2;

  public static final int GL_RGB4
    = GL.GL_RGB4;

  public static final int GL_RGB5
    = GL.GL_RGB5;

  public static final int GL_RGB8
    = GL.GL_RGB8;

  public static final int GL_RGB10
    = GL.GL_RGB10;

  public static final int GL_RGB12
    = GL.GL_RGB12;

  public static final int GL_RGB16
    = GL.GL_RGB16;

  public static final int GL_RGBA2
    = GL.GL_RGBA2;

  public static final int GL_RGBA4
    = GL.GL_RGBA4;

  public static final int GL_RGB5_A1
    = GL.GL_RGB5_A1;

  public static final int GL_RGBA8
    = GL.GL_RGBA8;

  public static final int GL_RGB10_A2
    = GL.GL_RGB10_A2;

  public static final int GL_RGBA12
    = GL.GL_RGBA12;

  public static final int GL_RGBA16
    = GL.GL_RGBA16;

  public static final int GL_CLIENT_PIXEL_STORE_BIT
    = GL.GL_CLIENT_PIXEL_STORE_BIT;

  public static final int GL_CLIENT_VERTEX_ARRAY_BIT
    = GL.GL_CLIENT_VERTEX_ARRAY_BIT;

  public static final long GL_ALL_CLIENT_ATTRIB_BITS
    = GL.GL_ALL_CLIENT_ATTRIB_BITS;

  public static final long GL_CLIENT_ALL_ATTRIB_BITS
    = GL.GL_CLIENT_ALL_ATTRIB_BITS;

  public static final int GL_VERTEX_ARRAY
    = GL.GL_VERTEX_ARRAY;

  public static final int GL_NORMAL_ARRAY
    = GL.GL_NORMAL_ARRAY;

  public static final int GL_COLOR_ARRAY
    = GL.GL_COLOR_ARRAY;

  public static final int GL_INDEX_ARRAY
    = GL.GL_INDEX_ARRAY;

  public static final int GL_TEXTURE_COORD_ARRAY
    = GL.GL_TEXTURE_COORD_ARRAY;

  public static final int GL_EDGE_FLAG_ARRAY
    = GL.GL_EDGE_FLAG_ARRAY;

  public static final int GL_VERTEX_ARRAY_SIZE
    = GL.GL_VERTEX_ARRAY_SIZE;

  public static final int GL_VERTEX_ARRAY_TYPE
    = GL.GL_VERTEX_ARRAY_TYPE;

  public static final int GL_VERTEX_ARRAY_STRIDE
    = GL.GL_VERTEX_ARRAY_STRIDE;

  public static final int GL_NORMAL_ARRAY_TYPE
    = GL.GL_NORMAL_ARRAY_TYPE;

  public static final int GL_NORMAL_ARRAY_STRIDE
    = GL.GL_NORMAL_ARRAY_STRIDE;

  public static final int GL_COLOR_ARRAY_SIZE
    = GL.GL_COLOR_ARRAY_SIZE;

  public static final int GL_COLOR_ARRAY_TYPE
    = GL.GL_COLOR_ARRAY_TYPE;

  public static final int GL_COLOR_ARRAY_STRIDE
    = GL.GL_COLOR_ARRAY_STRIDE;

  public static final int GL_INDEX_ARRAY_TYPE
    = GL.GL_INDEX_ARRAY_TYPE;

  public static final int GL_INDEX_ARRAY_STRIDE
    = GL.GL_INDEX_ARRAY_STRIDE;

  public static final int GL_TEXTURE_COORD_ARRAY_SIZE
    = GL.GL_TEXTURE_COORD_ARRAY_SIZE;

  public static final int GL_TEXTURE_COORD_ARRAY_TYPE
    = GL.GL_TEXTURE_COORD_ARRAY_TYPE;

  public static final int GL_TEXTURE_COORD_ARRAY_STRIDE
    = GL.GL_TEXTURE_COORD_ARRAY_STRIDE;

  public static final int GL_EDGE_FLAG_ARRAY_STRIDE
    = GL.GL_EDGE_FLAG_ARRAY_STRIDE;

  public static final int GL_VERTEX_ARRAY_POINTER
    = GL.GL_VERTEX_ARRAY_POINTER;

  public static final int GL_NORMAL_ARRAY_POINTER
    = GL.GL_NORMAL_ARRAY_POINTER;

  public static final int GL_COLOR_ARRAY_POINTER
    = GL.GL_COLOR_ARRAY_POINTER;

  public static final int GL_INDEX_ARRAY_POINTER
    = GL.GL_INDEX_ARRAY_POINTER;

  public static final int GL_TEXTURE_COORD_ARRAY_POINTER
    = GL.GL_TEXTURE_COORD_ARRAY_POINTER;

  public static final int GL_EDGE_FLAG_ARRAY_POINTER
    = GL.GL_EDGE_FLAG_ARRAY_POINTER;

  public static final int GL_V2F
    = GL.GL_V2F;

  public static final int GL_V3F
    = GL.GL_V3F;

  public static final int GL_C4UB_V2F
    = GL.GL_C4UB_V2F;

  public static final int GL_C4UB_V3F
    = GL.GL_C4UB_V3F;

  public static final int GL_C3F_V3F
    = GL.GL_C3F_V3F;

  public static final int GL_N3F_V3F
    = GL.GL_N3F_V3F;

  public static final int GL_C4F_N3F_V3F
    = GL.GL_C4F_N3F_V3F;

  public static final int GL_T2F_V3F
    = GL.GL_T2F_V3F;

  public static final int GL_T4F_V4F
    = GL.GL_T4F_V4F;

  public static final int GL_T2F_C4UB_V3F
    = GL.GL_T2F_C4UB_V3F;

  public static final int GL_T2F_C3F_V3F
    = GL.GL_T2F_C3F_V3F;

  public static final int GL_T2F_N3F_V3F
    = GL.GL_T2F_N3F_V3F;

  public static final int GL_T2F_C4F_N3F_V3F
    = GL.GL_T2F_C4F_N3F_V3F;

  public static final int GL_T4F_C4F_N3F_V4F
    = GL.GL_T4F_C4F_N3F_V4F;

  public static final int GL_GLEXT_VERSION
    = GL.GL_GLEXT_VERSION;

  public static final int GL_UNSIGNED_BYTE_3_3_2
    = GL.GL_UNSIGNED_BYTE_3_3_2;

  public static final int GL_UNSIGNED_SHORT_4_4_4_4
    = GL.GL_UNSIGNED_SHORT_4_4_4_4;

  public static final int GL_UNSIGNED_SHORT_5_5_5_1
    = GL.GL_UNSIGNED_SHORT_5_5_5_1;

  public static final int GL_UNSIGNED_INT_8_8_8_8
    = GL.GL_UNSIGNED_INT_8_8_8_8;

  public static final int GL_UNSIGNED_INT_10_10_10_2
    = GL.GL_UNSIGNED_INT_10_10_10_2;

  public static final int GL_RESCALE_NORMAL
    = GL.GL_RESCALE_NORMAL;

  public static final int GL_TEXTURE_BINDING_3D
    = GL.GL_TEXTURE_BINDING_3D;

  public static final int GL_PACK_SKIP_IMAGES
    = GL.GL_PACK_SKIP_IMAGES;

  public static final int GL_PACK_IMAGE_HEIGHT
    = GL.GL_PACK_IMAGE_HEIGHT;

  public static final int GL_UNPACK_SKIP_IMAGES
    = GL.GL_UNPACK_SKIP_IMAGES;

  public static final int GL_UNPACK_IMAGE_HEIGHT
    = GL.GL_UNPACK_IMAGE_HEIGHT;

  public static final int GL_TEXTURE_3D
    = GL.GL_TEXTURE_3D;

  public static final int GL_PROXY_TEXTURE_3D
    = GL.GL_PROXY_TEXTURE_3D;

  public static final int GL_TEXTURE_DEPTH
    = GL.GL_TEXTURE_DEPTH;

  public static final int GL_TEXTURE_WRAP_R
    = GL.GL_TEXTURE_WRAP_R;

  public static final int GL_MAX_3D_TEXTURE_SIZE
    = GL.GL_MAX_3D_TEXTURE_SIZE;

  public static final int GL_UNSIGNED_BYTE_2_3_3_REV
    = GL.GL_UNSIGNED_BYTE_2_3_3_REV;

  public static final int GL_UNSIGNED_SHORT_5_6_5
    = GL.GL_UNSIGNED_SHORT_5_6_5;

  public static final int GL_UNSIGNED_SHORT_5_6_5_REV
    = GL.GL_UNSIGNED_SHORT_5_6_5_REV;

  public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV
    = GL.GL_UNSIGNED_SHORT_4_4_4_4_REV;

  public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV
    = GL.GL_UNSIGNED_SHORT_1_5_5_5_REV;

  public static final int GL_UNSIGNED_INT_8_8_8_8_REV
    = GL.GL_UNSIGNED_INT_8_8_8_8_REV;

  public static final int GL_UNSIGNED_INT_2_10_10_10_REV
    = GL.GL_UNSIGNED_INT_2_10_10_10_REV;

  public static final int GL_BGR
    = GL.GL_BGR;

  public static final int GL_BGRA
    = GL.GL_BGRA;

  public static final int GL_MAX_ELEMENTS_VERTICES
    = GL.GL_MAX_ELEMENTS_VERTICES;

  public static final int GL_MAX_ELEMENTS_INDICES
    = GL.GL_MAX_ELEMENTS_INDICES;

  public static final int GL_CLAMP_TO_EDGE
    = GL.GL_CLAMP_TO_EDGE;

  public static final int GL_TEXTURE_MIN_LOD
    = GL.GL_TEXTURE_MIN_LOD;

  public static final int GL_TEXTURE_MAX_LOD
    = GL.GL_TEXTURE_MAX_LOD;

  public static final int GL_TEXTURE_BASE_LEVEL
    = GL.GL_TEXTURE_BASE_LEVEL;

  public static final int GL_TEXTURE_MAX_LEVEL
    = GL.GL_TEXTURE_MAX_LEVEL;

  public static final int GL_LIGHT_MODEL_COLOR_CONTROL
    = GL.GL_LIGHT_MODEL_COLOR_CONTROL;

  public static final int GL_SINGLE_COLOR
    = GL.GL_SINGLE_COLOR;

  public static final int GL_SEPARATE_SPECULAR_COLOR
    = GL.GL_SEPARATE_SPECULAR_COLOR;

  public static final int GL_SMOOTH_POINT_SIZE_RANGE
    = GL.GL_SMOOTH_POINT_SIZE_RANGE;

  public static final int GL_SMOOTH_POINT_SIZE_GRANULARITY
    = GL.GL_SMOOTH_POINT_SIZE_GRANULARITY;

  public static final int GL_SMOOTH_LINE_WIDTH_RANGE
    = GL.GL_SMOOTH_LINE_WIDTH_RANGE;

  public static final int GL_SMOOTH_LINE_WIDTH_GRANULARITY
    = GL.GL_SMOOTH_LINE_WIDTH_GRANULARITY;

  public static final int GL_ALIASED_POINT_SIZE_RANGE
    = GL.GL_ALIASED_POINT_SIZE_RANGE;

  public static final int GL_ALIASED_LINE_WIDTH_RANGE
    = GL.GL_ALIASED_LINE_WIDTH_RANGE;

  public static final int GL_CONSTANT_COLOR
    = GL.GL_CONSTANT_COLOR;

  public static final int GL_ONE_MINUS_CONSTANT_COLOR
    = GL.GL_ONE_MINUS_CONSTANT_COLOR;

  public static final int GL_CONSTANT_ALPHA
    = GL.GL_CONSTANT_ALPHA;

  public static final int GL_ONE_MINUS_CONSTANT_ALPHA
    = GL.GL_ONE_MINUS_CONSTANT_ALPHA;

  public static final int GL_BLEND_COLOR
    = GL.GL_BLEND_COLOR;

  public static final int GL_FUNC_ADD
    = GL.GL_FUNC_ADD;

  public static final int GL_MIN
    = GL.GL_MIN;

  public static final int GL_MAX
    = GL.GL_MAX;

  public static final int GL_BLEND_EQUATION
    = GL.GL_BLEND_EQUATION;

  public static final int GL_FUNC_SUBTRACT
    = GL.GL_FUNC_SUBTRACT;

  public static final int GL_FUNC_REVERSE_SUBTRACT
    = GL.GL_FUNC_REVERSE_SUBTRACT;

  public static final int GL_CONVOLUTION_1D
    = GL.GL_CONVOLUTION_1D;

  public static final int GL_CONVOLUTION_2D
    = GL.GL_CONVOLUTION_2D;

  public static final int GL_SEPARABLE_2D
    = GL.GL_SEPARABLE_2D;

  public static final int GL_CONVOLUTION_BORDER_MODE
    = GL.GL_CONVOLUTION_BORDER_MODE;

  public static final int GL_CONVOLUTION_FILTER_SCALE
    = GL.GL_CONVOLUTION_FILTER_SCALE;

  public static final int GL_CONVOLUTION_FILTER_BIAS
    = GL.GL_CONVOLUTION_FILTER_BIAS;

  public static final int GL_REDUCE
    = GL.GL_REDUCE;

  public static final int GL_CONVOLUTION_FORMAT
    = GL.GL_CONVOLUTION_FORMAT;

  public static final int GL_CONVOLUTION_WIDTH
    = GL.GL_CONVOLUTION_WIDTH;

  public static final int GL_CONVOLUTION_HEIGHT
    = GL.GL_CONVOLUTION_HEIGHT;

  public static final int GL_MAX_CONVOLUTION_WIDTH
    = GL.GL_MAX_CONVOLUTION_WIDTH;

  public static final int GL_MAX_CONVOLUTION_HEIGHT
    = GL.GL_MAX_CONVOLUTION_HEIGHT;

  public static final int GL_POST_CONVOLUTION_RED_SCALE
    = GL.GL_POST_CONVOLUTION_RED_SCALE;

  public static final int GL_POST_CONVOLUTION_GREEN_SCALE
    = GL.GL_POST_CONVOLUTION_GREEN_SCALE;

  public static final int GL_POST_CONVOLUTION_BLUE_SCALE
    = GL.GL_POST_CONVOLUTION_BLUE_SCALE;

  public static final int GL_POST_CONVOLUTION_ALPHA_SCALE
    = GL.GL_POST_CONVOLUTION_ALPHA_SCALE;

  public static final int GL_POST_CONVOLUTION_RED_BIAS
    = GL.GL_POST_CONVOLUTION_RED_BIAS;

  public static final int GL_POST_CONVOLUTION_GREEN_BIAS
    = GL.GL_POST_CONVOLUTION_GREEN_BIAS;

  public static final int GL_POST_CONVOLUTION_BLUE_BIAS
    = GL.GL_POST_CONVOLUTION_BLUE_BIAS;

  public static final int GL_POST_CONVOLUTION_ALPHA_BIAS
    = GL.GL_POST_CONVOLUTION_ALPHA_BIAS;

  public static final int GL_HISTOGRAM
    = GL.GL_HISTOGRAM;

  public static final int GL_PROXY_HISTOGRAM
    = GL.GL_PROXY_HISTOGRAM;

  public static final int GL_HISTOGRAM_WIDTH
    = GL.GL_HISTOGRAM_WIDTH;

  public static final int GL_HISTOGRAM_FORMAT
    = GL.GL_HISTOGRAM_FORMAT;

  public static final int GL_HISTOGRAM_RED_SIZE
    = GL.GL_HISTOGRAM_RED_SIZE;

  public static final int GL_HISTOGRAM_GREEN_SIZE
    = GL.GL_HISTOGRAM_GREEN_SIZE;

  public static final int GL_HISTOGRAM_BLUE_SIZE
    = GL.GL_HISTOGRAM_BLUE_SIZE;

  public static final int GL_HISTOGRAM_ALPHA_SIZE
    = GL.GL_HISTOGRAM_ALPHA_SIZE;

  public static final int GL_HISTOGRAM_LUMINANCE_SIZE
    = GL.GL_HISTOGRAM_LUMINANCE_SIZE;

  public static final int GL_HISTOGRAM_SINK
    = GL.GL_HISTOGRAM_SINK;

  public static final int GL_MINMAX
    = GL.GL_MINMAX;

  public static final int GL_MINMAX_FORMAT
    = GL.GL_MINMAX_FORMAT;

  public static final int GL_MINMAX_SINK
    = GL.GL_MINMAX_SINK;

  public static final int GL_TABLE_TOO_LARGE
    = GL.GL_TABLE_TOO_LARGE;

  public static final int GL_COLOR_MATRIX
    = GL.GL_COLOR_MATRIX;

  public static final int GL_COLOR_MATRIX_STACK_DEPTH
    = GL.GL_COLOR_MATRIX_STACK_DEPTH;

  public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH
    = GL.GL_MAX_COLOR_MATRIX_STACK_DEPTH;

  public static final int GL_POST_COLOR_MATRIX_RED_SCALE
    = GL.GL_POST_COLOR_MATRIX_RED_SCALE;

  public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE
    = GL.GL_POST_COLOR_MATRIX_GREEN_SCALE;

  public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE
    = GL.GL_POST_COLOR_MATRIX_BLUE_SCALE;

  public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE
    = GL.GL_POST_COLOR_MATRIX_ALPHA_SCALE;

  public static final int GL_POST_COLOR_MATRIX_RED_BIAS
    = GL.GL_POST_COLOR_MATRIX_RED_BIAS;

  public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS
    = GL.GL_POST_COLOR_MATRIX_GREEN_BIAS;

  public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS
    = GL.GL_POST_COLOR_MATRIX_BLUE_BIAS;

  public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS
    = GL.GL_POST_COLOR_MATRIX_ALPHA_BIAS;

  public static final int GL_COLOR_TABLE
    = GL.GL_COLOR_TABLE;

  public static final int GL_POST_CONVOLUTION_COLOR_TABLE
    = GL.GL_POST_CONVOLUTION_COLOR_TABLE;

  public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE
    = GL.GL_POST_COLOR_MATRIX_COLOR_TABLE;

  public static final int GL_PROXY_COLOR_TABLE
    = GL.GL_PROXY_COLOR_TABLE;

  public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE
    = GL.GL_PROXY_POST_CONVOLUTION_COLOR_TABLE;

  public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE
    = GL.GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE;

  public static final int GL_COLOR_TABLE_SCALE
    = GL.GL_COLOR_TABLE_SCALE;

  public static final int GL_COLOR_TABLE_BIAS
    = GL.GL_COLOR_TABLE_BIAS;

  public static final int GL_COLOR_TABLE_FORMAT
    = GL.GL_COLOR_TABLE_FORMAT;

  public static final int GL_COLOR_TABLE_WIDTH
    = GL.GL_COLOR_TABLE_WIDTH;

  public static final int GL_COLOR_TABLE_RED_SIZE
    = GL.GL_COLOR_TABLE_RED_SIZE;

  public static final int GL_COLOR_TABLE_GREEN_SIZE
    = GL.GL_COLOR_TABLE_GREEN_SIZE;

  public static final int GL_COLOR_TABLE_BLUE_SIZE
    = GL.GL_COLOR_TABLE_BLUE_SIZE;

  public static final int GL_COLOR_TABLE_ALPHA_SIZE
    = GL.GL_COLOR_TABLE_ALPHA_SIZE;

  public static final int GL_COLOR_TABLE_LUMINANCE_SIZE
    = GL.GL_COLOR_TABLE_LUMINANCE_SIZE;

  public static final int GL_COLOR_TABLE_INTENSITY_SIZE
    = GL.GL_COLOR_TABLE_INTENSITY_SIZE;

  public static final int GL_CONSTANT_BORDER
    = GL.GL_CONSTANT_BORDER;

  public static final int GL_REPLICATE_BORDER
    = GL.GL_REPLICATE_BORDER;

  public static final int GL_CONVOLUTION_BORDER_COLOR
    = GL.GL_CONVOLUTION_BORDER_COLOR;

  public static final int GL_TEXTURE0
    = GL.GL_TEXTURE0;

  public static final int GL_TEXTURE1
    = GL.GL_TEXTURE1;

  public static final int GL_TEXTURE2
    = GL.GL_TEXTURE2;

  public static final int GL_TEXTURE3
    = GL.GL_TEXTURE3;

  public static final int GL_TEXTURE4
    = GL.GL_TEXTURE4;

  public static final int GL_TEXTURE5
    = GL.GL_TEXTURE5;

  public static final int GL_TEXTURE6
    = GL.GL_TEXTURE6;

  public static final int GL_TEXTURE7
    = GL.GL_TEXTURE7;

  public static final int GL_TEXTURE8
    = GL.GL_TEXTURE8;

  public static final int GL_TEXTURE9
    = GL.GL_TEXTURE9;

  public static final int GL_TEXTURE10
    = GL.GL_TEXTURE10;

  public static final int GL_TEXTURE11
    = GL.GL_TEXTURE11;

  public static final int GL_TEXTURE12
    = GL.GL_TEXTURE12;

  public static final int GL_TEXTURE13
    = GL.GL_TEXTURE13;

  public static final int GL_TEXTURE14
    = GL.GL_TEXTURE14;

  public static final int GL_TEXTURE15
    = GL.GL_TEXTURE15;

  public static final int GL_TEXTURE16
    = GL.GL_TEXTURE16;

  public static final int GL_TEXTURE17
    = GL.GL_TEXTURE17;

  public static final int GL_TEXTURE18
    = GL.GL_TEXTURE18;

  public static final int GL_TEXTURE19
    = GL.GL_TEXTURE19;

  public static final int GL_TEXTURE20
    = GL.GL_TEXTURE20;

  public static final int GL_TEXTURE21
    = GL.GL_TEXTURE21;

  public static final int GL_TEXTURE22
    = GL.GL_TEXTURE22;

  public static final int GL_TEXTURE23
    = GL.GL_TEXTURE23;

  public static final int GL_TEXTURE24
    = GL.GL_TEXTURE24;

  public static final int GL_TEXTURE25
    = GL.GL_TEXTURE25;

  public static final int GL_TEXTURE26
    = GL.GL_TEXTURE26;

  public static final int GL_TEXTURE27
    = GL.GL_TEXTURE27;

  public static final int GL_TEXTURE28
    = GL.GL_TEXTURE28;

  public static final int GL_TEXTURE29
    = GL.GL_TEXTURE29;

  public static final int GL_TEXTURE30
    = GL.GL_TEXTURE30;

  public static final int GL_TEXTURE31
    = GL.GL_TEXTURE31;

  public static final int GL_ACTIVE_TEXTURE
    = GL.GL_ACTIVE_TEXTURE;

  public static final int GL_CLIENT_ACTIVE_TEXTURE
    = GL.GL_CLIENT_ACTIVE_TEXTURE;

  public static final int GL_MAX_TEXTURE_UNITS
    = GL.GL_MAX_TEXTURE_UNITS;

  public static final int GL_TRANSPOSE_MODELVIEW_MATRIX
    = GL.GL_TRANSPOSE_MODELVIEW_MATRIX;

  public static final int GL_TRANSPOSE_PROJECTION_MATRIX
    = GL.GL_TRANSPOSE_PROJECTION_MATRIX;

  public static final int GL_TRANSPOSE_TEXTURE_MATRIX
    = GL.GL_TRANSPOSE_TEXTURE_MATRIX;

  public static final int GL_TRANSPOSE_COLOR_MATRIX
    = GL.GL_TRANSPOSE_COLOR_MATRIX;

  public static final int GL_MULTISAMPLE
    = GL.GL_MULTISAMPLE;

  public static final int GL_SAMPLE_ALPHA_TO_COVERAGE
    = GL.GL_SAMPLE_ALPHA_TO_COVERAGE;

  public static final int GL_SAMPLE_ALPHA_TO_ONE
    = GL.GL_SAMPLE_ALPHA_TO_ONE;

  public static final int GL_SAMPLE_COVERAGE
    = GL.GL_SAMPLE_COVERAGE;

  public static final int GL_SAMPLE_BUFFERS
    = GL.GL_SAMPLE_BUFFERS;

  public static final int GL_SAMPLES
    = GL.GL_SAMPLES;

  public static final int GL_SAMPLE_COVERAGE_VALUE
    = GL.GL_SAMPLE_COVERAGE_VALUE;

  public static final int GL_SAMPLE_COVERAGE_INVERT
    = GL.GL_SAMPLE_COVERAGE_INVERT;

  public static final int GL_MULTISAMPLE_BIT
    = GL.GL_MULTISAMPLE_BIT;

  public static final int GL_NORMAL_MAP
    = GL.GL_NORMAL_MAP;

  public static final int GL_REFLECTION_MAP
    = GL.GL_REFLECTION_MAP;

  public static final int GL_TEXTURE_CUBE_MAP
    = GL.GL_TEXTURE_CUBE_MAP;

  public static final int GL_TEXTURE_BINDING_CUBE_MAP
    = GL.GL_TEXTURE_BINDING_CUBE_MAP;

  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X
    = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X
    = GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;

  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y
    = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;

  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y
    = GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;

  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z
    = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;

  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    = GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;

  public static final int GL_PROXY_TEXTURE_CUBE_MAP
    = GL.GL_PROXY_TEXTURE_CUBE_MAP;

  public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE
    = GL.GL_MAX_CUBE_MAP_TEXTURE_SIZE;

  public static final int GL_COMPRESSED_ALPHA
    = GL.GL_COMPRESSED_ALPHA;

  public static final int GL_COMPRESSED_LUMINANCE
    = GL.GL_COMPRESSED_LUMINANCE;

  public static final int GL_COMPRESSED_LUMINANCE_ALPHA
    = GL.GL_COMPRESSED_LUMINANCE_ALPHA;

  public static final int GL_COMPRESSED_INTENSITY
    = GL.GL_COMPRESSED_INTENSITY;

  public static final int GL_COMPRESSED_RGB
    = GL.GL_COMPRESSED_RGB;

  public static final int GL_COMPRESSED_RGBA
    = GL.GL_COMPRESSED_RGBA;

  public static final int GL_TEXTURE_COMPRESSION_HINT
    = GL.GL_TEXTURE_COMPRESSION_HINT;

  public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE
    = GL.GL_TEXTURE_COMPRESSED_IMAGE_SIZE;

  public static final int GL_TEXTURE_COMPRESSED
    = GL.GL_TEXTURE_COMPRESSED;

  public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS
    = GL.GL_NUM_COMPRESSED_TEXTURE_FORMATS;

  public static final int GL_COMPRESSED_TEXTURE_FORMATS
    = GL.GL_COMPRESSED_TEXTURE_FORMATS;

  public static final int GL_CLAMP_TO_BORDER
    = GL.GL_CLAMP_TO_BORDER;

  public static final int GL_COMBINE
    = GL.GL_COMBINE;

  public static final int GL_COMBINE_RGB
    = GL.GL_COMBINE_RGB;

  public static final int GL_COMBINE_ALPHA
    = GL.GL_COMBINE_ALPHA;

  public static final int GL_SOURCE0_RGB
    = GL.GL_SOURCE0_RGB;

  public static final int GL_SOURCE1_RGB
    = GL.GL_SOURCE1_RGB;

  public static final int GL_SOURCE2_RGB
    = GL.GL_SOURCE2_RGB;

  public static final int GL_SOURCE0_ALPHA
    = GL.GL_SOURCE0_ALPHA;

  public static final int GL_SOURCE1_ALPHA
    = GL.GL_SOURCE1_ALPHA;

  public static final int GL_SOURCE2_ALPHA
    = GL.GL_SOURCE2_ALPHA;

  public static final int GL_OPERAND0_RGB
    = GL.GL_OPERAND0_RGB;

  public static final int GL_OPERAND1_RGB
    = GL.GL_OPERAND1_RGB;

  public static final int GL_OPERAND2_RGB
    = GL.GL_OPERAND2_RGB;

  public static final int GL_OPERAND0_ALPHA
    = GL.GL_OPERAND0_ALPHA;

  public static final int GL_OPERAND1_ALPHA
    = GL.GL_OPERAND1_ALPHA;

  public static final int GL_OPERAND2_ALPHA
    = GL.GL_OPERAND2_ALPHA;

  public static final int GL_RGB_SCALE
    = GL.GL_RGB_SCALE;

  public static final int GL_ADD_SIGNED
    = GL.GL_ADD_SIGNED;

  public static final int GL_INTERPOLATE
    = GL.GL_INTERPOLATE;

  public static final int GL_SUBTRACT
    = GL.GL_SUBTRACT;

  public static final int GL_CONSTANT
    = GL.GL_CONSTANT;

  public static final int GL_PRIMARY_COLOR
    = GL.GL_PRIMARY_COLOR;

  public static final int GL_PREVIOUS
    = GL.GL_PREVIOUS;

  public static final int GL_DOT3_RGB
    = GL.GL_DOT3_RGB;

  public static final int GL_DOT3_RGBA
    = GL.GL_DOT3_RGBA;

  public static final int GL_BLEND_DST_RGB
    = GL.GL_BLEND_DST_RGB;

  public static final int GL_BLEND_SRC_RGB
    = GL.GL_BLEND_SRC_RGB;

  public static final int GL_BLEND_DST_ALPHA
    = GL.GL_BLEND_DST_ALPHA;

  public static final int GL_BLEND_SRC_ALPHA
    = GL.GL_BLEND_SRC_ALPHA;

  public static final int GL_POINT_SIZE_MIN
    = GL.GL_POINT_SIZE_MIN;

  public static final int GL_POINT_SIZE_MAX
    = GL.GL_POINT_SIZE_MAX;

  public static final int GL_POINT_FADE_THRESHOLD_SIZE
    = GL.GL_POINT_FADE_THRESHOLD_SIZE;

  public static final int GL_POINT_DISTANCE_ATTENUATION
    = GL.GL_POINT_DISTANCE_ATTENUATION;

  public static final int GL_GENERATE_MIPMAP
    = GL.GL_GENERATE_MIPMAP;

  public static final int GL_GENERATE_MIPMAP_HINT
    = GL.GL_GENERATE_MIPMAP_HINT;

  public static final int GL_DEPTH_COMPONENT16
    = GL.GL_DEPTH_COMPONENT16;

  public static final int GL_DEPTH_COMPONENT24
    = GL.GL_DEPTH_COMPONENT24;

  public static final int GL_DEPTH_COMPONENT32
    = GL.GL_DEPTH_COMPONENT32;

  public static final int GL_MIRRORED_REPEAT
    = GL.GL_MIRRORED_REPEAT;

  public static final int GL_FOG_COORDINATE_SOURCE
    = GL.GL_FOG_COORDINATE_SOURCE;

  public static final int GL_FOG_COORDINATE
    = GL.GL_FOG_COORDINATE;

  public static final int GL_FRAGMENT_DEPTH
    = GL.GL_FRAGMENT_DEPTH;

  public static final int GL_CURRENT_FOG_COORDINATE
    = GL.GL_CURRENT_FOG_COORDINATE;

  public static final int GL_FOG_COORDINATE_ARRAY_TYPE
    = GL.GL_FOG_COORDINATE_ARRAY_TYPE;

  public static final int GL_FOG_COORDINATE_ARRAY_STRIDE
    = GL.GL_FOG_COORDINATE_ARRAY_STRIDE;

  public static final int GL_FOG_COORDINATE_ARRAY_POINTER
    = GL.GL_FOG_COORDINATE_ARRAY_POINTER;

  public static final int GL_FOG_COORDINATE_ARRAY
    = GL.GL_FOG_COORDINATE_ARRAY;

  public static final int GL_COLOR_SUM
    = GL.GL_COLOR_SUM;

  public static final int GL_CURRENT_SECONDARY_COLOR
    = GL.GL_CURRENT_SECONDARY_COLOR;

  public static final int GL_SECONDARY_COLOR_ARRAY_SIZE
    = GL.GL_SECONDARY_COLOR_ARRAY_SIZE;

  public static final int GL_SECONDARY_COLOR_ARRAY_TYPE
    = GL.GL_SECONDARY_COLOR_ARRAY_TYPE;

  public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE
    = GL.GL_SECONDARY_COLOR_ARRAY_STRIDE;

  public static final int GL_SECONDARY_COLOR_ARRAY_POINTER
    = GL.GL_SECONDARY_COLOR_ARRAY_POINTER;

  public static final int GL_SECONDARY_COLOR_ARRAY
    = GL.GL_SECONDARY_COLOR_ARRAY;

  public static final int GL_MAX_TEXTURE_LOD_BIAS
    = GL.GL_MAX_TEXTURE_LOD_BIAS;

  public static final int GL_TEXTURE_FILTER_CONTROL
    = GL.GL_TEXTURE_FILTER_CONTROL;

  public static final int GL_TEXTURE_LOD_BIAS
    = GL.GL_TEXTURE_LOD_BIAS;

  public static final int GL_INCR_WRAP
    = GL.GL_INCR_WRAP;

  public static final int GL_DECR_WRAP
    = GL.GL_DECR_WRAP;

  public static final int GL_TEXTURE_DEPTH_SIZE
    = GL.GL_TEXTURE_DEPTH_SIZE;

  public static final int GL_DEPTH_TEXTURE_MODE
    = GL.GL_DEPTH_TEXTURE_MODE;

  public static final int GL_TEXTURE_COMPARE_MODE
    = GL.GL_TEXTURE_COMPARE_MODE;

  public static final int GL_TEXTURE_COMPARE_FUNC
    = GL.GL_TEXTURE_COMPARE_FUNC;

  public static final int GL_COMPARE_R_TO_TEXTURE
    = GL.GL_COMPARE_R_TO_TEXTURE;

  public static final int GL_BUFFER_SIZE
    = GL.GL_BUFFER_SIZE;

  public static final int GL_BUFFER_USAGE
    = GL.GL_BUFFER_USAGE;

  public static final int GL_QUERY_COUNTER_BITS
    = GL.GL_QUERY_COUNTER_BITS;

  public static final int GL_CURRENT_QUERY
    = GL.GL_CURRENT_QUERY;

  public static final int GL_QUERY_RESULT
    = GL.GL_QUERY_RESULT;

  public static final int GL_QUERY_RESULT_AVAILABLE
    = GL.GL_QUERY_RESULT_AVAILABLE;

  public static final int GL_ARRAY_BUFFER
    = GL.GL_ARRAY_BUFFER;

  public static final int GL_ELEMENT_ARRAY_BUFFER
    = GL.GL_ELEMENT_ARRAY_BUFFER;

  public static final int GL_ARRAY_BUFFER_BINDING
    = GL.GL_ARRAY_BUFFER_BINDING;

  public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING
    = GL.GL_ELEMENT_ARRAY_BUFFER_BINDING;

  public static final int GL_VERTEX_ARRAY_BUFFER_BINDING
    = GL.GL_VERTEX_ARRAY_BUFFER_BINDING;

  public static final int GL_NORMAL_ARRAY_BUFFER_BINDING
    = GL.GL_NORMAL_ARRAY_BUFFER_BINDING;

  public static final int GL_COLOR_ARRAY_BUFFER_BINDING
    = GL.GL_COLOR_ARRAY_BUFFER_BINDING;

  public static final int GL_INDEX_ARRAY_BUFFER_BINDING
    = GL.GL_INDEX_ARRAY_BUFFER_BINDING;

  public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING
    = GL.GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING;

  public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING
    = GL.GL_EDGE_FLAG_ARRAY_BUFFER_BINDING;

  public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING
    = GL.GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING;

  public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING
    = GL.GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING;

  public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING
    = GL.GL_WEIGHT_ARRAY_BUFFER_BINDING;

  public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING
    = GL.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING;

  public static final int GL_READ_ONLY
    = GL.GL_READ_ONLY;

  public static final int GL_WRITE_ONLY
    = GL.GL_WRITE_ONLY;

  public static final int GL_READ_WRITE
    = GL.GL_READ_WRITE;

  public static final int GL_BUFFER_ACCESS
    = GL.GL_BUFFER_ACCESS;

  public static final int GL_BUFFER_MAPPED
    = GL.GL_BUFFER_MAPPED;

  public static final int GL_BUFFER_MAP_POINTER
    = GL.GL_BUFFER_MAP_POINTER;

  public static final int GL_STREAM_DRAW
    = GL.GL_STREAM_DRAW;

  public static final int GL_STREAM_READ
    = GL.GL_STREAM_READ;

  public static final int GL_STREAM_COPY
    = GL.GL_STREAM_COPY;

  public static final int GL_STATIC_DRAW
    = GL.GL_STATIC_DRAW;

  public static final int GL_STATIC_READ
    = GL.GL_STATIC_READ;

  public static final int GL_STATIC_COPY
    = GL.GL_STATIC_COPY;

  public static final int GL_DYNAMIC_DRAW
    = GL.GL_DYNAMIC_DRAW;

  public static final int GL_DYNAMIC_READ
    = GL.GL_DYNAMIC_READ;

  public static final int GL_DYNAMIC_COPY
    = GL.GL_DYNAMIC_COPY;

  public static final int GL_SAMPLES_PASSED
    = GL.GL_SAMPLES_PASSED;

  public static final int GL_FOG_COORD_SRC
    = GL.GL_FOG_COORD_SRC;

  public static final int GL_FOG_COORD
    = GL.GL_FOG_COORD;

  public static final int GL_CURRENT_FOG_COORD
    = GL.GL_CURRENT_FOG_COORD;

  public static final int GL_FOG_COORD_ARRAY_TYPE
    = GL.GL_FOG_COORD_ARRAY_TYPE;

  public static final int GL_FOG_COORD_ARRAY_STRIDE
    = GL.GL_FOG_COORD_ARRAY_STRIDE;

  public static final int GL_FOG_COORD_ARRAY_POINTER
    = GL.GL_FOG_COORD_ARRAY_POINTER;

  public static final int GL_FOG_COORD_ARRAY
    = GL.GL_FOG_COORD_ARRAY;

  public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING
    = GL.GL_FOG_COORD_ARRAY_BUFFER_BINDING;

  public static final int GL_SRC0_RGB
    = GL.GL_SRC0_RGB;

  public static final int GL_SRC1_RGB
    = GL.GL_SRC1_RGB;

  public static final int GL_SRC2_RGB
    = GL.GL_SRC2_RGB;

  public static final int GL_SRC0_ALPHA
    = GL.GL_SRC0_ALPHA;

  public static final int GL_SRC1_ALPHA
    = GL.GL_SRC1_ALPHA;

  public static final int GL_SRC2_ALPHA
    = GL.GL_SRC2_ALPHA;

  public static final int GL_BLEND_EQUATION_RGB
    = GL.GL_BLEND_EQUATION_RGB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED
    = GL.GL_VERTEX_ATTRIB_ARRAY_ENABLED;

  public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE
    = GL.GL_VERTEX_ATTRIB_ARRAY_SIZE;

  public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE
    = GL.GL_VERTEX_ATTRIB_ARRAY_STRIDE;

  public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE
    = GL.GL_VERTEX_ATTRIB_ARRAY_TYPE;

  public static final int GL_CURRENT_VERTEX_ATTRIB
    = GL.GL_CURRENT_VERTEX_ATTRIB;

  public static final int GL_VERTEX_PROGRAM_POINT_SIZE
    = GL.GL_VERTEX_PROGRAM_POINT_SIZE;

  public static final int GL_VERTEX_PROGRAM_TWO_SIDE
    = GL.GL_VERTEX_PROGRAM_TWO_SIDE;

  public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER
    = GL.GL_VERTEX_ATTRIB_ARRAY_POINTER;

  public static final int GL_STENCIL_BACK_FUNC
    = GL.GL_STENCIL_BACK_FUNC;

  public static final int GL_STENCIL_BACK_FAIL
    = GL.GL_STENCIL_BACK_FAIL;

  public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL
    = GL.GL_STENCIL_BACK_PASS_DEPTH_FAIL;

  public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS
    = GL.GL_STENCIL_BACK_PASS_DEPTH_PASS;

  public static final int GL_MAX_DRAW_BUFFERS
    = GL.GL_MAX_DRAW_BUFFERS;

  public static final int GL_DRAW_BUFFER0
    = GL.GL_DRAW_BUFFER0;

  public static final int GL_DRAW_BUFFER1
    = GL.GL_DRAW_BUFFER1;

  public static final int GL_DRAW_BUFFER2
    = GL.GL_DRAW_BUFFER2;

  public static final int GL_DRAW_BUFFER3
    = GL.GL_DRAW_BUFFER3;

  public static final int GL_DRAW_BUFFER4
    = GL.GL_DRAW_BUFFER4;

  public static final int GL_DRAW_BUFFER5
    = GL.GL_DRAW_BUFFER5;

  public static final int GL_DRAW_BUFFER6
    = GL.GL_DRAW_BUFFER6;

  public static final int GL_DRAW_BUFFER7
    = GL.GL_DRAW_BUFFER7;

  public static final int GL_DRAW_BUFFER8
    = GL.GL_DRAW_BUFFER8;

  public static final int GL_DRAW_BUFFER9
    = GL.GL_DRAW_BUFFER9;

  public static final int GL_DRAW_BUFFER10
    = GL.GL_DRAW_BUFFER10;

  public static final int GL_DRAW_BUFFER11
    = GL.GL_DRAW_BUFFER11;

  public static final int GL_DRAW_BUFFER12
    = GL.GL_DRAW_BUFFER12;

  public static final int GL_DRAW_BUFFER13
    = GL.GL_DRAW_BUFFER13;

  public static final int GL_DRAW_BUFFER14
    = GL.GL_DRAW_BUFFER14;

  public static final int GL_DRAW_BUFFER15
    = GL.GL_DRAW_BUFFER15;

  public static final int GL_BLEND_EQUATION_ALPHA
    = GL.GL_BLEND_EQUATION_ALPHA;

  public static final int GL_POINT_SPRITE
    = GL.GL_POINT_SPRITE;

  public static final int GL_COORD_REPLACE
    = GL.GL_COORD_REPLACE;

  public static final int GL_MAX_VERTEX_ATTRIBS
    = GL.GL_MAX_VERTEX_ATTRIBS;

  public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED
    = GL.GL_VERTEX_ATTRIB_ARRAY_NORMALIZED;

  public static final int GL_MAX_TEXTURE_COORDS
    = GL.GL_MAX_TEXTURE_COORDS;

  public static final int GL_MAX_TEXTURE_IMAGE_UNITS
    = GL.GL_MAX_TEXTURE_IMAGE_UNITS;

  public static final int GL_FRAGMENT_SHADER
    = GL.GL_FRAGMENT_SHADER;

  public static final int GL_VERTEX_SHADER
    = GL.GL_VERTEX_SHADER;

  public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS
    = GL.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS;

  public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS
    = GL.GL_MAX_VERTEX_UNIFORM_COMPONENTS;

  public static final int GL_MAX_VARYING_FLOATS
    = GL.GL_MAX_VARYING_FLOATS;

  public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS
    = GL.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS;

  public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS
    = GL.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;

  public static final int GL_SHADER_TYPE
    = GL.GL_SHADER_TYPE;

  public static final int GL_FLOAT_VEC2
    = GL.GL_FLOAT_VEC2;

  public static final int GL_FLOAT_VEC3
    = GL.GL_FLOAT_VEC3;

  public static final int GL_FLOAT_VEC4
    = GL.GL_FLOAT_VEC4;

  public static final int GL_INT_VEC2
    = GL.GL_INT_VEC2;

  public static final int GL_INT_VEC3
    = GL.GL_INT_VEC3;

  public static final int GL_INT_VEC4
    = GL.GL_INT_VEC4;

  public static final int GL_BOOL
    = GL.GL_BOOL;

  public static final int GL_BOOL_VEC2
    = GL.GL_BOOL_VEC2;

  public static final int GL_BOOL_VEC3
    = GL.GL_BOOL_VEC3;

  public static final int GL_BOOL_VEC4
    = GL.GL_BOOL_VEC4;

  public static final int GL_FLOAT_MAT2
    = GL.GL_FLOAT_MAT2;

  public static final int GL_FLOAT_MAT3
    = GL.GL_FLOAT_MAT3;

  public static final int GL_FLOAT_MAT4
    = GL.GL_FLOAT_MAT4;

  public static final int GL_SAMPLER_1D
    = GL.GL_SAMPLER_1D;

  public static final int GL_SAMPLER_2D
    = GL.GL_SAMPLER_2D;

  public static final int GL_SAMPLER_3D
    = GL.GL_SAMPLER_3D;

  public static final int GL_SAMPLER_CUBE
    = GL.GL_SAMPLER_CUBE;

  public static final int GL_SAMPLER_1D_SHADOW
    = GL.GL_SAMPLER_1D_SHADOW;

  public static final int GL_SAMPLER_2D_SHADOW
    = GL.GL_SAMPLER_2D_SHADOW;

  public static final int GL_DELETE_STATUS
    = GL.GL_DELETE_STATUS;

  public static final int GL_COMPILE_STATUS
    = GL.GL_COMPILE_STATUS;

  public static final int GL_LINK_STATUS
    = GL.GL_LINK_STATUS;

  public static final int GL_VALIDATE_STATUS
    = GL.GL_VALIDATE_STATUS;

  public static final int GL_INFO_LOG_LENGTH
    = GL.GL_INFO_LOG_LENGTH;

  public static final int GL_ATTACHED_SHADERS
    = GL.GL_ATTACHED_SHADERS;

  public static final int GL_ACTIVE_UNIFORMS
    = GL.GL_ACTIVE_UNIFORMS;

  public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH
    = GL.GL_ACTIVE_UNIFORM_MAX_LENGTH;

  public static final int GL_SHADER_SOURCE_LENGTH
    = GL.GL_SHADER_SOURCE_LENGTH;

  public static final int GL_ACTIVE_ATTRIBUTES
    = GL.GL_ACTIVE_ATTRIBUTES;

  public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH
    = GL.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH;

  public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT
    = GL.GL_FRAGMENT_SHADER_DERIVATIVE_HINT;

  public static final int GL_SHADING_LANGUAGE_VERSION
    = GL.GL_SHADING_LANGUAGE_VERSION;

  public static final int GL_CURRENT_PROGRAM
    = GL.GL_CURRENT_PROGRAM;

  public static final int GL_POINT_SPRITE_COORD_ORIGIN
    = GL.GL_POINT_SPRITE_COORD_ORIGIN;

  public static final int GL_LOWER_LEFT
    = GL.GL_LOWER_LEFT;

  public static final int GL_UPPER_LEFT
    = GL.GL_UPPER_LEFT;

  public static final int GL_STENCIL_BACK_REF
    = GL.GL_STENCIL_BACK_REF;

  public static final int GL_STENCIL_BACK_VALUE_MASK
    = GL.GL_STENCIL_BACK_VALUE_MASK;

  public static final int GL_STENCIL_BACK_WRITEMASK
    = GL.GL_STENCIL_BACK_WRITEMASK;

  public static final int GL_POINT_SIZE_MIN_ARB
    = GL.GL_POINT_SIZE_MIN_ARB;

  public static final int GL_POINT_SIZE_MAX_ARB
    = GL.GL_POINT_SIZE_MAX_ARB;

  public static final int GL_POINT_FADE_THRESHOLD_SIZE_ARB
    = GL.GL_POINT_FADE_THRESHOLD_SIZE_ARB;

  public static final int GL_POINT_DISTANCE_ATTENUATION_ARB
    = GL.GL_POINT_DISTANCE_ATTENUATION_ARB;

  public static final int GL_MAX_VERTEX_UNITS_ARB
    = GL.GL_MAX_VERTEX_UNITS_ARB;

  public static final int GL_ACTIVE_VERTEX_UNITS_ARB
    = GL.GL_ACTIVE_VERTEX_UNITS_ARB;

  public static final int GL_WEIGHT_SUM_UNITY_ARB
    = GL.GL_WEIGHT_SUM_UNITY_ARB;

  public static final int GL_VERTEX_BLEND_ARB
    = GL.GL_VERTEX_BLEND_ARB;

  public static final int GL_CURRENT_WEIGHT_ARB
    = GL.GL_CURRENT_WEIGHT_ARB;

  public static final int GL_WEIGHT_ARRAY_TYPE_ARB
    = GL.GL_WEIGHT_ARRAY_TYPE_ARB;

  public static final int GL_WEIGHT_ARRAY_STRIDE_ARB
    = GL.GL_WEIGHT_ARRAY_STRIDE_ARB;

  public static final int GL_WEIGHT_ARRAY_SIZE_ARB
    = GL.GL_WEIGHT_ARRAY_SIZE_ARB;

  public static final int GL_WEIGHT_ARRAY_POINTER_ARB
    = GL.GL_WEIGHT_ARRAY_POINTER_ARB;

  public static final int GL_WEIGHT_ARRAY_ARB
    = GL.GL_WEIGHT_ARRAY_ARB;

  public static final int GL_MODELVIEW0_ARB
    = GL.GL_MODELVIEW0_ARB;

  public static final int GL_MODELVIEW1_ARB
    = GL.GL_MODELVIEW1_ARB;

  public static final int GL_MODELVIEW2_ARB
    = GL.GL_MODELVIEW2_ARB;

  public static final int GL_MODELVIEW3_ARB
    = GL.GL_MODELVIEW3_ARB;

  public static final int GL_MODELVIEW4_ARB
    = GL.GL_MODELVIEW4_ARB;

  public static final int GL_MODELVIEW5_ARB
    = GL.GL_MODELVIEW5_ARB;

  public static final int GL_MODELVIEW6_ARB
    = GL.GL_MODELVIEW6_ARB;

  public static final int GL_MODELVIEW7_ARB
    = GL.GL_MODELVIEW7_ARB;

  public static final int GL_MODELVIEW8_ARB
    = GL.GL_MODELVIEW8_ARB;

  public static final int GL_MODELVIEW9_ARB
    = GL.GL_MODELVIEW9_ARB;

  public static final int GL_MODELVIEW10_ARB
    = GL.GL_MODELVIEW10_ARB;

  public static final int GL_MODELVIEW11_ARB
    = GL.GL_MODELVIEW11_ARB;

  public static final int GL_MODELVIEW12_ARB
    = GL.GL_MODELVIEW12_ARB;

  public static final int GL_MODELVIEW13_ARB
    = GL.GL_MODELVIEW13_ARB;

  public static final int GL_MODELVIEW14_ARB
    = GL.GL_MODELVIEW14_ARB;

  public static final int GL_MODELVIEW15_ARB
    = GL.GL_MODELVIEW15_ARB;

  public static final int GL_MODELVIEW16_ARB
    = GL.GL_MODELVIEW16_ARB;

  public static final int GL_MODELVIEW17_ARB
    = GL.GL_MODELVIEW17_ARB;

  public static final int GL_MODELVIEW18_ARB
    = GL.GL_MODELVIEW18_ARB;

  public static final int GL_MODELVIEW19_ARB
    = GL.GL_MODELVIEW19_ARB;

  public static final int GL_MODELVIEW20_ARB
    = GL.GL_MODELVIEW20_ARB;

  public static final int GL_MODELVIEW21_ARB
    = GL.GL_MODELVIEW21_ARB;

  public static final int GL_MODELVIEW22_ARB
    = GL.GL_MODELVIEW22_ARB;

  public static final int GL_MODELVIEW23_ARB
    = GL.GL_MODELVIEW23_ARB;

  public static final int GL_MODELVIEW24_ARB
    = GL.GL_MODELVIEW24_ARB;

  public static final int GL_MODELVIEW25_ARB
    = GL.GL_MODELVIEW25_ARB;

  public static final int GL_MODELVIEW26_ARB
    = GL.GL_MODELVIEW26_ARB;

  public static final int GL_MODELVIEW27_ARB
    = GL.GL_MODELVIEW27_ARB;

  public static final int GL_MODELVIEW28_ARB
    = GL.GL_MODELVIEW28_ARB;

  public static final int GL_MODELVIEW29_ARB
    = GL.GL_MODELVIEW29_ARB;

  public static final int GL_MODELVIEW30_ARB
    = GL.GL_MODELVIEW30_ARB;

  public static final int GL_MODELVIEW31_ARB
    = GL.GL_MODELVIEW31_ARB;

  public static final int GL_MATRIX_PALETTE_ARB
    = GL.GL_MATRIX_PALETTE_ARB;

  public static final int GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB
    = GL.GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB;

  public static final int GL_MAX_PALETTE_MATRICES_ARB
    = GL.GL_MAX_PALETTE_MATRICES_ARB;

  public static final int GL_CURRENT_PALETTE_MATRIX_ARB
    = GL.GL_CURRENT_PALETTE_MATRIX_ARB;

  public static final int GL_MATRIX_INDEX_ARRAY_ARB
    = GL.GL_MATRIX_INDEX_ARRAY_ARB;

  public static final int GL_CURRENT_MATRIX_INDEX_ARB
    = GL.GL_CURRENT_MATRIX_INDEX_ARB;

  public static final int GL_MATRIX_INDEX_ARRAY_SIZE_ARB
    = GL.GL_MATRIX_INDEX_ARRAY_SIZE_ARB;

  public static final int GL_MATRIX_INDEX_ARRAY_TYPE_ARB
    = GL.GL_MATRIX_INDEX_ARRAY_TYPE_ARB;

  public static final int GL_MATRIX_INDEX_ARRAY_STRIDE_ARB
    = GL.GL_MATRIX_INDEX_ARRAY_STRIDE_ARB;

  public static final int GL_MATRIX_INDEX_ARRAY_POINTER_ARB
    = GL.GL_MATRIX_INDEX_ARRAY_POINTER_ARB;

  public static final int GL_MIRRORED_REPEAT_ARB
    = GL.GL_MIRRORED_REPEAT_ARB;

  public static final int GL_DEPTH_COMPONENT16_ARB
    = GL.GL_DEPTH_COMPONENT16_ARB;

  public static final int GL_DEPTH_COMPONENT24_ARB
    = GL.GL_DEPTH_COMPONENT24_ARB;

  public static final int GL_DEPTH_COMPONENT32_ARB
    = GL.GL_DEPTH_COMPONENT32_ARB;

  public static final int GL_TEXTURE_DEPTH_SIZE_ARB
    = GL.GL_TEXTURE_DEPTH_SIZE_ARB;

  public static final int GL_DEPTH_TEXTURE_MODE_ARB
    = GL.GL_DEPTH_TEXTURE_MODE_ARB;

  public static final int GL_TEXTURE_COMPARE_MODE_ARB
    = GL.GL_TEXTURE_COMPARE_MODE_ARB;

  public static final int GL_TEXTURE_COMPARE_FUNC_ARB
    = GL.GL_TEXTURE_COMPARE_FUNC_ARB;

  public static final int GL_COMPARE_R_TO_TEXTURE_ARB
    = GL.GL_COMPARE_R_TO_TEXTURE_ARB;

  public static final int GL_TEXTURE_COMPARE_FAIL_VALUE_ARB
    = GL.GL_TEXTURE_COMPARE_FAIL_VALUE_ARB;

  public static final int GL_COLOR_SUM_ARB
    = GL.GL_COLOR_SUM_ARB;

  public static final int GL_VERTEX_PROGRAM_ARB
    = GL.GL_VERTEX_PROGRAM_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB
    = GL.GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB
    = GL.GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB
    = GL.GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB
    = GL.GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB;

  public static final int GL_CURRENT_VERTEX_ATTRIB_ARB
    = GL.GL_CURRENT_VERTEX_ATTRIB_ARB;

  public static final int GL_PROGRAM_LENGTH_ARB
    = GL.GL_PROGRAM_LENGTH_ARB;

  public static final int GL_PROGRAM_STRING_ARB
    = GL.GL_PROGRAM_STRING_ARB;

  public static final int GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB
    = GL.GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB;

  public static final int GL_MAX_PROGRAM_MATRICES_ARB
    = GL.GL_MAX_PROGRAM_MATRICES_ARB;

  public static final int GL_CURRENT_MATRIX_STACK_DEPTH_ARB
    = GL.GL_CURRENT_MATRIX_STACK_DEPTH_ARB;

  public static final int GL_CURRENT_MATRIX_ARB
    = GL.GL_CURRENT_MATRIX_ARB;

  public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB
    = GL.GL_VERTEX_PROGRAM_POINT_SIZE_ARB;

  public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB
    = GL.GL_VERTEX_PROGRAM_TWO_SIDE_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB
    = GL.GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB;

  public static final int GL_PROGRAM_ERROR_POSITION_ARB
    = GL.GL_PROGRAM_ERROR_POSITION_ARB;

  public static final int GL_PROGRAM_BINDING_ARB
    = GL.GL_PROGRAM_BINDING_ARB;

  public static final int GL_MAX_VERTEX_ATTRIBS_ARB
    = GL.GL_MAX_VERTEX_ATTRIBS_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB
    = GL.GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB;

  public static final int GL_PROGRAM_ERROR_STRING_ARB
    = GL.GL_PROGRAM_ERROR_STRING_ARB;

  public static final int GL_PROGRAM_FORMAT_ASCII_ARB
    = GL.GL_PROGRAM_FORMAT_ASCII_ARB;

  public static final int GL_PROGRAM_FORMAT_ARB
    = GL.GL_PROGRAM_FORMAT_ARB;

  public static final int GL_PROGRAM_INSTRUCTIONS_ARB
    = GL.GL_PROGRAM_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_INSTRUCTIONS_ARB
    = GL.GL_MAX_PROGRAM_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB
    = GL.GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_TEMPORARIES_ARB
    = GL.GL_PROGRAM_TEMPORARIES_ARB;

  public static final int GL_MAX_PROGRAM_TEMPORARIES_ARB
    = GL.GL_MAX_PROGRAM_TEMPORARIES_ARB;

  public static final int GL_PROGRAM_NATIVE_TEMPORARIES_ARB
    = GL.GL_PROGRAM_NATIVE_TEMPORARIES_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB;

  public static final int GL_PROGRAM_PARAMETERS_ARB
    = GL.GL_PROGRAM_PARAMETERS_ARB;

  public static final int GL_MAX_PROGRAM_PARAMETERS_ARB
    = GL.GL_MAX_PROGRAM_PARAMETERS_ARB;

  public static final int GL_PROGRAM_NATIVE_PARAMETERS_ARB
    = GL.GL_PROGRAM_NATIVE_PARAMETERS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB;

  public static final int GL_PROGRAM_ATTRIBS_ARB
    = GL.GL_PROGRAM_ATTRIBS_ARB;

  public static final int GL_MAX_PROGRAM_ATTRIBS_ARB
    = GL.GL_MAX_PROGRAM_ATTRIBS_ARB;

  public static final int GL_PROGRAM_NATIVE_ATTRIBS_ARB
    = GL.GL_PROGRAM_NATIVE_ATTRIBS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB;

  public static final int GL_PROGRAM_ADDRESS_REGISTERS_ARB
    = GL.GL_PROGRAM_ADDRESS_REGISTERS_ARB;

  public static final int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB
    = GL.GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB;

  public static final int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB
    = GL.GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB;

  public static final int GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB
    = GL.GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB;

  public static final int GL_MAX_PROGRAM_ENV_PARAMETERS_ARB
    = GL.GL_MAX_PROGRAM_ENV_PARAMETERS_ARB;

  public static final int GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB
    = GL.GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB;

  public static final int GL_TRANSPOSE_CURRENT_MATRIX_ARB
    = GL.GL_TRANSPOSE_CURRENT_MATRIX_ARB;

  public static final int GL_MATRIX0_ARB
    = GL.GL_MATRIX0_ARB;

  public static final int GL_MATRIX1_ARB
    = GL.GL_MATRIX1_ARB;

  public static final int GL_MATRIX2_ARB
    = GL.GL_MATRIX2_ARB;

  public static final int GL_MATRIX3_ARB
    = GL.GL_MATRIX3_ARB;

  public static final int GL_MATRIX4_ARB
    = GL.GL_MATRIX4_ARB;

  public static final int GL_MATRIX5_ARB
    = GL.GL_MATRIX5_ARB;

  public static final int GL_MATRIX6_ARB
    = GL.GL_MATRIX6_ARB;

  public static final int GL_MATRIX7_ARB
    = GL.GL_MATRIX7_ARB;

  public static final int GL_MATRIX8_ARB
    = GL.GL_MATRIX8_ARB;

  public static final int GL_MATRIX9_ARB
    = GL.GL_MATRIX9_ARB;

  public static final int GL_MATRIX10_ARB
    = GL.GL_MATRIX10_ARB;

  public static final int GL_MATRIX11_ARB
    = GL.GL_MATRIX11_ARB;

  public static final int GL_MATRIX12_ARB
    = GL.GL_MATRIX12_ARB;

  public static final int GL_MATRIX13_ARB
    = GL.GL_MATRIX13_ARB;

  public static final int GL_MATRIX14_ARB
    = GL.GL_MATRIX14_ARB;

  public static final int GL_MATRIX15_ARB
    = GL.GL_MATRIX15_ARB;

  public static final int GL_MATRIX16_ARB
    = GL.GL_MATRIX16_ARB;

  public static final int GL_MATRIX17_ARB
    = GL.GL_MATRIX17_ARB;

  public static final int GL_MATRIX18_ARB
    = GL.GL_MATRIX18_ARB;

  public static final int GL_MATRIX19_ARB
    = GL.GL_MATRIX19_ARB;

  public static final int GL_MATRIX20_ARB
    = GL.GL_MATRIX20_ARB;

  public static final int GL_MATRIX21_ARB
    = GL.GL_MATRIX21_ARB;

  public static final int GL_MATRIX22_ARB
    = GL.GL_MATRIX22_ARB;

  public static final int GL_MATRIX23_ARB
    = GL.GL_MATRIX23_ARB;

  public static final int GL_MATRIX24_ARB
    = GL.GL_MATRIX24_ARB;

  public static final int GL_MATRIX25_ARB
    = GL.GL_MATRIX25_ARB;

  public static final int GL_MATRIX26_ARB
    = GL.GL_MATRIX26_ARB;

  public static final int GL_MATRIX27_ARB
    = GL.GL_MATRIX27_ARB;

  public static final int GL_MATRIX28_ARB
    = GL.GL_MATRIX28_ARB;

  public static final int GL_MATRIX29_ARB
    = GL.GL_MATRIX29_ARB;

  public static final int GL_MATRIX30_ARB
    = GL.GL_MATRIX30_ARB;

  public static final int GL_MATRIX31_ARB
    = GL.GL_MATRIX31_ARB;

  public static final int GL_FRAGMENT_PROGRAM_ARB
    = GL.GL_FRAGMENT_PROGRAM_ARB;

  public static final int GL_PROGRAM_ALU_INSTRUCTIONS_ARB
    = GL.GL_PROGRAM_ALU_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_TEX_INSTRUCTIONS_ARB
    = GL.GL_PROGRAM_TEX_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_TEX_INDIRECTIONS_ARB
    = GL.GL_PROGRAM_TEX_INDIRECTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB
    = GL.GL_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB
    = GL.GL_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB
    = GL.GL_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB;

  public static final int GL_MAX_PROGRAM_ALU_INSTRUCTIONS_ARB
    = GL.GL_MAX_PROGRAM_ALU_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_TEX_INSTRUCTIONS_ARB
    = GL.GL_MAX_PROGRAM_TEX_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_TEX_INDIRECTIONS_ARB
    = GL.GL_MAX_PROGRAM_TEX_INDIRECTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB
    = GL.GL_MAX_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB;

  public static final int GL_MAX_TEXTURE_COORDS_ARB
    = GL.GL_MAX_TEXTURE_COORDS_ARB;

  public static final int GL_MAX_TEXTURE_IMAGE_UNITS_ARB
    = GL.GL_MAX_TEXTURE_IMAGE_UNITS_ARB;

  public static final int GL_BUFFER_SIZE_ARB
    = GL.GL_BUFFER_SIZE_ARB;

  public static final int GL_BUFFER_USAGE_ARB
    = GL.GL_BUFFER_USAGE_ARB;

  public static final int GL_ARRAY_BUFFER_ARB
    = GL.GL_ARRAY_BUFFER_ARB;

  public static final int GL_ELEMENT_ARRAY_BUFFER_ARB
    = GL.GL_ELEMENT_ARRAY_BUFFER_ARB;

  public static final int GL_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_ELEMENT_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_VERTEX_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_VERTEX_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_NORMAL_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_NORMAL_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_COLOR_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_COLOR_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_INDEX_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_INDEX_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_EDGE_FLAG_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_WEIGHT_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING_ARB
    = GL.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING_ARB;

  public static final int GL_READ_ONLY_ARB
    = GL.GL_READ_ONLY_ARB;

  public static final int GL_WRITE_ONLY_ARB
    = GL.GL_WRITE_ONLY_ARB;

  public static final int GL_READ_WRITE_ARB
    = GL.GL_READ_WRITE_ARB;

  public static final int GL_BUFFER_ACCESS_ARB
    = GL.GL_BUFFER_ACCESS_ARB;

  public static final int GL_BUFFER_MAPPED_ARB
    = GL.GL_BUFFER_MAPPED_ARB;

  public static final int GL_BUFFER_MAP_POINTER_ARB
    = GL.GL_BUFFER_MAP_POINTER_ARB;

  public static final int GL_STREAM_DRAW_ARB
    = GL.GL_STREAM_DRAW_ARB;

  public static final int GL_STREAM_READ_ARB
    = GL.GL_STREAM_READ_ARB;

  public static final int GL_STREAM_COPY_ARB
    = GL.GL_STREAM_COPY_ARB;

  public static final int GL_STATIC_DRAW_ARB
    = GL.GL_STATIC_DRAW_ARB;

  public static final int GL_STATIC_READ_ARB
    = GL.GL_STATIC_READ_ARB;

  public static final int GL_STATIC_COPY_ARB
    = GL.GL_STATIC_COPY_ARB;

  public static final int GL_DYNAMIC_DRAW_ARB
    = GL.GL_DYNAMIC_DRAW_ARB;

  public static final int GL_DYNAMIC_READ_ARB
    = GL.GL_DYNAMIC_READ_ARB;

  public static final int GL_DYNAMIC_COPY_ARB
    = GL.GL_DYNAMIC_COPY_ARB;

  public static final int GL_QUERY_COUNTER_BITS_ARB
    = GL.GL_QUERY_COUNTER_BITS_ARB;

  public static final int GL_CURRENT_QUERY_ARB
    = GL.GL_CURRENT_QUERY_ARB;

  public static final int GL_QUERY_RESULT_ARB
    = GL.GL_QUERY_RESULT_ARB;

  public static final int GL_QUERY_RESULT_AVAILABLE_ARB
    = GL.GL_QUERY_RESULT_AVAILABLE_ARB;

  public static final int GL_SAMPLES_PASSED_ARB
    = GL.GL_SAMPLES_PASSED_ARB;

  public static final int GL_PROGRAM_OBJECT_ARB
    = GL.GL_PROGRAM_OBJECT_ARB;

  public static final int GL_SHADER_OBJECT_ARB
    = GL.GL_SHADER_OBJECT_ARB;

  public static final int GL_OBJECT_TYPE_ARB
    = GL.GL_OBJECT_TYPE_ARB;

  public static final int GL_OBJECT_SUBTYPE_ARB
    = GL.GL_OBJECT_SUBTYPE_ARB;

  public static final int GL_FLOAT_VEC2_ARB
    = GL.GL_FLOAT_VEC2_ARB;

  public static final int GL_FLOAT_VEC3_ARB
    = GL.GL_FLOAT_VEC3_ARB;

  public static final int GL_FLOAT_VEC4_ARB
    = GL.GL_FLOAT_VEC4_ARB;

  public static final int GL_INT_VEC2_ARB
    = GL.GL_INT_VEC2_ARB;

  public static final int GL_INT_VEC3_ARB
    = GL.GL_INT_VEC3_ARB;

  public static final int GL_INT_VEC4_ARB
    = GL.GL_INT_VEC4_ARB;

  public static final int GL_BOOL_ARB
    = GL.GL_BOOL_ARB;

  public static final int GL_BOOL_VEC2_ARB
    = GL.GL_BOOL_VEC2_ARB;

  public static final int GL_BOOL_VEC3_ARB
    = GL.GL_BOOL_VEC3_ARB;

  public static final int GL_BOOL_VEC4_ARB
    = GL.GL_BOOL_VEC4_ARB;

  public static final int GL_FLOAT_MAT2_ARB
    = GL.GL_FLOAT_MAT2_ARB;

  public static final int GL_FLOAT_MAT3_ARB
    = GL.GL_FLOAT_MAT3_ARB;

  public static final int GL_FLOAT_MAT4_ARB
    = GL.GL_FLOAT_MAT4_ARB;

  public static final int GL_SAMPLER_1D_ARB
    = GL.GL_SAMPLER_1D_ARB;

  public static final int GL_SAMPLER_2D_ARB
    = GL.GL_SAMPLER_2D_ARB;

  public static final int GL_SAMPLER_3D_ARB
    = GL.GL_SAMPLER_3D_ARB;

  public static final int GL_SAMPLER_CUBE_ARB
    = GL.GL_SAMPLER_CUBE_ARB;

  public static final int GL_SAMPLER_1D_SHADOW_ARB
    = GL.GL_SAMPLER_1D_SHADOW_ARB;

  public static final int GL_SAMPLER_2D_SHADOW_ARB
    = GL.GL_SAMPLER_2D_SHADOW_ARB;

  public static final int GL_SAMPLER_2D_RECT_ARB
    = GL.GL_SAMPLER_2D_RECT_ARB;

  public static final int GL_SAMPLER_2D_RECT_SHADOW_ARB
    = GL.GL_SAMPLER_2D_RECT_SHADOW_ARB;

  public static final int GL_OBJECT_DELETE_STATUS_ARB
    = GL.GL_OBJECT_DELETE_STATUS_ARB;

  public static final int GL_OBJECT_COMPILE_STATUS_ARB
    = GL.GL_OBJECT_COMPILE_STATUS_ARB;

  public static final int GL_OBJECT_LINK_STATUS_ARB
    = GL.GL_OBJECT_LINK_STATUS_ARB;

  public static final int GL_OBJECT_VALIDATE_STATUS_ARB
    = GL.GL_OBJECT_VALIDATE_STATUS_ARB;

  public static final int GL_OBJECT_INFO_LOG_LENGTH_ARB
    = GL.GL_OBJECT_INFO_LOG_LENGTH_ARB;

  public static final int GL_OBJECT_ATTACHED_OBJECTS_ARB
    = GL.GL_OBJECT_ATTACHED_OBJECTS_ARB;

  public static final int GL_OBJECT_ACTIVE_UNIFORMS_ARB
    = GL.GL_OBJECT_ACTIVE_UNIFORMS_ARB;

  public static final int GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB
    = GL.GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB;

  public static final int GL_OBJECT_SHADER_SOURCE_LENGTH_ARB
    = GL.GL_OBJECT_SHADER_SOURCE_LENGTH_ARB;

  public static final int GL_VERTEX_SHADER_ARB
    = GL.GL_VERTEX_SHADER_ARB;

  public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS_ARB
    = GL.GL_MAX_VERTEX_UNIFORM_COMPONENTS_ARB;

  public static final int GL_MAX_VARYING_FLOATS_ARB
    = GL.GL_MAX_VARYING_FLOATS_ARB;

  public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS_ARB
    = GL.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS_ARB;

  public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS_ARB
    = GL.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS_ARB;

  public static final int GL_OBJECT_ACTIVE_ATTRIBUTES_ARB
    = GL.GL_OBJECT_ACTIVE_ATTRIBUTES_ARB;

  public static final int GL_OBJECT_ACTIVE_ATTRIBUTE_MAX_LENGTH_ARB
    = GL.GL_OBJECT_ACTIVE_ATTRIBUTE_MAX_LENGTH_ARB;

  public static final int GL_FRAGMENT_SHADER_ARB
    = GL.GL_FRAGMENT_SHADER_ARB;

  public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS_ARB
    = GL.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS_ARB;

  public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT_ARB
    = GL.GL_FRAGMENT_SHADER_DERIVATIVE_HINT_ARB;

  public static final int GL_SHADING_LANGUAGE_VERSION_ARB
    = GL.GL_SHADING_LANGUAGE_VERSION_ARB;

  public static final int GL_POINT_SPRITE_ARB
    = GL.GL_POINT_SPRITE_ARB;

  public static final int GL_COORD_REPLACE_ARB
    = GL.GL_COORD_REPLACE_ARB;

  public static final int GL_MAX_DRAW_BUFFERS_ARB
    = GL.GL_MAX_DRAW_BUFFERS_ARB;

  public static final int GL_DRAW_BUFFER0_ARB
    = GL.GL_DRAW_BUFFER0_ARB;

  public static final int GL_DRAW_BUFFER1_ARB
    = GL.GL_DRAW_BUFFER1_ARB;

  public static final int GL_DRAW_BUFFER2_ARB
    = GL.GL_DRAW_BUFFER2_ARB;

  public static final int GL_DRAW_BUFFER3_ARB
    = GL.GL_DRAW_BUFFER3_ARB;

  public static final int GL_DRAW_BUFFER4_ARB
    = GL.GL_DRAW_BUFFER4_ARB;

  public static final int GL_DRAW_BUFFER5_ARB
    = GL.GL_DRAW_BUFFER5_ARB;

  public static final int GL_DRAW_BUFFER6_ARB
    = GL.GL_DRAW_BUFFER6_ARB;

  public static final int GL_DRAW_BUFFER7_ARB
    = GL.GL_DRAW_BUFFER7_ARB;

  public static final int GL_DRAW_BUFFER8_ARB
    = GL.GL_DRAW_BUFFER8_ARB;

  public static final int GL_DRAW_BUFFER9_ARB
    = GL.GL_DRAW_BUFFER9_ARB;

  public static final int GL_DRAW_BUFFER10_ARB
    = GL.GL_DRAW_BUFFER10_ARB;

  public static final int GL_DRAW_BUFFER11_ARB
    = GL.GL_DRAW_BUFFER11_ARB;

  public static final int GL_DRAW_BUFFER12_ARB
    = GL.GL_DRAW_BUFFER12_ARB;

  public static final int GL_DRAW_BUFFER13_ARB
    = GL.GL_DRAW_BUFFER13_ARB;

  public static final int GL_DRAW_BUFFER14_ARB
    = GL.GL_DRAW_BUFFER14_ARB;

  public static final int GL_DRAW_BUFFER15_ARB
    = GL.GL_DRAW_BUFFER15_ARB;

  public static final int GL_TEXTURE_RECTANGLE_ARB
    = GL.GL_TEXTURE_RECTANGLE_ARB;

  public static final int GL_TEXTURE_BINDING_RECTANGLE_ARB
    = GL.GL_TEXTURE_BINDING_RECTANGLE_ARB;

  public static final int GL_PROXY_TEXTURE_RECTANGLE_ARB
    = GL.GL_PROXY_TEXTURE_RECTANGLE_ARB;

  public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB
    = GL.GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB;

  public static final int GL_RGBA_FLOAT_MODE_ARB
    = GL.GL_RGBA_FLOAT_MODE_ARB;

  public static final int GL_CLAMP_VERTEX_COLOR_ARB
    = GL.GL_CLAMP_VERTEX_COLOR_ARB;

  public static final int GL_CLAMP_FRAGMENT_COLOR_ARB
    = GL.GL_CLAMP_FRAGMENT_COLOR_ARB;

  public static final int GL_CLAMP_READ_COLOR_ARB
    = GL.GL_CLAMP_READ_COLOR_ARB;

  public static final int GL_FIXED_ONLY_ARB
    = GL.GL_FIXED_ONLY_ARB;

  public static final int GL_HALF_FLOAT_ARB
    = GL.GL_HALF_FLOAT_ARB;

  public static final int GL_TEXTURE_RED_TYPE_ARB
    = GL.GL_TEXTURE_RED_TYPE_ARB;

  public static final int GL_TEXTURE_GREEN_TYPE_ARB
    = GL.GL_TEXTURE_GREEN_TYPE_ARB;

  public static final int GL_TEXTURE_BLUE_TYPE_ARB
    = GL.GL_TEXTURE_BLUE_TYPE_ARB;

  public static final int GL_TEXTURE_ALPHA_TYPE_ARB
    = GL.GL_TEXTURE_ALPHA_TYPE_ARB;

  public static final int GL_TEXTURE_LUMINANCE_TYPE_ARB
    = GL.GL_TEXTURE_LUMINANCE_TYPE_ARB;

  public static final int GL_TEXTURE_INTENSITY_TYPE_ARB
    = GL.GL_TEXTURE_INTENSITY_TYPE_ARB;

  public static final int GL_TEXTURE_DEPTH_TYPE_ARB
    = GL.GL_TEXTURE_DEPTH_TYPE_ARB;

  public static final int GL_UNSIGNED_NORMALIZED_ARB
    = GL.GL_UNSIGNED_NORMALIZED_ARB;

  public static final int GL_RGBA32F_ARB
    = GL.GL_RGBA32F_ARB;

  public static final int GL_RGB32F_ARB
    = GL.GL_RGB32F_ARB;

  public static final int GL_ALPHA32F_ARB
    = GL.GL_ALPHA32F_ARB;

  public static final int GL_INTENSITY32F_ARB
    = GL.GL_INTENSITY32F_ARB;

  public static final int GL_LUMINANCE32F_ARB
    = GL.GL_LUMINANCE32F_ARB;

  public static final int GL_LUMINANCE_ALPHA32F_ARB
    = GL.GL_LUMINANCE_ALPHA32F_ARB;

  public static final int GL_RGBA16F_ARB
    = GL.GL_RGBA16F_ARB;

  public static final int GL_RGB16F_ARB
    = GL.GL_RGB16F_ARB;

  public static final int GL_ALPHA16F_ARB
    = GL.GL_ALPHA16F_ARB;

  public static final int GL_INTENSITY16F_ARB
    = GL.GL_INTENSITY16F_ARB;

  public static final int GL_LUMINANCE16F_ARB
    = GL.GL_LUMINANCE16F_ARB;

  public static final int GL_LUMINANCE_ALPHA16F_ARB
    = GL.GL_LUMINANCE_ALPHA16F_ARB;

  public static final int GL_PIXEL_PACK_BUFFER_ARB
    = GL.GL_PIXEL_PACK_BUFFER_ARB;

  public static final int GL_PIXEL_UNPACK_BUFFER_ARB
    = GL.GL_PIXEL_UNPACK_BUFFER_ARB;

  public static final int GL_PIXEL_PACK_BUFFER_BINDING_ARB
    = GL.GL_PIXEL_PACK_BUFFER_BINDING_ARB;

  public static final int GL_PIXEL_UNPACK_BUFFER_BINDING_ARB
    = GL.GL_PIXEL_UNPACK_BUFFER_BINDING_ARB;

  public static final int GL_ABGR_EXT
    = GL.GL_ABGR_EXT;

  public static final int GL_FILTER4_SGIS
    = GL.GL_FILTER4_SGIS;

  public static final int GL_TEXTURE_FILTER4_SIZE_SGIS
    = GL.GL_TEXTURE_FILTER4_SIZE_SGIS;

  public static final int GL_PIXEL_TEXTURE_SGIS
    = GL.GL_PIXEL_TEXTURE_SGIS;

  public static final int GL_PIXEL_FRAGMENT_RGB_SOURCE_SGIS
    = GL.GL_PIXEL_FRAGMENT_RGB_SOURCE_SGIS;

  public static final int GL_PIXEL_FRAGMENT_ALPHA_SOURCE_SGIS
    = GL.GL_PIXEL_FRAGMENT_ALPHA_SOURCE_SGIS;

  public static final int GL_PIXEL_GROUP_COLOR_SGIS
    = GL.GL_PIXEL_GROUP_COLOR_SGIS;

  public static final int GL_PIXEL_TEX_GEN_SGIX
    = GL.GL_PIXEL_TEX_GEN_SGIX;

  public static final int GL_PIXEL_TEX_GEN_MODE_SGIX
    = GL.GL_PIXEL_TEX_GEN_MODE_SGIX;

  public static final int GL_PACK_SKIP_VOLUMES_SGIS
    = GL.GL_PACK_SKIP_VOLUMES_SGIS;

  public static final int GL_PACK_IMAGE_DEPTH_SGIS
    = GL.GL_PACK_IMAGE_DEPTH_SGIS;

  public static final int GL_UNPACK_SKIP_VOLUMES_SGIS
    = GL.GL_UNPACK_SKIP_VOLUMES_SGIS;

  public static final int GL_UNPACK_IMAGE_DEPTH_SGIS
    = GL.GL_UNPACK_IMAGE_DEPTH_SGIS;

  public static final int GL_TEXTURE_4D_SGIS
    = GL.GL_TEXTURE_4D_SGIS;

  public static final int GL_PROXY_TEXTURE_4D_SGIS
    = GL.GL_PROXY_TEXTURE_4D_SGIS;

  public static final int GL_TEXTURE_4DSIZE_SGIS
    = GL.GL_TEXTURE_4DSIZE_SGIS;

  public static final int GL_TEXTURE_WRAP_Q_SGIS
    = GL.GL_TEXTURE_WRAP_Q_SGIS;

  public static final int GL_MAX_4D_TEXTURE_SIZE_SGIS
    = GL.GL_MAX_4D_TEXTURE_SIZE_SGIS;

  public static final int GL_TEXTURE_4D_BINDING_SGIS
    = GL.GL_TEXTURE_4D_BINDING_SGIS;

  public static final int GL_TEXTURE_COLOR_TABLE_SGI
    = GL.GL_TEXTURE_COLOR_TABLE_SGI;

  public static final int GL_PROXY_TEXTURE_COLOR_TABLE_SGI
    = GL.GL_PROXY_TEXTURE_COLOR_TABLE_SGI;

  public static final int GL_CMYK_EXT
    = GL.GL_CMYK_EXT;

  public static final int GL_CMYKA_EXT
    = GL.GL_CMYKA_EXT;

  public static final int GL_PACK_CMYK_HINT_EXT
    = GL.GL_PACK_CMYK_HINT_EXT;

  public static final int GL_UNPACK_CMYK_HINT_EXT
    = GL.GL_UNPACK_CMYK_HINT_EXT;

  public static final int GL_DETAIL_TEXTURE_2D_SGIS
    = GL.GL_DETAIL_TEXTURE_2D_SGIS;

  public static final int GL_DETAIL_TEXTURE_2D_BINDING_SGIS
    = GL.GL_DETAIL_TEXTURE_2D_BINDING_SGIS;

  public static final int GL_LINEAR_DETAIL_SGIS
    = GL.GL_LINEAR_DETAIL_SGIS;

  public static final int GL_LINEAR_DETAIL_ALPHA_SGIS
    = GL.GL_LINEAR_DETAIL_ALPHA_SGIS;

  public static final int GL_LINEAR_DETAIL_COLOR_SGIS
    = GL.GL_LINEAR_DETAIL_COLOR_SGIS;

  public static final int GL_DETAIL_TEXTURE_LEVEL_SGIS
    = GL.GL_DETAIL_TEXTURE_LEVEL_SGIS;

  public static final int GL_DETAIL_TEXTURE_MODE_SGIS
    = GL.GL_DETAIL_TEXTURE_MODE_SGIS;

  public static final int GL_DETAIL_TEXTURE_FUNC_POINTS_SGIS
    = GL.GL_DETAIL_TEXTURE_FUNC_POINTS_SGIS;

  public static final int GL_LINEAR_SHARPEN_SGIS
    = GL.GL_LINEAR_SHARPEN_SGIS;

  public static final int GL_LINEAR_SHARPEN_ALPHA_SGIS
    = GL.GL_LINEAR_SHARPEN_ALPHA_SGIS;

  public static final int GL_LINEAR_SHARPEN_COLOR_SGIS
    = GL.GL_LINEAR_SHARPEN_COLOR_SGIS;

  public static final int GL_SHARPEN_TEXTURE_FUNC_POINTS_SGIS
    = GL.GL_SHARPEN_TEXTURE_FUNC_POINTS_SGIS;

  public static final int GL_MULTISAMPLE_SGIS
    = GL.GL_MULTISAMPLE_SGIS;

  public static final int GL_SAMPLE_ALPHA_TO_MASK_SGIS
    = GL.GL_SAMPLE_ALPHA_TO_MASK_SGIS;

  public static final int GL_SAMPLE_ALPHA_TO_ONE_SGIS
    = GL.GL_SAMPLE_ALPHA_TO_ONE_SGIS;

  public static final int GL_SAMPLE_MASK_SGIS
    = GL.GL_SAMPLE_MASK_SGIS;

  public static final int GL_1PASS_SGIS
    = GL.GL_1PASS_SGIS;

  public static final int GL_2PASS_0_SGIS
    = GL.GL_2PASS_0_SGIS;

  public static final int GL_2PASS_1_SGIS
    = GL.GL_2PASS_1_SGIS;

  public static final int GL_4PASS_0_SGIS
    = GL.GL_4PASS_0_SGIS;

  public static final int GL_4PASS_1_SGIS
    = GL.GL_4PASS_1_SGIS;

  public static final int GL_4PASS_2_SGIS
    = GL.GL_4PASS_2_SGIS;

  public static final int GL_4PASS_3_SGIS
    = GL.GL_4PASS_3_SGIS;

  public static final int GL_SAMPLE_BUFFERS_SGIS
    = GL.GL_SAMPLE_BUFFERS_SGIS;

  public static final int GL_SAMPLES_SGIS
    = GL.GL_SAMPLES_SGIS;

  public static final int GL_SAMPLE_MASK_VALUE_SGIS
    = GL.GL_SAMPLE_MASK_VALUE_SGIS;

  public static final int GL_SAMPLE_MASK_INVERT_SGIS
    = GL.GL_SAMPLE_MASK_INVERT_SGIS;

  public static final int GL_SAMPLE_PATTERN_SGIS
    = GL.GL_SAMPLE_PATTERN_SGIS;

  public static final int GL_GENERATE_MIPMAP_SGIS
    = GL.GL_GENERATE_MIPMAP_SGIS;

  public static final int GL_GENERATE_MIPMAP_HINT_SGIS
    = GL.GL_GENERATE_MIPMAP_HINT_SGIS;

  public static final int GL_LINEAR_CLIPMAP_LINEAR_SGIX
    = GL.GL_LINEAR_CLIPMAP_LINEAR_SGIX;

  public static final int GL_TEXTURE_CLIPMAP_CENTER_SGIX
    = GL.GL_TEXTURE_CLIPMAP_CENTER_SGIX;

  public static final int GL_TEXTURE_CLIPMAP_FRAME_SGIX
    = GL.GL_TEXTURE_CLIPMAP_FRAME_SGIX;

  public static final int GL_TEXTURE_CLIPMAP_OFFSET_SGIX
    = GL.GL_TEXTURE_CLIPMAP_OFFSET_SGIX;

  public static final int GL_TEXTURE_CLIPMAP_VIRTUAL_DEPTH_SGIX
    = GL.GL_TEXTURE_CLIPMAP_VIRTUAL_DEPTH_SGIX;

  public static final int GL_TEXTURE_CLIPMAP_LOD_OFFSET_SGIX
    = GL.GL_TEXTURE_CLIPMAP_LOD_OFFSET_SGIX;

  public static final int GL_TEXTURE_CLIPMAP_DEPTH_SGIX
    = GL.GL_TEXTURE_CLIPMAP_DEPTH_SGIX;

  public static final int GL_MAX_CLIPMAP_DEPTH_SGIX
    = GL.GL_MAX_CLIPMAP_DEPTH_SGIX;

  public static final int GL_MAX_CLIPMAP_VIRTUAL_DEPTH_SGIX
    = GL.GL_MAX_CLIPMAP_VIRTUAL_DEPTH_SGIX;

  public static final int GL_NEAREST_CLIPMAP_NEAREST_SGIX
    = GL.GL_NEAREST_CLIPMAP_NEAREST_SGIX;

  public static final int GL_NEAREST_CLIPMAP_LINEAR_SGIX
    = GL.GL_NEAREST_CLIPMAP_LINEAR_SGIX;

  public static final int GL_LINEAR_CLIPMAP_NEAREST_SGIX
    = GL.GL_LINEAR_CLIPMAP_NEAREST_SGIX;

  public static final int GL_TEXTURE_COMPARE_SGIX
    = GL.GL_TEXTURE_COMPARE_SGIX;

  public static final int GL_TEXTURE_COMPARE_OPERATOR_SGIX
    = GL.GL_TEXTURE_COMPARE_OPERATOR_SGIX;

  public static final int GL_TEXTURE_LEQUAL_R_SGIX
    = GL.GL_TEXTURE_LEQUAL_R_SGIX;

  public static final int GL_TEXTURE_GEQUAL_R_SGIX
    = GL.GL_TEXTURE_GEQUAL_R_SGIX;

  public static final int GL_INTERLACE_SGIX
    = GL.GL_INTERLACE_SGIX;

  public static final int GL_PIXEL_TILE_BEST_ALIGNMENT_SGIX
    = GL.GL_PIXEL_TILE_BEST_ALIGNMENT_SGIX;

  public static final int GL_PIXEL_TILE_CACHE_INCREMENT_SGIX
    = GL.GL_PIXEL_TILE_CACHE_INCREMENT_SGIX;

  public static final int GL_PIXEL_TILE_WIDTH_SGIX
    = GL.GL_PIXEL_TILE_WIDTH_SGIX;

  public static final int GL_PIXEL_TILE_HEIGHT_SGIX
    = GL.GL_PIXEL_TILE_HEIGHT_SGIX;

  public static final int GL_PIXEL_TILE_GRID_WIDTH_SGIX
    = GL.GL_PIXEL_TILE_GRID_WIDTH_SGIX;

  public static final int GL_PIXEL_TILE_GRID_HEIGHT_SGIX
    = GL.GL_PIXEL_TILE_GRID_HEIGHT_SGIX;

  public static final int GL_PIXEL_TILE_GRID_DEPTH_SGIX
    = GL.GL_PIXEL_TILE_GRID_DEPTH_SGIX;

  public static final int GL_PIXEL_TILE_CACHE_SIZE_SGIX
    = GL.GL_PIXEL_TILE_CACHE_SIZE_SGIX;

  public static final int GL_DUAL_ALPHA4_SGIS
    = GL.GL_DUAL_ALPHA4_SGIS;

  public static final int GL_DUAL_ALPHA8_SGIS
    = GL.GL_DUAL_ALPHA8_SGIS;

  public static final int GL_DUAL_ALPHA12_SGIS
    = GL.GL_DUAL_ALPHA12_SGIS;

  public static final int GL_DUAL_ALPHA16_SGIS
    = GL.GL_DUAL_ALPHA16_SGIS;

  public static final int GL_DUAL_LUMINANCE4_SGIS
    = GL.GL_DUAL_LUMINANCE4_SGIS;

  public static final int GL_DUAL_LUMINANCE8_SGIS
    = GL.GL_DUAL_LUMINANCE8_SGIS;

  public static final int GL_DUAL_LUMINANCE12_SGIS
    = GL.GL_DUAL_LUMINANCE12_SGIS;

  public static final int GL_DUAL_LUMINANCE16_SGIS
    = GL.GL_DUAL_LUMINANCE16_SGIS;

  public static final int GL_DUAL_INTENSITY4_SGIS
    = GL.GL_DUAL_INTENSITY4_SGIS;

  public static final int GL_DUAL_INTENSITY8_SGIS
    = GL.GL_DUAL_INTENSITY8_SGIS;

  public static final int GL_DUAL_INTENSITY12_SGIS
    = GL.GL_DUAL_INTENSITY12_SGIS;

  public static final int GL_DUAL_INTENSITY16_SGIS
    = GL.GL_DUAL_INTENSITY16_SGIS;

  public static final int GL_DUAL_LUMINANCE_ALPHA4_SGIS
    = GL.GL_DUAL_LUMINANCE_ALPHA4_SGIS;

  public static final int GL_DUAL_LUMINANCE_ALPHA8_SGIS
    = GL.GL_DUAL_LUMINANCE_ALPHA8_SGIS;

  public static final int GL_QUAD_ALPHA4_SGIS
    = GL.GL_QUAD_ALPHA4_SGIS;

  public static final int GL_QUAD_ALPHA8_SGIS
    = GL.GL_QUAD_ALPHA8_SGIS;

  public static final int GL_QUAD_LUMINANCE4_SGIS
    = GL.GL_QUAD_LUMINANCE4_SGIS;

  public static final int GL_QUAD_LUMINANCE8_SGIS
    = GL.GL_QUAD_LUMINANCE8_SGIS;

  public static final int GL_QUAD_INTENSITY4_SGIS
    = GL.GL_QUAD_INTENSITY4_SGIS;

  public static final int GL_QUAD_INTENSITY8_SGIS
    = GL.GL_QUAD_INTENSITY8_SGIS;

  public static final int GL_DUAL_TEXTURE_SELECT_SGIS
    = GL.GL_DUAL_TEXTURE_SELECT_SGIS;

  public static final int GL_QUAD_TEXTURE_SELECT_SGIS
    = GL.GL_QUAD_TEXTURE_SELECT_SGIS;

  public static final int GL_SPRITE_SGIX
    = GL.GL_SPRITE_SGIX;

  public static final int GL_SPRITE_MODE_SGIX
    = GL.GL_SPRITE_MODE_SGIX;

  public static final int GL_SPRITE_AXIS_SGIX
    = GL.GL_SPRITE_AXIS_SGIX;

  public static final int GL_SPRITE_TRANSLATION_SGIX
    = GL.GL_SPRITE_TRANSLATION_SGIX;

  public static final int GL_SPRITE_AXIAL_SGIX
    = GL.GL_SPRITE_AXIAL_SGIX;

  public static final int GL_SPRITE_OBJECT_ALIGNED_SGIX
    = GL.GL_SPRITE_OBJECT_ALIGNED_SGIX;

  public static final int GL_SPRITE_EYE_ALIGNED_SGIX
    = GL.GL_SPRITE_EYE_ALIGNED_SGIX;

  public static final int GL_TEXTURE_MULTI_BUFFER_HINT_SGIX
    = GL.GL_TEXTURE_MULTI_BUFFER_HINT_SGIX;

  public static final int GL_POINT_SIZE_MIN_EXT
    = GL.GL_POINT_SIZE_MIN_EXT;

  public static final int GL_POINT_SIZE_MAX_EXT
    = GL.GL_POINT_SIZE_MAX_EXT;

  public static final int GL_POINT_FADE_THRESHOLD_SIZE_EXT
    = GL.GL_POINT_FADE_THRESHOLD_SIZE_EXT;

  public static final int GL_DISTANCE_ATTENUATION_EXT
    = GL.GL_DISTANCE_ATTENUATION_EXT;

  public static final int GL_POINT_SIZE_MIN_SGIS
    = GL.GL_POINT_SIZE_MIN_SGIS;

  public static final int GL_POINT_SIZE_MAX_SGIS
    = GL.GL_POINT_SIZE_MAX_SGIS;

  public static final int GL_POINT_FADE_THRESHOLD_SIZE_SGIS
    = GL.GL_POINT_FADE_THRESHOLD_SIZE_SGIS;

  public static final int GL_DISTANCE_ATTENUATION_SGIS
    = GL.GL_DISTANCE_ATTENUATION_SGIS;

  public static final int GL_INSTRUMENT_BUFFER_POINTER_SGIX
    = GL.GL_INSTRUMENT_BUFFER_POINTER_SGIX;

  public static final int GL_INSTRUMENT_MEASUREMENTS_SGIX
    = GL.GL_INSTRUMENT_MEASUREMENTS_SGIX;

  public static final int GL_POST_TEXTURE_FILTER_BIAS_SGIX
    = GL.GL_POST_TEXTURE_FILTER_BIAS_SGIX;

  public static final int GL_POST_TEXTURE_FILTER_SCALE_SGIX
    = GL.GL_POST_TEXTURE_FILTER_SCALE_SGIX;

  public static final int GL_POST_TEXTURE_FILTER_BIAS_RANGE_SGIX
    = GL.GL_POST_TEXTURE_FILTER_BIAS_RANGE_SGIX;

  public static final int GL_POST_TEXTURE_FILTER_SCALE_RANGE_SGIX
    = GL.GL_POST_TEXTURE_FILTER_SCALE_RANGE_SGIX;

  public static final int GL_FRAMEZOOM_SGIX
    = GL.GL_FRAMEZOOM_SGIX;

  public static final int GL_FRAMEZOOM_FACTOR_SGIX
    = GL.GL_FRAMEZOOM_FACTOR_SGIX;

  public static final int GL_MAX_FRAMEZOOM_FACTOR_SGIX
    = GL.GL_MAX_FRAMEZOOM_FACTOR_SGIX;

  public static final int GL_TEXTURE_DEFORMATION_BIT_SGIX
    = GL.GL_TEXTURE_DEFORMATION_BIT_SGIX;

  public static final int GL_GEOMETRY_DEFORMATION_BIT_SGIX
    = GL.GL_GEOMETRY_DEFORMATION_BIT_SGIX;

  public static final int GL_GEOMETRY_DEFORMATION_SGIX
    = GL.GL_GEOMETRY_DEFORMATION_SGIX;

  public static final int GL_TEXTURE_DEFORMATION_SGIX
    = GL.GL_TEXTURE_DEFORMATION_SGIX;

  public static final int GL_DEFORMATIONS_MASK_SGIX
    = GL.GL_DEFORMATIONS_MASK_SGIX;

  public static final int GL_MAX_DEFORMATION_ORDER_SGIX
    = GL.GL_MAX_DEFORMATION_ORDER_SGIX;

  public static final int GL_REFERENCE_PLANE_SGIX
    = GL.GL_REFERENCE_PLANE_SGIX;

  public static final int GL_REFERENCE_PLANE_EQUATION_SGIX
    = GL.GL_REFERENCE_PLANE_EQUATION_SGIX;

  public static final int GL_DEPTH_COMPONENT16_SGIX
    = GL.GL_DEPTH_COMPONENT16_SGIX;

  public static final int GL_DEPTH_COMPONENT24_SGIX
    = GL.GL_DEPTH_COMPONENT24_SGIX;

  public static final int GL_DEPTH_COMPONENT32_SGIX
    = GL.GL_DEPTH_COMPONENT32_SGIX;

  public static final int GL_FOG_FUNC_SGIS
    = GL.GL_FOG_FUNC_SGIS;

  public static final int GL_FOG_FUNC_POINTS_SGIS
    = GL.GL_FOG_FUNC_POINTS_SGIS;

  public static final int GL_MAX_FOG_FUNC_POINTS_SGIS
    = GL.GL_MAX_FOG_FUNC_POINTS_SGIS;

  public static final int GL_FOG_OFFSET_SGIX
    = GL.GL_FOG_OFFSET_SGIX;

  public static final int GL_FOG_OFFSET_VALUE_SGIX
    = GL.GL_FOG_OFFSET_VALUE_SGIX;

  public static final int GL_IMAGE_SCALE_X_HP
    = GL.GL_IMAGE_SCALE_X_HP;

  public static final int GL_IMAGE_SCALE_Y_HP
    = GL.GL_IMAGE_SCALE_Y_HP;

  public static final int GL_IMAGE_TRANSLATE_X_HP
    = GL.GL_IMAGE_TRANSLATE_X_HP;

  public static final int GL_IMAGE_TRANSLATE_Y_HP
    = GL.GL_IMAGE_TRANSLATE_Y_HP;

  public static final int GL_IMAGE_ROTATE_ANGLE_HP
    = GL.GL_IMAGE_ROTATE_ANGLE_HP;

  public static final int GL_IMAGE_ROTATE_ORIGIN_X_HP
    = GL.GL_IMAGE_ROTATE_ORIGIN_X_HP;

  public static final int GL_IMAGE_ROTATE_ORIGIN_Y_HP
    = GL.GL_IMAGE_ROTATE_ORIGIN_Y_HP;

  public static final int GL_IMAGE_MAG_FILTER_HP
    = GL.GL_IMAGE_MAG_FILTER_HP;

  public static final int GL_IMAGE_MIN_FILTER_HP
    = GL.GL_IMAGE_MIN_FILTER_HP;

  public static final int GL_IMAGE_CUBIC_WEIGHT_HP
    = GL.GL_IMAGE_CUBIC_WEIGHT_HP;

  public static final int GL_CUBIC_HP
    = GL.GL_CUBIC_HP;

  public static final int GL_AVERAGE_HP
    = GL.GL_AVERAGE_HP;

  public static final int GL_IMAGE_TRANSFORM_2D_HP
    = GL.GL_IMAGE_TRANSFORM_2D_HP;

  public static final int GL_POST_IMAGE_TRANSFORM_COLOR_TABLE_HP
    = GL.GL_POST_IMAGE_TRANSFORM_COLOR_TABLE_HP;

  public static final int GL_PROXY_POST_IMAGE_TRANSFORM_COLOR_TABLE_HP
    = GL.GL_PROXY_POST_IMAGE_TRANSFORM_COLOR_TABLE_HP;

  public static final int GL_TEXTURE_ENV_BIAS_SGIX
    = GL.GL_TEXTURE_ENV_BIAS_SGIX;

  public static final int GL_VERTEX_DATA_HINT_PGI
    = GL.GL_VERTEX_DATA_HINT_PGI;

  public static final int GL_VERTEX_CONSISTENT_HINT_PGI
    = GL.GL_VERTEX_CONSISTENT_HINT_PGI;

  public static final int GL_MATERIAL_SIDE_HINT_PGI
    = GL.GL_MATERIAL_SIDE_HINT_PGI;

  public static final int GL_MAX_VERTEX_HINT_PGI
    = GL.GL_MAX_VERTEX_HINT_PGI;

  public static final int GL_COLOR3_BIT_PGI
    = GL.GL_COLOR3_BIT_PGI;

  public static final int GL_COLOR4_BIT_PGI
    = GL.GL_COLOR4_BIT_PGI;

  public static final int GL_EDGEFLAG_BIT_PGI
    = GL.GL_EDGEFLAG_BIT_PGI;

  public static final int GL_INDEX_BIT_PGI
    = GL.GL_INDEX_BIT_PGI;

  public static final int GL_MAT_AMBIENT_BIT_PGI
    = GL.GL_MAT_AMBIENT_BIT_PGI;

  public static final int GL_MAT_AMBIENT_AND_DIFFUSE_BIT_PGI
    = GL.GL_MAT_AMBIENT_AND_DIFFUSE_BIT_PGI;

  public static final int GL_MAT_DIFFUSE_BIT_PGI
    = GL.GL_MAT_DIFFUSE_BIT_PGI;

  public static final int GL_MAT_EMISSION_BIT_PGI
    = GL.GL_MAT_EMISSION_BIT_PGI;

  public static final int GL_MAT_COLOR_INDEXES_BIT_PGI
    = GL.GL_MAT_COLOR_INDEXES_BIT_PGI;

  public static final int GL_MAT_SHININESS_BIT_PGI
    = GL.GL_MAT_SHININESS_BIT_PGI;

  public static final int GL_MAT_SPECULAR_BIT_PGI
    = GL.GL_MAT_SPECULAR_BIT_PGI;

  public static final int GL_NORMAL_BIT_PGI
    = GL.GL_NORMAL_BIT_PGI;

  public static final int GL_TEXCOORD1_BIT_PGI
    = GL.GL_TEXCOORD1_BIT_PGI;

  public static final int GL_TEXCOORD2_BIT_PGI
    = GL.GL_TEXCOORD2_BIT_PGI;

  public static final int GL_TEXCOORD3_BIT_PGI
    = GL.GL_TEXCOORD3_BIT_PGI;

  public static final long GL_TEXCOORD4_BIT_PGI
    = GL.GL_TEXCOORD4_BIT_PGI;

  public static final int GL_VERTEX23_BIT_PGI
    = GL.GL_VERTEX23_BIT_PGI;

  public static final int GL_VERTEX4_BIT_PGI
    = GL.GL_VERTEX4_BIT_PGI;

  public static final int GL_PREFER_DOUBLEBUFFER_HINT_PGI
    = GL.GL_PREFER_DOUBLEBUFFER_HINT_PGI;

  public static final int GL_CONSERVE_MEMORY_HINT_PGI
    = GL.GL_CONSERVE_MEMORY_HINT_PGI;

  public static final int GL_RECLAIM_MEMORY_HINT_PGI
    = GL.GL_RECLAIM_MEMORY_HINT_PGI;

  public static final int GL_NATIVE_GRAPHICS_HANDLE_PGI
    = GL.GL_NATIVE_GRAPHICS_HANDLE_PGI;

  public static final int GL_NATIVE_GRAPHICS_BEGIN_HINT_PGI
    = GL.GL_NATIVE_GRAPHICS_BEGIN_HINT_PGI;

  public static final int GL_NATIVE_GRAPHICS_END_HINT_PGI
    = GL.GL_NATIVE_GRAPHICS_END_HINT_PGI;

  public static final int GL_ALWAYS_FAST_HINT_PGI
    = GL.GL_ALWAYS_FAST_HINT_PGI;

  public static final int GL_ALWAYS_SOFT_HINT_PGI
    = GL.GL_ALWAYS_SOFT_HINT_PGI;

  public static final int GL_ALLOW_DRAW_OBJ_HINT_PGI
    = GL.GL_ALLOW_DRAW_OBJ_HINT_PGI;

  public static final int GL_ALLOW_DRAW_WIN_HINT_PGI
    = GL.GL_ALLOW_DRAW_WIN_HINT_PGI;

  public static final int GL_ALLOW_DRAW_FRG_HINT_PGI
    = GL.GL_ALLOW_DRAW_FRG_HINT_PGI;

  public static final int GL_ALLOW_DRAW_MEM_HINT_PGI
    = GL.GL_ALLOW_DRAW_MEM_HINT_PGI;

  public static final int GL_STRICT_DEPTHFUNC_HINT_PGI
    = GL.GL_STRICT_DEPTHFUNC_HINT_PGI;

  public static final int GL_STRICT_LIGHTING_HINT_PGI
    = GL.GL_STRICT_LIGHTING_HINT_PGI;

  public static final int GL_STRICT_SCISSOR_HINT_PGI
    = GL.GL_STRICT_SCISSOR_HINT_PGI;

  public static final int GL_FULL_STIPPLE_HINT_PGI
    = GL.GL_FULL_STIPPLE_HINT_PGI;

  public static final int GL_CLIP_NEAR_HINT_PGI
    = GL.GL_CLIP_NEAR_HINT_PGI;

  public static final int GL_CLIP_FAR_HINT_PGI
    = GL.GL_CLIP_FAR_HINT_PGI;

  public static final int GL_WIDE_LINE_HINT_PGI
    = GL.GL_WIDE_LINE_HINT_PGI;

  public static final int GL_BACK_NORMALS_HINT_PGI
    = GL.GL_BACK_NORMALS_HINT_PGI;

  public static final int GL_COLOR_INDEX1_EXT
    = GL.GL_COLOR_INDEX1_EXT;

  public static final int GL_COLOR_INDEX2_EXT
    = GL.GL_COLOR_INDEX2_EXT;

  public static final int GL_COLOR_INDEX4_EXT
    = GL.GL_COLOR_INDEX4_EXT;

  public static final int GL_COLOR_INDEX8_EXT
    = GL.GL_COLOR_INDEX8_EXT;

  public static final int GL_COLOR_INDEX12_EXT
    = GL.GL_COLOR_INDEX12_EXT;

  public static final int GL_COLOR_INDEX16_EXT
    = GL.GL_COLOR_INDEX16_EXT;

  public static final int GL_TEXTURE_INDEX_SIZE_EXT
    = GL.GL_TEXTURE_INDEX_SIZE_EXT;

  public static final int GL_CLIP_VOLUME_CLIPPING_HINT_EXT
    = GL.GL_CLIP_VOLUME_CLIPPING_HINT_EXT;

  public static final int GL_LIST_PRIORITY_SGIX
    = GL.GL_LIST_PRIORITY_SGIX;

  public static final int GL_IR_INSTRUMENT1_SGIX
    = GL.GL_IR_INSTRUMENT1_SGIX;

  public static final int GL_CALLIGRAPHIC_FRAGMENT_SGIX
    = GL.GL_CALLIGRAPHIC_FRAGMENT_SGIX;

  public static final int GL_TEXTURE_LOD_BIAS_S_SGIX
    = GL.GL_TEXTURE_LOD_BIAS_S_SGIX;

  public static final int GL_TEXTURE_LOD_BIAS_T_SGIX
    = GL.GL_TEXTURE_LOD_BIAS_T_SGIX;

  public static final int GL_TEXTURE_LOD_BIAS_R_SGIX
    = GL.GL_TEXTURE_LOD_BIAS_R_SGIX;

  public static final int GL_SHADOW_AMBIENT_SGIX
    = GL.GL_SHADOW_AMBIENT_SGIX;

  public static final int GL_INDEX_MATERIAL_EXT
    = GL.GL_INDEX_MATERIAL_EXT;

  public static final int GL_INDEX_MATERIAL_PARAMETER_EXT
    = GL.GL_INDEX_MATERIAL_PARAMETER_EXT;

  public static final int GL_INDEX_MATERIAL_FACE_EXT
    = GL.GL_INDEX_MATERIAL_FACE_EXT;

  public static final int GL_INDEX_TEST_EXT
    = GL.GL_INDEX_TEST_EXT;

  public static final int GL_INDEX_TEST_FUNC_EXT
    = GL.GL_INDEX_TEST_FUNC_EXT;

  public static final int GL_INDEX_TEST_REF_EXT
    = GL.GL_INDEX_TEST_REF_EXT;

  public static final int GL_IUI_V2F_EXT
    = GL.GL_IUI_V2F_EXT;

  public static final int GL_IUI_V3F_EXT
    = GL.GL_IUI_V3F_EXT;

  public static final int GL_IUI_N3F_V2F_EXT
    = GL.GL_IUI_N3F_V2F_EXT;

  public static final int GL_IUI_N3F_V3F_EXT
    = GL.GL_IUI_N3F_V3F_EXT;

  public static final int GL_T2F_IUI_V2F_EXT
    = GL.GL_T2F_IUI_V2F_EXT;

  public static final int GL_T2F_IUI_V3F_EXT
    = GL.GL_T2F_IUI_V3F_EXT;

  public static final int GL_T2F_IUI_N3F_V2F_EXT
    = GL.GL_T2F_IUI_N3F_V2F_EXT;

  public static final int GL_T2F_IUI_N3F_V3F_EXT
    = GL.GL_T2F_IUI_N3F_V3F_EXT;

  public static final int GL_ARRAY_ELEMENT_LOCK_FIRST_EXT
    = GL.GL_ARRAY_ELEMENT_LOCK_FIRST_EXT;

  public static final int GL_ARRAY_ELEMENT_LOCK_COUNT_EXT
    = GL.GL_ARRAY_ELEMENT_LOCK_COUNT_EXT;

  public static final int GL_CULL_VERTEX_EXT
    = GL.GL_CULL_VERTEX_EXT;

  public static final int GL_CULL_VERTEX_EYE_POSITION_EXT
    = GL.GL_CULL_VERTEX_EYE_POSITION_EXT;

  public static final int GL_CULL_VERTEX_OBJECT_POSITION_EXT
    = GL.GL_CULL_VERTEX_OBJECT_POSITION_EXT;

  public static final int GL_YCRCB_422_SGIX
    = GL.GL_YCRCB_422_SGIX;

  public static final int GL_YCRCB_444_SGIX
    = GL.GL_YCRCB_444_SGIX;

  public static final int GL_FRAGMENT_LIGHTING_SGIX
    = GL.GL_FRAGMENT_LIGHTING_SGIX;

  public static final int GL_FRAGMENT_COLOR_MATERIAL_SGIX
    = GL.GL_FRAGMENT_COLOR_MATERIAL_SGIX;

  public static final int GL_FRAGMENT_COLOR_MATERIAL_FACE_SGIX
    = GL.GL_FRAGMENT_COLOR_MATERIAL_FACE_SGIX;

  public static final int GL_FRAGMENT_COLOR_MATERIAL_PARAMETER_SGIX
    = GL.GL_FRAGMENT_COLOR_MATERIAL_PARAMETER_SGIX;

  public static final int GL_MAX_FRAGMENT_LIGHTS_SGIX
    = GL.GL_MAX_FRAGMENT_LIGHTS_SGIX;

  public static final int GL_MAX_ACTIVE_LIGHTS_SGIX
    = GL.GL_MAX_ACTIVE_LIGHTS_SGIX;

  public static final int GL_CURRENT_RASTER_NORMAL_SGIX
    = GL.GL_CURRENT_RASTER_NORMAL_SGIX;

  public static final int GL_LIGHT_ENV_MODE_SGIX
    = GL.GL_LIGHT_ENV_MODE_SGIX;

  public static final int GL_FRAGMENT_LIGHT_MODEL_LOCAL_VIEWER_SGIX
    = GL.GL_FRAGMENT_LIGHT_MODEL_LOCAL_VIEWER_SGIX;

  public static final int GL_FRAGMENT_LIGHT_MODEL_TWO_SIDE_SGIX
    = GL.GL_FRAGMENT_LIGHT_MODEL_TWO_SIDE_SGIX;

  public static final int GL_FRAGMENT_LIGHT_MODEL_AMBIENT_SGIX
    = GL.GL_FRAGMENT_LIGHT_MODEL_AMBIENT_SGIX;

  public static final int GL_FRAGMENT_LIGHT_MODEL_NORMAL_INTERPOLATION_SGIX
    = GL.GL_FRAGMENT_LIGHT_MODEL_NORMAL_INTERPOLATION_SGIX;

  public static final int GL_FRAGMENT_LIGHT0_SGIX
    = GL.GL_FRAGMENT_LIGHT0_SGIX;

  public static final int GL_FRAGMENT_LIGHT1_SGIX
    = GL.GL_FRAGMENT_LIGHT1_SGIX;

  public static final int GL_FRAGMENT_LIGHT2_SGIX
    = GL.GL_FRAGMENT_LIGHT2_SGIX;

  public static final int GL_FRAGMENT_LIGHT3_SGIX
    = GL.GL_FRAGMENT_LIGHT3_SGIX;

  public static final int GL_FRAGMENT_LIGHT4_SGIX
    = GL.GL_FRAGMENT_LIGHT4_SGIX;

  public static final int GL_FRAGMENT_LIGHT5_SGIX
    = GL.GL_FRAGMENT_LIGHT5_SGIX;

  public static final int GL_FRAGMENT_LIGHT6_SGIX
    = GL.GL_FRAGMENT_LIGHT6_SGIX;

  public static final int GL_FRAGMENT_LIGHT7_SGIX
    = GL.GL_FRAGMENT_LIGHT7_SGIX;

  public static final int GL_RASTER_POSITION_UNCLIPPED_IBM
    = GL.GL_RASTER_POSITION_UNCLIPPED_IBM;

  public static final int GL_TEXTURE_LIGHTING_MODE_HP
    = GL.GL_TEXTURE_LIGHTING_MODE_HP;

  public static final int GL_TEXTURE_POST_SPECULAR_HP
    = GL.GL_TEXTURE_POST_SPECULAR_HP;

  public static final int GL_TEXTURE_PRE_SPECULAR_HP
    = GL.GL_TEXTURE_PRE_SPECULAR_HP;

  public static final int GL_PHONG_WIN
    = GL.GL_PHONG_WIN;

  public static final int GL_PHONG_HINT_WIN
    = GL.GL_PHONG_HINT_WIN;

  public static final int GL_FOG_SPECULAR_TEXTURE_WIN
    = GL.GL_FOG_SPECULAR_TEXTURE_WIN;

  public static final int GL_FRAGMENT_MATERIAL_EXT
    = GL.GL_FRAGMENT_MATERIAL_EXT;

  public static final int GL_FRAGMENT_NORMAL_EXT
    = GL.GL_FRAGMENT_NORMAL_EXT;

  public static final int GL_FRAGMENT_COLOR_EXT
    = GL.GL_FRAGMENT_COLOR_EXT;

  public static final int GL_ATTENUATION_EXT
    = GL.GL_ATTENUATION_EXT;

  public static final int GL_SHADOW_ATTENUATION_EXT
    = GL.GL_SHADOW_ATTENUATION_EXT;

  public static final int GL_TEXTURE_APPLICATION_MODE_EXT
    = GL.GL_TEXTURE_APPLICATION_MODE_EXT;

  public static final int GL_TEXTURE_LIGHT_EXT
    = GL.GL_TEXTURE_LIGHT_EXT;

  public static final int GL_TEXTURE_MATERIAL_FACE_EXT
    = GL.GL_TEXTURE_MATERIAL_FACE_EXT;

  public static final int GL_TEXTURE_MATERIAL_PARAMETER_EXT
    = GL.GL_TEXTURE_MATERIAL_PARAMETER_EXT;

  public static final int GL_ALPHA_MIN_SGIX
    = GL.GL_ALPHA_MIN_SGIX;

  public static final int GL_ALPHA_MAX_SGIX
    = GL.GL_ALPHA_MAX_SGIX;

  public static final int GL_PIXEL_TEX_GEN_Q_CEILING_SGIX
    = GL.GL_PIXEL_TEX_GEN_Q_CEILING_SGIX;

  public static final int GL_PIXEL_TEX_GEN_Q_ROUND_SGIX
    = GL.GL_PIXEL_TEX_GEN_Q_ROUND_SGIX;

  public static final int GL_PIXEL_TEX_GEN_Q_FLOOR_SGIX
    = GL.GL_PIXEL_TEX_GEN_Q_FLOOR_SGIX;

  public static final int GL_PIXEL_TEX_GEN_ALPHA_REPLACE_SGIX
    = GL.GL_PIXEL_TEX_GEN_ALPHA_REPLACE_SGIX;

  public static final int GL_PIXEL_TEX_GEN_ALPHA_NO_REPLACE_SGIX
    = GL.GL_PIXEL_TEX_GEN_ALPHA_NO_REPLACE_SGIX;

  public static final int GL_PIXEL_TEX_GEN_ALPHA_LS_SGIX
    = GL.GL_PIXEL_TEX_GEN_ALPHA_LS_SGIX;

  public static final int GL_PIXEL_TEX_GEN_ALPHA_MS_SGIX
    = GL.GL_PIXEL_TEX_GEN_ALPHA_MS_SGIX;

  public static final int GL_ASYNC_MARKER_SGIX
    = GL.GL_ASYNC_MARKER_SGIX;

  public static final int GL_ASYNC_TEX_IMAGE_SGIX
    = GL.GL_ASYNC_TEX_IMAGE_SGIX;

  public static final int GL_ASYNC_DRAW_PIXELS_SGIX
    = GL.GL_ASYNC_DRAW_PIXELS_SGIX;

  public static final int GL_ASYNC_READ_PIXELS_SGIX
    = GL.GL_ASYNC_READ_PIXELS_SGIX;

  public static final int GL_MAX_ASYNC_TEX_IMAGE_SGIX
    = GL.GL_MAX_ASYNC_TEX_IMAGE_SGIX;

  public static final int GL_MAX_ASYNC_DRAW_PIXELS_SGIX
    = GL.GL_MAX_ASYNC_DRAW_PIXELS_SGIX;

  public static final int GL_MAX_ASYNC_READ_PIXELS_SGIX
    = GL.GL_MAX_ASYNC_READ_PIXELS_SGIX;

  public static final int GL_ASYNC_HISTOGRAM_SGIX
    = GL.GL_ASYNC_HISTOGRAM_SGIX;

  public static final int GL_MAX_ASYNC_HISTOGRAM_SGIX
    = GL.GL_MAX_ASYNC_HISTOGRAM_SGIX;

  public static final int GL_OCCLUSION_TEST_HP
    = GL.GL_OCCLUSION_TEST_HP;

  public static final int GL_OCCLUSION_TEST_RESULT_HP
    = GL.GL_OCCLUSION_TEST_RESULT_HP;

  public static final int GL_PIXEL_TRANSFORM_2D_EXT
    = GL.GL_PIXEL_TRANSFORM_2D_EXT;

  public static final int GL_PIXEL_MAG_FILTER_EXT
    = GL.GL_PIXEL_MAG_FILTER_EXT;

  public static final int GL_PIXEL_MIN_FILTER_EXT
    = GL.GL_PIXEL_MIN_FILTER_EXT;

  public static final int GL_PIXEL_CUBIC_WEIGHT_EXT
    = GL.GL_PIXEL_CUBIC_WEIGHT_EXT;

  public static final int GL_CUBIC_EXT
    = GL.GL_CUBIC_EXT;

  public static final int GL_AVERAGE_EXT
    = GL.GL_AVERAGE_EXT;

  public static final int GL_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT
    = GL.GL_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT;

  public static final int GL_MAX_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT
    = GL.GL_MAX_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT;

  public static final int GL_PIXEL_TRANSFORM_2D_MATRIX_EXT
    = GL.GL_PIXEL_TRANSFORM_2D_MATRIX_EXT;

  public static final int GL_SHARED_TEXTURE_PALETTE_EXT
    = GL.GL_SHARED_TEXTURE_PALETTE_EXT;

  public static final int GL_COLOR_SUM_EXT
    = GL.GL_COLOR_SUM_EXT;

  public static final int GL_CURRENT_SECONDARY_COLOR_EXT
    = GL.GL_CURRENT_SECONDARY_COLOR_EXT;

  public static final int GL_SECONDARY_COLOR_ARRAY_SIZE_EXT
    = GL.GL_SECONDARY_COLOR_ARRAY_SIZE_EXT;

  public static final int GL_SECONDARY_COLOR_ARRAY_TYPE_EXT
    = GL.GL_SECONDARY_COLOR_ARRAY_TYPE_EXT;

  public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT
    = GL.GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT;

  public static final int GL_SECONDARY_COLOR_ARRAY_POINTER_EXT
    = GL.GL_SECONDARY_COLOR_ARRAY_POINTER_EXT;

  public static final int GL_SECONDARY_COLOR_ARRAY_EXT
    = GL.GL_SECONDARY_COLOR_ARRAY_EXT;

  public static final int GL_PERTURB_EXT
    = GL.GL_PERTURB_EXT;

  public static final int GL_TEXTURE_NORMAL_EXT
    = GL.GL_TEXTURE_NORMAL_EXT;

  public static final int GL_FOG_COORDINATE_SOURCE_EXT
    = GL.GL_FOG_COORDINATE_SOURCE_EXT;

  public static final int GL_FOG_COORDINATE_EXT
    = GL.GL_FOG_COORDINATE_EXT;

  public static final int GL_FRAGMENT_DEPTH_EXT
    = GL.GL_FRAGMENT_DEPTH_EXT;

  public static final int GL_CURRENT_FOG_COORDINATE_EXT
    = GL.GL_CURRENT_FOG_COORDINATE_EXT;

  public static final int GL_FOG_COORDINATE_ARRAY_TYPE_EXT
    = GL.GL_FOG_COORDINATE_ARRAY_TYPE_EXT;

  public static final int GL_FOG_COORDINATE_ARRAY_STRIDE_EXT
    = GL.GL_FOG_COORDINATE_ARRAY_STRIDE_EXT;

  public static final int GL_FOG_COORDINATE_ARRAY_POINTER_EXT
    = GL.GL_FOG_COORDINATE_ARRAY_POINTER_EXT;

  public static final int GL_FOG_COORDINATE_ARRAY_EXT
    = GL.GL_FOG_COORDINATE_ARRAY_EXT;

  public static final int GL_SCREEN_COORDINATES_REND
    = GL.GL_SCREEN_COORDINATES_REND;

  public static final int GL_INVERTED_SCREEN_W_REND
    = GL.GL_INVERTED_SCREEN_W_REND;

  public static final int GL_LIGHT_MODEL_SPECULAR_VECTOR_APPLE
    = GL.GL_LIGHT_MODEL_SPECULAR_VECTOR_APPLE;

  public static final int GL_TRANSFORM_HINT_APPLE
    = GL.GL_TRANSFORM_HINT_APPLE;

  public static final int GL_FOG_SCALE_SGIX
    = GL.GL_FOG_SCALE_SGIX;

  public static final int GL_FOG_SCALE_VALUE_SGIX
    = GL.GL_FOG_SCALE_VALUE_SGIX;

  public static final int GL_UNPACK_CONSTANT_DATA_SUNX
    = GL.GL_UNPACK_CONSTANT_DATA_SUNX;

  public static final int GL_TEXTURE_CONSTANT_DATA_SUNX
    = GL.GL_TEXTURE_CONSTANT_DATA_SUNX;

  public static final int GL_GLOBAL_ALPHA_SUN
    = GL.GL_GLOBAL_ALPHA_SUN;

  public static final int GL_GLOBAL_ALPHA_FACTOR_SUN
    = GL.GL_GLOBAL_ALPHA_FACTOR_SUN;

  public static final int GL_BLEND_DST_RGB_EXT
    = GL.GL_BLEND_DST_RGB_EXT;

  public static final int GL_BLEND_SRC_RGB_EXT
    = GL.GL_BLEND_SRC_RGB_EXT;

  public static final int GL_BLEND_DST_ALPHA_EXT
    = GL.GL_BLEND_DST_ALPHA_EXT;

  public static final int GL_BLEND_SRC_ALPHA_EXT
    = GL.GL_BLEND_SRC_ALPHA_EXT;

  public static final int GL_RED_MIN_CLAMP_INGR
    = GL.GL_RED_MIN_CLAMP_INGR;

  public static final int GL_GREEN_MIN_CLAMP_INGR
    = GL.GL_GREEN_MIN_CLAMP_INGR;

  public static final int GL_BLUE_MIN_CLAMP_INGR
    = GL.GL_BLUE_MIN_CLAMP_INGR;

  public static final int GL_ALPHA_MIN_CLAMP_INGR
    = GL.GL_ALPHA_MIN_CLAMP_INGR;

  public static final int GL_RED_MAX_CLAMP_INGR
    = GL.GL_RED_MAX_CLAMP_INGR;

  public static final int GL_GREEN_MAX_CLAMP_INGR
    = GL.GL_GREEN_MAX_CLAMP_INGR;

  public static final int GL_BLUE_MAX_CLAMP_INGR
    = GL.GL_BLUE_MAX_CLAMP_INGR;

  public static final int GL_ALPHA_MAX_CLAMP_INGR
    = GL.GL_ALPHA_MAX_CLAMP_INGR;

  public static final int GL_INTERLACE_READ_INGR
    = GL.GL_INTERLACE_READ_INGR;

  public static final int GL_INCR_WRAP_EXT
    = GL.GL_INCR_WRAP_EXT;

  public static final int GL_DECR_WRAP_EXT
    = GL.GL_DECR_WRAP_EXT;

  public static final int GL_422_EXT
    = GL.GL_422_EXT;

  public static final int GL_422_REV_EXT
    = GL.GL_422_REV_EXT;

  public static final int GL_422_AVERAGE_EXT
    = GL.GL_422_AVERAGE_EXT;

  public static final int GL_422_REV_AVERAGE_EXT
    = GL.GL_422_REV_AVERAGE_EXT;

  public static final int GL_NORMAL_MAP_NV
    = GL.GL_NORMAL_MAP_NV;

  public static final int GL_REFLECTION_MAP_NV
    = GL.GL_REFLECTION_MAP_NV;

  public static final int GL_WRAP_BORDER_SUN
    = GL.GL_WRAP_BORDER_SUN;

  public static final int GL_MAX_TEXTURE_LOD_BIAS_EXT
    = GL.GL_MAX_TEXTURE_LOD_BIAS_EXT;

  public static final int GL_TEXTURE_FILTER_CONTROL_EXT
    = GL.GL_TEXTURE_FILTER_CONTROL_EXT;

  public static final int GL_TEXTURE_LOD_BIAS_EXT
    = GL.GL_TEXTURE_LOD_BIAS_EXT;

  public static final int GL_TEXTURE_MAX_ANISOTROPY_EXT
    = GL.GL_TEXTURE_MAX_ANISOTROPY_EXT;

  public static final int GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT
    = GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;

  public static final int GL_MODELVIEW0_STACK_DEPTH_EXT
    = GL.GL_MODELVIEW0_STACK_DEPTH_EXT;

  public static final int GL_MODELVIEW1_STACK_DEPTH_EXT
    = GL.GL_MODELVIEW1_STACK_DEPTH_EXT;

  public static final int GL_MODELVIEW0_MATRIX_EXT
    = GL.GL_MODELVIEW0_MATRIX_EXT;

  public static final int GL_MODELVIEW1_MATRIX_EXT
    = GL.GL_MODELVIEW1_MATRIX_EXT;

  public static final int GL_VERTEX_WEIGHTING_EXT
    = GL.GL_VERTEX_WEIGHTING_EXT;

  public static final int GL_MODELVIEW0_EXT
    = GL.GL_MODELVIEW0_EXT;

  public static final int GL_MODELVIEW1_EXT
    = GL.GL_MODELVIEW1_EXT;

  public static final int GL_CURRENT_VERTEX_WEIGHT_EXT
    = GL.GL_CURRENT_VERTEX_WEIGHT_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_EXT
    = GL.GL_VERTEX_WEIGHT_ARRAY_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT
    = GL.GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT
    = GL.GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT
    = GL.GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT
    = GL.GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT;

  public static final int GL_MAX_SHININESS_NV
    = GL.GL_MAX_SHININESS_NV;

  public static final int GL_MAX_SPOT_EXPONENT_NV
    = GL.GL_MAX_SPOT_EXPONENT_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_NV
    = GL.GL_VERTEX_ARRAY_RANGE_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_NV
    = GL.GL_VERTEX_ARRAY_RANGE_LENGTH_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_VALID_NV
    = GL.GL_VERTEX_ARRAY_RANGE_VALID_NV;

  public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV
    = GL.GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_POINTER_NV
    = GL.GL_VERTEX_ARRAY_RANGE_POINTER_NV;

  public static final int GL_REGISTER_COMBINERS_NV
    = GL.GL_REGISTER_COMBINERS_NV;

  public static final int GL_VARIABLE_A_NV
    = GL.GL_VARIABLE_A_NV;

  public static final int GL_VARIABLE_B_NV
    = GL.GL_VARIABLE_B_NV;

  public static final int GL_VARIABLE_C_NV
    = GL.GL_VARIABLE_C_NV;

  public static final int GL_VARIABLE_D_NV
    = GL.GL_VARIABLE_D_NV;

  public static final int GL_VARIABLE_E_NV
    = GL.GL_VARIABLE_E_NV;

  public static final int GL_VARIABLE_F_NV
    = GL.GL_VARIABLE_F_NV;

  public static final int GL_VARIABLE_G_NV
    = GL.GL_VARIABLE_G_NV;

  public static final int GL_CONSTANT_COLOR0_NV
    = GL.GL_CONSTANT_COLOR0_NV;

  public static final int GL_CONSTANT_COLOR1_NV
    = GL.GL_CONSTANT_COLOR1_NV;

  public static final int GL_PRIMARY_COLOR_NV
    = GL.GL_PRIMARY_COLOR_NV;

  public static final int GL_SECONDARY_COLOR_NV
    = GL.GL_SECONDARY_COLOR_NV;

  public static final int GL_SPARE0_NV
    = GL.GL_SPARE0_NV;

  public static final int GL_SPARE1_NV
    = GL.GL_SPARE1_NV;

  public static final int GL_DISCARD_NV
    = GL.GL_DISCARD_NV;

  public static final int GL_E_TIMES_F_NV
    = GL.GL_E_TIMES_F_NV;

  public static final int GL_SPARE0_PLUS_SECONDARY_COLOR_NV
    = GL.GL_SPARE0_PLUS_SECONDARY_COLOR_NV;

  public static final int GL_UNSIGNED_IDENTITY_NV
    = GL.GL_UNSIGNED_IDENTITY_NV;

  public static final int GL_UNSIGNED_INVERT_NV
    = GL.GL_UNSIGNED_INVERT_NV;

  public static final int GL_EXPAND_NORMAL_NV
    = GL.GL_EXPAND_NORMAL_NV;

  public static final int GL_EXPAND_NEGATE_NV
    = GL.GL_EXPAND_NEGATE_NV;

  public static final int GL_HALF_BIAS_NORMAL_NV
    = GL.GL_HALF_BIAS_NORMAL_NV;

  public static final int GL_HALF_BIAS_NEGATE_NV
    = GL.GL_HALF_BIAS_NEGATE_NV;

  public static final int GL_SIGNED_IDENTITY_NV
    = GL.GL_SIGNED_IDENTITY_NV;

  public static final int GL_SIGNED_NEGATE_NV
    = GL.GL_SIGNED_NEGATE_NV;

  public static final int GL_SCALE_BY_TWO_NV
    = GL.GL_SCALE_BY_TWO_NV;

  public static final int GL_SCALE_BY_FOUR_NV
    = GL.GL_SCALE_BY_FOUR_NV;

  public static final int GL_SCALE_BY_ONE_HALF_NV
    = GL.GL_SCALE_BY_ONE_HALF_NV;

  public static final int GL_BIAS_BY_NEGATIVE_ONE_HALF_NV
    = GL.GL_BIAS_BY_NEGATIVE_ONE_HALF_NV;

  public static final int GL_COMBINER_INPUT_NV
    = GL.GL_COMBINER_INPUT_NV;

  public static final int GL_COMBINER_MAPPING_NV
    = GL.GL_COMBINER_MAPPING_NV;

  public static final int GL_COMBINER_COMPONENT_USAGE_NV
    = GL.GL_COMBINER_COMPONENT_USAGE_NV;

  public static final int GL_COMBINER_AB_DOT_PRODUCT_NV
    = GL.GL_COMBINER_AB_DOT_PRODUCT_NV;

  public static final int GL_COMBINER_CD_DOT_PRODUCT_NV
    = GL.GL_COMBINER_CD_DOT_PRODUCT_NV;

  public static final int GL_COMBINER_MUX_SUM_NV
    = GL.GL_COMBINER_MUX_SUM_NV;

  public static final int GL_COMBINER_SCALE_NV
    = GL.GL_COMBINER_SCALE_NV;

  public static final int GL_COMBINER_BIAS_NV
    = GL.GL_COMBINER_BIAS_NV;

  public static final int GL_COMBINER_AB_OUTPUT_NV
    = GL.GL_COMBINER_AB_OUTPUT_NV;

  public static final int GL_COMBINER_CD_OUTPUT_NV
    = GL.GL_COMBINER_CD_OUTPUT_NV;

  public static final int GL_COMBINER_SUM_OUTPUT_NV
    = GL.GL_COMBINER_SUM_OUTPUT_NV;

  public static final int GL_MAX_GENERAL_COMBINERS_NV
    = GL.GL_MAX_GENERAL_COMBINERS_NV;

  public static final int GL_NUM_GENERAL_COMBINERS_NV
    = GL.GL_NUM_GENERAL_COMBINERS_NV;

  public static final int GL_COLOR_SUM_CLAMP_NV
    = GL.GL_COLOR_SUM_CLAMP_NV;

  public static final int GL_COMBINER0_NV
    = GL.GL_COMBINER0_NV;

  public static final int GL_COMBINER1_NV
    = GL.GL_COMBINER1_NV;

  public static final int GL_COMBINER2_NV
    = GL.GL_COMBINER2_NV;

  public static final int GL_COMBINER3_NV
    = GL.GL_COMBINER3_NV;

  public static final int GL_COMBINER4_NV
    = GL.GL_COMBINER4_NV;

  public static final int GL_COMBINER5_NV
    = GL.GL_COMBINER5_NV;

  public static final int GL_COMBINER6_NV
    = GL.GL_COMBINER6_NV;

  public static final int GL_COMBINER7_NV
    = GL.GL_COMBINER7_NV;

  public static final int GL_FOG_DISTANCE_MODE_NV
    = GL.GL_FOG_DISTANCE_MODE_NV;

  public static final int GL_EYE_RADIAL_NV
    = GL.GL_EYE_RADIAL_NV;

  public static final int GL_EYE_PLANE_ABSOLUTE_NV
    = GL.GL_EYE_PLANE_ABSOLUTE_NV;

  public static final int GL_EMBOSS_LIGHT_NV
    = GL.GL_EMBOSS_LIGHT_NV;

  public static final int GL_EMBOSS_CONSTANT_NV
    = GL.GL_EMBOSS_CONSTANT_NV;

  public static final int GL_EMBOSS_MAP_NV
    = GL.GL_EMBOSS_MAP_NV;

  public static final int GL_COMBINE4_NV
    = GL.GL_COMBINE4_NV;

  public static final int GL_SOURCE3_RGB_NV
    = GL.GL_SOURCE3_RGB_NV;

  public static final int GL_SOURCE3_ALPHA_NV
    = GL.GL_SOURCE3_ALPHA_NV;

  public static final int GL_OPERAND3_RGB_NV
    = GL.GL_OPERAND3_RGB_NV;

  public static final int GL_OPERAND3_ALPHA_NV
    = GL.GL_OPERAND3_ALPHA_NV;

  public static final int GL_COMPRESSED_RGB_S3TC_DXT1_EXT
    = GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT;

  public static final int GL_COMPRESSED_RGBA_S3TC_DXT1_EXT
    = GL.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;

  public static final int GL_COMPRESSED_RGBA_S3TC_DXT3_EXT
    = GL.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;

  public static final int GL_COMPRESSED_RGBA_S3TC_DXT5_EXT
    = GL.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;

  public static final int GL_CULL_VERTEX_IBM
    = GL.GL_CULL_VERTEX_IBM;

  public static final int GL_PACK_SUBSAMPLE_RATE_SGIX
    = GL.GL_PACK_SUBSAMPLE_RATE_SGIX;

  public static final int GL_UNPACK_SUBSAMPLE_RATE_SGIX
    = GL.GL_UNPACK_SUBSAMPLE_RATE_SGIX;

  public static final int GL_PIXEL_SUBSAMPLE_4444_SGIX
    = GL.GL_PIXEL_SUBSAMPLE_4444_SGIX;

  public static final int GL_PIXEL_SUBSAMPLE_2424_SGIX
    = GL.GL_PIXEL_SUBSAMPLE_2424_SGIX;

  public static final int GL_PIXEL_SUBSAMPLE_4242_SGIX
    = GL.GL_PIXEL_SUBSAMPLE_4242_SGIX;

  public static final int GL_YCRCB_SGIX
    = GL.GL_YCRCB_SGIX;

  public static final int GL_YCRCBA_SGIX
    = GL.GL_YCRCBA_SGIX;

  public static final int GL_DEPTH_PASS_INSTRUMENT_SGIX
    = GL.GL_DEPTH_PASS_INSTRUMENT_SGIX;

  public static final int GL_DEPTH_PASS_INSTRUMENT_COUNTERS_SGIX
    = GL.GL_DEPTH_PASS_INSTRUMENT_COUNTERS_SGIX;

  public static final int GL_DEPTH_PASS_INSTRUMENT_MAX_SGIX
    = GL.GL_DEPTH_PASS_INSTRUMENT_MAX_SGIX;

  public static final int GL_COMPRESSED_RGB_FXT1_3DFX
    = GL.GL_COMPRESSED_RGB_FXT1_3DFX;

  public static final int GL_COMPRESSED_RGBA_FXT1_3DFX
    = GL.GL_COMPRESSED_RGBA_FXT1_3DFX;

  public static final int GL_MULTISAMPLE_3DFX
    = GL.GL_MULTISAMPLE_3DFX;

  public static final int GL_SAMPLE_BUFFERS_3DFX
    = GL.GL_SAMPLE_BUFFERS_3DFX;

  public static final int GL_SAMPLES_3DFX
    = GL.GL_SAMPLES_3DFX;

  public static final int GL_MULTISAMPLE_BIT_3DFX
    = GL.GL_MULTISAMPLE_BIT_3DFX;

  public static final int GL_MULTISAMPLE_EXT
    = GL.GL_MULTISAMPLE_EXT;

  public static final int GL_SAMPLE_ALPHA_TO_MASK_EXT
    = GL.GL_SAMPLE_ALPHA_TO_MASK_EXT;

  public static final int GL_SAMPLE_ALPHA_TO_ONE_EXT
    = GL.GL_SAMPLE_ALPHA_TO_ONE_EXT;

  public static final int GL_SAMPLE_MASK_EXT
    = GL.GL_SAMPLE_MASK_EXT;

  public static final int GL_1PASS_EXT
    = GL.GL_1PASS_EXT;

  public static final int GL_2PASS_0_EXT
    = GL.GL_2PASS_0_EXT;

  public static final int GL_2PASS_1_EXT
    = GL.GL_2PASS_1_EXT;

  public static final int GL_4PASS_0_EXT
    = GL.GL_4PASS_0_EXT;

  public static final int GL_4PASS_1_EXT
    = GL.GL_4PASS_1_EXT;

  public static final int GL_4PASS_2_EXT
    = GL.GL_4PASS_2_EXT;

  public static final int GL_4PASS_3_EXT
    = GL.GL_4PASS_3_EXT;

  public static final int GL_SAMPLE_BUFFERS_EXT
    = GL.GL_SAMPLE_BUFFERS_EXT;

  public static final int GL_SAMPLES_EXT
    = GL.GL_SAMPLES_EXT;

  public static final int GL_SAMPLE_MASK_VALUE_EXT
    = GL.GL_SAMPLE_MASK_VALUE_EXT;

  public static final int GL_SAMPLE_MASK_INVERT_EXT
    = GL.GL_SAMPLE_MASK_INVERT_EXT;

  public static final int GL_SAMPLE_PATTERN_EXT
    = GL.GL_SAMPLE_PATTERN_EXT;

  public static final int GL_MULTISAMPLE_BIT_EXT
    = GL.GL_MULTISAMPLE_BIT_EXT;

  public static final int GL_VERTEX_PRECLIP_SGIX
    = GL.GL_VERTEX_PRECLIP_SGIX;

  public static final int GL_VERTEX_PRECLIP_HINT_SGIX
    = GL.GL_VERTEX_PRECLIP_HINT_SGIX;

  public static final int GL_CONVOLUTION_HINT_SGIX
    = GL.GL_CONVOLUTION_HINT_SGIX;

  public static final int GL_PACK_RESAMPLE_SGIX
    = GL.GL_PACK_RESAMPLE_SGIX;

  public static final int GL_UNPACK_RESAMPLE_SGIX
    = GL.GL_UNPACK_RESAMPLE_SGIX;

  public static final int GL_RESAMPLE_REPLICATE_SGIX
    = GL.GL_RESAMPLE_REPLICATE_SGIX;

  public static final int GL_RESAMPLE_ZERO_FILL_SGIX
    = GL.GL_RESAMPLE_ZERO_FILL_SGIX;

  public static final int GL_RESAMPLE_DECIMATE_SGIX
    = GL.GL_RESAMPLE_DECIMATE_SGIX;

  public static final int GL_EYE_DISTANCE_TO_POINT_SGIS
    = GL.GL_EYE_DISTANCE_TO_POINT_SGIS;

  public static final int GL_OBJECT_DISTANCE_TO_POINT_SGIS
    = GL.GL_OBJECT_DISTANCE_TO_POINT_SGIS;

  public static final int GL_EYE_DISTANCE_TO_LINE_SGIS
    = GL.GL_EYE_DISTANCE_TO_LINE_SGIS;

  public static final int GL_OBJECT_DISTANCE_TO_LINE_SGIS
    = GL.GL_OBJECT_DISTANCE_TO_LINE_SGIS;

  public static final int GL_EYE_POINT_SGIS
    = GL.GL_EYE_POINT_SGIS;

  public static final int GL_OBJECT_POINT_SGIS
    = GL.GL_OBJECT_POINT_SGIS;

  public static final int GL_EYE_LINE_SGIS
    = GL.GL_EYE_LINE_SGIS;

  public static final int GL_OBJECT_LINE_SGIS
    = GL.GL_OBJECT_LINE_SGIS;

  public static final int GL_TEXTURE_COLOR_WRITEMASK_SGIS
    = GL.GL_TEXTURE_COLOR_WRITEMASK_SGIS;

  public static final int GL_MIRROR_CLAMP_ATI
    = GL.GL_MIRROR_CLAMP_ATI;

  public static final int GL_MIRROR_CLAMP_TO_EDGE_ATI
    = GL.GL_MIRROR_CLAMP_TO_EDGE_ATI;

  public static final int GL_ALL_COMPLETED_NV
    = GL.GL_ALL_COMPLETED_NV;

  public static final int GL_FENCE_STATUS_NV
    = GL.GL_FENCE_STATUS_NV;

  public static final int GL_FENCE_CONDITION_NV
    = GL.GL_FENCE_CONDITION_NV;

  public static final int GL_MIRRORED_REPEAT_IBM
    = GL.GL_MIRRORED_REPEAT_IBM;

  public static final int GL_EVAL_2D_NV
    = GL.GL_EVAL_2D_NV;

  public static final int GL_EVAL_TRIANGULAR_2D_NV
    = GL.GL_EVAL_TRIANGULAR_2D_NV;

  public static final int GL_MAP_TESSELLATION_NV
    = GL.GL_MAP_TESSELLATION_NV;

  public static final int GL_MAP_ATTRIB_U_ORDER_NV
    = GL.GL_MAP_ATTRIB_U_ORDER_NV;

  public static final int GL_MAP_ATTRIB_V_ORDER_NV
    = GL.GL_MAP_ATTRIB_V_ORDER_NV;

  public static final int GL_EVAL_FRACTIONAL_TESSELLATION_NV
    = GL.GL_EVAL_FRACTIONAL_TESSELLATION_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB0_NV
    = GL.GL_EVAL_VERTEX_ATTRIB0_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB1_NV
    = GL.GL_EVAL_VERTEX_ATTRIB1_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB2_NV
    = GL.GL_EVAL_VERTEX_ATTRIB2_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB3_NV
    = GL.GL_EVAL_VERTEX_ATTRIB3_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB4_NV
    = GL.GL_EVAL_VERTEX_ATTRIB4_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB5_NV
    = GL.GL_EVAL_VERTEX_ATTRIB5_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB6_NV
    = GL.GL_EVAL_VERTEX_ATTRIB6_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB7_NV
    = GL.GL_EVAL_VERTEX_ATTRIB7_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB8_NV
    = GL.GL_EVAL_VERTEX_ATTRIB8_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB9_NV
    = GL.GL_EVAL_VERTEX_ATTRIB9_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB10_NV
    = GL.GL_EVAL_VERTEX_ATTRIB10_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB11_NV
    = GL.GL_EVAL_VERTEX_ATTRIB11_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB12_NV
    = GL.GL_EVAL_VERTEX_ATTRIB12_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB13_NV
    = GL.GL_EVAL_VERTEX_ATTRIB13_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB14_NV
    = GL.GL_EVAL_VERTEX_ATTRIB14_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB15_NV
    = GL.GL_EVAL_VERTEX_ATTRIB15_NV;

  public static final int GL_MAX_MAP_TESSELLATION_NV
    = GL.GL_MAX_MAP_TESSELLATION_NV;

  public static final int GL_MAX_RATIONAL_EVAL_ORDER_NV
    = GL.GL_MAX_RATIONAL_EVAL_ORDER_NV;

  public static final int GL_DEPTH_STENCIL_NV
    = GL.GL_DEPTH_STENCIL_NV;

  public static final int GL_UNSIGNED_INT_24_8_NV
    = GL.GL_UNSIGNED_INT_24_8_NV;

  public static final int GL_PER_STAGE_CONSTANTS_NV
    = GL.GL_PER_STAGE_CONSTANTS_NV;

  public static final int GL_TEXTURE_RECTANGLE_NV
    = GL.GL_TEXTURE_RECTANGLE_NV;

  public static final int GL_TEXTURE_BINDING_RECTANGLE_NV
    = GL.GL_TEXTURE_BINDING_RECTANGLE_NV;

  public static final int GL_PROXY_TEXTURE_RECTANGLE_NV
    = GL.GL_PROXY_TEXTURE_RECTANGLE_NV;

  public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE_NV
    = GL.GL_MAX_RECTANGLE_TEXTURE_SIZE_NV;

  public static final int GL_OFFSET_TEXTURE_RECTANGLE_NV
    = GL.GL_OFFSET_TEXTURE_RECTANGLE_NV;

  public static final int GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV
    = GL.GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV
    = GL.GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV;

  public static final int GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV
    = GL.GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV;

  public static final int GL_UNSIGNED_INT_S8_S8_8_8_NV
    = GL.GL_UNSIGNED_INT_S8_S8_8_8_NV;

  public static final int GL_UNSIGNED_INT_8_8_S8_S8_REV_NV
    = GL.GL_UNSIGNED_INT_8_8_S8_S8_REV_NV;

  public static final int GL_DSDT_MAG_INTENSITY_NV
    = GL.GL_DSDT_MAG_INTENSITY_NV;

  public static final int GL_SHADER_CONSISTENT_NV
    = GL.GL_SHADER_CONSISTENT_NV;

  public static final int GL_TEXTURE_SHADER_NV
    = GL.GL_TEXTURE_SHADER_NV;

  public static final int GL_SHADER_OPERATION_NV
    = GL.GL_SHADER_OPERATION_NV;

  public static final int GL_CULL_MODES_NV
    = GL.GL_CULL_MODES_NV;

  public static final int GL_OFFSET_TEXTURE_MATRIX_NV
    = GL.GL_OFFSET_TEXTURE_MATRIX_NV;

  public static final int GL_OFFSET_TEXTURE_SCALE_NV
    = GL.GL_OFFSET_TEXTURE_SCALE_NV;

  public static final int GL_OFFSET_TEXTURE_BIAS_NV
    = GL.GL_OFFSET_TEXTURE_BIAS_NV;

  public static final int GL_OFFSET_TEXTURE_2D_MATRIX_NV
    = GL.GL_OFFSET_TEXTURE_2D_MATRIX_NV;

  public static final int GL_OFFSET_TEXTURE_2D_SCALE_NV
    = GL.GL_OFFSET_TEXTURE_2D_SCALE_NV;

  public static final int GL_OFFSET_TEXTURE_2D_BIAS_NV
    = GL.GL_OFFSET_TEXTURE_2D_BIAS_NV;

  public static final int GL_PREVIOUS_TEXTURE_INPUT_NV
    = GL.GL_PREVIOUS_TEXTURE_INPUT_NV;

  public static final int GL_CONST_EYE_NV
    = GL.GL_CONST_EYE_NV;

  public static final int GL_PASS_THROUGH_NV
    = GL.GL_PASS_THROUGH_NV;

  public static final int GL_CULL_FRAGMENT_NV
    = GL.GL_CULL_FRAGMENT_NV;

  public static final int GL_OFFSET_TEXTURE_2D_NV
    = GL.GL_OFFSET_TEXTURE_2D_NV;

  public static final int GL_DEPENDENT_AR_TEXTURE_2D_NV
    = GL.GL_DEPENDENT_AR_TEXTURE_2D_NV;

  public static final int GL_DEPENDENT_GB_TEXTURE_2D_NV
    = GL.GL_DEPENDENT_GB_TEXTURE_2D_NV;

  public static final int GL_DOT_PRODUCT_NV
    = GL.GL_DOT_PRODUCT_NV;

  public static final int GL_DOT_PRODUCT_DEPTH_REPLACE_NV
    = GL.GL_DOT_PRODUCT_DEPTH_REPLACE_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_2D_NV
    = GL.GL_DOT_PRODUCT_TEXTURE_2D_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV
    = GL.GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV
    = GL.GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV
    = GL.GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV
    = GL.GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV;

  public static final int GL_HILO_NV
    = GL.GL_HILO_NV;

  public static final int GL_DSDT_NV
    = GL.GL_DSDT_NV;

  public static final int GL_DSDT_MAG_NV
    = GL.GL_DSDT_MAG_NV;

  public static final int GL_DSDT_MAG_VIB_NV
    = GL.GL_DSDT_MAG_VIB_NV;

  public static final int GL_HILO16_NV
    = GL.GL_HILO16_NV;

  public static final int GL_SIGNED_HILO_NV
    = GL.GL_SIGNED_HILO_NV;

  public static final int GL_SIGNED_HILO16_NV
    = GL.GL_SIGNED_HILO16_NV;

  public static final int GL_SIGNED_RGBA_NV
    = GL.GL_SIGNED_RGBA_NV;

  public static final int GL_SIGNED_RGBA8_NV
    = GL.GL_SIGNED_RGBA8_NV;

  public static final int GL_SIGNED_RGB_NV
    = GL.GL_SIGNED_RGB_NV;

  public static final int GL_SIGNED_RGB8_NV
    = GL.GL_SIGNED_RGB8_NV;

  public static final int GL_SIGNED_LUMINANCE_NV
    = GL.GL_SIGNED_LUMINANCE_NV;

  public static final int GL_SIGNED_LUMINANCE8_NV
    = GL.GL_SIGNED_LUMINANCE8_NV;

  public static final int GL_SIGNED_LUMINANCE_ALPHA_NV
    = GL.GL_SIGNED_LUMINANCE_ALPHA_NV;

  public static final int GL_SIGNED_LUMINANCE8_ALPHA8_NV
    = GL.GL_SIGNED_LUMINANCE8_ALPHA8_NV;

  public static final int GL_SIGNED_ALPHA_NV
    = GL.GL_SIGNED_ALPHA_NV;

  public static final int GL_SIGNED_ALPHA8_NV
    = GL.GL_SIGNED_ALPHA8_NV;

  public static final int GL_SIGNED_INTENSITY_NV
    = GL.GL_SIGNED_INTENSITY_NV;

  public static final int GL_SIGNED_INTENSITY8_NV
    = GL.GL_SIGNED_INTENSITY8_NV;

  public static final int GL_DSDT8_NV
    = GL.GL_DSDT8_NV;

  public static final int GL_DSDT8_MAG8_NV
    = GL.GL_DSDT8_MAG8_NV;

  public static final int GL_DSDT8_MAG8_INTENSITY8_NV
    = GL.GL_DSDT8_MAG8_INTENSITY8_NV;

  public static final int GL_SIGNED_RGB_UNSIGNED_ALPHA_NV
    = GL.GL_SIGNED_RGB_UNSIGNED_ALPHA_NV;

  public static final int GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV
    = GL.GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV;

  public static final int GL_HI_SCALE_NV
    = GL.GL_HI_SCALE_NV;

  public static final int GL_LO_SCALE_NV
    = GL.GL_LO_SCALE_NV;

  public static final int GL_DS_SCALE_NV
    = GL.GL_DS_SCALE_NV;

  public static final int GL_DT_SCALE_NV
    = GL.GL_DT_SCALE_NV;

  public static final int GL_MAGNITUDE_SCALE_NV
    = GL.GL_MAGNITUDE_SCALE_NV;

  public static final int GL_VIBRANCE_SCALE_NV
    = GL.GL_VIBRANCE_SCALE_NV;

  public static final int GL_HI_BIAS_NV
    = GL.GL_HI_BIAS_NV;

  public static final int GL_LO_BIAS_NV
    = GL.GL_LO_BIAS_NV;

  public static final int GL_DS_BIAS_NV
    = GL.GL_DS_BIAS_NV;

  public static final int GL_DT_BIAS_NV
    = GL.GL_DT_BIAS_NV;

  public static final int GL_MAGNITUDE_BIAS_NV
    = GL.GL_MAGNITUDE_BIAS_NV;

  public static final int GL_VIBRANCE_BIAS_NV
    = GL.GL_VIBRANCE_BIAS_NV;

  public static final int GL_TEXTURE_BORDER_VALUES_NV
    = GL.GL_TEXTURE_BORDER_VALUES_NV;

  public static final int GL_TEXTURE_HI_SIZE_NV
    = GL.GL_TEXTURE_HI_SIZE_NV;

  public static final int GL_TEXTURE_LO_SIZE_NV
    = GL.GL_TEXTURE_LO_SIZE_NV;

  public static final int GL_TEXTURE_DS_SIZE_NV
    = GL.GL_TEXTURE_DS_SIZE_NV;

  public static final int GL_TEXTURE_DT_SIZE_NV
    = GL.GL_TEXTURE_DT_SIZE_NV;

  public static final int GL_TEXTURE_MAG_SIZE_NV
    = GL.GL_TEXTURE_MAG_SIZE_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_3D_NV
    = GL.GL_DOT_PRODUCT_TEXTURE_3D_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_WITHOUT_FLUSH_NV
    = GL.GL_VERTEX_ARRAY_RANGE_WITHOUT_FLUSH_NV;

  public static final int GL_VERTEX_PROGRAM_NV
    = GL.GL_VERTEX_PROGRAM_NV;

  public static final int GL_VERTEX_STATE_PROGRAM_NV
    = GL.GL_VERTEX_STATE_PROGRAM_NV;

  public static final int GL_ATTRIB_ARRAY_SIZE_NV
    = GL.GL_ATTRIB_ARRAY_SIZE_NV;

  public static final int GL_ATTRIB_ARRAY_STRIDE_NV
    = GL.GL_ATTRIB_ARRAY_STRIDE_NV;

  public static final int GL_ATTRIB_ARRAY_TYPE_NV
    = GL.GL_ATTRIB_ARRAY_TYPE_NV;

  public static final int GL_CURRENT_ATTRIB_NV
    = GL.GL_CURRENT_ATTRIB_NV;

  public static final int GL_PROGRAM_LENGTH_NV
    = GL.GL_PROGRAM_LENGTH_NV;

  public static final int GL_PROGRAM_STRING_NV
    = GL.GL_PROGRAM_STRING_NV;

  public static final int GL_MODELVIEW_PROJECTION_NV
    = GL.GL_MODELVIEW_PROJECTION_NV;

  public static final int GL_IDENTITY_NV
    = GL.GL_IDENTITY_NV;

  public static final int GL_INVERSE_NV
    = GL.GL_INVERSE_NV;

  public static final int GL_TRANSPOSE_NV
    = GL.GL_TRANSPOSE_NV;

  public static final int GL_INVERSE_TRANSPOSE_NV
    = GL.GL_INVERSE_TRANSPOSE_NV;

  public static final int GL_MAX_TRACK_MATRIX_STACK_DEPTH_NV
    = GL.GL_MAX_TRACK_MATRIX_STACK_DEPTH_NV;

  public static final int GL_MAX_TRACK_MATRICES_NV
    = GL.GL_MAX_TRACK_MATRICES_NV;

  public static final int GL_MATRIX0_NV
    = GL.GL_MATRIX0_NV;

  public static final int GL_MATRIX1_NV
    = GL.GL_MATRIX1_NV;

  public static final int GL_MATRIX2_NV
    = GL.GL_MATRIX2_NV;

  public static final int GL_MATRIX3_NV
    = GL.GL_MATRIX3_NV;

  public static final int GL_MATRIX4_NV
    = GL.GL_MATRIX4_NV;

  public static final int GL_MATRIX5_NV
    = GL.GL_MATRIX5_NV;

  public static final int GL_MATRIX6_NV
    = GL.GL_MATRIX6_NV;

  public static final int GL_MATRIX7_NV
    = GL.GL_MATRIX7_NV;

  public static final int GL_CURRENT_MATRIX_STACK_DEPTH_NV
    = GL.GL_CURRENT_MATRIX_STACK_DEPTH_NV;

  public static final int GL_CURRENT_MATRIX_NV
    = GL.GL_CURRENT_MATRIX_NV;

  public static final int GL_VERTEX_PROGRAM_POINT_SIZE_NV
    = GL.GL_VERTEX_PROGRAM_POINT_SIZE_NV;

  public static final int GL_VERTEX_PROGRAM_TWO_SIDE_NV
    = GL.GL_VERTEX_PROGRAM_TWO_SIDE_NV;

  public static final int GL_PROGRAM_PARAMETER_NV
    = GL.GL_PROGRAM_PARAMETER_NV;

  public static final int GL_ATTRIB_ARRAY_POINTER_NV
    = GL.GL_ATTRIB_ARRAY_POINTER_NV;

  public static final int GL_PROGRAM_TARGET_NV
    = GL.GL_PROGRAM_TARGET_NV;

  public static final int GL_PROGRAM_RESIDENT_NV
    = GL.GL_PROGRAM_RESIDENT_NV;

  public static final int GL_TRACK_MATRIX_NV
    = GL.GL_TRACK_MATRIX_NV;

  public static final int GL_TRACK_MATRIX_TRANSFORM_NV
    = GL.GL_TRACK_MATRIX_TRANSFORM_NV;

  public static final int GL_VERTEX_PROGRAM_BINDING_NV
    = GL.GL_VERTEX_PROGRAM_BINDING_NV;

  public static final int GL_PROGRAM_ERROR_POSITION_NV
    = GL.GL_PROGRAM_ERROR_POSITION_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY0_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY0_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY1_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY1_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY2_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY2_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY3_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY3_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY4_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY4_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY5_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY5_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY6_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY6_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY7_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY7_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY8_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY8_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY9_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY9_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY10_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY10_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY11_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY11_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY12_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY12_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY13_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY13_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY14_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY14_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY15_NV
    = GL.GL_VERTEX_ATTRIB_ARRAY15_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB0_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB0_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB1_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB1_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB2_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB2_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB3_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB3_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB4_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB4_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB5_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB5_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB6_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB6_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB7_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB7_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB8_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB8_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB9_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB9_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB10_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB10_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB11_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB11_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB12_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB12_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB13_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB13_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB14_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB14_4_NV;

  public static final int GL_MAP1_VERTEX_ATTRIB15_4_NV
    = GL.GL_MAP1_VERTEX_ATTRIB15_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB0_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB0_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB1_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB1_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB2_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB2_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB3_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB3_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB4_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB4_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB5_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB5_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB6_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB6_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB7_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB7_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB8_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB8_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB9_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB9_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB10_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB10_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB11_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB11_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB12_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB12_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB13_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB13_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB14_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB14_4_NV;

  public static final int GL_MAP2_VERTEX_ATTRIB15_4_NV
    = GL.GL_MAP2_VERTEX_ATTRIB15_4_NV;

  public static final int GL_TEXTURE_MAX_CLAMP_S_SGIX
    = GL.GL_TEXTURE_MAX_CLAMP_S_SGIX;

  public static final int GL_TEXTURE_MAX_CLAMP_T_SGIX
    = GL.GL_TEXTURE_MAX_CLAMP_T_SGIX;

  public static final int GL_TEXTURE_MAX_CLAMP_R_SGIX
    = GL.GL_TEXTURE_MAX_CLAMP_R_SGIX;

  public static final int GL_SCALEBIAS_HINT_SGIX
    = GL.GL_SCALEBIAS_HINT_SGIX;

  public static final int GL_INTERLACE_OML
    = GL.GL_INTERLACE_OML;

  public static final int GL_INTERLACE_READ_OML
    = GL.GL_INTERLACE_READ_OML;

  public static final int GL_FORMAT_SUBSAMPLE_24_24_OML
    = GL.GL_FORMAT_SUBSAMPLE_24_24_OML;

  public static final int GL_FORMAT_SUBSAMPLE_244_244_OML
    = GL.GL_FORMAT_SUBSAMPLE_244_244_OML;

  public static final int GL_PACK_RESAMPLE_OML
    = GL.GL_PACK_RESAMPLE_OML;

  public static final int GL_UNPACK_RESAMPLE_OML
    = GL.GL_UNPACK_RESAMPLE_OML;

  public static final int GL_RESAMPLE_REPLICATE_OML
    = GL.GL_RESAMPLE_REPLICATE_OML;

  public static final int GL_RESAMPLE_ZERO_FILL_OML
    = GL.GL_RESAMPLE_ZERO_FILL_OML;

  public static final int GL_RESAMPLE_AVERAGE_OML
    = GL.GL_RESAMPLE_AVERAGE_OML;

  public static final int GL_RESAMPLE_DECIMATE_OML
    = GL.GL_RESAMPLE_DECIMATE_OML;

  public static final int GL_DEPTH_STENCIL_TO_RGBA_NV
    = GL.GL_DEPTH_STENCIL_TO_RGBA_NV;

  public static final int GL_DEPTH_STENCIL_TO_BGRA_NV
    = GL.GL_DEPTH_STENCIL_TO_BGRA_NV;

  public static final int GL_BUMP_ROT_MATRIX_ATI
    = GL.GL_BUMP_ROT_MATRIX_ATI;

  public static final int GL_BUMP_ROT_MATRIX_SIZE_ATI
    = GL.GL_BUMP_ROT_MATRIX_SIZE_ATI;

  public static final int GL_BUMP_NUM_TEX_UNITS_ATI
    = GL.GL_BUMP_NUM_TEX_UNITS_ATI;

  public static final int GL_BUMP_TEX_UNITS_ATI
    = GL.GL_BUMP_TEX_UNITS_ATI;

  public static final int GL_DUDV_ATI
    = GL.GL_DUDV_ATI;

  public static final int GL_DU8DV8_ATI
    = GL.GL_DU8DV8_ATI;

  public static final int GL_BUMP_ENVMAP_ATI
    = GL.GL_BUMP_ENVMAP_ATI;

  public static final int GL_BUMP_TARGET_ATI
    = GL.GL_BUMP_TARGET_ATI;

  public static final int GL_FRAGMENT_SHADER_ATI
    = GL.GL_FRAGMENT_SHADER_ATI;

  public static final int GL_REG_0_ATI
    = GL.GL_REG_0_ATI;

  public static final int GL_REG_1_ATI
    = GL.GL_REG_1_ATI;

  public static final int GL_REG_2_ATI
    = GL.GL_REG_2_ATI;

  public static final int GL_REG_3_ATI
    = GL.GL_REG_3_ATI;

  public static final int GL_REG_4_ATI
    = GL.GL_REG_4_ATI;

  public static final int GL_REG_5_ATI
    = GL.GL_REG_5_ATI;

  public static final int GL_REG_6_ATI
    = GL.GL_REG_6_ATI;

  public static final int GL_REG_7_ATI
    = GL.GL_REG_7_ATI;

  public static final int GL_REG_8_ATI
    = GL.GL_REG_8_ATI;

  public static final int GL_REG_9_ATI
    = GL.GL_REG_9_ATI;

  public static final int GL_REG_10_ATI
    = GL.GL_REG_10_ATI;

  public static final int GL_REG_11_ATI
    = GL.GL_REG_11_ATI;

  public static final int GL_REG_12_ATI
    = GL.GL_REG_12_ATI;

  public static final int GL_REG_13_ATI
    = GL.GL_REG_13_ATI;

  public static final int GL_REG_14_ATI
    = GL.GL_REG_14_ATI;

  public static final int GL_REG_15_ATI
    = GL.GL_REG_15_ATI;

  public static final int GL_REG_16_ATI
    = GL.GL_REG_16_ATI;

  public static final int GL_REG_17_ATI
    = GL.GL_REG_17_ATI;

  public static final int GL_REG_18_ATI
    = GL.GL_REG_18_ATI;

  public static final int GL_REG_19_ATI
    = GL.GL_REG_19_ATI;

  public static final int GL_REG_20_ATI
    = GL.GL_REG_20_ATI;

  public static final int GL_REG_21_ATI
    = GL.GL_REG_21_ATI;

  public static final int GL_REG_22_ATI
    = GL.GL_REG_22_ATI;

  public static final int GL_REG_23_ATI
    = GL.GL_REG_23_ATI;

  public static final int GL_REG_24_ATI
    = GL.GL_REG_24_ATI;

  public static final int GL_REG_25_ATI
    = GL.GL_REG_25_ATI;

  public static final int GL_REG_26_ATI
    = GL.GL_REG_26_ATI;

  public static final int GL_REG_27_ATI
    = GL.GL_REG_27_ATI;

  public static final int GL_REG_28_ATI
    = GL.GL_REG_28_ATI;

  public static final int GL_REG_29_ATI
    = GL.GL_REG_29_ATI;

  public static final int GL_REG_30_ATI
    = GL.GL_REG_30_ATI;

  public static final int GL_REG_31_ATI
    = GL.GL_REG_31_ATI;

  public static final int GL_CON_0_ATI
    = GL.GL_CON_0_ATI;

  public static final int GL_CON_1_ATI
    = GL.GL_CON_1_ATI;

  public static final int GL_CON_2_ATI
    = GL.GL_CON_2_ATI;

  public static final int GL_CON_3_ATI
    = GL.GL_CON_3_ATI;

  public static final int GL_CON_4_ATI
    = GL.GL_CON_4_ATI;

  public static final int GL_CON_5_ATI
    = GL.GL_CON_5_ATI;

  public static final int GL_CON_6_ATI
    = GL.GL_CON_6_ATI;

  public static final int GL_CON_7_ATI
    = GL.GL_CON_7_ATI;

  public static final int GL_CON_8_ATI
    = GL.GL_CON_8_ATI;

  public static final int GL_CON_9_ATI
    = GL.GL_CON_9_ATI;

  public static final int GL_CON_10_ATI
    = GL.GL_CON_10_ATI;

  public static final int GL_CON_11_ATI
    = GL.GL_CON_11_ATI;

  public static final int GL_CON_12_ATI
    = GL.GL_CON_12_ATI;

  public static final int GL_CON_13_ATI
    = GL.GL_CON_13_ATI;

  public static final int GL_CON_14_ATI
    = GL.GL_CON_14_ATI;

  public static final int GL_CON_15_ATI
    = GL.GL_CON_15_ATI;

  public static final int GL_CON_16_ATI
    = GL.GL_CON_16_ATI;

  public static final int GL_CON_17_ATI
    = GL.GL_CON_17_ATI;

  public static final int GL_CON_18_ATI
    = GL.GL_CON_18_ATI;

  public static final int GL_CON_19_ATI
    = GL.GL_CON_19_ATI;

  public static final int GL_CON_20_ATI
    = GL.GL_CON_20_ATI;

  public static final int GL_CON_21_ATI
    = GL.GL_CON_21_ATI;

  public static final int GL_CON_22_ATI
    = GL.GL_CON_22_ATI;

  public static final int GL_CON_23_ATI
    = GL.GL_CON_23_ATI;

  public static final int GL_CON_24_ATI
    = GL.GL_CON_24_ATI;

  public static final int GL_CON_25_ATI
    = GL.GL_CON_25_ATI;

  public static final int GL_CON_26_ATI
    = GL.GL_CON_26_ATI;

  public static final int GL_CON_27_ATI
    = GL.GL_CON_27_ATI;

  public static final int GL_CON_28_ATI
    = GL.GL_CON_28_ATI;

  public static final int GL_CON_29_ATI
    = GL.GL_CON_29_ATI;

  public static final int GL_CON_30_ATI
    = GL.GL_CON_30_ATI;

  public static final int GL_CON_31_ATI
    = GL.GL_CON_31_ATI;

  public static final int GL_MOV_ATI
    = GL.GL_MOV_ATI;

  public static final int GL_ADD_ATI
    = GL.GL_ADD_ATI;

  public static final int GL_MUL_ATI
    = GL.GL_MUL_ATI;

  public static final int GL_SUB_ATI
    = GL.GL_SUB_ATI;

  public static final int GL_DOT3_ATI
    = GL.GL_DOT3_ATI;

  public static final int GL_DOT4_ATI
    = GL.GL_DOT4_ATI;

  public static final int GL_MAD_ATI
    = GL.GL_MAD_ATI;

  public static final int GL_LERP_ATI
    = GL.GL_LERP_ATI;

  public static final int GL_CND_ATI
    = GL.GL_CND_ATI;

  public static final int GL_CND0_ATI
    = GL.GL_CND0_ATI;

  public static final int GL_DOT2_ADD_ATI
    = GL.GL_DOT2_ADD_ATI;

  public static final int GL_SECONDARY_INTERPOLATOR_ATI
    = GL.GL_SECONDARY_INTERPOLATOR_ATI;

  public static final int GL_NUM_FRAGMENT_REGISTERS_ATI
    = GL.GL_NUM_FRAGMENT_REGISTERS_ATI;

  public static final int GL_NUM_FRAGMENT_CONSTANTS_ATI
    = GL.GL_NUM_FRAGMENT_CONSTANTS_ATI;

  public static final int GL_NUM_PASSES_ATI
    = GL.GL_NUM_PASSES_ATI;

  public static final int GL_NUM_INSTRUCTIONS_PER_PASS_ATI
    = GL.GL_NUM_INSTRUCTIONS_PER_PASS_ATI;

  public static final int GL_NUM_INSTRUCTIONS_TOTAL_ATI
    = GL.GL_NUM_INSTRUCTIONS_TOTAL_ATI;

  public static final int GL_NUM_INPUT_INTERPOLATOR_COMPONENTS_ATI
    = GL.GL_NUM_INPUT_INTERPOLATOR_COMPONENTS_ATI;

  public static final int GL_NUM_LOOPBACK_COMPONENTS_ATI
    = GL.GL_NUM_LOOPBACK_COMPONENTS_ATI;

  public static final int GL_COLOR_ALPHA_PAIRING_ATI
    = GL.GL_COLOR_ALPHA_PAIRING_ATI;

  public static final int GL_SWIZZLE_STR_ATI
    = GL.GL_SWIZZLE_STR_ATI;

  public static final int GL_SWIZZLE_STQ_ATI
    = GL.GL_SWIZZLE_STQ_ATI;

  public static final int GL_SWIZZLE_STR_DR_ATI
    = GL.GL_SWIZZLE_STR_DR_ATI;

  public static final int GL_SWIZZLE_STQ_DQ_ATI
    = GL.GL_SWIZZLE_STQ_DQ_ATI;

  public static final int GL_SWIZZLE_STRQ_ATI
    = GL.GL_SWIZZLE_STRQ_ATI;

  public static final int GL_SWIZZLE_STRQ_DQ_ATI
    = GL.GL_SWIZZLE_STRQ_DQ_ATI;

  public static final int GL_RED_BIT_ATI
    = GL.GL_RED_BIT_ATI;

  public static final int GL_GREEN_BIT_ATI
    = GL.GL_GREEN_BIT_ATI;

  public static final int GL_BLUE_BIT_ATI
    = GL.GL_BLUE_BIT_ATI;

  public static final int GL_2X_BIT_ATI
    = GL.GL_2X_BIT_ATI;

  public static final int GL_4X_BIT_ATI
    = GL.GL_4X_BIT_ATI;

  public static final int GL_8X_BIT_ATI
    = GL.GL_8X_BIT_ATI;

  public static final int GL_HALF_BIT_ATI
    = GL.GL_HALF_BIT_ATI;

  public static final int GL_QUARTER_BIT_ATI
    = GL.GL_QUARTER_BIT_ATI;

  public static final int GL_EIGHTH_BIT_ATI
    = GL.GL_EIGHTH_BIT_ATI;

  public static final int GL_SATURATE_BIT_ATI
    = GL.GL_SATURATE_BIT_ATI;

  public static final int GL_COMP_BIT_ATI
    = GL.GL_COMP_BIT_ATI;

  public static final int GL_NEGATE_BIT_ATI
    = GL.GL_NEGATE_BIT_ATI;

  public static final int GL_BIAS_BIT_ATI
    = GL.GL_BIAS_BIT_ATI;

  public static final int GL_PN_TRIANGLES_ATI
    = GL.GL_PN_TRIANGLES_ATI;

  public static final int GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI
    = GL.GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI;

  public static final int GL_PN_TRIANGLES_POINT_MODE_ATI
    = GL.GL_PN_TRIANGLES_POINT_MODE_ATI;

  public static final int GL_PN_TRIANGLES_NORMAL_MODE_ATI
    = GL.GL_PN_TRIANGLES_NORMAL_MODE_ATI;

  public static final int GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI
    = GL.GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI;

  public static final int GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI
    = GL.GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI;

  public static final int GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI
    = GL.GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI;

  public static final int GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI
    = GL.GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI;

  public static final int GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI
    = GL.GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI;

  public static final int GL_STATIC_ATI
    = GL.GL_STATIC_ATI;

  public static final int GL_DYNAMIC_ATI
    = GL.GL_DYNAMIC_ATI;

  public static final int GL_PRESERVE_ATI
    = GL.GL_PRESERVE_ATI;

  public static final int GL_DISCARD_ATI
    = GL.GL_DISCARD_ATI;

  public static final int GL_OBJECT_BUFFER_SIZE_ATI
    = GL.GL_OBJECT_BUFFER_SIZE_ATI;

  public static final int GL_OBJECT_BUFFER_USAGE_ATI
    = GL.GL_OBJECT_BUFFER_USAGE_ATI;

  public static final int GL_ARRAY_OBJECT_BUFFER_ATI
    = GL.GL_ARRAY_OBJECT_BUFFER_ATI;

  public static final int GL_ARRAY_OBJECT_OFFSET_ATI
    = GL.GL_ARRAY_OBJECT_OFFSET_ATI;

  public static final int GL_VERTEX_SHADER_EXT
    = GL.GL_VERTEX_SHADER_EXT;

  public static final int GL_VERTEX_SHADER_BINDING_EXT
    = GL.GL_VERTEX_SHADER_BINDING_EXT;

  public static final int GL_OP_INDEX_EXT
    = GL.GL_OP_INDEX_EXT;

  public static final int GL_OP_NEGATE_EXT
    = GL.GL_OP_NEGATE_EXT;

  public static final int GL_OP_DOT3_EXT
    = GL.GL_OP_DOT3_EXT;

  public static final int GL_OP_DOT4_EXT
    = GL.GL_OP_DOT4_EXT;

  public static final int GL_OP_MUL_EXT
    = GL.GL_OP_MUL_EXT;

  public static final int GL_OP_ADD_EXT
    = GL.GL_OP_ADD_EXT;

  public static final int GL_OP_MADD_EXT
    = GL.GL_OP_MADD_EXT;

  public static final int GL_OP_FRAC_EXT
    = GL.GL_OP_FRAC_EXT;

  public static final int GL_OP_MAX_EXT
    = GL.GL_OP_MAX_EXT;

  public static final int GL_OP_MIN_EXT
    = GL.GL_OP_MIN_EXT;

  public static final int GL_OP_SET_GE_EXT
    = GL.GL_OP_SET_GE_EXT;

  public static final int GL_OP_SET_LT_EXT
    = GL.GL_OP_SET_LT_EXT;

  public static final int GL_OP_CLAMP_EXT
    = GL.GL_OP_CLAMP_EXT;

  public static final int GL_OP_FLOOR_EXT
    = GL.GL_OP_FLOOR_EXT;

  public static final int GL_OP_ROUND_EXT
    = GL.GL_OP_ROUND_EXT;

  public static final int GL_OP_EXP_BASE_2_EXT
    = GL.GL_OP_EXP_BASE_2_EXT;

  public static final int GL_OP_LOG_BASE_2_EXT
    = GL.GL_OP_LOG_BASE_2_EXT;

  public static final int GL_OP_POWER_EXT
    = GL.GL_OP_POWER_EXT;

  public static final int GL_OP_RECIP_EXT
    = GL.GL_OP_RECIP_EXT;

  public static final int GL_OP_RECIP_SQRT_EXT
    = GL.GL_OP_RECIP_SQRT_EXT;

  public static final int GL_OP_SUB_EXT
    = GL.GL_OP_SUB_EXT;

  public static final int GL_OP_CROSS_PRODUCT_EXT
    = GL.GL_OP_CROSS_PRODUCT_EXT;

  public static final int GL_OP_MULTIPLY_MATRIX_EXT
    = GL.GL_OP_MULTIPLY_MATRIX_EXT;

  public static final int GL_OP_MOV_EXT
    = GL.GL_OP_MOV_EXT;

  public static final int GL_OUTPUT_VERTEX_EXT
    = GL.GL_OUTPUT_VERTEX_EXT;

  public static final int GL_OUTPUT_COLOR0_EXT
    = GL.GL_OUTPUT_COLOR0_EXT;

  public static final int GL_OUTPUT_COLOR1_EXT
    = GL.GL_OUTPUT_COLOR1_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD0_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD0_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD1_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD1_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD2_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD2_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD3_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD3_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD4_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD4_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD5_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD5_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD6_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD6_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD7_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD7_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD8_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD8_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD9_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD9_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD10_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD10_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD11_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD11_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD12_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD12_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD13_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD13_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD14_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD14_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD15_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD15_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD16_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD16_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD17_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD17_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD18_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD18_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD19_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD19_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD20_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD20_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD21_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD21_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD22_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD22_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD23_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD23_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD24_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD24_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD25_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD25_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD26_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD26_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD27_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD27_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD28_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD28_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD29_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD29_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD30_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD30_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD31_EXT
    = GL.GL_OUTPUT_TEXTURE_COORD31_EXT;

  public static final int GL_OUTPUT_FOG_EXT
    = GL.GL_OUTPUT_FOG_EXT;

  public static final int GL_SCALAR_EXT
    = GL.GL_SCALAR_EXT;

  public static final int GL_VECTOR_EXT
    = GL.GL_VECTOR_EXT;

  public static final int GL_MATRIX_EXT
    = GL.GL_MATRIX_EXT;

  public static final int GL_VARIANT_EXT
    = GL.GL_VARIANT_EXT;

  public static final int GL_INVARIANT_EXT
    = GL.GL_INVARIANT_EXT;

  public static final int GL_LOCAL_CONSTANT_EXT
    = GL.GL_LOCAL_CONSTANT_EXT;

  public static final int GL_LOCAL_EXT
    = GL.GL_LOCAL_EXT;

  public static final int GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT
    = GL.GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_VARIANTS_EXT
    = GL.GL_MAX_VERTEX_SHADER_VARIANTS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_INVARIANTS_EXT
    = GL.GL_MAX_VERTEX_SHADER_INVARIANTS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT
    = GL.GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_LOCALS_EXT
    = GL.GL_MAX_VERTEX_SHADER_LOCALS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT
    = GL.GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT
    = GL.GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT
    = GL.GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT
    = GL.GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT
    = GL.GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT;

  public static final int GL_VERTEX_SHADER_INSTRUCTIONS_EXT
    = GL.GL_VERTEX_SHADER_INSTRUCTIONS_EXT;

  public static final int GL_VERTEX_SHADER_VARIANTS_EXT
    = GL.GL_VERTEX_SHADER_VARIANTS_EXT;

  public static final int GL_VERTEX_SHADER_INVARIANTS_EXT
    = GL.GL_VERTEX_SHADER_INVARIANTS_EXT;

  public static final int GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT
    = GL.GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT;

  public static final int GL_VERTEX_SHADER_LOCALS_EXT
    = GL.GL_VERTEX_SHADER_LOCALS_EXT;

  public static final int GL_VERTEX_SHADER_OPTIMIZED_EXT
    = GL.GL_VERTEX_SHADER_OPTIMIZED_EXT;

  public static final int GL_X_EXT
    = GL.GL_X_EXT;

  public static final int GL_Y_EXT
    = GL.GL_Y_EXT;

  public static final int GL_Z_EXT
    = GL.GL_Z_EXT;

  public static final int GL_W_EXT
    = GL.GL_W_EXT;

  public static final int GL_NEGATIVE_X_EXT
    = GL.GL_NEGATIVE_X_EXT;

  public static final int GL_NEGATIVE_Y_EXT
    = GL.GL_NEGATIVE_Y_EXT;

  public static final int GL_NEGATIVE_Z_EXT
    = GL.GL_NEGATIVE_Z_EXT;

  public static final int GL_NEGATIVE_W_EXT
    = GL.GL_NEGATIVE_W_EXT;

  public static final int GL_ZERO_EXT
    = GL.GL_ZERO_EXT;

  public static final int GL_ONE_EXT
    = GL.GL_ONE_EXT;

  public static final int GL_NEGATIVE_ONE_EXT
    = GL.GL_NEGATIVE_ONE_EXT;

  public static final int GL_NORMALIZED_RANGE_EXT
    = GL.GL_NORMALIZED_RANGE_EXT;

  public static final int GL_FULL_RANGE_EXT
    = GL.GL_FULL_RANGE_EXT;

  public static final int GL_CURRENT_VERTEX_EXT
    = GL.GL_CURRENT_VERTEX_EXT;

  public static final int GL_MVP_MATRIX_EXT
    = GL.GL_MVP_MATRIX_EXT;

  public static final int GL_VARIANT_VALUE_EXT
    = GL.GL_VARIANT_VALUE_EXT;

  public static final int GL_VARIANT_DATATYPE_EXT
    = GL.GL_VARIANT_DATATYPE_EXT;

  public static final int GL_VARIANT_ARRAY_STRIDE_EXT
    = GL.GL_VARIANT_ARRAY_STRIDE_EXT;

  public static final int GL_VARIANT_ARRAY_TYPE_EXT
    = GL.GL_VARIANT_ARRAY_TYPE_EXT;

  public static final int GL_VARIANT_ARRAY_EXT
    = GL.GL_VARIANT_ARRAY_EXT;

  public static final int GL_VARIANT_ARRAY_POINTER_EXT
    = GL.GL_VARIANT_ARRAY_POINTER_EXT;

  public static final int GL_INVARIANT_VALUE_EXT
    = GL.GL_INVARIANT_VALUE_EXT;

  public static final int GL_INVARIANT_DATATYPE_EXT
    = GL.GL_INVARIANT_DATATYPE_EXT;

  public static final int GL_LOCAL_CONSTANT_VALUE_EXT
    = GL.GL_LOCAL_CONSTANT_VALUE_EXT;

  public static final int GL_LOCAL_CONSTANT_DATATYPE_EXT
    = GL.GL_LOCAL_CONSTANT_DATATYPE_EXT;

  public static final int GL_TEXTURE_RECTANGLE_EXT
    = GL.GL_TEXTURE_RECTANGLE_EXT;

  public static final int GL_TEXTURE_BINDING_RECTANGLE_EXT
    = GL.GL_TEXTURE_BINDING_RECTANGLE_EXT;

  public static final int GL_PROXY_TEXTURE_RECTANGLE_EXT
    = GL.GL_PROXY_TEXTURE_RECTANGLE_EXT;

  public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE_EXT
    = GL.GL_MAX_RECTANGLE_TEXTURE_SIZE_EXT;

  public static final int GL_MAX_VERTEX_STREAMS_ATI
    = GL.GL_MAX_VERTEX_STREAMS_ATI;

  public static final int GL_VERTEX_STREAM0_ATI
    = GL.GL_VERTEX_STREAM0_ATI;

  public static final int GL_VERTEX_STREAM1_ATI
    = GL.GL_VERTEX_STREAM1_ATI;

  public static final int GL_VERTEX_STREAM2_ATI
    = GL.GL_VERTEX_STREAM2_ATI;

  public static final int GL_VERTEX_STREAM3_ATI
    = GL.GL_VERTEX_STREAM3_ATI;

  public static final int GL_VERTEX_STREAM4_ATI
    = GL.GL_VERTEX_STREAM4_ATI;

  public static final int GL_VERTEX_STREAM5_ATI
    = GL.GL_VERTEX_STREAM5_ATI;

  public static final int GL_VERTEX_STREAM6_ATI
    = GL.GL_VERTEX_STREAM6_ATI;

  public static final int GL_VERTEX_STREAM7_ATI
    = GL.GL_VERTEX_STREAM7_ATI;

  public static final int GL_VERTEX_SOURCE_ATI
    = GL.GL_VERTEX_SOURCE_ATI;

  public static final int GL_ELEMENT_ARRAY_ATI
    = GL.GL_ELEMENT_ARRAY_ATI;

  public static final int GL_ELEMENT_ARRAY_TYPE_ATI
    = GL.GL_ELEMENT_ARRAY_TYPE_ATI;

  public static final int GL_ELEMENT_ARRAY_POINTER_ATI
    = GL.GL_ELEMENT_ARRAY_POINTER_ATI;

  public static final int GL_QUAD_MESH_SUN
    = GL.GL_QUAD_MESH_SUN;

  public static final int GL_TRIANGLE_MESH_SUN
    = GL.GL_TRIANGLE_MESH_SUN;

  public static final int GL_SLICE_ACCUM_SUN
    = GL.GL_SLICE_ACCUM_SUN;

  public static final int GL_MULTISAMPLE_FILTER_HINT_NV
    = GL.GL_MULTISAMPLE_FILTER_HINT_NV;

  public static final int GL_DEPTH_CLAMP_NV
    = GL.GL_DEPTH_CLAMP_NV;

  public static final int GL_PIXEL_COUNTER_BITS_NV
    = GL.GL_PIXEL_COUNTER_BITS_NV;

  public static final int GL_CURRENT_OCCLUSION_QUERY_ID_NV
    = GL.GL_CURRENT_OCCLUSION_QUERY_ID_NV;

  public static final int GL_PIXEL_COUNT_NV
    = GL.GL_PIXEL_COUNT_NV;

  public static final int GL_PIXEL_COUNT_AVAILABLE_NV
    = GL.GL_PIXEL_COUNT_AVAILABLE_NV;

  public static final int GL_POINT_SPRITE_NV
    = GL.GL_POINT_SPRITE_NV;

  public static final int GL_COORD_REPLACE_NV
    = GL.GL_COORD_REPLACE_NV;

  public static final int GL_POINT_SPRITE_R_MODE_NV
    = GL.GL_POINT_SPRITE_R_MODE_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_2D_NV
    = GL.GL_OFFSET_PROJECTIVE_TEXTURE_2D_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_2D_SCALE_NV
    = GL.GL_OFFSET_PROJECTIVE_TEXTURE_2D_SCALE_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_NV
    = GL.GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_SCALE_NV
    = GL.GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_SCALE_NV;

  public static final int GL_OFFSET_HILO_TEXTURE_2D_NV
    = GL.GL_OFFSET_HILO_TEXTURE_2D_NV;

  public static final int GL_OFFSET_HILO_TEXTURE_RECTANGLE_NV
    = GL.GL_OFFSET_HILO_TEXTURE_RECTANGLE_NV;

  public static final int GL_OFFSET_HILO_PROJECTIVE_TEXTURE_2D_NV
    = GL.GL_OFFSET_HILO_PROJECTIVE_TEXTURE_2D_NV;

  public static final int GL_OFFSET_HILO_PROJECTIVE_TEXTURE_RECTANGLE_NV
    = GL.GL_OFFSET_HILO_PROJECTIVE_TEXTURE_RECTANGLE_NV;

  public static final int GL_DEPENDENT_HILO_TEXTURE_2D_NV
    = GL.GL_DEPENDENT_HILO_TEXTURE_2D_NV;

  public static final int GL_DEPENDENT_RGB_TEXTURE_3D_NV
    = GL.GL_DEPENDENT_RGB_TEXTURE_3D_NV;

  public static final int GL_DEPENDENT_RGB_TEXTURE_CUBE_MAP_NV
    = GL.GL_DEPENDENT_RGB_TEXTURE_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_PASS_THROUGH_NV
    = GL.GL_DOT_PRODUCT_PASS_THROUGH_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_1D_NV
    = GL.GL_DOT_PRODUCT_TEXTURE_1D_NV;

  public static final int GL_DOT_PRODUCT_AFFINE_DEPTH_REPLACE_NV
    = GL.GL_DOT_PRODUCT_AFFINE_DEPTH_REPLACE_NV;

  public static final int GL_HILO8_NV
    = GL.GL_HILO8_NV;

  public static final int GL_SIGNED_HILO8_NV
    = GL.GL_SIGNED_HILO8_NV;

  public static final int GL_FORCE_BLUE_TO_ONE_NV
    = GL.GL_FORCE_BLUE_TO_ONE_NV;

  public static final int GL_STENCIL_TEST_TWO_SIDE_EXT
    = GL.GL_STENCIL_TEST_TWO_SIDE_EXT;

  public static final int GL_ACTIVE_STENCIL_FACE_EXT
    = GL.GL_ACTIVE_STENCIL_FACE_EXT;

  public static final int GL_TEXT_FRAGMENT_SHADER_ATI
    = GL.GL_TEXT_FRAGMENT_SHADER_ATI;

  public static final int GL_UNPACK_CLIENT_STORAGE_APPLE
    = GL.GL_UNPACK_CLIENT_STORAGE_APPLE;

  public static final int GL_ELEMENT_ARRAY_APPLE
    = GL.GL_ELEMENT_ARRAY_APPLE;

  public static final int GL_ELEMENT_ARRAY_TYPE_APPLE
    = GL.GL_ELEMENT_ARRAY_TYPE_APPLE;

  public static final int GL_ELEMENT_ARRAY_POINTER_APPLE
    = GL.GL_ELEMENT_ARRAY_POINTER_APPLE;

  public static final int GL_DRAW_PIXELS_APPLE
    = GL.GL_DRAW_PIXELS_APPLE;

  public static final int GL_FENCE_APPLE
    = GL.GL_FENCE_APPLE;

  public static final int GL_VERTEX_ARRAY_BINDING_APPLE
    = GL.GL_VERTEX_ARRAY_BINDING_APPLE;

  public static final int GL_VERTEX_ARRAY_RANGE_APPLE
    = GL.GL_VERTEX_ARRAY_RANGE_APPLE;

  public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_APPLE
    = GL.GL_VERTEX_ARRAY_RANGE_LENGTH_APPLE;

  public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_APPLE
    = GL.GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_APPLE;

  public static final int GL_VERTEX_ARRAY_RANGE_POINTER_APPLE
    = GL.GL_VERTEX_ARRAY_RANGE_POINTER_APPLE;

  public static final int GL_VERTEX_ARRAY_STORAGE_HINT_APPLE
    = GL.GL_VERTEX_ARRAY_STORAGE_HINT_APPLE;

  public static final int GL_STORAGE_PRIVATE_APPLE
    = GL.GL_STORAGE_PRIVATE_APPLE;

  public static final int GL_STORAGE_CACHED_APPLE
    = GL.GL_STORAGE_CACHED_APPLE;

  public static final int GL_STORAGE_SHARED_APPLE
    = GL.GL_STORAGE_SHARED_APPLE;

  public static final int GL_YCBCR_422_APPLE
    = GL.GL_YCBCR_422_APPLE;

  public static final int GL_UNSIGNED_SHORT_8_8_APPLE
    = GL.GL_UNSIGNED_SHORT_8_8_APPLE;

  public static final int GL_UNSIGNED_SHORT_8_8_REV_APPLE
    = GL.GL_UNSIGNED_SHORT_8_8_REV_APPLE;

  public static final int GL_RGB_S3TC
    = GL.GL_RGB_S3TC;

  public static final int GL_RGB4_S3TC
    = GL.GL_RGB4_S3TC;

  public static final int GL_RGBA_S3TC
    = GL.GL_RGBA_S3TC;

  public static final int GL_RGBA4_S3TC
    = GL.GL_RGBA4_S3TC;

  public static final int GL_MAX_DRAW_BUFFERS_ATI
    = GL.GL_MAX_DRAW_BUFFERS_ATI;

  public static final int GL_DRAW_BUFFER0_ATI
    = GL.GL_DRAW_BUFFER0_ATI;

  public static final int GL_DRAW_BUFFER1_ATI
    = GL.GL_DRAW_BUFFER1_ATI;

  public static final int GL_DRAW_BUFFER2_ATI
    = GL.GL_DRAW_BUFFER2_ATI;

  public static final int GL_DRAW_BUFFER3_ATI
    = GL.GL_DRAW_BUFFER3_ATI;

  public static final int GL_DRAW_BUFFER4_ATI
    = GL.GL_DRAW_BUFFER4_ATI;

  public static final int GL_DRAW_BUFFER5_ATI
    = GL.GL_DRAW_BUFFER5_ATI;

  public static final int GL_DRAW_BUFFER6_ATI
    = GL.GL_DRAW_BUFFER6_ATI;

  public static final int GL_DRAW_BUFFER7_ATI
    = GL.GL_DRAW_BUFFER7_ATI;

  public static final int GL_DRAW_BUFFER8_ATI
    = GL.GL_DRAW_BUFFER8_ATI;

  public static final int GL_DRAW_BUFFER9_ATI
    = GL.GL_DRAW_BUFFER9_ATI;

  public static final int GL_DRAW_BUFFER10_ATI
    = GL.GL_DRAW_BUFFER10_ATI;

  public static final int GL_DRAW_BUFFER11_ATI
    = GL.GL_DRAW_BUFFER11_ATI;

  public static final int GL_DRAW_BUFFER12_ATI
    = GL.GL_DRAW_BUFFER12_ATI;

  public static final int GL_DRAW_BUFFER13_ATI
    = GL.GL_DRAW_BUFFER13_ATI;

  public static final int GL_DRAW_BUFFER14_ATI
    = GL.GL_DRAW_BUFFER14_ATI;

  public static final int GL_DRAW_BUFFER15_ATI
    = GL.GL_DRAW_BUFFER15_ATI;

  public static final int GL_TYPE_RGBA_FLOAT_ATI
    = GL.GL_TYPE_RGBA_FLOAT_ATI;

  public static final int GL_COLOR_CLEAR_UNCLAMPED_VALUE_ATI
    = GL.GL_COLOR_CLEAR_UNCLAMPED_VALUE_ATI;

  public static final int GL_MODULATE_ADD_ATI
    = GL.GL_MODULATE_ADD_ATI;

  public static final int GL_MODULATE_SIGNED_ADD_ATI
    = GL.GL_MODULATE_SIGNED_ADD_ATI;

  public static final int GL_MODULATE_SUBTRACT_ATI
    = GL.GL_MODULATE_SUBTRACT_ATI;

  public static final int GL_RGBA_FLOAT32_ATI
    = GL.GL_RGBA_FLOAT32_ATI;

  public static final int GL_RGB_FLOAT32_ATI
    = GL.GL_RGB_FLOAT32_ATI;

  public static final int GL_ALPHA_FLOAT32_ATI
    = GL.GL_ALPHA_FLOAT32_ATI;

  public static final int GL_INTENSITY_FLOAT32_ATI
    = GL.GL_INTENSITY_FLOAT32_ATI;

  public static final int GL_LUMINANCE_FLOAT32_ATI
    = GL.GL_LUMINANCE_FLOAT32_ATI;

  public static final int GL_LUMINANCE_ALPHA_FLOAT32_ATI
    = GL.GL_LUMINANCE_ALPHA_FLOAT32_ATI;

  public static final int GL_RGBA_FLOAT16_ATI
    = GL.GL_RGBA_FLOAT16_ATI;

  public static final int GL_RGB_FLOAT16_ATI
    = GL.GL_RGB_FLOAT16_ATI;

  public static final int GL_ALPHA_FLOAT16_ATI
    = GL.GL_ALPHA_FLOAT16_ATI;

  public static final int GL_INTENSITY_FLOAT16_ATI
    = GL.GL_INTENSITY_FLOAT16_ATI;

  public static final int GL_LUMINANCE_FLOAT16_ATI
    = GL.GL_LUMINANCE_FLOAT16_ATI;

  public static final int GL_LUMINANCE_ALPHA_FLOAT16_ATI
    = GL.GL_LUMINANCE_ALPHA_FLOAT16_ATI;

  public static final int GL_FLOAT_R_NV
    = GL.GL_FLOAT_R_NV;

  public static final int GL_FLOAT_RG_NV
    = GL.GL_FLOAT_RG_NV;

  public static final int GL_FLOAT_RGB_NV
    = GL.GL_FLOAT_RGB_NV;

  public static final int GL_FLOAT_RGBA_NV
    = GL.GL_FLOAT_RGBA_NV;

  public static final int GL_FLOAT_R16_NV
    = GL.GL_FLOAT_R16_NV;

  public static final int GL_FLOAT_R32_NV
    = GL.GL_FLOAT_R32_NV;

  public static final int GL_FLOAT_RG16_NV
    = GL.GL_FLOAT_RG16_NV;

  public static final int GL_FLOAT_RG32_NV
    = GL.GL_FLOAT_RG32_NV;

  public static final int GL_FLOAT_RGB16_NV
    = GL.GL_FLOAT_RGB16_NV;

  public static final int GL_FLOAT_RGB32_NV
    = GL.GL_FLOAT_RGB32_NV;

  public static final int GL_FLOAT_RGBA16_NV
    = GL.GL_FLOAT_RGBA16_NV;

  public static final int GL_FLOAT_RGBA32_NV
    = GL.GL_FLOAT_RGBA32_NV;

  public static final int GL_TEXTURE_FLOAT_COMPONENTS_NV
    = GL.GL_TEXTURE_FLOAT_COMPONENTS_NV;

  public static final int GL_FLOAT_CLEAR_COLOR_VALUE_NV
    = GL.GL_FLOAT_CLEAR_COLOR_VALUE_NV;

  public static final int GL_FLOAT_RGBA_MODE_NV
    = GL.GL_FLOAT_RGBA_MODE_NV;

  public static final int GL_MAX_FRAGMENT_PROGRAM_LOCAL_PARAMETERS_NV
    = GL.GL_MAX_FRAGMENT_PROGRAM_LOCAL_PARAMETERS_NV;

  public static final int GL_FRAGMENT_PROGRAM_NV
    = GL.GL_FRAGMENT_PROGRAM_NV;

  public static final int GL_MAX_TEXTURE_COORDS_NV
    = GL.GL_MAX_TEXTURE_COORDS_NV;

  public static final int GL_MAX_TEXTURE_IMAGE_UNITS_NV
    = GL.GL_MAX_TEXTURE_IMAGE_UNITS_NV;

  public static final int GL_FRAGMENT_PROGRAM_BINDING_NV
    = GL.GL_FRAGMENT_PROGRAM_BINDING_NV;

  public static final int GL_PROGRAM_ERROR_STRING_NV
    = GL.GL_PROGRAM_ERROR_STRING_NV;

  public static final int GL_HALF_FLOAT_NV
    = GL.GL_HALF_FLOAT_NV;

  public static final int GL_WRITE_PIXEL_DATA_RANGE_NV
    = GL.GL_WRITE_PIXEL_DATA_RANGE_NV;

  public static final int GL_READ_PIXEL_DATA_RANGE_NV
    = GL.GL_READ_PIXEL_DATA_RANGE_NV;

  public static final int GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV
    = GL.GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV;

  public static final int GL_READ_PIXEL_DATA_RANGE_LENGTH_NV
    = GL.GL_READ_PIXEL_DATA_RANGE_LENGTH_NV;

  public static final int GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV
    = GL.GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV;

  public static final int GL_READ_PIXEL_DATA_RANGE_POINTER_NV
    = GL.GL_READ_PIXEL_DATA_RANGE_POINTER_NV;

  public static final int GL_PRIMITIVE_RESTART_NV
    = GL.GL_PRIMITIVE_RESTART_NV;

  public static final int GL_PRIMITIVE_RESTART_INDEX_NV
    = GL.GL_PRIMITIVE_RESTART_INDEX_NV;

  public static final int GL_TEXTURE_UNSIGNED_REMAP_MODE_NV
    = GL.GL_TEXTURE_UNSIGNED_REMAP_MODE_NV;

  public static final int GL_STENCIL_BACK_FUNC_ATI
    = GL.GL_STENCIL_BACK_FUNC_ATI;

  public static final int GL_STENCIL_BACK_FAIL_ATI
    = GL.GL_STENCIL_BACK_FAIL_ATI;

  public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI
    = GL.GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI;

  public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI
    = GL.GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI;

  public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE_OES
    = GL.GL_IMPLEMENTATION_COLOR_READ_TYPE_OES;

  public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT_OES
    = GL.GL_IMPLEMENTATION_COLOR_READ_FORMAT_OES;

  public static final int GL_DEPTH_BOUNDS_TEST_EXT
    = GL.GL_DEPTH_BOUNDS_TEST_EXT;

  public static final int GL_DEPTH_BOUNDS_EXT
    = GL.GL_DEPTH_BOUNDS_EXT;

  public static final int GL_MIRROR_CLAMP_EXT
    = GL.GL_MIRROR_CLAMP_EXT;

  public static final int GL_MIRROR_CLAMP_TO_EDGE_EXT
    = GL.GL_MIRROR_CLAMP_TO_EDGE_EXT;

  public static final int GL_MIRROR_CLAMP_TO_BORDER_EXT
    = GL.GL_MIRROR_CLAMP_TO_BORDER_EXT;

  public static final int GL_BLEND_EQUATION_RGB_EXT
    = GL.GL_BLEND_EQUATION_RGB_EXT;

  public static final int GL_BLEND_EQUATION_ALPHA_EXT
    = GL.GL_BLEND_EQUATION_ALPHA_EXT;

  public static final int GL_PACK_INVERT_MESA
    = GL.GL_PACK_INVERT_MESA;

  public static final int GL_UNSIGNED_SHORT_8_8_MESA
    = GL.GL_UNSIGNED_SHORT_8_8_MESA;

  public static final int GL_UNSIGNED_SHORT_8_8_REV_MESA
    = GL.GL_UNSIGNED_SHORT_8_8_REV_MESA;

  public static final int GL_YCBCR_MESA
    = GL.GL_YCBCR_MESA;

  public static final int GL_PIXEL_PACK_BUFFER_EXT
    = GL.GL_PIXEL_PACK_BUFFER_EXT;

  public static final int GL_PIXEL_UNPACK_BUFFER_EXT
    = GL.GL_PIXEL_UNPACK_BUFFER_EXT;

  public static final int GL_PIXEL_PACK_BUFFER_BINDING_EXT
    = GL.GL_PIXEL_PACK_BUFFER_BINDING_EXT;

  public static final int GL_PIXEL_UNPACK_BUFFER_BINDING_EXT
    = GL.GL_PIXEL_UNPACK_BUFFER_BINDING_EXT;

  public static final int GL_MAX_PROGRAM_EXEC_INSTRUCTIONS_NV
    = GL.GL_MAX_PROGRAM_EXEC_INSTRUCTIONS_NV;

  public static final int GL_MAX_PROGRAM_CALL_DEPTH_NV
    = GL.GL_MAX_PROGRAM_CALL_DEPTH_NV;

  public static final int GL_MAX_PROGRAM_IF_DEPTH_NV
    = GL.GL_MAX_PROGRAM_IF_DEPTH_NV;

  public static final int GL_MAX_PROGRAM_LOOP_DEPTH_NV
    = GL.GL_MAX_PROGRAM_LOOP_DEPTH_NV;

  public static final int GL_MAX_PROGRAM_LOOP_COUNT_NV
    = GL.GL_MAX_PROGRAM_LOOP_COUNT_NV;

  public static final int GL_INVALID_FRAMEBUFFER_OPERATION_EXT
    = GL.GL_INVALID_FRAMEBUFFER_OPERATION_EXT;

  public static final int GL_MAX_RENDERBUFFER_SIZE_EXT
    = GL.GL_MAX_RENDERBUFFER_SIZE_EXT;

  public static final int GL_FRAMEBUFFER_BINDING_EXT
    = GL.GL_FRAMEBUFFER_BINDING_EXT;

  public static final int GL_RENDERBUFFER_BINDING_EXT
    = GL.GL_RENDERBUFFER_BINDING_EXT;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT
    = GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT
    = GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT
    = GL.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT
    = GL.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_EXT
    = GL.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_EXT;

  public static final int GL_FRAMEBUFFER_COMPLETE_EXT
    = GL.GL_FRAMEBUFFER_COMPLETE_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENTS_EXT
    = GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENTS_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT
    = GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_DUPLICATE_ATTACHMENT_EXT
    = GL.GL_FRAMEBUFFER_INCOMPLETE_DUPLICATE_ATTACHMENT_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT
    = GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT
    = GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT
    = GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT
    = GL.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT;

  public static final int GL_FRAMEBUFFER_UNSUPPORTED_EXT
    = GL.GL_FRAMEBUFFER_UNSUPPORTED_EXT;

  public static final int GL_FRAMEBUFFER_STATUS_ERROR_EXT
    = GL.GL_FRAMEBUFFER_STATUS_ERROR_EXT;

  public static final int GL_MAX_COLOR_ATTACHMENTS_EXT
    = GL.GL_MAX_COLOR_ATTACHMENTS_EXT;

  public static final int GL_COLOR_ATTACHMENT0_EXT
    = GL.GL_COLOR_ATTACHMENT0_EXT;

  public static final int GL_COLOR_ATTACHMENT1_EXT
    = GL.GL_COLOR_ATTACHMENT1_EXT;

  public static final int GL_COLOR_ATTACHMENT2_EXT
    = GL.GL_COLOR_ATTACHMENT2_EXT;

  public static final int GL_COLOR_ATTACHMENT3_EXT
    = GL.GL_COLOR_ATTACHMENT3_EXT;

  public static final int GL_COLOR_ATTACHMENT4_EXT
    = GL.GL_COLOR_ATTACHMENT4_EXT;

  public static final int GL_COLOR_ATTACHMENT5_EXT
    = GL.GL_COLOR_ATTACHMENT5_EXT;

  public static final int GL_COLOR_ATTACHMENT6_EXT
    = GL.GL_COLOR_ATTACHMENT6_EXT;

  public static final int GL_COLOR_ATTACHMENT7_EXT
    = GL.GL_COLOR_ATTACHMENT7_EXT;

  public static final int GL_COLOR_ATTACHMENT8_EXT
    = GL.GL_COLOR_ATTACHMENT8_EXT;

  public static final int GL_COLOR_ATTACHMENT9_EXT
    = GL.GL_COLOR_ATTACHMENT9_EXT;

  public static final int GL_COLOR_ATTACHMENT10_EXT
    = GL.GL_COLOR_ATTACHMENT10_EXT;

  public static final int GL_COLOR_ATTACHMENT11_EXT
    = GL.GL_COLOR_ATTACHMENT11_EXT;

  public static final int GL_COLOR_ATTACHMENT12_EXT
    = GL.GL_COLOR_ATTACHMENT12_EXT;

  public static final int GL_COLOR_ATTACHMENT13_EXT
    = GL.GL_COLOR_ATTACHMENT13_EXT;

  public static final int GL_COLOR_ATTACHMENT14_EXT
    = GL.GL_COLOR_ATTACHMENT14_EXT;

  public static final int GL_COLOR_ATTACHMENT15_EXT
    = GL.GL_COLOR_ATTACHMENT15_EXT;

  public static final int GL_DEPTH_ATTACHMENT_EXT
    = GL.GL_DEPTH_ATTACHMENT_EXT;

  public static final int GL_STENCIL_ATTACHMENT_EXT
    = GL.GL_STENCIL_ATTACHMENT_EXT;

  public static final int GL_FRAMEBUFFER_EXT
    = GL.GL_FRAMEBUFFER_EXT;

  public static final int GL_RENDERBUFFER_EXT
    = GL.GL_RENDERBUFFER_EXT;

  public static final int GL_RENDERBUFFER_WIDTH_EXT
    = GL.GL_RENDERBUFFER_WIDTH_EXT;

  public static final int GL_RENDERBUFFER_HEIGHT_EXT
    = GL.GL_RENDERBUFFER_HEIGHT_EXT;

  public static final int GL_RENDERBUFFER_INTERNAL_FORMAT_EXT
    = GL.GL_RENDERBUFFER_INTERNAL_FORMAT_EXT;

  public static final int GL_STENCIL_INDEX_EXT
    = GL.GL_STENCIL_INDEX_EXT;

  public static final int GL_STENCIL_INDEX1_EXT
    = GL.GL_STENCIL_INDEX1_EXT;

  public static final int GL_STENCIL_INDEX4_EXT
    = GL.GL_STENCIL_INDEX4_EXT;

  public static final int GL_STENCIL_INDEX8_EXT
    = GL.GL_STENCIL_INDEX8_EXT;

  public static final int GL_STENCIL_INDEX16_EXT
    = GL.GL_STENCIL_INDEX16_EXT;

  public static final int GL_OES_read_format
    = GL.GL_OES_read_format;

  public static final int GL_GREMEDY_string_marker
    = GL.GL_GREMEDY_string_marker;

  public static final int GL_KTX_FRONT_REGION
    = GL.GL_KTX_FRONT_REGION;

  public static final int GL_KTX_BACK_REGION
    = GL.GL_KTX_BACK_REGION;

  public static final int GL_KTX_Z_REGION
    = GL.GL_KTX_Z_REGION;

  public static final int GL_KTX_STENCIL_REGION
    = GL.GL_KTX_STENCIL_REGION;

  public static final int GL_TEXTURE_RANGE_LENGTH_APPLE
    = GL.GL_TEXTURE_RANGE_LENGTH_APPLE;

  public static final int GL_TEXTURE_RANGE_POINTER_APPLE
    = GL.GL_TEXTURE_RANGE_POINTER_APPLE;

  public static final int GL_TEXTURE_STORAGE_HINT_APPLE
    = GL.GL_TEXTURE_STORAGE_HINT_APPLE;

  public static final int GL_TEXTURE_MINIMIZE_STORAGE_APPLE
    = GL.GL_TEXTURE_MINIMIZE_STORAGE_APPLE;

  public static final int GL_HALF_APPLE
    = GL.GL_HALF_APPLE;

  public static final int GL_COLOR_FLOAT_APPLE
    = GL.GL_COLOR_FLOAT_APPLE;

  public static final int GL_RGBA_FLOAT32_APPLE
    = GL.GL_RGBA_FLOAT32_APPLE;

  public static final int GL_RGB_FLOAT32_APPLE
    = GL.GL_RGB_FLOAT32_APPLE;

  public static final int GL_ALPHA_FLOAT32_APPLE
    = GL.GL_ALPHA_FLOAT32_APPLE;

  public static final int GL_INTENSITY_FLOAT32_APPLE
    = GL.GL_INTENSITY_FLOAT32_APPLE;

  public static final int GL_LUMINANCE_FLOAT32_APPLE
    = GL.GL_LUMINANCE_FLOAT32_APPLE;

  public static final int GL_LUMINANCE_ALPHA_FLOAT32_APPLE
    = GL.GL_LUMINANCE_ALPHA_FLOAT32_APPLE;

  public static final int GL_RGBA_FLOAT16_APPLE
    = GL.GL_RGBA_FLOAT16_APPLE;

  public static final int GL_RGB_FLOAT16_APPLE
    = GL.GL_RGB_FLOAT16_APPLE;

  public static final int GL_ALPHA_FLOAT16_APPLE
    = GL.GL_ALPHA_FLOAT16_APPLE;

  public static final int GL_INTENSITY_FLOAT16_APPLE
    = GL.GL_INTENSITY_FLOAT16_APPLE;

  public static final int GL_LUMINANCE_FLOAT16_APPLE
    = GL.GL_LUMINANCE_FLOAT16_APPLE;

  public static final int GL_LUMINANCE_ALPHA_FLOAT16_APPLE
    = GL.GL_LUMINANCE_ALPHA_FLOAT16_APPLE;

  public static final int GL_MIN_PBUFFER_VIEWPORT_DIMS_APPLE
    = GL.GL_MIN_PBUFFER_VIEWPORT_DIMS_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP1_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP2_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_SIZE_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP1_SIZE_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_COEFF_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP1_COEFF_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_ORDER_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP1_ORDER_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_DOMAIN_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP1_DOMAIN_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_SIZE_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP2_SIZE_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_COEFF_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP2_COEFF_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_ORDER_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP2_ORDER_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_DOMAIN_APPLE
    = GL.GL_VERTEX_ATTRIB_MAP2_DOMAIN_APPLE;

  public static void glAccum(int op,
                             float value) {
    gl().glAccum(
      op,
      value);
  }

  public static void glActiveStencilFaceEXT(int mode) {
    gl().glActiveStencilFaceEXT(
      mode);
  }

  public static void glActiveTexture(int mode) {
    gl().glActiveTexture(
      mode);
  }

  public static void glAlphaFragmentOp1ATI(int stage,
                                           int portion,
                                           int variable,
                                           int input,
                                           int mapping,
                                           int componentUsage) {
    gl().glAlphaFragmentOp1ATI(
      stage,
      portion,
      variable,
      input,
      mapping,
      componentUsage);
  }

  public static void glAlphaFragmentOp2ATI(int op,
                                           int dst,
                                           int dstMod,
                                           int arg1,
                                           int arg1Rep,
                                           int arg1Mod,
                                           int arg2,
                                           int arg2Rep,
                                           int arg2Mod) {
    gl().glAlphaFragmentOp2ATI(
      op,
      dst,
      dstMod,
      arg1,
      arg1Rep,
      arg1Mod,
      arg2,
      arg2Rep,
      arg2Mod);
  }

  public static void glAlphaFragmentOp3ATI(int op,
                                           int dst,
                                           int dstMod,
                                           int arg1,
                                           int arg1Rep,
                                           int arg1Mod,
                                           int arg2,
                                           int arg2Rep,
                                           int arg2Mod,
                                           int arg3,
                                           int arg3Rep,
                                           int arg3Mod) {
    gl().glAlphaFragmentOp3ATI(
      op,
      dst,
      dstMod,
      arg1,
      arg1Rep,
      arg1Mod,
      arg2,
      arg2Rep,
      arg2Mod,
      arg3,
      arg3Rep,
      arg3Mod);
  }

  public static void glAlphaFunc(int func,
                                 float ref) {
    gl().glAlphaFunc(
      func,
      ref);
  }

  public static void glApplyTextureEXT(int mode) {
    gl().glApplyTextureEXT(
      mode);
  }

  public static boolean glAreProgramsResidentNV(int n,
                                                IntBuffer textures,
                                                ByteBuffer residences) {
    return gl().glAreProgramsResidentNV(
      n,
      textures,
      residences);
  }

  public static boolean glAreProgramsResidentNV(int n,
                                                int[] textures,
                                                int textures_offset,
                                                byte[] residences,
                                                int residences_offset) {
    return gl().glAreProgramsResidentNV(
      n,
      textures,
      textures_offset,
      residences,
      residences_offset);
  }

  public static boolean glAreTexturesResident(int n,
                                              IntBuffer textures,
                                              ByteBuffer residences) {
    return gl().glAreTexturesResident(
      n,
      textures,
      residences);
  }

  public static boolean glAreTexturesResident(int n,
                                              int[] textures,
                                              int textures_offset,
                                              byte[] residences,
                                              int residences_offset) {
    return gl().glAreTexturesResident(
      n,
      textures,
      textures_offset,
      residences,
      residences_offset);
  }

  public static void glArrayElement(int i) {
    gl().glArrayElement(
      i);
  }

  public static void glArrayObjectATI(int array,
                                      int size,
                                      int type,
                                      int stride,
                                      int buffer,
                                      int offset) {
    gl().glArrayObjectATI(
      array,
      size,
      type,
      stride,
      buffer,
      offset);
  }

  public static void glAsyncMarkerSGIX(int mode) {
    gl().glAsyncMarkerSGIX(
      mode);
  }

  public static void glAttachObjectARB(int target,
                                       int id) {
    gl().glAttachObjectARB(
      target,
      id);
  }

  public static void glAttachShader(int target,
                                    int id) {
    gl().glAttachShader(
      target,
      id);
  }

  public static void glBegin(int mode) {
    gl().glBegin(
      mode);
  }

  public static void glBeginFragmentShaderATI() {
    gl().glBeginFragmentShaderATI();
  }

  public static void glBeginOcclusionQueryNV(int mode) {
    gl().glBeginOcclusionQueryNV(
      mode);
  }

  public static void glBeginQuery(int target,
                                  int id) {
    gl().glBeginQuery(
      target,
      id);
  }

  public static void glBeginQueryARB(int target,
                                     int id) {
    gl().glBeginQueryARB(
      target,
      id);
  }

  public static void glBeginVertexShaderEXT() {
    gl().glBeginVertexShaderEXT();
  }

  public static void glBindAttribLocation(int program,
                                          int index,
                                          String name) {
    gl().glBindAttribLocation(
      program,
      index,
      name);
  }

  public static void glBindAttribLocationARB(int program,
                                             int index,
                                             String name) {
    gl().glBindAttribLocationARB(
      program,
      index,
      name);
  }

  public static void glBindBuffer(int target,
                                  int id) {
    gl().glBindBuffer(
      target,
      id);
  }

  public static void glBindBufferARB(int target,
                                     int id) {
    gl().glBindBufferARB(
      target,
      id);
  }

  public static void glBindFragmentShaderATI(int mode) {
    gl().glBindFragmentShaderATI(
      mode);
  }

  public static void glBindFramebufferEXT(int target,
                                          int id) {
    gl().glBindFramebufferEXT(
      target,
      id);
  }

  public static int glBindLightParameterEXT(int light,
                                            int value) {
    return gl().glBindLightParameterEXT(
      light,
      value);
  }

  public static int glBindMaterialParameterEXT(int light,
                                               int value) {
    return gl().glBindMaterialParameterEXT(
      light,
      value);
  }

  public static int glBindParameterEXT(int type) {
    return gl().glBindParameterEXT(
      type);
  }

  public static void glBindProgramARB(int target,
                                      int id) {
    gl().glBindProgramARB(
      target,
      id);
  }

  public static void glBindProgramNV(int target,
                                     int id) {
    gl().glBindProgramNV(
      target,
      id);
  }

  public static void glBindRenderbufferEXT(int target,
                                           int id) {
    gl().glBindRenderbufferEXT(
      target,
      id);
  }

  public static int glBindTexGenParameterEXT(int unit,
                                             int coord,
                                             int value) {
    return gl().glBindTexGenParameterEXT(
      unit,
      coord,
      value);
  }

  public static void glBindTexture(int target,
                                   int texture) {
    gl().glBindTexture(
      target,
      texture);
  }

  public static int glBindTextureUnitParameterEXT(int light,
                                                  int value) {
    return gl().glBindTextureUnitParameterEXT(
      light,
      value);
  }

  public static void glBindVertexArrayAPPLE(int mode) {
    gl().glBindVertexArrayAPPLE(
      mode);
  }

  public static void glBindVertexShaderEXT(int mode) {
    gl().glBindVertexShaderEXT(
      mode);
  }

  public static void glBitmap(int width,
                              int height,
                              float xorig,
                              float yorig,
                              float xmove,
                              float ymove,
                              ByteBuffer bitmap) {
    gl().glBitmap(
      width,
      height,
      xorig,
      yorig,
      xmove,
      ymove,
      bitmap);
  }

  public static void glBitmap(int width,
                              int height,
                              float xorig,
                              float yorig,
                              float xmove,
                              float ymove,
                              byte[] bitmap,
                              int bitmap_offset) {
    gl().glBitmap(
      width,
      height,
      xorig,
      yorig,
      xmove,
      ymove,
      bitmap,
      bitmap_offset);
  }

  public static void glBitmap(int width,
                              int height,
                              float xorig,
                              float yorig,
                              float xmove,
                              float ymove,
                              long bitmap_buffer_offset) {
    gl().glBitmap(
      width,
      height,
      xorig,
      yorig,
      xmove,
      ymove,
      bitmap_buffer_offset);
  }

  public static void glBlendColor(float red,
                                  float green,
                                  float blue,
                                  float alpha) {
    gl().glBlendColor(
      red,
      green,
      blue,
      alpha);
  }

  public static void glBlendEquation(int mode) {
    gl().glBlendEquation(
      mode);
  }

  public static void glBlendEquationSeparate(int target,
                                             int id) {
    gl().glBlendEquationSeparate(
      target,
      id);
  }

  public static void glBlendEquationSeparateEXT(int target,
                                                int id) {
    gl().glBlendEquationSeparateEXT(
      target,
      id);
  }

  public static void glBlendFunc(int sfactor,
                                 int dfactor) {
    gl().glBlendFunc(
      sfactor,
      dfactor);
  }

  public static void glBlendFuncSeparate(int sfactorRGB,
                                         int dfactorRGB,
                                         int sfactorAlpha,
                                         int dfactorAlpha) {
    gl().glBlendFuncSeparate(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glBlendFuncSeparateEXT(int sfactorRGB,
                                            int dfactorRGB,
                                            int sfactorAlpha,
                                            int dfactorAlpha) {
    gl().glBlendFuncSeparateEXT(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glBlendFuncSeparateINGR(int sfactorRGB,
                                             int dfactorRGB,
                                             int sfactorAlpha,
                                             int dfactorAlpha) {
    gl().glBlendFuncSeparateINGR(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glBufferData(int target,
                                  int size,
                                  Buffer data,
                                  int usage) {
    gl().glBufferData(
      target,
      size,
      data,
      usage);
  }

  public static void glBufferDataARB(int target,
                                     int size,
                                     Buffer data,
                                     int usage) {
    gl().glBufferDataARB(
      target,
      size,
      data,
      usage);
  }

  public static int glBufferRegionEnabled() {
    return gl().glBufferRegionEnabled();
  }

  public static void glBufferSubData(int target,
                                     int offset,
                                     int size,
                                     Buffer data) {
    gl().glBufferSubData(
      target,
      offset,
      size,
      data);
  }

  public static void glBufferSubDataARB(int target,
                                        int offset,
                                        int size,
                                        Buffer data) {
    gl().glBufferSubDataARB(
      target,
      offset,
      size,
      data);
  }

  public static void glCallList(int list) {
    gl().glCallList(
      list);
  }

  public static void glCallLists(int n,
                                 int type,
                                 Buffer lists) {
    gl().glCallLists(
      n,
      type,
      lists);
  }

  public static int glCheckFramebufferStatusEXT(int type) {
    return gl().glCheckFramebufferStatusEXT(
      type);
  }

  public static void glClampColorARB(int target,
                                     int id) {
    gl().glClampColorARB(
      target,
      id);
  }

  public static void glClear(int mask) {
    gl().glClear(
      mask);
  }

  public static void glClearAccum(float red,
                                  float green,
                                  float blue,
                                  float alpha) {
    gl().glClearAccum(
      red,
      green,
      blue,
      alpha);
  }

  public static void glClearColor(float red,
                                  float green,
                                  float blue,
                                  float alpha) {
    gl().glClearColor(
      red,
      green,
      blue,
      alpha);
  }

  public static void glClearDepth(double depth) {
    gl().glClearDepth(
      depth);
  }

  public static void glClearIndex(float c) {
    gl().glClearIndex(
      c);
  }

  public static void glClearStencil(int s) {
    gl().glClearStencil(
      s);
  }

  public static void glClientActiveTexture(int mode) {
    gl().glClientActiveTexture(
      mode);
  }

  public static void glClientActiveVertexStreamATI(int mode) {
    gl().glClientActiveVertexStreamATI(
      mode);
  }

  public static void glClipPlane(int plane,
                                 DoubleBuffer equation) {
    gl().glClipPlane(
      plane,
      equation);
  }

  public static void glClipPlane(int plane,
                                 double[] equation,
                                 int equation_offset) {
    gl().glClipPlane(
      plane,
      equation,
      equation_offset);
  }

  public static void glColor3b(byte red,
                               byte green,
                               byte blue) {
    gl().glColor3b(
      red,
      green,
      blue);
  }

  public static void glColor3bv(ByteBuffer v) {
    gl().glColor3bv(
      v);
  }

  public static void glColor3bv(byte[] v,
                                int v_offset) {
    gl().glColor3bv(
      v,
      v_offset);
  }

  public static void glColor3d(double red,
                               double green,
                               double blue) {
    gl().glColor3d(
      red,
      green,
      blue);
  }

  public static void glColor3dv(DoubleBuffer v) {
    gl().glColor3dv(
      v);
  }

  public static void glColor3dv(double[] v,
                                int v_offset) {
    gl().glColor3dv(
      v,
      v_offset);
  }

  public static void glColor3f(float red,
                               float green,
                               float blue) {
    gl().glColor3f(
      red,
      green,
      blue);
  }

  public static void glColor3fVertex3fSUN(float r,
                                          float g,
                                          float b,
                                          float x,
                                          float y,
                                          float z) {
    gl().glColor3fVertex3fSUN(
      r,
      g,
      b,
      x,
      y,
      z);
  }

  public static void glColor3fVertex3fvSUN(FloatBuffer c,
                                           FloatBuffer v) {
    gl().glColor3fVertex3fvSUN(
      c,
      v);
  }

  public static void glColor3fVertex3fvSUN(float[] c,
                                           int c_offset,
                                           float[] v,
                                           int v_offset) {
    gl().glColor3fVertex3fvSUN(
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glColor3fv(FloatBuffer v) {
    gl().glColor3fv(
      v);
  }

  public static void glColor3fv(float[] v,
                                int v_offset) {
    gl().glColor3fv(
      v,
      v_offset);
  }

  public static void glColor3hNV(short red,
                                 short green,
                                 short blue) {
    gl().glColor3hNV(
      red,
      green,
      blue);
  }

  public static void glColor3hvNV(ShortBuffer v) {
    gl().glColor3hvNV(
      v);
  }

  public static void glColor3hvNV(short[] v,
                                  int v_offset) {
    gl().glColor3hvNV(
      v,
      v_offset);
  }

  public static void glColor3i(int red,
                               int green,
                               int blue) {
    gl().glColor3i(
      red,
      green,
      blue);
  }

  public static void glColor3iv(IntBuffer v) {
    gl().glColor3iv(
      v);
  }

  public static void glColor3iv(int[] v,
                                int v_offset) {
    gl().glColor3iv(
      v,
      v_offset);
  }

  public static void glColor3s(short red,
                               short green,
                               short blue) {
    gl().glColor3s(
      red,
      green,
      blue);
  }

  public static void glColor3sv(ShortBuffer v) {
    gl().glColor3sv(
      v);
  }

  public static void glColor3sv(short[] v,
                                int v_offset) {
    gl().glColor3sv(
      v,
      v_offset);
  }

  public static void glColor3ub(byte red,
                                byte green,
                                byte blue) {
    gl().glColor3ub(
      red,
      green,
      blue);
  }

  public static void glColor3ubv(ByteBuffer v) {
    gl().glColor3ubv(
      v);
  }

  public static void glColor3ubv(byte[] v,
                                 int v_offset) {
    gl().glColor3ubv(
      v,
      v_offset);
  }

  public static void glColor3ui(int red,
                                int green,
                                int blue) {
    gl().glColor3ui(
      red,
      green,
      blue);
  }

  public static void glColor3uiv(IntBuffer v) {
    gl().glColor3uiv(
      v);
  }

  public static void glColor3uiv(int[] v,
                                 int v_offset) {
    gl().glColor3uiv(
      v,
      v_offset);
  }

  public static void glColor3us(short red,
                                short green,
                                short blue) {
    gl().glColor3us(
      red,
      green,
      blue);
  }

  public static void glColor3usv(ShortBuffer v) {
    gl().glColor3usv(
      v);
  }

  public static void glColor3usv(short[] v,
                                 int v_offset) {
    gl().glColor3usv(
      v,
      v_offset);
  }

  public static void glColor4b(byte red,
                               byte green,
                               byte blue,
                               byte alpha) {
    gl().glColor4b(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4bv(ByteBuffer v) {
    gl().glColor4bv(
      v);
  }

  public static void glColor4bv(byte[] v,
                                int v_offset) {
    gl().glColor4bv(
      v,
      v_offset);
  }

  public static void glColor4d(double red,
                               double green,
                               double blue,
                               double alpha) {
    gl().glColor4d(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4dv(DoubleBuffer v) {
    gl().glColor4dv(
      v);
  }

  public static void glColor4dv(double[] v,
                                int v_offset) {
    gl().glColor4dv(
      v,
      v_offset);
  }

  public static void glColor4f(float red,
                               float green,
                               float blue,
                               float alpha) {
    gl().glColor4f(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4fNormal3fVertex3fSUN(float r,
                                                  float g,
                                                  float b,
                                                  float a,
                                                  float nx,
                                                  float ny,
                                                  float nz,
                                                  float x,
                                                  float y,
                                                  float z) {
    gl().glColor4fNormal3fVertex3fSUN(
      r,
      g,
      b,
      a,
      nx,
      ny,
      nz,
      x,
      y,
      z);
  }

  public static void glColor4fNormal3fVertex3fvSUN(FloatBuffer c,
                                                   FloatBuffer n,
                                                   FloatBuffer v) {
    gl().glColor4fNormal3fVertex3fvSUN(
      c,
      n,
      v);
  }

  public static void glColor4fNormal3fVertex3fvSUN(float[] c,
                                                   int c_offset,
                                                   float[] n,
                                                   int n_offset,
                                                   float[] v,
                                                   int v_offset) {
    gl().glColor4fNormal3fVertex3fvSUN(
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glColor4fv(FloatBuffer v) {
    gl().glColor4fv(
      v);
  }

  public static void glColor4fv(float[] v,
                                int v_offset) {
    gl().glColor4fv(
      v,
      v_offset);
  }

  public static void glColor4hNV(short x,
                                 short y,
                                 short z,
                                 short w) {
    gl().glColor4hNV(
      x,
      y,
      z,
      w);
  }

  public static void glColor4hvNV(ShortBuffer v) {
    gl().glColor4hvNV(
      v);
  }

  public static void glColor4hvNV(short[] v,
                                  int v_offset) {
    gl().glColor4hvNV(
      v,
      v_offset);
  }

  public static void glColor4i(int red,
                               int green,
                               int blue,
                               int alpha) {
    gl().glColor4i(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4iv(IntBuffer v) {
    gl().glColor4iv(
      v);
  }

  public static void glColor4iv(int[] v,
                                int v_offset) {
    gl().glColor4iv(
      v,
      v_offset);
  }

  public static void glColor4s(short red,
                               short green,
                               short blue,
                               short alpha) {
    gl().glColor4s(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4sv(ShortBuffer v) {
    gl().glColor4sv(
      v);
  }

  public static void glColor4sv(short[] v,
                                int v_offset) {
    gl().glColor4sv(
      v,
      v_offset);
  }

  public static void glColor4ub(byte red,
                                byte green,
                                byte blue,
                                byte alpha) {
    gl().glColor4ub(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4ubVertex2fSUN(byte r,
                                           byte g,
                                           byte b,
                                           byte a,
                                           float x,
                                           float y) {
    gl().glColor4ubVertex2fSUN(
      r,
      g,
      b,
      a,
      x,
      y);
  }

  public static void glColor4ubVertex2fvSUN(ByteBuffer c,
                                            FloatBuffer v) {
    gl().glColor4ubVertex2fvSUN(
      c,
      v);
  }

  public static void glColor4ubVertex2fvSUN(byte[] c,
                                            int c_offset,
                                            float[] v,
                                            int v_offset) {
    gl().glColor4ubVertex2fvSUN(
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glColor4ubVertex3fSUN(byte r,
                                           byte g,
                                           byte b,
                                           byte a,
                                           float x,
                                           float y,
                                           float z) {
    gl().glColor4ubVertex3fSUN(
      r,
      g,
      b,
      a,
      x,
      y,
      z);
  }

  public static void glColor4ubVertex3fvSUN(ByteBuffer c,
                                            FloatBuffer v) {
    gl().glColor4ubVertex3fvSUN(
      c,
      v);
  }

  public static void glColor4ubVertex3fvSUN(byte[] c,
                                            int c_offset,
                                            float[] v,
                                            int v_offset) {
    gl().glColor4ubVertex3fvSUN(
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glColor4ubv(ByteBuffer v) {
    gl().glColor4ubv(
      v);
  }

  public static void glColor4ubv(byte[] v,
                                 int v_offset) {
    gl().glColor4ubv(
      v,
      v_offset);
  }

  public static void glColor4ui(int red,
                                int green,
                                int blue,
                                int alpha) {
    gl().glColor4ui(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4uiv(IntBuffer v) {
    gl().glColor4uiv(
      v);
  }

  public static void glColor4uiv(int[] v,
                                 int v_offset) {
    gl().glColor4uiv(
      v,
      v_offset);
  }

  public static void glColor4us(short red,
                                short green,
                                short blue,
                                short alpha) {
    gl().glColor4us(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4usv(ShortBuffer v) {
    gl().glColor4usv(
      v);
  }

  public static void glColor4usv(short[] v,
                                 int v_offset) {
    gl().glColor4usv(
      v,
      v_offset);
  }

  public static void glColorFragmentOp1ATI(int op,
                                           int dst,
                                           int dstMask,
                                           int dstMod,
                                           int arg1,
                                           int arg1Rep,
                                           int arg1Mod) {
    gl().glColorFragmentOp1ATI(
      op,
      dst,
      dstMask,
      dstMod,
      arg1,
      arg1Rep,
      arg1Mod);
  }

  public static void glColorFragmentOp2ATI(int op,
                                           int dst,
                                           int dstMask,
                                           int dstMod,
                                           int arg1,
                                           int arg1Rep,
                                           int arg1Mod,
                                           int arg2,
                                           int arg2Rep,
                                           int arg2Mod) {
    gl().glColorFragmentOp2ATI(
      op,
      dst,
      dstMask,
      dstMod,
      arg1,
      arg1Rep,
      arg1Mod,
      arg2,
      arg2Rep,
      arg2Mod);
  }

  public static void glColorFragmentOp3ATI(int op,
                                           int dst,
                                           int dstMask,
                                           int dstMod,
                                           int arg1,
                                           int arg1Rep,
                                           int arg1Mod,
                                           int arg2,
                                           int arg2Rep,
                                           int arg2Mod,
                                           int arg3,
                                           int arg3Rep,
                                           int arg3Mod) {
    gl().glColorFragmentOp3ATI(
      op,
      dst,
      dstMask,
      dstMod,
      arg1,
      arg1Rep,
      arg1Mod,
      arg2,
      arg2Rep,
      arg2Mod,
      arg3,
      arg3Rep,
      arg3Mod);
  }

  public static void glColorMask(boolean red,
                                 boolean green,
                                 boolean blue,
                                 boolean alpha) {
    gl().glColorMask(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColorMaterial(int face,
                                     int mode) {
    gl().glColorMaterial(
      face,
      mode);
  }

  public static void glColorPointer(int size,
                                    int type,
                                    int stride,
                                    Buffer ptr) {
    gl().glColorPointer(
      size,
      type,
      stride,
      ptr);
  }

  public static void glColorPointer(int size,
                                    int type,
                                    int stride,
                                    long ptr_buffer_offset) {
    gl().glColorPointer(
      size,
      type,
      stride,
      ptr_buffer_offset);
  }

  public static void glColorSubTable(int target,
                                     int start,
                                     int count,
                                     int format,
                                     int type,
                                     Buffer data) {
    gl().glColorSubTable(
      target,
      start,
      count,
      format,
      type,
      data);
  }

  public static void glColorSubTable(int target,
                                     int start,
                                     int count,
                                     int format,
                                     int type,
                                     long data_buffer_offset) {
    gl().glColorSubTable(
      target,
      start,
      count,
      format,
      type,
      data_buffer_offset);
  }

  public static void glColorTable(int target,
                                  int internalformat,
                                  int width,
                                  int format,
                                  int type,
                                  Buffer table) {
    gl().glColorTable(
      target,
      internalformat,
      width,
      format,
      type,
      table);
  }

  public static void glColorTable(int target,
                                  int internalformat,
                                  int width,
                                  int format,
                                  int type,
                                  long table_buffer_offset) {
    gl().glColorTable(
      target,
      internalformat,
      width,
      format,
      type,
      table_buffer_offset);
  }

  public static void glColorTableEXT(int target,
                                     int internalformat,
                                     int width,
                                     int format,
                                     int type,
                                     Buffer table) {
    gl().glColorTableEXT(
      target,
      internalformat,
      width,
      format,
      type,
      table);
  }

  public static void glColorTableParameterfv(int target,
                                             int pname,
                                             FloatBuffer params) {
    gl().glColorTableParameterfv(
      target,
      pname,
      params);
  }

  public static void glColorTableParameterfv(int target,
                                             int pname,
                                             float[] params,
                                             int params_offset) {
    gl().glColorTableParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glColorTableParameteriv(int target,
                                             int pname,
                                             IntBuffer params) {
    gl().glColorTableParameteriv(
      target,
      pname,
      params);
  }

  public static void glColorTableParameteriv(int target,
                                             int pname,
                                             int[] params,
                                             int params_offset) {
    gl().glColorTableParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glCombinerInputNV(int stage,
                                       int portion,
                                       int variable,
                                       int input,
                                       int mapping,
                                       int componentUsage) {
    gl().glCombinerInputNV(
      stage,
      portion,
      variable,
      input,
      mapping,
      componentUsage);
  }

  public static void glCombinerOutputNV(int stage,
                                        int portion,
                                        int abOutput,
                                        int cdOutput,
                                        int sumOutput,
                                        int scale,
                                        int bias,
                                        boolean abDotProduct,
                                        boolean cdDotProduct,
                                        boolean muxSum) {
    gl().glCombinerOutputNV(
      stage,
      portion,
      abOutput,
      cdOutput,
      sumOutput,
      scale,
      bias,
      abDotProduct,
      cdDotProduct,
      muxSum);
  }

  public static void glCombinerParameterfNV(int target,
                                            float s) {
    gl().glCombinerParameterfNV(
      target,
      s);
  }

  public static void glCombinerParameterfvNV(int target,
                                             FloatBuffer v) {
    gl().glCombinerParameterfvNV(
      target,
      v);
  }

  public static void glCombinerParameterfvNV(int target,
                                             float[] v,
                                             int v_offset) {
    gl().glCombinerParameterfvNV(
      target,
      v,
      v_offset);
  }

  public static void glCombinerParameteriNV(int target,
                                            int s) {
    gl().glCombinerParameteriNV(
      target,
      s);
  }

  public static void glCombinerParameterivNV(int target,
                                             IntBuffer v) {
    gl().glCombinerParameterivNV(
      target,
      v);
  }

  public static void glCombinerParameterivNV(int target,
                                             int[] v,
                                             int v_offset) {
    gl().glCombinerParameterivNV(
      target,
      v,
      v_offset);
  }

  public static void glCombinerStageParameterfvNV(int target,
                                                  int pname,
                                                  FloatBuffer params) {
    gl().glCombinerStageParameterfvNV(
      target,
      pname,
      params);
  }

  public static void glCombinerStageParameterfvNV(int target,
                                                  int pname,
                                                  float[] params,
                                                  int params_offset) {
    gl().glCombinerStageParameterfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glCompileShader(int mode) {
    gl().glCompileShader(
      mode);
  }

  public static void glCompileShaderARB(int mode) {
    gl().glCompileShaderARB(
      mode);
  }

  public static void glCompressedTexImage1D(int target,
                                            int level,
                                            int internalformat,
                                            int width,
                                            int border,
                                            int imageSize,
                                            Buffer data) {
    gl().glCompressedTexImage1D(
      target,
      level,
      internalformat,
      width,
      border,
      imageSize,
      data);
  }

  public static void glCompressedTexImage1D(int target,
                                            int level,
                                            int internalformat,
                                            int width,
                                            int border,
                                            int imageSize,
                                            long data_buffer_offset) {
    gl().glCompressedTexImage1D(
      target,
      level,
      internalformat,
      width,
      border,
      imageSize,
      data_buffer_offset);
  }

  public static void glCompressedTexImage2D(int target,
                                            int level,
                                            int internalformat,
                                            int width,
                                            int height,
                                            int border,
                                            int imageSize,
                                            Buffer data) {
    gl().glCompressedTexImage2D(
      target,
      level,
      internalformat,
      width,
      height,
      border,
      imageSize,
      data);
  }

  public static void glCompressedTexImage2D(int target,
                                            int level,
                                            int internalformat,
                                            int width,
                                            int height,
                                            int border,
                                            int imageSize,
                                            long data_buffer_offset) {
    gl().glCompressedTexImage2D(
      target,
      level,
      internalformat,
      width,
      height,
      border,
      imageSize,
      data_buffer_offset);
  }

  public static void glCompressedTexImage3D(int target,
                                            int level,
                                            int internalformat,
                                            int width,
                                            int height,
                                            int depth,
                                            int border,
                                            int imageSize,
                                            Buffer data) {
    gl().glCompressedTexImage3D(
      target,
      level,
      internalformat,
      width,
      height,
      depth,
      border,
      imageSize,
      data);
  }

  public static void glCompressedTexImage3D(int target,
                                            int level,
                                            int internalformat,
                                            int width,
                                            int height,
                                            int depth,
                                            int border,
                                            int imageSize,
                                            long data_buffer_offset) {
    gl().glCompressedTexImage3D(
      target,
      level,
      internalformat,
      width,
      height,
      depth,
      border,
      imageSize,
      data_buffer_offset);
  }

  public static void glCompressedTexSubImage1D(int target,
                                               int level,
                                               int xoffset,
                                               int width,
                                               int format,
                                               int imageSize,
                                               Buffer data) {
    gl().glCompressedTexSubImage1D(
      target,
      level,
      xoffset,
      width,
      format,
      imageSize,
      data);
  }

  public static void glCompressedTexSubImage1D(int target,
                                               int level,
                                               int xoffset,
                                               int width,
                                               int format,
                                               int imageSize,
                                               long data_buffer_offset) {
    gl().glCompressedTexSubImage1D(
      target,
      level,
      xoffset,
      width,
      format,
      imageSize,
      data_buffer_offset);
  }

  public static void glCompressedTexSubImage2D(int target,
                                               int level,
                                               int xoffset,
                                               int yoffset,
                                               int width,
                                               int height,
                                               int format,
                                               int imageSize,
                                               Buffer data) {
    gl().glCompressedTexSubImage2D(
      target,
      level,
      xoffset,
      yoffset,
      width,
      height,
      format,
      imageSize,
      data);
  }

  public static void glCompressedTexSubImage2D(int target,
                                               int level,
                                               int xoffset,
                                               int yoffset,
                                               int width,
                                               int height,
                                               int format,
                                               int imageSize,
                                               long data_buffer_offset) {
    gl().glCompressedTexSubImage2D(
      target,
      level,
      xoffset,
      yoffset,
      width,
      height,
      format,
      imageSize,
      data_buffer_offset);
  }

  public static void glCompressedTexSubImage3D(int target,
                                               int level,
                                               int xoffset,
                                               int yoffset,
                                               int zoffset,
                                               int width,
                                               int height,
                                               int depth,
                                               int format,
                                               int imageSize,
                                               Buffer data) {
    gl().glCompressedTexSubImage3D(
      target,
      level,
      xoffset,
      yoffset,
      zoffset,
      width,
      height,
      depth,
      format,
      imageSize,
      data);
  }

  public static void glCompressedTexSubImage3D(int target,
                                               int level,
                                               int xoffset,
                                               int yoffset,
                                               int zoffset,
                                               int width,
                                               int height,
                                               int depth,
                                               int format,
                                               int imageSize,
                                               long data_buffer_offset) {
    gl().glCompressedTexSubImage3D(
      target,
      level,
      xoffset,
      yoffset,
      zoffset,
      width,
      height,
      depth,
      format,
      imageSize,
      data_buffer_offset);
  }

  public static void glConvolutionFilter1D(int target,
                                           int internalformat,
                                           int width,
                                           int format,
                                           int type,
                                           Buffer table) {
    gl().glConvolutionFilter1D(
      target,
      internalformat,
      width,
      format,
      type,
      table);
  }

  public static void glConvolutionFilter1D(int target,
                                           int internalformat,
                                           int width,
                                           int format,
                                           int type,
                                           long table_buffer_offset) {
    gl().glConvolutionFilter1D(
      target,
      internalformat,
      width,
      format,
      type,
      table_buffer_offset);
  }

  public static void glConvolutionFilter2D(int target,
                                           int internalformat,
                                           int width,
                                           int height,
                                           int format,
                                           int type,
                                           Buffer image) {
    gl().glConvolutionFilter2D(
      target,
      internalformat,
      width,
      height,
      format,
      type,
      image);
  }

  public static void glConvolutionFilter2D(int target,
                                           int internalformat,
                                           int width,
                                           int height,
                                           int format,
                                           int type,
                                           long image_buffer_offset) {
    gl().glConvolutionFilter2D(
      target,
      internalformat,
      width,
      height,
      format,
      type,
      image_buffer_offset);
  }

  public static void glConvolutionParameterf(int target,
                                             int pname,
                                             float params) {
    gl().glConvolutionParameterf(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameterfv(int target,
                                              int pname,
                                              FloatBuffer params) {
    gl().glConvolutionParameterfv(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameterfv(int target,
                                              int pname,
                                              float[] params,
                                              int params_offset) {
    gl().glConvolutionParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glConvolutionParameteri(int target,
                                             int pname,
                                             int params) {
    gl().glConvolutionParameteri(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameteriv(int target,
                                              int pname,
                                              IntBuffer params) {
    gl().glConvolutionParameteriv(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameteriv(int target,
                                              int pname,
                                              int[] params,
                                              int params_offset) {
    gl().glConvolutionParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glCopyColorSubTable(int target,
                                         int start,
                                         int x,
                                         int y,
                                         int width) {
    gl().glCopyColorSubTable(
      target,
      start,
      x,
      y,
      width);
  }

  public static void glCopyColorTable(int target,
                                      int internalformat,
                                      int x,
                                      int y,
                                      int width) {
    gl().glCopyColorTable(
      target,
      internalformat,
      x,
      y,
      width);
  }

  public static void glCopyConvolutionFilter1D(int target,
                                               int internalformat,
                                               int x,
                                               int y,
                                               int width) {
    gl().glCopyConvolutionFilter1D(
      target,
      internalformat,
      x,
      y,
      width);
  }

  public static void glCopyConvolutionFilter2D(int target,
                                               int internalformat,
                                               int x,
                                               int y,
                                               int width,
                                               int height) {
    gl().glCopyConvolutionFilter2D(
      target,
      internalformat,
      x,
      y,
      width,
      height);
  }

  public static void glCopyPixels(int x,
                                  int y,
                                  int width,
                                  int height,
                                  int type) {
    gl().glCopyPixels(
      x,
      y,
      width,
      height,
      type);
  }

  public static void glCopyTexImage1D(int target,
                                      int level,
                                      int internalformat,
                                      int x,
                                      int y,
                                      int width,
                                      int border) {
    gl().glCopyTexImage1D(
      target,
      level,
      internalformat,
      x,
      y,
      width,
      border);
  }

  public static void glCopyTexImage2D(int target,
                                      int level,
                                      int internalformat,
                                      int x,
                                      int y,
                                      int width,
                                      int height,
                                      int border) {
    gl().glCopyTexImage2D(
      target,
      level,
      internalformat,
      x,
      y,
      width,
      height,
      border);
  }

  public static void glCopyTexSubImage1D(int target,
                                         int level,
                                         int xoffset,
                                         int x,
                                         int y,
                                         int width) {
    gl().glCopyTexSubImage1D(
      target,
      level,
      xoffset,
      x,
      y,
      width);
  }

  public static void glCopyTexSubImage2D(int target,
                                         int level,
                                         int xoffset,
                                         int yoffset,
                                         int x,
                                         int y,
                                         int width,
                                         int height) {
    gl().glCopyTexSubImage2D(
      target,
      level,
      xoffset,
      yoffset,
      x,
      y,
      width,
      height);
  }

  public static void glCopyTexSubImage3D(int target,
                                         int level,
                                         int xoffset,
                                         int yoffset,
                                         int zoffset,
                                         int x,
                                         int y,
                                         int width,
                                         int height) {
    gl().glCopyTexSubImage3D(
      target,
      level,
      xoffset,
      yoffset,
      zoffset,
      x,
      y,
      width,
      height);
  }

  public static int glCreateProgram() {
    return gl().glCreateProgram();
  }

  public static int glCreateProgramObjectARB() {
    return gl().glCreateProgramObjectARB();
  }

  public static int glCreateShader(int type) {
    return gl().glCreateShader(
      type);
  }

  public static int glCreateShaderObjectARB(int type) {
    return gl().glCreateShaderObjectARB(
      type);
  }

  public static void glCullFace(int mode) {
    gl().glCullFace(
      mode);
  }

  public static void glCullParameterdvEXT(int pname,
                                          DoubleBuffer params) {
    gl().glCullParameterdvEXT(
      pname,
      params);
  }

  public static void glCullParameterdvEXT(int pname,
                                          double[] params,
                                          int params_offset) {
    gl().glCullParameterdvEXT(
      pname,
      params,
      params_offset);
  }

  public static void glCullParameterfvEXT(int pname,
                                          FloatBuffer params) {
    gl().glCullParameterfvEXT(
      pname,
      params);
  }

  public static void glCullParameterfvEXT(int pname,
                                          float[] params,
                                          int params_offset) {
    gl().glCullParameterfvEXT(
      pname,
      params,
      params_offset);
  }

  public static void glCurrentPaletteMatrixARB(int count) {
    gl().glCurrentPaletteMatrixARB(
      count);
  }

  public static void glDeformSGIX(int mode) {
    gl().glDeformSGIX(
      mode);
  }

  public static void glDeformationMap3dSGIX(int target,
                                            double u1,
                                            double u2,
                                            int ustride,
                                            int uorder,
                                            double v1,
                                            double v2,
                                            int vstride,
                                            int vorder,
                                            double w1,
                                            double w2,
                                            int wstride,
                                            int worder,
                                            DoubleBuffer points) {
    gl().glDeformationMap3dSGIX(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      w1,
      w2,
      wstride,
      worder,
      points);
  }

  public static void glDeformationMap3dSGIX(int target,
                                            double u1,
                                            double u2,
                                            int ustride,
                                            int uorder,
                                            double v1,
                                            double v2,
                                            int vstride,
                                            int vorder,
                                            double w1,
                                            double w2,
                                            int wstride,
                                            int worder,
                                            double[] points,
                                            int points_offset) {
    gl().glDeformationMap3dSGIX(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      w1,
      w2,
      wstride,
      worder,
      points,
      points_offset);
  }

  public static void glDeformationMap3fSGIX(int target,
                                            float u1,
                                            float u2,
                                            int ustride,
                                            int uorder,
                                            float v1,
                                            float v2,
                                            int vstride,
                                            int vorder,
                                            float w1,
                                            float w2,
                                            int wstride,
                                            int worder,
                                            FloatBuffer points) {
    gl().glDeformationMap3fSGIX(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      w1,
      w2,
      wstride,
      worder,
      points);
  }

  public static void glDeformationMap3fSGIX(int target,
                                            float u1,
                                            float u2,
                                            int ustride,
                                            int uorder,
                                            float v1,
                                            float v2,
                                            int vstride,
                                            int vorder,
                                            float w1,
                                            float w2,
                                            int wstride,
                                            int worder,
                                            float[] points,
                                            int points_offset) {
    gl().glDeformationMap3fSGIX(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      w1,
      w2,
      wstride,
      worder,
      points,
      points_offset);
  }

  public static void glDeleteAsyncMarkersSGIX(int target,
                                              int s) {
    gl().glDeleteAsyncMarkersSGIX(
      target,
      s);
  }

  public static void glDeleteBufferRegion(int mode) {
    gl().glDeleteBufferRegion(
      mode);
  }

  public static void glDeleteBuffers(int n,
                                     IntBuffer ids) {
    gl().glDeleteBuffers(
      n,
      ids);
  }

  public static void glDeleteBuffers(int n,
                                     int[] ids,
                                     int ids_offset) {
    gl().glDeleteBuffers(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteBuffersARB(int n,
                                        IntBuffer ids) {
    gl().glDeleteBuffersARB(
      n,
      ids);
  }

  public static void glDeleteBuffersARB(int n,
                                        int[] ids,
                                        int ids_offset) {
    gl().glDeleteBuffersARB(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteFencesAPPLE(int n,
                                         IntBuffer ids) {
    gl().glDeleteFencesAPPLE(
      n,
      ids);
  }

  public static void glDeleteFencesAPPLE(int n,
                                         int[] ids,
                                         int ids_offset) {
    gl().glDeleteFencesAPPLE(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteFencesNV(int n,
                                      IntBuffer ids) {
    gl().glDeleteFencesNV(
      n,
      ids);
  }

  public static void glDeleteFencesNV(int n,
                                      int[] ids,
                                      int ids_offset) {
    gl().glDeleteFencesNV(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteFragmentShaderATI(int mode) {
    gl().glDeleteFragmentShaderATI(
      mode);
  }

  public static void glDeleteFramebuffersEXT(int n,
                                             IntBuffer ids) {
    gl().glDeleteFramebuffersEXT(
      n,
      ids);
  }

  public static void glDeleteFramebuffersEXT(int n,
                                             int[] ids,
                                             int ids_offset) {
    gl().glDeleteFramebuffersEXT(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteLists(int list,
                                   int range) {
    gl().glDeleteLists(
      list,
      range);
  }

  public static void glDeleteObjectARB(int mode) {
    gl().glDeleteObjectARB(
      mode);
  }

  public static void glDeleteOcclusionQueriesNV(int n,
                                                IntBuffer ids) {
    gl().glDeleteOcclusionQueriesNV(
      n,
      ids);
  }

  public static void glDeleteOcclusionQueriesNV(int n,
                                                int[] ids,
                                                int ids_offset) {
    gl().glDeleteOcclusionQueriesNV(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteProgram(int mode) {
    gl().glDeleteProgram(
      mode);
  }

  public static void glDeleteProgramsARB(int n,
                                         IntBuffer ids) {
    gl().glDeleteProgramsARB(
      n,
      ids);
  }

  public static void glDeleteProgramsARB(int n,
                                         int[] ids,
                                         int ids_offset) {
    gl().glDeleteProgramsARB(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteProgramsNV(int n,
                                        IntBuffer ids) {
    gl().glDeleteProgramsNV(
      n,
      ids);
  }

  public static void glDeleteProgramsNV(int n,
                                        int[] ids,
                                        int ids_offset) {
    gl().glDeleteProgramsNV(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteQueries(int n,
                                     IntBuffer ids) {
    gl().glDeleteQueries(
      n,
      ids);
  }

  public static void glDeleteQueries(int n,
                                     int[] ids,
                                     int ids_offset) {
    gl().glDeleteQueries(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteQueriesARB(int n,
                                        IntBuffer ids) {
    gl().glDeleteQueriesARB(
      n,
      ids);
  }

  public static void glDeleteQueriesARB(int n,
                                        int[] ids,
                                        int ids_offset) {
    gl().glDeleteQueriesARB(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteRenderbuffersEXT(int n,
                                              IntBuffer ids) {
    gl().glDeleteRenderbuffersEXT(
      n,
      ids);
  }

  public static void glDeleteRenderbuffersEXT(int n,
                                              int[] ids,
                                              int ids_offset) {
    gl().glDeleteRenderbuffersEXT(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteShader(int mode) {
    gl().glDeleteShader(
      mode);
  }

  public static void glDeleteTextures(int n,
                                      IntBuffer textures) {
    gl().glDeleteTextures(
      n,
      textures);
  }

  public static void glDeleteTextures(int n,
                                      int[] textures,
                                      int textures_offset) {
    gl().glDeleteTextures(
      n,
      textures,
      textures_offset);
  }

  public static void glDeleteVertexArraysAPPLE(int n,
                                               IntBuffer ids) {
    gl().glDeleteVertexArraysAPPLE(
      n,
      ids);
  }

  public static void glDeleteVertexArraysAPPLE(int n,
                                               int[] ids,
                                               int ids_offset) {
    gl().glDeleteVertexArraysAPPLE(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteVertexShaderEXT(int mode) {
    gl().glDeleteVertexShaderEXT(
      mode);
  }

  public static void glDepthBoundsEXT(double x,
                                      double y) {
    gl().glDepthBoundsEXT(
      x,
      y);
  }

  public static void glDepthFunc(int func) {
    gl().glDepthFunc(
      func);
  }

  public static void glDepthMask(boolean flag) {
    gl().glDepthMask(
      flag);
  }

  public static void glDepthRange(double near_val,
                                  double far_val) {
    gl().glDepthRange(
      near_val,
      far_val);
  }

  public static void glDetachObjectARB(int target,
                                       int id) {
    gl().glDetachObjectARB(
      target,
      id);
  }

  public static void glDetachShader(int target,
                                    int id) {
    gl().glDetachShader(
      target,
      id);
  }

  public static void glDetailTexFuncSGIS(int target,
                                         int n,
                                         FloatBuffer points) {
    gl().glDetailTexFuncSGIS(
      target,
      n,
      points);
  }

  public static void glDetailTexFuncSGIS(int target,
                                         int n,
                                         float[] points,
                                         int points_offset) {
    gl().glDetailTexFuncSGIS(
      target,
      n,
      points,
      points_offset);
  }

  public static void glDisable(int cap) {
    gl().glDisable(
      cap);
  }

  public static void glDisableClientState(int cap) {
    gl().glDisableClientState(
      cap);
  }

  public static void glDisableVariantClientStateEXT(int mode) {
    gl().glDisableVariantClientStateEXT(
      mode);
  }

  public static void glDisableVertexAttribAPPLE(int index,
                                                int pname) {
    gl().glDisableVertexAttribAPPLE(
      index,
      pname);
  }

  public static void glDisableVertexAttribArray(int mode) {
    gl().glDisableVertexAttribArray(
      mode);
  }

  public static void glDisableVertexAttribArrayARB(int mode) {
    gl().glDisableVertexAttribArrayARB(
      mode);
  }

  public static void glDrawArrays(int mode,
                                  int first,
                                  int count) {
    gl().glDrawArrays(
      mode,
      first,
      count);
  }

  public static void glDrawBuffer(int mode) {
    gl().glDrawBuffer(
      mode);
  }

  public static void glDrawBufferRegion(int region,
                                        int x,
                                        int y,
                                        int width,
                                        int height,
                                        int xDest,
                                        int yDest) {
    gl().glDrawBufferRegion(
      region,
      x,
      y,
      width,
      height,
      xDest,
      yDest);
  }

  public static void glDrawBuffers(int n,
                                   IntBuffer ids) {
    gl().glDrawBuffers(
      n,
      ids);
  }

  public static void glDrawBuffers(int n,
                                   int[] ids,
                                   int ids_offset) {
    gl().glDrawBuffers(
      n,
      ids,
      ids_offset);
  }

  public static void glDrawBuffersARB(int n,
                                      IntBuffer ids) {
    gl().glDrawBuffersARB(
      n,
      ids);
  }

  public static void glDrawBuffersARB(int n,
                                      int[] ids,
                                      int ids_offset) {
    gl().glDrawBuffersARB(
      n,
      ids,
      ids_offset);
  }

  public static void glDrawBuffersATI(int n,
                                      IntBuffer ids) {
    gl().glDrawBuffersATI(
      n,
      ids);
  }

  public static void glDrawBuffersATI(int n,
                                      int[] ids,
                                      int ids_offset) {
    gl().glDrawBuffersATI(
      n,
      ids,
      ids_offset);
  }

  public static void glDrawElementArrayAPPLE(int target,
                                             int s,
                                             int t) {
    gl().glDrawElementArrayAPPLE(
      target,
      s,
      t);
  }

  public static void glDrawElementArrayATI(int target,
                                           int s) {
    gl().glDrawElementArrayATI(
      target,
      s);
  }

  public static void glDrawElements(int mode,
                                    int count,
                                    int type,
                                    Buffer indices) {
    gl().glDrawElements(
      mode,
      count,
      type,
      indices);
  }

  public static void glDrawElements(int mode,
                                    int count,
                                    int type,
                                    long indices_buffer_offset) {
    gl().glDrawElements(
      mode,
      count,
      type,
      indices_buffer_offset);
  }

  public static void glDrawMeshArraysSUN(int target,
                                         int s,
                                         int t,
                                         int r) {
    gl().glDrawMeshArraysSUN(
      target,
      s,
      t,
      r);
  }

  public static void glDrawPixels(int width,
                                  int height,
                                  int format,
                                  int type,
                                  Buffer pixels) {
    gl().glDrawPixels(
      width,
      height,
      format,
      type,
      pixels);
  }

  public static void glDrawPixels(int width,
                                  int height,
                                  int format,
                                  int type,
                                  long pixels_buffer_offset) {
    gl().glDrawPixels(
      width,
      height,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glDrawRangeElementArrayAPPLE(int mode,
                                                  int start,
                                                  int end,
                                                  int first,
                                                  int count) {
    gl().glDrawRangeElementArrayAPPLE(
      mode,
      start,
      end,
      first,
      count);
  }

  public static void glDrawRangeElementArrayATI(int mode,
                                                int start,
                                                int end,
                                                int count) {
    gl().glDrawRangeElementArrayATI(
      mode,
      start,
      end,
      count);
  }

  public static void glDrawRangeElements(int mode,
                                         int start,
                                         int end,
                                         int count,
                                         int type,
                                         Buffer indices) {
    gl().glDrawRangeElements(
      mode,
      start,
      end,
      count,
      type,
      indices);
  }

  public static void glDrawRangeElements(int mode,
                                         int start,
                                         int end,
                                         int count,
                                         int type,
                                         long indices_buffer_offset) {
    gl().glDrawRangeElements(
      mode,
      start,
      end,
      count,
      type,
      indices_buffer_offset);
  }

  public static void glEdgeFlag(boolean flag) {
    gl().glEdgeFlag(
      flag);
  }

  public static void glEdgeFlagPointer(int stride,
                                       Buffer ptr) {
    gl().glEdgeFlagPointer(
      stride,
      ptr);
  }

  public static void glEdgeFlagPointer(int stride,
                                       long ptr_buffer_offset) {
    gl().glEdgeFlagPointer(
      stride,
      ptr_buffer_offset);
  }

  public static void glEdgeFlagv(ByteBuffer flag) {
    gl().glEdgeFlagv(
      flag);
  }

  public static void glEdgeFlagv(byte[] flag,
                                 int flag_offset) {
    gl().glEdgeFlagv(
      flag,
      flag_offset);
  }

  public static void glElementPointerAPPLE(int pname,
                                           Buffer params) {
    gl().glElementPointerAPPLE(
      pname,
      params);
  }

  public static void glElementPointerATI(int pname,
                                         Buffer params) {
    gl().glElementPointerATI(
      pname,
      params);
  }

  public static void glElementPointerATI(int pname,
                                         long params_buffer_offset) {
    gl().glElementPointerATI(
      pname,
      params_buffer_offset);
  }

  public static void glEnable(int cap) {
    gl().glEnable(
      cap);
  }

  public static void glEnableClientState(int cap) {
    gl().glEnableClientState(
      cap);
  }

  public static void glEnableVariantClientStateEXT(int mode) {
    gl().glEnableVariantClientStateEXT(
      mode);
  }

  public static void glEnableVertexAttribAPPLE(int index,
                                               int pname) {
    gl().glEnableVertexAttribAPPLE(
      index,
      pname);
  }

  public static void glEnableVertexAttribArray(int mode) {
    gl().glEnableVertexAttribArray(
      mode);
  }

  public static void glEnableVertexAttribArrayARB(int mode) {
    gl().glEnableVertexAttribArrayARB(
      mode);
  }

  public static void glEnd() {
    gl().glEnd();
  }

  public static void glEndFragmentShaderATI() {
    gl().glEndFragmentShaderATI();
  }

  public static void glEndList() {
    gl().glEndList();
  }

  public static void glEndOcclusionQueryNV() {
    gl().glEndOcclusionQueryNV();
  }

  public static void glEndQuery(int mode) {
    gl().glEndQuery(
      mode);
  }

  public static void glEndQueryARB(int mode) {
    gl().glEndQueryARB(
      mode);
  }

  public static void glEndVertexShaderEXT() {
    gl().glEndVertexShaderEXT();
  }

  public static void glEvalCoord1d(double u) {
    gl().glEvalCoord1d(
      u);
  }

  public static void glEvalCoord1dv(DoubleBuffer u) {
    gl().glEvalCoord1dv(
      u);
  }

  public static void glEvalCoord1dv(double[] u,
                                    int u_offset) {
    gl().glEvalCoord1dv(
      u,
      u_offset);
  }

  public static void glEvalCoord1f(float u) {
    gl().glEvalCoord1f(
      u);
  }

  public static void glEvalCoord1fv(FloatBuffer u) {
    gl().glEvalCoord1fv(
      u);
  }

  public static void glEvalCoord1fv(float[] u,
                                    int u_offset) {
    gl().glEvalCoord1fv(
      u,
      u_offset);
  }

  public static void glEvalCoord2d(double u,
                                   double v) {
    gl().glEvalCoord2d(
      u,
      v);
  }

  public static void glEvalCoord2dv(DoubleBuffer u) {
    gl().glEvalCoord2dv(
      u);
  }

  public static void glEvalCoord2dv(double[] u,
                                    int u_offset) {
    gl().glEvalCoord2dv(
      u,
      u_offset);
  }

  public static void glEvalCoord2f(float u,
                                   float v) {
    gl().glEvalCoord2f(
      u,
      v);
  }

  public static void glEvalCoord2fv(FloatBuffer u) {
    gl().glEvalCoord2fv(
      u);
  }

  public static void glEvalCoord2fv(float[] u,
                                    int u_offset) {
    gl().glEvalCoord2fv(
      u,
      u_offset);
  }

  public static void glEvalMapsNV(int target,
                                  int id) {
    gl().glEvalMapsNV(
      target,
      id);
  }

  public static void glEvalMesh1(int mode,
                                 int i1,
                                 int i2) {
    gl().glEvalMesh1(
      mode,
      i1,
      i2);
  }

  public static void glEvalMesh2(int mode,
                                 int i1,
                                 int i2,
                                 int j1,
                                 int j2) {
    gl().glEvalMesh2(
      mode,
      i1,
      i2,
      j1,
      j2);
  }

  public static void glEvalPoint1(int i) {
    gl().glEvalPoint1(
      i);
  }

  public static void glEvalPoint2(int i,
                                  int j) {
    gl().glEvalPoint2(
      i,
      j);
  }

  public static void glExecuteProgramNV(int target,
                                        int pname,
                                        FloatBuffer params) {
    gl().glExecuteProgramNV(
      target,
      pname,
      params);
  }

  public static void glExecuteProgramNV(int target,
                                        int pname,
                                        float[] params,
                                        int params_offset) {
    gl().glExecuteProgramNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glExtractComponentEXT(int red,
                                           int green,
                                           int blue) {
    gl().glExtractComponentEXT(
      red,
      green,
      blue);
  }

  public static void glFeedbackBuffer(int size,
                                      int type,
                                      FloatBuffer buffer) {
    gl().glFeedbackBuffer(
      size,
      type,
      buffer);
  }

  public static void glFinalCombinerInputNV(int sfactorRGB,
                                            int dfactorRGB,
                                            int sfactorAlpha,
                                            int dfactorAlpha) {
    gl().glFinalCombinerInputNV(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glFinish() {
    gl().glFinish();
  }

  public static int glFinishAsyncSGIX(IntBuffer markerp) {
    return gl().glFinishAsyncSGIX(
      markerp);
  }

  public static int glFinishAsyncSGIX(int[] markerp,
                                      int markerp_offset) {
    return gl().glFinishAsyncSGIX(
      markerp,
      markerp_offset);
  }

  public static void glFinishFenceAPPLE(int mode) {
    gl().glFinishFenceAPPLE(
      mode);
  }

  public static void glFinishFenceNV(int mode) {
    gl().glFinishFenceNV(
      mode);
  }

  public static void glFinishObjectAPPLE(int target,
                                         int s) {
    gl().glFinishObjectAPPLE(
      target,
      s);
  }

  public static void glFinishRenderAPPLE() {
    gl().glFinishRenderAPPLE();
  }

  public static void glFinishTextureSUNX() {
    gl().glFinishTextureSUNX();
  }

  public static void glFlush() {
    gl().glFlush();
  }

  public static void glFlushPixelDataRangeNV(int mode) {
    gl().glFlushPixelDataRangeNV(
      mode);
  }

  public static void glFlushRasterSGIX() {
    gl().glFlushRasterSGIX();
  }

  public static void glFlushRenderAPPLE() {
    gl().glFlushRenderAPPLE();
  }

  public static void glFlushVertexArrayRangeAPPLE(int length,
                                                  Buffer pointer) {
    gl().glFlushVertexArrayRangeAPPLE(
      length,
      pointer);
  }

  public static void glFlushVertexArrayRangeNV() {
    gl().glFlushVertexArrayRangeNV();
  }

  public static void glFogCoordPointer(int type,
                                       int stride,
                                       Buffer pointer) {
    gl().glFogCoordPointer(
      type,
      stride,
      pointer);
  }

  public static void glFogCoordPointer(int type,
                                       int stride,
                                       long pointer_buffer_offset) {
    gl().glFogCoordPointer(
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glFogCoordPointerEXT(int type,
                                          int stride,
                                          Buffer pointer) {
    gl().glFogCoordPointerEXT(
      type,
      stride,
      pointer);
  }

  public static void glFogCoordPointerEXT(int type,
                                          int stride,
                                          long pointer_buffer_offset) {
    gl().glFogCoordPointerEXT(
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glFogCoordd(double coord) {
    gl().glFogCoordd(
      coord);
  }

  public static void glFogCoorddEXT(double coord) {
    gl().glFogCoorddEXT(
      coord);
  }

  public static void glFogCoorddv(DoubleBuffer m) {
    gl().glFogCoorddv(
      m);
  }

  public static void glFogCoorddv(double[] m,
                                  int m_offset) {
    gl().glFogCoorddv(
      m,
      m_offset);
  }

  public static void glFogCoorddvEXT(DoubleBuffer m) {
    gl().glFogCoorddvEXT(
      m);
  }

  public static void glFogCoorddvEXT(double[] m,
                                     int m_offset) {
    gl().glFogCoorddvEXT(
      m,
      m_offset);
  }

  public static void glFogCoordf(float coord) {
    gl().glFogCoordf(
      coord);
  }

  public static void glFogCoordfEXT(float coord) {
    gl().glFogCoordfEXT(
      coord);
  }

  public static void glFogCoordfv(FloatBuffer m) {
    gl().glFogCoordfv(
      m);
  }

  public static void glFogCoordfv(float[] m,
                                  int m_offset) {
    gl().glFogCoordfv(
      m,
      m_offset);
  }

  public static void glFogCoordfvEXT(FloatBuffer m) {
    gl().glFogCoordfvEXT(
      m);
  }

  public static void glFogCoordfvEXT(float[] m,
                                     int m_offset) {
    gl().glFogCoordfvEXT(
      m,
      m_offset);
  }

  public static void glFogCoordhNV(short factor) {
    gl().glFogCoordhNV(
      factor);
  }

  public static void glFogCoordhvNV(ShortBuffer v) {
    gl().glFogCoordhvNV(
      v);
  }

  public static void glFogCoordhvNV(short[] v,
                                    int v_offset) {
    gl().glFogCoordhvNV(
      v,
      v_offset);
  }

  public static void glFogFuncSGIS(int size,
                                   FloatBuffer weights) {
    gl().glFogFuncSGIS(
      size,
      weights);
  }

  public static void glFogFuncSGIS(int size,
                                   float[] weights,
                                   int weights_offset) {
    gl().glFogFuncSGIS(
      size,
      weights,
      weights_offset);
  }

  public static void glFogf(int pname,
                            float param) {
    gl().glFogf(
      pname,
      param);
  }

  public static void glFogfv(int pname,
                             FloatBuffer params) {
    gl().glFogfv(
      pname,
      params);
  }

  public static void glFogfv(int pname,
                             float[] params,
                             int params_offset) {
    gl().glFogfv(
      pname,
      params,
      params_offset);
  }

  public static void glFogi(int pname,
                            int param) {
    gl().glFogi(
      pname,
      param);
  }

  public static void glFogiv(int pname,
                             IntBuffer params) {
    gl().glFogiv(
      pname,
      params);
  }

  public static void glFogiv(int pname,
                             int[] params,
                             int params_offset) {
    gl().glFogiv(
      pname,
      params,
      params_offset);
  }

  public static void glFragmentColorMaterialSGIX(int target,
                                                 int id) {
    gl().glFragmentColorMaterialSGIX(
      target,
      id);
  }

  public static void glFragmentLightModelfSGIX(int target,
                                               float s) {
    gl().glFragmentLightModelfSGIX(
      target,
      s);
  }

  public static void glFragmentLightModelfvSGIX(int target,
                                                FloatBuffer v) {
    gl().glFragmentLightModelfvSGIX(
      target,
      v);
  }

  public static void glFragmentLightModelfvSGIX(int target,
                                                float[] v,
                                                int v_offset) {
    gl().glFragmentLightModelfvSGIX(
      target,
      v,
      v_offset);
  }

  public static void glFragmentLightModeliSGIX(int target,
                                               int s) {
    gl().glFragmentLightModeliSGIX(
      target,
      s);
  }

  public static void glFragmentLightModelivSGIX(int target,
                                                IntBuffer v) {
    gl().glFragmentLightModelivSGIX(
      target,
      v);
  }

  public static void glFragmentLightModelivSGIX(int target,
                                                int[] v,
                                                int v_offset) {
    gl().glFragmentLightModelivSGIX(
      target,
      v,
      v_offset);
  }

  public static void glFragmentLightfSGIX(int target,
                                          int pname,
                                          float params) {
    gl().glFragmentLightfSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentLightfvSGIX(int target,
                                           int pname,
                                           FloatBuffer params) {
    gl().glFragmentLightfvSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentLightfvSGIX(int target,
                                           int pname,
                                           float[] params,
                                           int params_offset) {
    gl().glFragmentLightfvSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glFragmentLightiSGIX(int target,
                                          int pname,
                                          int params) {
    gl().glFragmentLightiSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentLightivSGIX(int target,
                                           int pname,
                                           IntBuffer params) {
    gl().glFragmentLightivSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentLightivSGIX(int target,
                                           int pname,
                                           int[] params,
                                           int params_offset) {
    gl().glFragmentLightivSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glFragmentMaterialfSGIX(int target,
                                             int pname,
                                             float params) {
    gl().glFragmentMaterialfSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentMaterialfvSGIX(int target,
                                              int pname,
                                              FloatBuffer params) {
    gl().glFragmentMaterialfvSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentMaterialfvSGIX(int target,
                                              int pname,
                                              float[] params,
                                              int params_offset) {
    gl().glFragmentMaterialfvSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glFragmentMaterialiSGIX(int target,
                                             int pname,
                                             int params) {
    gl().glFragmentMaterialiSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentMaterialivSGIX(int target,
                                              int pname,
                                              IntBuffer params) {
    gl().glFragmentMaterialivSGIX(
      target,
      pname,
      params);
  }

  public static void glFragmentMaterialivSGIX(int target,
                                              int pname,
                                              int[] params,
                                              int params_offset) {
    gl().glFragmentMaterialivSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glFrameZoomSGIX(int count) {
    gl().glFrameZoomSGIX(
      count);
  }

  public static void glFramebufferRenderbufferEXT(int sfactorRGB,
                                                  int dfactorRGB,
                                                  int sfactorAlpha,
                                                  int dfactorAlpha) {
    gl().glFramebufferRenderbufferEXT(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glFramebufferTexture1DEXT(int target,
                                               int attachment,
                                               int textarget,
                                               int texture,
                                               int level) {
    gl().glFramebufferTexture1DEXT(
      target,
      attachment,
      textarget,
      texture,
      level);
  }

  public static void glFramebufferTexture2DEXT(int target,
                                               int attachment,
                                               int textarget,
                                               int texture,
                                               int level) {
    gl().glFramebufferTexture2DEXT(
      target,
      attachment,
      textarget,
      texture,
      level);
  }

  public static void glFramebufferTexture3DEXT(int target,
                                               int attachment,
                                               int textarget,
                                               int texture,
                                               int level,
                                               int zoffset) {
    gl().glFramebufferTexture3DEXT(
      target,
      attachment,
      textarget,
      texture,
      level,
      zoffset);
  }

  public static void glFreeObjectBufferATI(int mode) {
    gl().glFreeObjectBufferATI(
      mode);
  }

  public static void glFrontFace(int mode) {
    gl().glFrontFace(
      mode);
  }

  public static void glFrustum(double left,
                               double right,
                               double bottom,
                               double top,
                               double near_val,
                               double far_val) {
    gl().glFrustum(
      left,
      right,
      bottom,
      top,
      near_val,
      far_val);
  }

  public static int glGenAsyncMarkersSGIX(int range) {
    return gl().glGenAsyncMarkersSGIX(
      range);
  }

  public static void glGenBuffers(int n,
                                  IntBuffer ids) {
    gl().glGenBuffers(
      n,
      ids);
  }

  public static void glGenBuffers(int n,
                                  int[] ids,
                                  int ids_offset) {
    gl().glGenBuffers(
      n,
      ids,
      ids_offset);
  }

  public static void glGenBuffersARB(int n,
                                     IntBuffer ids) {
    gl().glGenBuffersARB(
      n,
      ids);
  }

  public static void glGenBuffersARB(int n,
                                     int[] ids,
                                     int ids_offset) {
    gl().glGenBuffersARB(
      n,
      ids,
      ids_offset);
  }

  public static void glGenFencesAPPLE(int n,
                                      IntBuffer ids) {
    gl().glGenFencesAPPLE(
      n,
      ids);
  }

  public static void glGenFencesAPPLE(int n,
                                      int[] ids,
                                      int ids_offset) {
    gl().glGenFencesAPPLE(
      n,
      ids,
      ids_offset);
  }

  public static void glGenFencesNV(int n,
                                   IntBuffer ids) {
    gl().glGenFencesNV(
      n,
      ids);
  }

  public static void glGenFencesNV(int n,
                                   int[] ids,
                                   int ids_offset) {
    gl().glGenFencesNV(
      n,
      ids,
      ids_offset);
  }

  public static int glGenFragmentShadersATI(int type) {
    return gl().glGenFragmentShadersATI(
      type);
  }

  public static void glGenFramebuffersEXT(int n,
                                          IntBuffer ids) {
    gl().glGenFramebuffersEXT(
      n,
      ids);
  }

  public static void glGenFramebuffersEXT(int n,
                                          int[] ids,
                                          int ids_offset) {
    gl().glGenFramebuffersEXT(
      n,
      ids,
      ids_offset);
  }

  public static int glGenLists(int range) {
    return gl().glGenLists(
      range);
  }

  public static void glGenOcclusionQueriesNV(int n,
                                             IntBuffer ids) {
    gl().glGenOcclusionQueriesNV(
      n,
      ids);
  }

  public static void glGenOcclusionQueriesNV(int n,
                                             int[] ids,
                                             int ids_offset) {
    gl().glGenOcclusionQueriesNV(
      n,
      ids,
      ids_offset);
  }

  public static void glGenProgramsARB(int n,
                                      IntBuffer ids) {
    gl().glGenProgramsARB(
      n,
      ids);
  }

  public static void glGenProgramsARB(int n,
                                      int[] ids,
                                      int ids_offset) {
    gl().glGenProgramsARB(
      n,
      ids,
      ids_offset);
  }

  public static void glGenProgramsNV(int n,
                                     IntBuffer ids) {
    gl().glGenProgramsNV(
      n,
      ids);
  }

  public static void glGenProgramsNV(int n,
                                     int[] ids,
                                     int ids_offset) {
    gl().glGenProgramsNV(
      n,
      ids,
      ids_offset);
  }

  public static void glGenQueries(int n,
                                  IntBuffer ids) {
    gl().glGenQueries(
      n,
      ids);
  }

  public static void glGenQueries(int n,
                                  int[] ids,
                                  int ids_offset) {
    gl().glGenQueries(
      n,
      ids,
      ids_offset);
  }

  public static void glGenQueriesARB(int n,
                                     IntBuffer ids) {
    gl().glGenQueriesARB(
      n,
      ids);
  }

  public static void glGenQueriesARB(int n,
                                     int[] ids,
                                     int ids_offset) {
    gl().glGenQueriesARB(
      n,
      ids,
      ids_offset);
  }

  public static void glGenRenderbuffersEXT(int n,
                                           IntBuffer ids) {
    gl().glGenRenderbuffersEXT(
      n,
      ids);
  }

  public static void glGenRenderbuffersEXT(int n,
                                           int[] ids,
                                           int ids_offset) {
    gl().glGenRenderbuffersEXT(
      n,
      ids,
      ids_offset);
  }

  public static int glGenSymbolsEXT(int datatype,
                                    int storagetype,
                                    int range,
                                    int components) {
    return gl().glGenSymbolsEXT(
      datatype,
      storagetype,
      range,
      components);
  }

  public static void glGenTextures(int n,
                                   IntBuffer textures) {
    gl().glGenTextures(
      n,
      textures);
  }

  public static void glGenTextures(int n,
                                   int[] textures,
                                   int textures_offset) {
    gl().glGenTextures(
      n,
      textures,
      textures_offset);
  }

  public static void glGenVertexArraysAPPLE(int n,
                                            IntBuffer ids) {
    gl().glGenVertexArraysAPPLE(
      n,
      ids);
  }

  public static void glGenVertexArraysAPPLE(int n,
                                            int[] ids,
                                            int ids_offset) {
    gl().glGenVertexArraysAPPLE(
      n,
      ids,
      ids_offset);
  }

  public static int glGenVertexShadersEXT(int type) {
    return gl().glGenVertexShadersEXT(
      type);
  }

  public static void glGenerateMipmapEXT(int mode) {
    gl().glGenerateMipmapEXT(
      mode);
  }

  public static void glGetActiveAttrib(int program,
                                       int index,
                                       int bufSize,
                                       IntBuffer length,
                                       IntBuffer size,
                                       IntBuffer type,
                                       ByteBuffer name) {
    gl().glGetActiveAttrib(
      program,
      index,
      bufSize,
      length,
      size,
      type,
      name);
  }

  public static void glGetActiveAttrib(int program,
                                       int index,
                                       int bufSize,
                                       int[] length,
                                       int length_offset,
                                       int[] size,
                                       int size_offset,
                                       int[] type,
                                       int type_offset,
                                       byte[] name,
                                       int name_offset) {
    gl().glGetActiveAttrib(
      program,
      index,
      bufSize,
      length,
      length_offset,
      size,
      size_offset,
      type,
      type_offset,
      name,
      name_offset);
  }

  public static void glGetActiveAttribARB(int program,
                                          int index,
                                          int bufSize,
                                          IntBuffer length,
                                          IntBuffer size,
                                          IntBuffer type,
                                          ByteBuffer name) {
    gl().glGetActiveAttribARB(
      program,
      index,
      bufSize,
      length,
      size,
      type,
      name);
  }

  public static void glGetActiveAttribARB(int program,
                                          int index,
                                          int bufSize,
                                          int[] length,
                                          int length_offset,
                                          int[] size,
                                          int size_offset,
                                          int[] type,
                                          int type_offset,
                                          byte[] name,
                                          int name_offset) {
    gl().glGetActiveAttribARB(
      program,
      index,
      bufSize,
      length,
      length_offset,
      size,
      size_offset,
      type,
      type_offset,
      name,
      name_offset);
  }

  public static void glGetActiveUniform(int program,
                                        int index,
                                        int bufSize,
                                        IntBuffer length,
                                        IntBuffer size,
                                        IntBuffer type,
                                        ByteBuffer name) {
    gl().glGetActiveUniform(
      program,
      index,
      bufSize,
      length,
      size,
      type,
      name);
  }

  public static void glGetActiveUniform(int program,
                                        int index,
                                        int bufSize,
                                        int[] length,
                                        int length_offset,
                                        int[] size,
                                        int size_offset,
                                        int[] type,
                                        int type_offset,
                                        byte[] name,
                                        int name_offset) {
    gl().glGetActiveUniform(
      program,
      index,
      bufSize,
      length,
      length_offset,
      size,
      size_offset,
      type,
      type_offset,
      name,
      name_offset);
  }

  public static void glGetActiveUniformARB(int program,
                                           int index,
                                           int bufSize,
                                           IntBuffer length,
                                           IntBuffer size,
                                           IntBuffer type,
                                           ByteBuffer name) {
    gl().glGetActiveUniformARB(
      program,
      index,
      bufSize,
      length,
      size,
      type,
      name);
  }

  public static void glGetActiveUniformARB(int program,
                                           int index,
                                           int bufSize,
                                           int[] length,
                                           int length_offset,
                                           int[] size,
                                           int size_offset,
                                           int[] type,
                                           int type_offset,
                                           byte[] name,
                                           int name_offset) {
    gl().glGetActiveUniformARB(
      program,
      index,
      bufSize,
      length,
      length_offset,
      size,
      size_offset,
      type,
      type_offset,
      name,
      name_offset);
  }

  public static void glGetArrayObjectfvATI(int target,
                                           int pname,
                                           FloatBuffer params) {
    gl().glGetArrayObjectfvATI(
      target,
      pname,
      params);
  }

  public static void glGetArrayObjectfvATI(int target,
                                           int pname,
                                           float[] params,
                                           int params_offset) {
    gl().glGetArrayObjectfvATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetArrayObjectivATI(int target,
                                           int pname,
                                           IntBuffer params) {
    gl().glGetArrayObjectivATI(
      target,
      pname,
      params);
  }

  public static void glGetArrayObjectivATI(int target,
                                           int pname,
                                           int[] params,
                                           int params_offset) {
    gl().glGetArrayObjectivATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetAttachedObjectsARB(int program,
                                             int maxCount,
                                             IntBuffer count,
                                             IntBuffer obj) {
    gl().glGetAttachedObjectsARB(
      program,
      maxCount,
      count,
      obj);
  }

  public static void glGetAttachedObjectsARB(int program,
                                             int maxCount,
                                             int[] count,
                                             int count_offset,
                                             int[] obj,
                                             int obj_offset) {
    gl().glGetAttachedObjectsARB(
      program,
      maxCount,
      count,
      count_offset,
      obj,
      obj_offset);
  }

  public static void glGetAttachedShaders(int program,
                                          int maxCount,
                                          IntBuffer count,
                                          IntBuffer obj) {
    gl().glGetAttachedShaders(
      program,
      maxCount,
      count,
      obj);
  }

  public static void glGetAttachedShaders(int program,
                                          int maxCount,
                                          int[] count,
                                          int count_offset,
                                          int[] obj,
                                          int obj_offset) {
    gl().glGetAttachedShaders(
      program,
      maxCount,
      count,
      count_offset,
      obj,
      obj_offset);
  }

  public static int glGetAttribLocation(int program,
                                        String name) {
    return gl().glGetAttribLocation(
      program,
      name);
  }

  public static int glGetAttribLocationARB(int program,
                                           String name) {
    return gl().glGetAttribLocationARB(
      program,
      name);
  }

  public static void glGetBooleanv(int pname,
                                   ByteBuffer params) {
    gl().glGetBooleanv(
      pname,
      params);
  }

  public static void glGetBooleanv(int pname,
                                   byte[] params,
                                   int params_offset) {
    gl().glGetBooleanv(
      pname,
      params,
      params_offset);
  }

  public static void glGetBufferParameteriv(int target,
                                            int pname,
                                            IntBuffer params) {
    gl().glGetBufferParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetBufferParameteriv(int target,
                                            int pname,
                                            int[] params,
                                            int params_offset) {
    gl().glGetBufferParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetBufferParameterivARB(int target,
                                               int pname,
                                               IntBuffer params) {
    gl().glGetBufferParameterivARB(
      target,
      pname,
      params);
  }

  public static void glGetBufferParameterivARB(int target,
                                               int pname,
                                               int[] params,
                                               int params_offset) {
    gl().glGetBufferParameterivARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetBufferSubData(int target,
                                        int offset,
                                        int size,
                                        Buffer data) {
    gl().glGetBufferSubData(
      target,
      offset,
      size,
      data);
  }

  public static void glGetBufferSubDataARB(int target,
                                           int offset,
                                           int size,
                                           Buffer data) {
    gl().glGetBufferSubDataARB(
      target,
      offset,
      size,
      data);
  }

  public static void glGetClipPlane(int plane,
                                    DoubleBuffer equation) {
    gl().glGetClipPlane(
      plane,
      equation);
  }

  public static void glGetClipPlane(int plane,
                                    double[] equation,
                                    int equation_offset) {
    gl().glGetClipPlane(
      plane,
      equation,
      equation_offset);
  }

  public static void glGetColorTable(int target,
                                     int format,
                                     int type,
                                     Buffer table) {
    gl().glGetColorTable(
      target,
      format,
      type,
      table);
  }

  public static void glGetColorTable(int target,
                                     int format,
                                     int type,
                                     long table_buffer_offset) {
    gl().glGetColorTable(
      target,
      format,
      type,
      table_buffer_offset);
  }

  public static void glGetColorTableEXT(int target,
                                        int format,
                                        int type,
                                        Buffer table) {
    gl().glGetColorTableEXT(
      target,
      format,
      type,
      table);
  }

  public static void glGetColorTableParameterfv(int target,
                                                int pname,
                                                FloatBuffer params) {
    gl().glGetColorTableParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetColorTableParameterfv(int target,
                                                int pname,
                                                float[] params,
                                                int params_offset) {
    gl().glGetColorTableParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetColorTableParameterfvEXT(int target,
                                                   int pname,
                                                   FloatBuffer params) {
    gl().glGetColorTableParameterfvEXT(
      target,
      pname,
      params);
  }

  public static void glGetColorTableParameterfvEXT(int target,
                                                   int pname,
                                                   float[] params,
                                                   int params_offset) {
    gl().glGetColorTableParameterfvEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetColorTableParameteriv(int target,
                                                int pname,
                                                IntBuffer params) {
    gl().glGetColorTableParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetColorTableParameteriv(int target,
                                                int pname,
                                                int[] params,
                                                int params_offset) {
    gl().glGetColorTableParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetColorTableParameterivEXT(int target,
                                                   int pname,
                                                   IntBuffer params) {
    gl().glGetColorTableParameterivEXT(
      target,
      pname,
      params);
  }

  public static void glGetColorTableParameterivEXT(int target,
                                                   int pname,
                                                   int[] params,
                                                   int params_offset) {
    gl().glGetColorTableParameterivEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetCombinerInputParameterfvNV(int stage,
                                                     int portion,
                                                     int variable,
                                                     int pname,
                                                     FloatBuffer params) {
    gl().glGetCombinerInputParameterfvNV(
      stage,
      portion,
      variable,
      pname,
      params);
  }

  public static void glGetCombinerInputParameterfvNV(int stage,
                                                     int portion,
                                                     int variable,
                                                     int pname,
                                                     float[] params,
                                                     int params_offset) {
    gl().glGetCombinerInputParameterfvNV(
      stage,
      portion,
      variable,
      pname,
      params,
      params_offset);
  }

  public static void glGetCombinerInputParameterivNV(int stage,
                                                     int portion,
                                                     int variable,
                                                     int pname,
                                                     IntBuffer params) {
    gl().glGetCombinerInputParameterivNV(
      stage,
      portion,
      variable,
      pname,
      params);
  }

  public static void glGetCombinerInputParameterivNV(int stage,
                                                     int portion,
                                                     int variable,
                                                     int pname,
                                                     int[] params,
                                                     int params_offset) {
    gl().glGetCombinerInputParameterivNV(
      stage,
      portion,
      variable,
      pname,
      params,
      params_offset);
  }

  public static void glGetCombinerOutputParameterfvNV(int stage,
                                                      int portion,
                                                      int pname,
                                                      FloatBuffer params) {
    gl().glGetCombinerOutputParameterfvNV(
      stage,
      portion,
      pname,
      params);
  }

  public static void glGetCombinerOutputParameterfvNV(int stage,
                                                      int portion,
                                                      int pname,
                                                      float[] params,
                                                      int params_offset) {
    gl().glGetCombinerOutputParameterfvNV(
      stage,
      portion,
      pname,
      params,
      params_offset);
  }

  public static void glGetCombinerOutputParameterivNV(int stage,
                                                      int portion,
                                                      int pname,
                                                      IntBuffer params) {
    gl().glGetCombinerOutputParameterivNV(
      stage,
      portion,
      pname,
      params);
  }

  public static void glGetCombinerOutputParameterivNV(int stage,
                                                      int portion,
                                                      int pname,
                                                      int[] params,
                                                      int params_offset) {
    gl().glGetCombinerOutputParameterivNV(
      stage,
      portion,
      pname,
      params,
      params_offset);
  }

  public static void glGetCombinerStageParameterfvNV(int target,
                                                     int pname,
                                                     FloatBuffer params) {
    gl().glGetCombinerStageParameterfvNV(
      target,
      pname,
      params);
  }

  public static void glGetCombinerStageParameterfvNV(int target,
                                                     int pname,
                                                     float[] params,
                                                     int params_offset) {
    gl().glGetCombinerStageParameterfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetCompressedTexImage(int target,
                                             int level,
                                             Buffer img) {
    gl().glGetCompressedTexImage(
      target,
      level,
      img);
  }

  public static void glGetCompressedTexImage(int target,
                                             int level,
                                             long img_buffer_offset) {
    gl().glGetCompressedTexImage(
      target,
      level,
      img_buffer_offset);
  }

  public static void glGetConvolutionFilter(int target,
                                            int format,
                                            int type,
                                            Buffer table) {
    gl().glGetConvolutionFilter(
      target,
      format,
      type,
      table);
  }

  public static void glGetConvolutionFilter(int target,
                                            int format,
                                            int type,
                                            long table_buffer_offset) {
    gl().glGetConvolutionFilter(
      target,
      format,
      type,
      table_buffer_offset);
  }

  public static void glGetConvolutionParameterfv(int target,
                                                 int pname,
                                                 FloatBuffer params) {
    gl().glGetConvolutionParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetConvolutionParameterfv(int target,
                                                 int pname,
                                                 float[] params,
                                                 int params_offset) {
    gl().glGetConvolutionParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetConvolutionParameteriv(int target,
                                                 int pname,
                                                 IntBuffer params) {
    gl().glGetConvolutionParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetConvolutionParameteriv(int target,
                                                 int pname,
                                                 int[] params,
                                                 int params_offset) {
    gl().glGetConvolutionParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetDetailTexFuncSGIS(int pname,
                                            FloatBuffer params) {
    gl().glGetDetailTexFuncSGIS(
      pname,
      params);
  }

  public static void glGetDetailTexFuncSGIS(int pname,
                                            float[] params,
                                            int params_offset) {
    gl().glGetDetailTexFuncSGIS(
      pname,
      params,
      params_offset);
  }

  public static void glGetDoublev(int pname,
                                  DoubleBuffer params) {
    gl().glGetDoublev(
      pname,
      params);
  }

  public static void glGetDoublev(int pname,
                                  double[] params,
                                  int params_offset) {
    gl().glGetDoublev(
      pname,
      params,
      params_offset);
  }

  public static int glGetError() {
    return gl().glGetError();
  }

  public static void glGetFenceivNV(int target,
                                    int pname,
                                    IntBuffer params) {
    gl().glGetFenceivNV(
      target,
      pname,
      params);
  }

  public static void glGetFenceivNV(int target,
                                    int pname,
                                    int[] params,
                                    int params_offset) {
    gl().glGetFenceivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetFinalCombinerInputParameterfvNV(int target,
                                                          int pname,
                                                          FloatBuffer params) {
    gl().glGetFinalCombinerInputParameterfvNV(
      target,
      pname,
      params);
  }

  public static void glGetFinalCombinerInputParameterfvNV(int target,
                                                          int pname,
                                                          float[] params,
                                                          int params_offset) {
    gl().glGetFinalCombinerInputParameterfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetFinalCombinerInputParameterivNV(int target,
                                                          int pname,
                                                          IntBuffer params) {
    gl().glGetFinalCombinerInputParameterivNV(
      target,
      pname,
      params);
  }

  public static void glGetFinalCombinerInputParameterivNV(int target,
                                                          int pname,
                                                          int[] params,
                                                          int params_offset) {
    gl().glGetFinalCombinerInputParameterivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetFloatv(int pname,
                                 FloatBuffer params) {
    gl().glGetFloatv(
      pname,
      params);
  }

  public static void glGetFloatv(int pname,
                                 float[] params,
                                 int params_offset) {
    gl().glGetFloatv(
      pname,
      params,
      params_offset);
  }

  public static void glGetFogFuncSGIS(FloatBuffer points) {
    gl().glGetFogFuncSGIS(
      points);
  }

  public static void glGetFogFuncSGIS(float[] points,
                                      int points_offset) {
    gl().glGetFogFuncSGIS(
      points,
      points_offset);
  }

  public static void glGetFragmentLightfvSGIX(int target,
                                              int pname,
                                              FloatBuffer params) {
    gl().glGetFragmentLightfvSGIX(
      target,
      pname,
      params);
  }

  public static void glGetFragmentLightfvSGIX(int target,
                                              int pname,
                                              float[] params,
                                              int params_offset) {
    gl().glGetFragmentLightfvSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetFragmentLightivSGIX(int target,
                                              int pname,
                                              IntBuffer params) {
    gl().glGetFragmentLightivSGIX(
      target,
      pname,
      params);
  }

  public static void glGetFragmentLightivSGIX(int target,
                                              int pname,
                                              int[] params,
                                              int params_offset) {
    gl().glGetFragmentLightivSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetFragmentMaterialfvSGIX(int target,
                                                 int pname,
                                                 FloatBuffer params) {
    gl().glGetFragmentMaterialfvSGIX(
      target,
      pname,
      params);
  }

  public static void glGetFragmentMaterialfvSGIX(int target,
                                                 int pname,
                                                 float[] params,
                                                 int params_offset) {
    gl().glGetFragmentMaterialfvSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetFragmentMaterialivSGIX(int target,
                                                 int pname,
                                                 IntBuffer params) {
    gl().glGetFragmentMaterialivSGIX(
      target,
      pname,
      params);
  }

  public static void glGetFragmentMaterialivSGIX(int target,
                                                 int pname,
                                                 int[] params,
                                                 int params_offset) {
    gl().glGetFragmentMaterialivSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetFramebufferAttachmentParameterivEXT(int stage,
                                                              int portion,
                                                              int pname,
                                                              IntBuffer params) {
    gl().glGetFramebufferAttachmentParameterivEXT(
      stage,
      portion,
      pname,
      params);
  }

  public static void glGetFramebufferAttachmentParameterivEXT(int stage,
                                                              int portion,
                                                              int pname,
                                                              int[] params,
                                                              int params_offset) {
    gl().glGetFramebufferAttachmentParameterivEXT(
      stage,
      portion,
      pname,
      params,
      params_offset);
  }

  public static int glGetHandleARB(int type) {
    return gl().glGetHandleARB(
      type);
  }

  public static void glGetHistogram(int target,
                                    boolean reset,
                                    int format,
                                    int type,
                                    Buffer values) {
    gl().glGetHistogram(
      target,
      reset,
      format,
      type,
      values);
  }

  public static void glGetHistogram(int target,
                                    boolean reset,
                                    int format,
                                    int type,
                                    long values_buffer_offset) {
    gl().glGetHistogram(
      target,
      reset,
      format,
      type,
      values_buffer_offset);
  }

  public static void glGetHistogramParameterfv(int target,
                                               int pname,
                                               FloatBuffer params) {
    gl().glGetHistogramParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetHistogramParameterfv(int target,
                                               int pname,
                                               float[] params,
                                               int params_offset) {
    gl().glGetHistogramParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetHistogramParameteriv(int target,
                                               int pname,
                                               IntBuffer params) {
    gl().glGetHistogramParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetHistogramParameteriv(int target,
                                               int pname,
                                               int[] params,
                                               int params_offset) {
    gl().glGetHistogramParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetImageTransformParameterfvHP(int target,
                                                      int pname,
                                                      FloatBuffer params) {
    gl().glGetImageTransformParameterfvHP(
      target,
      pname,
      params);
  }

  public static void glGetImageTransformParameterfvHP(int target,
                                                      int pname,
                                                      float[] params,
                                                      int params_offset) {
    gl().glGetImageTransformParameterfvHP(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetImageTransformParameterivHP(int target,
                                                      int pname,
                                                      IntBuffer params) {
    gl().glGetImageTransformParameterivHP(
      target,
      pname,
      params);
  }

  public static void glGetImageTransformParameterivHP(int target,
                                                      int pname,
                                                      int[] params,
                                                      int params_offset) {
    gl().glGetImageTransformParameterivHP(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetInfoLogARB(int program,
                                     int bufSize,
                                     IntBuffer length,
                                     ByteBuffer infoLog) {
    gl().glGetInfoLogARB(
      program,
      bufSize,
      length,
      infoLog);
  }

  public static void glGetInfoLogARB(int program,
                                     int bufSize,
                                     int[] length,
                                     int length_offset,
                                     byte[] infoLog,
                                     int infoLog_offset) {
    gl().glGetInfoLogARB(
      program,
      bufSize,
      length,
      length_offset,
      infoLog,
      infoLog_offset);
  }

  public static int glGetInstrumentsSGIX() {
    return gl().glGetInstrumentsSGIX();
  }

  public static void glGetIntegerv(int pname,
                                   IntBuffer params) {
    gl().glGetIntegerv(
      pname,
      params);
  }

  public static void glGetIntegerv(int pname,
                                   int[] params,
                                   int params_offset) {
    gl().glGetIntegerv(
      pname,
      params,
      params_offset);
  }

  public static void glGetInvariantBooleanvEXT(int id,
                                               int pname,
                                               ByteBuffer program) {
    gl().glGetInvariantBooleanvEXT(
      id,
      pname,
      program);
  }

  public static void glGetInvariantBooleanvEXT(int id,
                                               int pname,
                                               byte[] program,
                                               int program_offset) {
    gl().glGetInvariantBooleanvEXT(
      id,
      pname,
      program,
      program_offset);
  }

  public static void glGetInvariantFloatvEXT(int target,
                                             int pname,
                                             FloatBuffer params) {
    gl().glGetInvariantFloatvEXT(
      target,
      pname,
      params);
  }

  public static void glGetInvariantFloatvEXT(int target,
                                             int pname,
                                             float[] params,
                                             int params_offset) {
    gl().glGetInvariantFloatvEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetInvariantIntegervEXT(int target,
                                               int pname,
                                               IntBuffer params) {
    gl().glGetInvariantIntegervEXT(
      target,
      pname,
      params);
  }

  public static void glGetInvariantIntegervEXT(int target,
                                               int pname,
                                               int[] params,
                                               int params_offset) {
    gl().glGetInvariantIntegervEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetLightfv(int light,
                                  int pname,
                                  FloatBuffer params) {
    gl().glGetLightfv(
      light,
      pname,
      params);
  }

  public static void glGetLightfv(int light,
                                  int pname,
                                  float[] params,
                                  int params_offset) {
    gl().glGetLightfv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glGetLightiv(int light,
                                  int pname,
                                  IntBuffer params) {
    gl().glGetLightiv(
      light,
      pname,
      params);
  }

  public static void glGetLightiv(int light,
                                  int pname,
                                  int[] params,
                                  int params_offset) {
    gl().glGetLightiv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glGetListParameterfvSGIX(int target,
                                              int pname,
                                              FloatBuffer params) {
    gl().glGetListParameterfvSGIX(
      target,
      pname,
      params);
  }

  public static void glGetListParameterfvSGIX(int target,
                                              int pname,
                                              float[] params,
                                              int params_offset) {
    gl().glGetListParameterfvSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetListParameterivSGIX(int target,
                                              int pname,
                                              IntBuffer params) {
    gl().glGetListParameterivSGIX(
      target,
      pname,
      params);
  }

  public static void glGetListParameterivSGIX(int target,
                                              int pname,
                                              int[] params,
                                              int params_offset) {
    gl().glGetListParameterivSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetLocalConstantBooleanvEXT(int id,
                                                   int pname,
                                                   ByteBuffer program) {
    gl().glGetLocalConstantBooleanvEXT(
      id,
      pname,
      program);
  }

  public static void glGetLocalConstantBooleanvEXT(int id,
                                                   int pname,
                                                   byte[] program,
                                                   int program_offset) {
    gl().glGetLocalConstantBooleanvEXT(
      id,
      pname,
      program,
      program_offset);
  }

  public static void glGetLocalConstantFloatvEXT(int target,
                                                 int pname,
                                                 FloatBuffer params) {
    gl().glGetLocalConstantFloatvEXT(
      target,
      pname,
      params);
  }

  public static void glGetLocalConstantFloatvEXT(int target,
                                                 int pname,
                                                 float[] params,
                                                 int params_offset) {
    gl().glGetLocalConstantFloatvEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetLocalConstantIntegervEXT(int target,
                                                   int pname,
                                                   IntBuffer params) {
    gl().glGetLocalConstantIntegervEXT(
      target,
      pname,
      params);
  }

  public static void glGetLocalConstantIntegervEXT(int target,
                                                   int pname,
                                                   int[] params,
                                                   int params_offset) {
    gl().glGetLocalConstantIntegervEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapAttribParameterfvNV(int stage,
                                                 int portion,
                                                 int pname,
                                                 FloatBuffer params) {
    gl().glGetMapAttribParameterfvNV(
      stage,
      portion,
      pname,
      params);
  }

  public static void glGetMapAttribParameterfvNV(int stage,
                                                 int portion,
                                                 int pname,
                                                 float[] params,
                                                 int params_offset) {
    gl().glGetMapAttribParameterfvNV(
      stage,
      portion,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapAttribParameterivNV(int stage,
                                                 int portion,
                                                 int pname,
                                                 IntBuffer params) {
    gl().glGetMapAttribParameterivNV(
      stage,
      portion,
      pname,
      params);
  }

  public static void glGetMapAttribParameterivNV(int stage,
                                                 int portion,
                                                 int pname,
                                                 int[] params,
                                                 int params_offset) {
    gl().glGetMapAttribParameterivNV(
      stage,
      portion,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapControlPointsNV(int target,
                                             int index,
                                             int type,
                                             int ustride,
                                             int vstride,
                                             boolean packed,
                                             Buffer points) {
    gl().glGetMapControlPointsNV(
      target,
      index,
      type,
      ustride,
      vstride,
      packed,
      points);
  }

  public static void glGetMapParameterfvNV(int target,
                                           int pname,
                                           FloatBuffer params) {
    gl().glGetMapParameterfvNV(
      target,
      pname,
      params);
  }

  public static void glGetMapParameterfvNV(int target,
                                           int pname,
                                           float[] params,
                                           int params_offset) {
    gl().glGetMapParameterfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapParameterivNV(int target,
                                           int pname,
                                           IntBuffer params) {
    gl().glGetMapParameterivNV(
      target,
      pname,
      params);
  }

  public static void glGetMapParameterivNV(int target,
                                           int pname,
                                           int[] params,
                                           int params_offset) {
    gl().glGetMapParameterivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapdv(int target,
                                int query,
                                DoubleBuffer v) {
    gl().glGetMapdv(
      target,
      query,
      v);
  }

  public static void glGetMapdv(int target,
                                int query,
                                double[] v,
                                int v_offset) {
    gl().glGetMapdv(
      target,
      query,
      v,
      v_offset);
  }

  public static void glGetMapfv(int target,
                                int query,
                                FloatBuffer v) {
    gl().glGetMapfv(
      target,
      query,
      v);
  }

  public static void glGetMapfv(int target,
                                int query,
                                float[] v,
                                int v_offset) {
    gl().glGetMapfv(
      target,
      query,
      v,
      v_offset);
  }

  public static void glGetMapiv(int target,
                                int query,
                                IntBuffer v) {
    gl().glGetMapiv(
      target,
      query,
      v);
  }

  public static void glGetMapiv(int target,
                                int query,
                                int[] v,
                                int v_offset) {
    gl().glGetMapiv(
      target,
      query,
      v,
      v_offset);
  }

  public static void glGetMaterialfv(int face,
                                     int pname,
                                     FloatBuffer params) {
    gl().glGetMaterialfv(
      face,
      pname,
      params);
  }

  public static void glGetMaterialfv(int face,
                                     int pname,
                                     float[] params,
                                     int params_offset) {
    gl().glGetMaterialfv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glGetMaterialiv(int face,
                                     int pname,
                                     IntBuffer params) {
    gl().glGetMaterialiv(
      face,
      pname,
      params);
  }

  public static void glGetMaterialiv(int face,
                                     int pname,
                                     int[] params,
                                     int params_offset) {
    gl().glGetMaterialiv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glGetMinmax(int target,
                                 boolean reset,
                                 int format,
                                 int type,
                                 Buffer values) {
    gl().glGetMinmax(
      target,
      reset,
      format,
      type,
      values);
  }

  public static void glGetMinmax(int target,
                                 boolean reset,
                                 int format,
                                 int type,
                                 long values_buffer_offset) {
    gl().glGetMinmax(
      target,
      reset,
      format,
      type,
      values_buffer_offset);
  }

  public static void glGetMinmaxParameterfv(int target,
                                            int pname,
                                            FloatBuffer params) {
    gl().glGetMinmaxParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetMinmaxParameterfv(int target,
                                            int pname,
                                            float[] params,
                                            int params_offset) {
    gl().glGetMinmaxParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMinmaxParameteriv(int target,
                                            int pname,
                                            IntBuffer params) {
    gl().glGetMinmaxParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetMinmaxParameteriv(int target,
                                            int pname,
                                            int[] params,
                                            int params_offset) {
    gl().glGetMinmaxParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetObjectBufferfvATI(int target,
                                            int pname,
                                            FloatBuffer params) {
    gl().glGetObjectBufferfvATI(
      target,
      pname,
      params);
  }

  public static void glGetObjectBufferfvATI(int target,
                                            int pname,
                                            float[] params,
                                            int params_offset) {
    gl().glGetObjectBufferfvATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetObjectBufferivATI(int target,
                                            int pname,
                                            IntBuffer params) {
    gl().glGetObjectBufferivATI(
      target,
      pname,
      params);
  }

  public static void glGetObjectBufferivATI(int target,
                                            int pname,
                                            int[] params,
                                            int params_offset) {
    gl().glGetObjectBufferivATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetObjectParameterfvARB(int target,
                                               int pname,
                                               FloatBuffer params) {
    gl().glGetObjectParameterfvARB(
      target,
      pname,
      params);
  }

  public static void glGetObjectParameterfvARB(int target,
                                               int pname,
                                               float[] params,
                                               int params_offset) {
    gl().glGetObjectParameterfvARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetObjectParameterivARB(int target,
                                               int pname,
                                               IntBuffer params) {
    gl().glGetObjectParameterivARB(
      target,
      pname,
      params);
  }

  public static void glGetObjectParameterivARB(int target,
                                               int pname,
                                               int[] params,
                                               int params_offset) {
    gl().glGetObjectParameterivARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetOcclusionQueryivNV(int target,
                                             int pname,
                                             IntBuffer params) {
    gl().glGetOcclusionQueryivNV(
      target,
      pname,
      params);
  }

  public static void glGetOcclusionQueryivNV(int target,
                                             int pname,
                                             int[] params,
                                             int params_offset) {
    gl().glGetOcclusionQueryivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetOcclusionQueryuivNV(int id,
                                              int pname,
                                              IntBuffer params) {
    gl().glGetOcclusionQueryuivNV(
      id,
      pname,
      params);
  }

  public static void glGetOcclusionQueryuivNV(int id,
                                              int pname,
                                              int[] params,
                                              int params_offset) {
    gl().glGetOcclusionQueryuivNV(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetPixelMapfv(int map,
                                     FloatBuffer values) {
    gl().glGetPixelMapfv(
      map,
      values);
  }

  public static void glGetPixelMapfv(int map,
                                     float[] values,
                                     int values_offset) {
    gl().glGetPixelMapfv(
      map,
      values,
      values_offset);
  }

  public static void glGetPixelMapfv(int map,
                                     long values_buffer_offset) {
    gl().glGetPixelMapfv(
      map,
      values_buffer_offset);
  }

  public static void glGetPixelMapuiv(int map,
                                      IntBuffer values) {
    gl().glGetPixelMapuiv(
      map,
      values);
  }

  public static void glGetPixelMapuiv(int map,
                                      int[] values,
                                      int values_offset) {
    gl().glGetPixelMapuiv(
      map,
      values,
      values_offset);
  }

  public static void glGetPixelMapuiv(int map,
                                      long values_buffer_offset) {
    gl().glGetPixelMapuiv(
      map,
      values_buffer_offset);
  }

  public static void glGetPixelMapusv(int map,
                                      ShortBuffer values) {
    gl().glGetPixelMapusv(
      map,
      values);
  }

  public static void glGetPixelMapusv(int map,
                                      short[] values,
                                      int values_offset) {
    gl().glGetPixelMapusv(
      map,
      values,
      values_offset);
  }

  public static void glGetPixelMapusv(int map,
                                      long values_buffer_offset) {
    gl().glGetPixelMapusv(
      map,
      values_buffer_offset);
  }

  public static void glGetPixelTexGenParameterfvSGIS(int pname,
                                                     FloatBuffer params) {
    gl().glGetPixelTexGenParameterfvSGIS(
      pname,
      params);
  }

  public static void glGetPixelTexGenParameterfvSGIS(int pname,
                                                     float[] params,
                                                     int params_offset) {
    gl().glGetPixelTexGenParameterfvSGIS(
      pname,
      params,
      params_offset);
  }

  public static void glGetPixelTexGenParameterivSGIS(int pname,
                                                     IntBuffer params) {
    gl().glGetPixelTexGenParameterivSGIS(
      pname,
      params);
  }

  public static void glGetPixelTexGenParameterivSGIS(int pname,
                                                     int[] params,
                                                     int params_offset) {
    gl().glGetPixelTexGenParameterivSGIS(
      pname,
      params,
      params_offset);
  }

  public static void glGetPolygonStipple(ByteBuffer mask) {
    gl().glGetPolygonStipple(
      mask);
  }

  public static void glGetPolygonStipple(byte[] mask,
                                         int mask_offset) {
    gl().glGetPolygonStipple(
      mask,
      mask_offset);
  }

  public static void glGetPolygonStipple(long mask_buffer_offset) {
    gl().glGetPolygonStipple(
      mask_buffer_offset);
  }

  public static void glGetProgramEnvParameterdvARB(int index,
                                                   int pname,
                                                   DoubleBuffer params) {
    gl().glGetProgramEnvParameterdvARB(
      index,
      pname,
      params);
  }

  public static void glGetProgramEnvParameterdvARB(int index,
                                                   int pname,
                                                   double[] params,
                                                   int params_offset) {
    gl().glGetProgramEnvParameterdvARB(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramEnvParameterfvARB(int target,
                                                   int pname,
                                                   FloatBuffer params) {
    gl().glGetProgramEnvParameterfvARB(
      target,
      pname,
      params);
  }

  public static void glGetProgramEnvParameterfvARB(int target,
                                                   int pname,
                                                   float[] params,
                                                   int params_offset) {
    gl().glGetProgramEnvParameterfvARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramInfoLog(int program,
                                         int bufSize,
                                         IntBuffer length,
                                         ByteBuffer infoLog) {
    gl().glGetProgramInfoLog(
      program,
      bufSize,
      length,
      infoLog);
  }

  public static void glGetProgramInfoLog(int program,
                                         int bufSize,
                                         int[] length,
                                         int length_offset,
                                         byte[] infoLog,
                                         int infoLog_offset) {
    gl().glGetProgramInfoLog(
      program,
      bufSize,
      length,
      length_offset,
      infoLog,
      infoLog_offset);
  }

  public static void glGetProgramLocalParameterdvARB(int index,
                                                     int pname,
                                                     DoubleBuffer params) {
    gl().glGetProgramLocalParameterdvARB(
      index,
      pname,
      params);
  }

  public static void glGetProgramLocalParameterdvARB(int index,
                                                     int pname,
                                                     double[] params,
                                                     int params_offset) {
    gl().glGetProgramLocalParameterdvARB(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramLocalParameterfvARB(int target,
                                                     int pname,
                                                     FloatBuffer params) {
    gl().glGetProgramLocalParameterfvARB(
      target,
      pname,
      params);
  }

  public static void glGetProgramLocalParameterfvARB(int target,
                                                     int pname,
                                                     float[] params,
                                                     int params_offset) {
    gl().glGetProgramLocalParameterfvARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramNamedParameterdvNV(int id,
                                                    int len,
                                                    String name,
                                                    DoubleBuffer params) {
    gl().glGetProgramNamedParameterdvNV(
      id,
      len,
      name,
      params);
  }

  public static void glGetProgramNamedParameterdvNV(int id,
                                                    int len,
                                                    String name,
                                                    double[] params,
                                                    int params_offset) {
    gl().glGetProgramNamedParameterdvNV(
      id,
      len,
      name,
      params,
      params_offset);
  }

  public static void glGetProgramNamedParameterfvNV(int id,
                                                    int len,
                                                    String name,
                                                    FloatBuffer params) {
    gl().glGetProgramNamedParameterfvNV(
      id,
      len,
      name,
      params);
  }

  public static void glGetProgramNamedParameterfvNV(int id,
                                                    int len,
                                                    String name,
                                                    float[] params,
                                                    int params_offset) {
    gl().glGetProgramNamedParameterfvNV(
      id,
      len,
      name,
      params,
      params_offset);
  }

  public static void glGetProgramParameterdvNV(int target,
                                               int index,
                                               int pname,
                                               DoubleBuffer params) {
    gl().glGetProgramParameterdvNV(
      target,
      index,
      pname,
      params);
  }

  public static void glGetProgramParameterdvNV(int target,
                                               int index,
                                               int pname,
                                               double[] params,
                                               int params_offset) {
    gl().glGetProgramParameterdvNV(
      target,
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramParameterfvNV(int stage,
                                               int portion,
                                               int pname,
                                               FloatBuffer params) {
    gl().glGetProgramParameterfvNV(
      stage,
      portion,
      pname,
      params);
  }

  public static void glGetProgramParameterfvNV(int stage,
                                               int portion,
                                               int pname,
                                               float[] params,
                                               int params_offset) {
    gl().glGetProgramParameterfvNV(
      stage,
      portion,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramStringARB(int target,
                                           int pname,
                                           Buffer string) {
    gl().glGetProgramStringARB(
      target,
      pname,
      string);
  }

  public static void glGetProgramStringNV(int id,
                                          int pname,
                                          ByteBuffer program) {
    gl().glGetProgramStringNV(
      id,
      pname,
      program);
  }

  public static void glGetProgramStringNV(int id,
                                          int pname,
                                          byte[] program,
                                          int program_offset) {
    gl().glGetProgramStringNV(
      id,
      pname,
      program,
      program_offset);
  }

  public static void glGetProgramiv(int target,
                                    int pname,
                                    IntBuffer params) {
    gl().glGetProgramiv(
      target,
      pname,
      params);
  }

  public static void glGetProgramiv(int target,
                                    int pname,
                                    int[] params,
                                    int params_offset) {
    gl().glGetProgramiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramivARB(int target,
                                       int pname,
                                       IntBuffer params) {
    gl().glGetProgramivARB(
      target,
      pname,
      params);
  }

  public static void glGetProgramivARB(int target,
                                       int pname,
                                       int[] params,
                                       int params_offset) {
    gl().glGetProgramivARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramivNV(int target,
                                      int pname,
                                      IntBuffer params) {
    gl().glGetProgramivNV(
      target,
      pname,
      params);
  }

  public static void glGetProgramivNV(int target,
                                      int pname,
                                      int[] params,
                                      int params_offset) {
    gl().glGetProgramivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectiv(int target,
                                        int pname,
                                        IntBuffer params) {
    gl().glGetQueryObjectiv(
      target,
      pname,
      params);
  }

  public static void glGetQueryObjectiv(int target,
                                        int pname,
                                        int[] params,
                                        int params_offset) {
    gl().glGetQueryObjectiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectivARB(int target,
                                           int pname,
                                           IntBuffer params) {
    gl().glGetQueryObjectivARB(
      target,
      pname,
      params);
  }

  public static void glGetQueryObjectivARB(int target,
                                           int pname,
                                           int[] params,
                                           int params_offset) {
    gl().glGetQueryObjectivARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectuiv(int id,
                                         int pname,
                                         IntBuffer params) {
    gl().glGetQueryObjectuiv(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjectuiv(int id,
                                         int pname,
                                         int[] params,
                                         int params_offset) {
    gl().glGetQueryObjectuiv(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectuivARB(int id,
                                            int pname,
                                            IntBuffer params) {
    gl().glGetQueryObjectuivARB(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjectuivARB(int id,
                                            int pname,
                                            int[] params,
                                            int params_offset) {
    gl().glGetQueryObjectuivARB(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryiv(int target,
                                  int pname,
                                  IntBuffer params) {
    gl().glGetQueryiv(
      target,
      pname,
      params);
  }

  public static void glGetQueryiv(int target,
                                  int pname,
                                  int[] params,
                                  int params_offset) {
    gl().glGetQueryiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryivARB(int target,
                                     int pname,
                                     IntBuffer params) {
    gl().glGetQueryivARB(
      target,
      pname,
      params);
  }

  public static void glGetQueryivARB(int target,
                                     int pname,
                                     int[] params,
                                     int params_offset) {
    gl().glGetQueryivARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetRenderbufferParameterivEXT(int target,
                                                     int pname,
                                                     IntBuffer params) {
    gl().glGetRenderbufferParameterivEXT(
      target,
      pname,
      params);
  }

  public static void glGetRenderbufferParameterivEXT(int target,
                                                     int pname,
                                                     int[] params,
                                                     int params_offset) {
    gl().glGetRenderbufferParameterivEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetSeparableFilter(int target,
                                          int format,
                                          int type,
                                          Buffer row,
                                          Buffer column,
                                          Buffer span) {
    gl().glGetSeparableFilter(
      target,
      format,
      type,
      row,
      column,
      span);
  }

  public static void glGetSeparableFilter(int target,
                                          int format,
                                          int type,
                                          long row_buffer_offset,
                                          long column_buffer_offset,
                                          long span_buffer_offset) {
    gl().glGetSeparableFilter(
      target,
      format,
      type,
      row_buffer_offset,
      column_buffer_offset,
      span_buffer_offset);
  }

  public static void glGetShaderInfoLog(int program,
                                        int bufSize,
                                        IntBuffer length,
                                        ByteBuffer infoLog) {
    gl().glGetShaderInfoLog(
      program,
      bufSize,
      length,
      infoLog);
  }

  public static void glGetShaderInfoLog(int program,
                                        int bufSize,
                                        int[] length,
                                        int length_offset,
                                        byte[] infoLog,
                                        int infoLog_offset) {
    gl().glGetShaderInfoLog(
      program,
      bufSize,
      length,
      length_offset,
      infoLog,
      infoLog_offset);
  }

  public static void glGetShaderSource(int program,
                                       int bufSize,
                                       IntBuffer length,
                                       ByteBuffer infoLog) {
    gl().glGetShaderSource(
      program,
      bufSize,
      length,
      infoLog);
  }

  public static void glGetShaderSource(int program,
                                       int bufSize,
                                       int[] length,
                                       int length_offset,
                                       byte[] infoLog,
                                       int infoLog_offset) {
    gl().glGetShaderSource(
      program,
      bufSize,
      length,
      length_offset,
      infoLog,
      infoLog_offset);
  }

  public static void glGetShaderSourceARB(int program,
                                          int bufSize,
                                          IntBuffer length,
                                          ByteBuffer infoLog) {
    gl().glGetShaderSourceARB(
      program,
      bufSize,
      length,
      infoLog);
  }

  public static void glGetShaderSourceARB(int program,
                                          int bufSize,
                                          int[] length,
                                          int length_offset,
                                          byte[] infoLog,
                                          int infoLog_offset) {
    gl().glGetShaderSourceARB(
      program,
      bufSize,
      length,
      length_offset,
      infoLog,
      infoLog_offset);
  }

  public static void glGetShaderiv(int target,
                                   int pname,
                                   IntBuffer params) {
    gl().glGetShaderiv(
      target,
      pname,
      params);
  }

  public static void glGetShaderiv(int target,
                                   int pname,
                                   int[] params,
                                   int params_offset) {
    gl().glGetShaderiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetSharpenTexFuncSGIS(int pname,
                                             FloatBuffer params) {
    gl().glGetSharpenTexFuncSGIS(
      pname,
      params);
  }

  public static void glGetSharpenTexFuncSGIS(int pname,
                                             float[] params,
                                             int params_offset) {
    gl().glGetSharpenTexFuncSGIS(
      pname,
      params,
      params_offset);
  }

  public static String glGetString(int name) {
    return gl().glGetString(
      name);
  }

  public static void glGetTexBumpParameterfvATI(int pname,
                                                FloatBuffer params) {
    gl().glGetTexBumpParameterfvATI(
      pname,
      params);
  }

  public static void glGetTexBumpParameterfvATI(int pname,
                                                float[] params,
                                                int params_offset) {
    gl().glGetTexBumpParameterfvATI(
      pname,
      params,
      params_offset);
  }

  public static void glGetTexBumpParameterivATI(int pname,
                                                IntBuffer params) {
    gl().glGetTexBumpParameterivATI(
      pname,
      params);
  }

  public static void glGetTexBumpParameterivATI(int pname,
                                                int[] params,
                                                int params_offset) {
    gl().glGetTexBumpParameterivATI(
      pname,
      params,
      params_offset);
  }

  public static void glGetTexEnvfv(int target,
                                   int pname,
                                   FloatBuffer params) {
    gl().glGetTexEnvfv(
      target,
      pname,
      params);
  }

  public static void glGetTexEnvfv(int target,
                                   int pname,
                                   float[] params,
                                   int params_offset) {
    gl().glGetTexEnvfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexEnviv(int target,
                                   int pname,
                                   IntBuffer params) {
    gl().glGetTexEnviv(
      target,
      pname,
      params);
  }

  public static void glGetTexEnviv(int target,
                                   int pname,
                                   int[] params,
                                   int params_offset) {
    gl().glGetTexEnviv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexFilterFuncSGIS(int target,
                                            int pname,
                                            FloatBuffer params) {
    gl().glGetTexFilterFuncSGIS(
      target,
      pname,
      params);
  }

  public static void glGetTexFilterFuncSGIS(int target,
                                            int pname,
                                            float[] params,
                                            int params_offset) {
    gl().glGetTexFilterFuncSGIS(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexGendv(int coord,
                                   int pname,
                                   DoubleBuffer params) {
    gl().glGetTexGendv(
      coord,
      pname,
      params);
  }

  public static void glGetTexGendv(int coord,
                                   int pname,
                                   double[] params,
                                   int params_offset) {
    gl().glGetTexGendv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexGenfv(int coord,
                                   int pname,
                                   FloatBuffer params) {
    gl().glGetTexGenfv(
      coord,
      pname,
      params);
  }

  public static void glGetTexGenfv(int coord,
                                   int pname,
                                   float[] params,
                                   int params_offset) {
    gl().glGetTexGenfv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexGeniv(int coord,
                                   int pname,
                                   IntBuffer params) {
    gl().glGetTexGeniv(
      coord,
      pname,
      params);
  }

  public static void glGetTexGeniv(int coord,
                                   int pname,
                                   int[] params,
                                   int params_offset) {
    gl().glGetTexGeniv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexImage(int target,
                                   int level,
                                   int format,
                                   int type,
                                   Buffer pixels) {
    gl().glGetTexImage(
      target,
      level,
      format,
      type,
      pixels);
  }

  public static void glGetTexImage(int target,
                                   int level,
                                   int format,
                                   int type,
                                   long pixels_buffer_offset) {
    gl().glGetTexImage(
      target,
      level,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glGetTexLevelParameterfv(int target,
                                              int level,
                                              int pname,
                                              FloatBuffer params) {
    gl().glGetTexLevelParameterfv(
      target,
      level,
      pname,
      params);
  }

  public static void glGetTexLevelParameterfv(int target,
                                              int level,
                                              int pname,
                                              float[] params,
                                              int params_offset) {
    gl().glGetTexLevelParameterfv(
      target,
      level,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexLevelParameteriv(int target,
                                              int level,
                                              int pname,
                                              IntBuffer params) {
    gl().glGetTexLevelParameteriv(
      target,
      level,
      pname,
      params);
  }

  public static void glGetTexLevelParameteriv(int target,
                                              int level,
                                              int pname,
                                              int[] params,
                                              int params_offset) {
    gl().glGetTexLevelParameteriv(
      target,
      level,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexParameterfv(int target,
                                         int pname,
                                         FloatBuffer params) {
    gl().glGetTexParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetTexParameterfv(int target,
                                         int pname,
                                         float[] params,
                                         int params_offset) {
    gl().glGetTexParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexParameteriv(int target,
                                         int pname,
                                         IntBuffer params) {
    gl().glGetTexParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetTexParameteriv(int target,
                                         int pname,
                                         int[] params,
                                         int params_offset) {
    gl().glGetTexParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTrackMatrixivNV(int stage,
                                          int portion,
                                          int pname,
                                          IntBuffer params) {
    gl().glGetTrackMatrixivNV(
      stage,
      portion,
      pname,
      params);
  }

  public static void glGetTrackMatrixivNV(int stage,
                                          int portion,
                                          int pname,
                                          int[] params,
                                          int params_offset) {
    gl().glGetTrackMatrixivNV(
      stage,
      portion,
      pname,
      params,
      params_offset);
  }

  public static int glGetUniformLocation(int program,
                                         String name) {
    return gl().glGetUniformLocation(
      program,
      name);
  }

  public static int glGetUniformLocationARB(int program,
                                            String name) {
    return gl().glGetUniformLocationARB(
      program,
      name);
  }

  public static void glGetUniformfv(int program,
                                    int location,
                                    FloatBuffer params) {
    gl().glGetUniformfv(
      program,
      location,
      params);
  }

  public static void glGetUniformfv(int program,
                                    int location,
                                    float[] params,
                                    int params_offset) {
    gl().glGetUniformfv(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetUniformfvARB(int program,
                                       int location,
                                       FloatBuffer params) {
    gl().glGetUniformfvARB(
      program,
      location,
      params);
  }

  public static void glGetUniformfvARB(int program,
                                       int location,
                                       float[] params,
                                       int params_offset) {
    gl().glGetUniformfvARB(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetUniformiv(int program,
                                    int location,
                                    IntBuffer params) {
    gl().glGetUniformiv(
      program,
      location,
      params);
  }

  public static void glGetUniformiv(int program,
                                    int location,
                                    int[] params,
                                    int params_offset) {
    gl().glGetUniformiv(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetUniformivARB(int program,
                                       int location,
                                       IntBuffer params) {
    gl().glGetUniformivARB(
      program,
      location,
      params);
  }

  public static void glGetUniformivARB(int program,
                                       int location,
                                       int[] params,
                                       int params_offset) {
    gl().glGetUniformivARB(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetVariantArrayObjectfvATI(int target,
                                                  int pname,
                                                  FloatBuffer params) {
    gl().glGetVariantArrayObjectfvATI(
      target,
      pname,
      params);
  }

  public static void glGetVariantArrayObjectfvATI(int target,
                                                  int pname,
                                                  float[] params,
                                                  int params_offset) {
    gl().glGetVariantArrayObjectfvATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVariantArrayObjectivATI(int target,
                                                  int pname,
                                                  IntBuffer params) {
    gl().glGetVariantArrayObjectivATI(
      target,
      pname,
      params);
  }

  public static void glGetVariantArrayObjectivATI(int target,
                                                  int pname,
                                                  int[] params,
                                                  int params_offset) {
    gl().glGetVariantArrayObjectivATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVariantBooleanvEXT(int id,
                                             int pname,
                                             ByteBuffer program) {
    gl().glGetVariantBooleanvEXT(
      id,
      pname,
      program);
  }

  public static void glGetVariantBooleanvEXT(int id,
                                             int pname,
                                             byte[] program,
                                             int program_offset) {
    gl().glGetVariantBooleanvEXT(
      id,
      pname,
      program,
      program_offset);
  }

  public static void glGetVariantFloatvEXT(int target,
                                           int pname,
                                           FloatBuffer params) {
    gl().glGetVariantFloatvEXT(
      target,
      pname,
      params);
  }

  public static void glGetVariantFloatvEXT(int target,
                                           int pname,
                                           float[] params,
                                           int params_offset) {
    gl().glGetVariantFloatvEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVariantIntegervEXT(int target,
                                             int pname,
                                             IntBuffer params) {
    gl().glGetVariantIntegervEXT(
      target,
      pname,
      params);
  }

  public static void glGetVariantIntegervEXT(int target,
                                             int pname,
                                             int[] params,
                                             int params_offset) {
    gl().glGetVariantIntegervEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribArrayObjectfvATI(int target,
                                                       int pname,
                                                       FloatBuffer params) {
    gl().glGetVertexAttribArrayObjectfvATI(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribArrayObjectfvATI(int target,
                                                       int pname,
                                                       float[] params,
                                                       int params_offset) {
    gl().glGetVertexAttribArrayObjectfvATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribArrayObjectivATI(int target,
                                                       int pname,
                                                       IntBuffer params) {
    gl().glGetVertexAttribArrayObjectivATI(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribArrayObjectivATI(int target,
                                                       int pname,
                                                       int[] params,
                                                       int params_offset) {
    gl().glGetVertexAttribArrayObjectivATI(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribdv(int index,
                                         int pname,
                                         DoubleBuffer params) {
    gl().glGetVertexAttribdv(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribdv(int index,
                                         int pname,
                                         double[] params,
                                         int params_offset) {
    gl().glGetVertexAttribdv(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribdvARB(int index,
                                            int pname,
                                            DoubleBuffer params) {
    gl().glGetVertexAttribdvARB(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribdvARB(int index,
                                            int pname,
                                            double[] params,
                                            int params_offset) {
    gl().glGetVertexAttribdvARB(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribdvNV(int index,
                                           int pname,
                                           DoubleBuffer params) {
    gl().glGetVertexAttribdvNV(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribdvNV(int index,
                                           int pname,
                                           double[] params,
                                           int params_offset) {
    gl().glGetVertexAttribdvNV(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribfv(int target,
                                         int pname,
                                         FloatBuffer params) {
    gl().glGetVertexAttribfv(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribfv(int target,
                                         int pname,
                                         float[] params,
                                         int params_offset) {
    gl().glGetVertexAttribfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribfvARB(int target,
                                            int pname,
                                            FloatBuffer params) {
    gl().glGetVertexAttribfvARB(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribfvARB(int target,
                                            int pname,
                                            float[] params,
                                            int params_offset) {
    gl().glGetVertexAttribfvARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribfvNV(int target,
                                           int pname,
                                           FloatBuffer params) {
    gl().glGetVertexAttribfvNV(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribfvNV(int target,
                                           int pname,
                                           float[] params,
                                           int params_offset) {
    gl().glGetVertexAttribfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribiv(int target,
                                         int pname,
                                         IntBuffer params) {
    gl().glGetVertexAttribiv(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribiv(int target,
                                         int pname,
                                         int[] params,
                                         int params_offset) {
    gl().glGetVertexAttribiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribivARB(int target,
                                            int pname,
                                            IntBuffer params) {
    gl().glGetVertexAttribivARB(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribivARB(int target,
                                            int pname,
                                            int[] params,
                                            int params_offset) {
    gl().glGetVertexAttribivARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribivNV(int target,
                                           int pname,
                                           IntBuffer params) {
    gl().glGetVertexAttribivNV(
      target,
      pname,
      params);
  }

  public static void glGetVertexAttribivNV(int target,
                                           int pname,
                                           int[] params,
                                           int params_offset) {
    gl().glGetVertexAttribivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGlobalAlphaFactorbSUN(byte factor) {
    gl().glGlobalAlphaFactorbSUN(
      factor);
  }

  public static void glGlobalAlphaFactordSUN(double coord) {
    gl().glGlobalAlphaFactordSUN(
      coord);
  }

  public static void glGlobalAlphaFactorfSUN(float coord) {
    gl().glGlobalAlphaFactorfSUN(
      coord);
  }

  public static void glGlobalAlphaFactoriSUN(int count) {
    gl().glGlobalAlphaFactoriSUN(
      count);
  }

  public static void glGlobalAlphaFactorsSUN(short factor) {
    gl().glGlobalAlphaFactorsSUN(
      factor);
  }

  public static void glGlobalAlphaFactorubSUN(byte factor) {
    gl().glGlobalAlphaFactorubSUN(
      factor);
  }

  public static void glGlobalAlphaFactoruiSUN(int mode) {
    gl().glGlobalAlphaFactoruiSUN(
      mode);
  }

  public static void glGlobalAlphaFactorusSUN(short factor) {
    gl().glGlobalAlphaFactorusSUN(
      factor);
  }

  public static void glHint(int target,
                            int mode) {
    gl().glHint(
      target,
      mode);
  }

  public static void glHintPGI(int target,
                               int s) {
    gl().glHintPGI(
      target,
      s);
  }

  public static void glHistogram(int target,
                                 int width,
                                 int internalformat,
                                 boolean sink) {
    gl().glHistogram(
      target,
      width,
      internalformat,
      sink);
  }

  public static void glIglooInterfaceSGIX(int pname,
                                          Buffer params) {
    gl().glIglooInterfaceSGIX(
      pname,
      params);
  }

  public static void glImageTransformParameterfHP(int target,
                                                  int pname,
                                                  float params) {
    gl().glImageTransformParameterfHP(
      target,
      pname,
      params);
  }

  public static void glImageTransformParameterfvHP(int target,
                                                   int pname,
                                                   FloatBuffer params) {
    gl().glImageTransformParameterfvHP(
      target,
      pname,
      params);
  }

  public static void glImageTransformParameterfvHP(int target,
                                                   int pname,
                                                   float[] params,
                                                   int params_offset) {
    gl().glImageTransformParameterfvHP(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glImageTransformParameteriHP(int target,
                                                  int pname,
                                                  int params) {
    gl().glImageTransformParameteriHP(
      target,
      pname,
      params);
  }

  public static void glImageTransformParameterivHP(int target,
                                                   int pname,
                                                   IntBuffer params) {
    gl().glImageTransformParameterivHP(
      target,
      pname,
      params);
  }

  public static void glImageTransformParameterivHP(int target,
                                                   int pname,
                                                   int[] params,
                                                   int params_offset) {
    gl().glImageTransformParameterivHP(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glIndexFuncEXT(int target,
                                    float s) {
    gl().glIndexFuncEXT(
      target,
      s);
  }

  public static void glIndexMask(int mask) {
    gl().glIndexMask(
      mask);
  }

  public static void glIndexMaterialEXT(int target,
                                        int id) {
    gl().glIndexMaterialEXT(
      target,
      id);
  }

  public static void glIndexPointer(int type,
                                    int stride,
                                    Buffer ptr) {
    gl().glIndexPointer(
      type,
      stride,
      ptr);
  }

  public static void glIndexd(double c) {
    gl().glIndexd(
      c);
  }

  public static void glIndexdv(DoubleBuffer c) {
    gl().glIndexdv(
      c);
  }

  public static void glIndexdv(double[] c,
                               int c_offset) {
    gl().glIndexdv(
      c,
      c_offset);
  }

  public static void glIndexf(float c) {
    gl().glIndexf(
      c);
  }

  public static void glIndexfv(FloatBuffer c) {
    gl().glIndexfv(
      c);
  }

  public static void glIndexfv(float[] c,
                               int c_offset) {
    gl().glIndexfv(
      c,
      c_offset);
  }

  public static void glIndexi(int c) {
    gl().glIndexi(
      c);
  }

  public static void glIndexiv(IntBuffer c) {
    gl().glIndexiv(
      c);
  }

  public static void glIndexiv(int[] c,
                               int c_offset) {
    gl().glIndexiv(
      c,
      c_offset);
  }

  public static void glIndexs(short c) {
    gl().glIndexs(
      c);
  }

  public static void glIndexsv(ShortBuffer c) {
    gl().glIndexsv(
      c);
  }

  public static void glIndexsv(short[] c,
                               int c_offset) {
    gl().glIndexsv(
      c,
      c_offset);
  }

  public static void glIndexub(byte c) {
    gl().glIndexub(
      c);
  }

  public static void glIndexubv(ByteBuffer c) {
    gl().glIndexubv(
      c);
  }

  public static void glIndexubv(byte[] c,
                                int c_offset) {
    gl().glIndexubv(
      c,
      c_offset);
  }

  public static void glInitNames() {
    gl().glInitNames();
  }

  public static void glInsertComponentEXT(int red,
                                          int green,
                                          int blue) {
    gl().glInsertComponentEXT(
      red,
      green,
      blue);
  }

  public static void glInstrumentsBufferSGIX(int size,
                                             IntBuffer buffer) {
    gl().glInstrumentsBufferSGIX(
      size,
      buffer);
  }

  public static void glInstrumentsBufferSGIX(int size,
                                             int[] buffer,
                                             int buffer_offset) {
    gl().glInstrumentsBufferSGIX(
      size,
      buffer,
      buffer_offset);
  }

  public static void glInterleavedArrays(int format,
                                         int stride,
                                         Buffer pointer) {
    gl().glInterleavedArrays(
      format,
      stride,
      pointer);
  }

  public static void glInterleavedArrays(int format,
                                         int stride,
                                         long pointer_buffer_offset) {
    gl().glInterleavedArrays(
      format,
      stride,
      pointer_buffer_offset);
  }

  public static boolean glIsAsyncMarkerSGIX(int id) {
    return gl().glIsAsyncMarkerSGIX(
      id);
  }

  public static boolean glIsBuffer(int id) {
    return gl().glIsBuffer(
      id);
  }

  public static boolean glIsBufferARB(int id) {
    return gl().glIsBufferARB(
      id);
  }

  public static boolean glIsEnabled(int cap) {
    return gl().glIsEnabled(
      cap);
  }

  public static boolean glIsFenceAPPLE(int id) {
    return gl().glIsFenceAPPLE(
      id);
  }

  public static boolean glIsFenceNV(int id) {
    return gl().glIsFenceNV(
      id);
  }

  public static boolean glIsFramebufferEXT(int id) {
    return gl().glIsFramebufferEXT(
      id);
  }

  public static boolean glIsList(int list) {
    return gl().glIsList(
      list);
  }

  public static boolean glIsObjectBufferATI(int id) {
    return gl().glIsObjectBufferATI(
      id);
  }

  public static boolean glIsOcclusionQueryNV(int id) {
    return gl().glIsOcclusionQueryNV(
      id);
  }

  public static boolean glIsProgram(int id) {
    return gl().glIsProgram(
      id);
  }

  public static boolean glIsProgramARB(int id) {
    return gl().glIsProgramARB(
      id);
  }

  public static boolean glIsProgramNV(int id) {
    return gl().glIsProgramNV(
      id);
  }

  public static boolean glIsQuery(int id) {
    return gl().glIsQuery(
      id);
  }

  public static boolean glIsQueryARB(int id) {
    return gl().glIsQueryARB(
      id);
  }

  public static boolean glIsRenderbufferEXT(int id) {
    return gl().glIsRenderbufferEXT(
      id);
  }

  public static boolean glIsShader(int id) {
    return gl().glIsShader(
      id);
  }

  public static boolean glIsTexture(int texture) {
    return gl().glIsTexture(
      texture);
  }

  public static boolean glIsVariantEnabledEXT(int id,
                                              int cap) {
    return gl().glIsVariantEnabledEXT(
      id,
      cap);
  }

  public static boolean glIsVertexArrayAPPLE(int id) {
    return gl().glIsVertexArrayAPPLE(
      id);
  }

  public static boolean glIsVertexAttribEnabledAPPLE(int index,
                                                     int pname) {
    return gl().glIsVertexAttribEnabledAPPLE(
      index,
      pname);
  }

  public static void glLightEnviSGIX(int target,
                                     int s) {
    gl().glLightEnviSGIX(
      target,
      s);
  }

  public static void glLightModelf(int pname,
                                   float param) {
    gl().glLightModelf(
      pname,
      param);
  }

  public static void glLightModelfv(int pname,
                                    FloatBuffer params) {
    gl().glLightModelfv(
      pname,
      params);
  }

  public static void glLightModelfv(int pname,
                                    float[] params,
                                    int params_offset) {
    gl().glLightModelfv(
      pname,
      params,
      params_offset);
  }

  public static void glLightModeli(int pname,
                                   int param) {
    gl().glLightModeli(
      pname,
      param);
  }

  public static void glLightModeliv(int pname,
                                    IntBuffer params) {
    gl().glLightModeliv(
      pname,
      params);
  }

  public static void glLightModeliv(int pname,
                                    int[] params,
                                    int params_offset) {
    gl().glLightModeliv(
      pname,
      params,
      params_offset);
  }

  public static void glLightf(int light,
                              int pname,
                              float param) {
    gl().glLightf(
      light,
      pname,
      param);
  }

  public static void glLightfv(int light,
                               int pname,
                               FloatBuffer params) {
    gl().glLightfv(
      light,
      pname,
      params);
  }

  public static void glLightfv(int light,
                               int pname,
                               float[] params,
                               int params_offset) {
    gl().glLightfv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glLighti(int light,
                              int pname,
                              int param) {
    gl().glLighti(
      light,
      pname,
      param);
  }

  public static void glLightiv(int light,
                               int pname,
                               IntBuffer params) {
    gl().glLightiv(
      light,
      pname,
      params);
  }

  public static void glLightiv(int light,
                               int pname,
                               int[] params,
                               int params_offset) {
    gl().glLightiv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glLineStipple(int factor,
                                   short pattern) {
    gl().glLineStipple(
      factor,
      pattern);
  }

  public static void glLineWidth(float width) {
    gl().glLineWidth(
      width);
  }

  public static void glLinkProgram(int mode) {
    gl().glLinkProgram(
      mode);
  }

  public static void glLinkProgramARB(int mode) {
    gl().glLinkProgramARB(
      mode);
  }

  public static void glListBase(int base) {
    gl().glListBase(
      base);
  }

  public static void glListParameterfSGIX(int target,
                                          int pname,
                                          float params) {
    gl().glListParameterfSGIX(
      target,
      pname,
      params);
  }

  public static void glListParameterfvSGIX(int target,
                                           int pname,
                                           FloatBuffer params) {
    gl().glListParameterfvSGIX(
      target,
      pname,
      params);
  }

  public static void glListParameterfvSGIX(int target,
                                           int pname,
                                           float[] params,
                                           int params_offset) {
    gl().glListParameterfvSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glListParameteriSGIX(int target,
                                          int pname,
                                          int params) {
    gl().glListParameteriSGIX(
      target,
      pname,
      params);
  }

  public static void glListParameterivSGIX(int target,
                                           int pname,
                                           IntBuffer params) {
    gl().glListParameterivSGIX(
      target,
      pname,
      params);
  }

  public static void glListParameterivSGIX(int target,
                                           int pname,
                                           int[] params,
                                           int params_offset) {
    gl().glListParameterivSGIX(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glLoadIdentity() {
    gl().glLoadIdentity();
  }

  public static void glLoadIdentityDeformationMapSGIX(int mode) {
    gl().glLoadIdentityDeformationMapSGIX(
      mode);
  }

  public static void glLoadMatrixd(DoubleBuffer m) {
    gl().glLoadMatrixd(
      m);
  }

  public static void glLoadMatrixd(double[] m,
                                   int m_offset) {
    gl().glLoadMatrixd(
      m,
      m_offset);
  }

  public static void glLoadMatrixf(FloatBuffer m) {
    gl().glLoadMatrixf(
      m);
  }

  public static void glLoadMatrixf(float[] m,
                                   int m_offset) {
    gl().glLoadMatrixf(
      m,
      m_offset);
  }

  public static void glLoadName(int name) {
    gl().glLoadName(
      name);
  }

  public static void glLoadProgramNV(int target,
                                     int id,
                                     int len,
                                     String program) {
    gl().glLoadProgramNV(
      target,
      id,
      len,
      program);
  }

  public static void glLoadTransposeMatrixd(DoubleBuffer m) {
    gl().glLoadTransposeMatrixd(
      m);
  }

  public static void glLoadTransposeMatrixd(double[] m,
                                            int m_offset) {
    gl().glLoadTransposeMatrixd(
      m,
      m_offset);
  }

  public static void glLoadTransposeMatrixf(FloatBuffer m) {
    gl().glLoadTransposeMatrixf(
      m);
  }

  public static void glLoadTransposeMatrixf(float[] m,
                                            int m_offset) {
    gl().glLoadTransposeMatrixf(
      m,
      m_offset);
  }

  public static void glLockArraysEXT(int x,
                                     int y) {
    gl().glLockArraysEXT(
      x,
      y);
  }

  public static void glLogicOp(int opcode) {
    gl().glLogicOp(
      opcode);
  }

  public static void glMap1d(int target,
                             double u1,
                             double u2,
                             int stride,
                             int order,
                             DoubleBuffer points) {
    gl().glMap1d(
      target,
      u1,
      u2,
      stride,
      order,
      points);
  }

  public static void glMap1d(int target,
                             double u1,
                             double u2,
                             int stride,
                             int order,
                             double[] points,
                             int points_offset) {
    gl().glMap1d(
      target,
      u1,
      u2,
      stride,
      order,
      points,
      points_offset);
  }

  public static void glMap1f(int target,
                             float u1,
                             float u2,
                             int stride,
                             int order,
                             FloatBuffer points) {
    gl().glMap1f(
      target,
      u1,
      u2,
      stride,
      order,
      points);
  }

  public static void glMap1f(int target,
                             float u1,
                             float u2,
                             int stride,
                             int order,
                             float[] points,
                             int points_offset) {
    gl().glMap1f(
      target,
      u1,
      u2,
      stride,
      order,
      points,
      points_offset);
  }

  public static void glMap2d(int target,
                             double u1,
                             double u2,
                             int ustride,
                             int uorder,
                             double v1,
                             double v2,
                             int vstride,
                             int vorder,
                             DoubleBuffer points) {
    gl().glMap2d(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points);
  }

  public static void glMap2d(int target,
                             double u1,
                             double u2,
                             int ustride,
                             int uorder,
                             double v1,
                             double v2,
                             int vstride,
                             int vorder,
                             double[] points,
                             int points_offset) {
    gl().glMap2d(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points,
      points_offset);
  }

  public static void glMap2f(int target,
                             float u1,
                             float u2,
                             int ustride,
                             int uorder,
                             float v1,
                             float v2,
                             int vstride,
                             int vorder,
                             FloatBuffer points) {
    gl().glMap2f(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points);
  }

  public static void glMap2f(int target,
                             float u1,
                             float u2,
                             int ustride,
                             int uorder,
                             float v1,
                             float v2,
                             int vstride,
                             int vorder,
                             float[] points,
                             int points_offset) {
    gl().glMap2f(
      target,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points,
      points_offset);
  }

  public static ByteBuffer glMapBuffer(int target,
                                       int access) {
    return gl().glMapBuffer(
      target,
      access);
  }

  public static ByteBuffer glMapBufferARB(int target,
                                          int access) {
    return gl().glMapBufferARB(
      target,
      access);
  }

  public static void glMapControlPointsNV(int target,
                                          int index,
                                          int type,
                                          int ustride,
                                          int vstride,
                                          int uorder,
                                          int vorder,
                                          boolean packed,
                                          Buffer points) {
    gl().glMapControlPointsNV(
      target,
      index,
      type,
      ustride,
      vstride,
      uorder,
      vorder,
      packed,
      points);
  }

  public static void glMapGrid1d(int un,
                                 double u1,
                                 double u2) {
    gl().glMapGrid1d(
      un,
      u1,
      u2);
  }

  public static void glMapGrid1f(int un,
                                 float u1,
                                 float u2) {
    gl().glMapGrid1f(
      un,
      u1,
      u2);
  }

  public static void glMapGrid2d(int un,
                                 double u1,
                                 double u2,
                                 int vn,
                                 double v1,
                                 double v2) {
    gl().glMapGrid2d(
      un,
      u1,
      u2,
      vn,
      v1,
      v2);
  }

  public static void glMapGrid2f(int un,
                                 float u1,
                                 float u2,
                                 int vn,
                                 float v1,
                                 float v2) {
    gl().glMapGrid2f(
      un,
      u1,
      u2,
      vn,
      v1,
      v2);
  }

  public static void glMapParameterfvNV(int target,
                                        int pname,
                                        FloatBuffer params) {
    gl().glMapParameterfvNV(
      target,
      pname,
      params);
  }

  public static void glMapParameterfvNV(int target,
                                        int pname,
                                        float[] params,
                                        int params_offset) {
    gl().glMapParameterfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMapParameterivNV(int target,
                                        int pname,
                                        IntBuffer params) {
    gl().glMapParameterivNV(
      target,
      pname,
      params);
  }

  public static void glMapParameterivNV(int target,
                                        int pname,
                                        int[] params,
                                        int params_offset) {
    gl().glMapParameterivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMapVertexAttrib1dAPPLE(int index,
                                              int size,
                                              double u1,
                                              double u2,
                                              int stride,
                                              int order,
                                              DoubleBuffer points) {
    gl().glMapVertexAttrib1dAPPLE(
      index,
      size,
      u1,
      u2,
      stride,
      order,
      points);
  }

  public static void glMapVertexAttrib1dAPPLE(int index,
                                              int size,
                                              double u1,
                                              double u2,
                                              int stride,
                                              int order,
                                              double[] points,
                                              int points_offset) {
    gl().glMapVertexAttrib1dAPPLE(
      index,
      size,
      u1,
      u2,
      stride,
      order,
      points,
      points_offset);
  }

  public static void glMapVertexAttrib1fAPPLE(int index,
                                              int size,
                                              float u1,
                                              float u2,
                                              int stride,
                                              int order,
                                              FloatBuffer points) {
    gl().glMapVertexAttrib1fAPPLE(
      index,
      size,
      u1,
      u2,
      stride,
      order,
      points);
  }

  public static void glMapVertexAttrib1fAPPLE(int index,
                                              int size,
                                              float u1,
                                              float u2,
                                              int stride,
                                              int order,
                                              float[] points,
                                              int points_offset) {
    gl().glMapVertexAttrib1fAPPLE(
      index,
      size,
      u1,
      u2,
      stride,
      order,
      points,
      points_offset);
  }

  public static void glMapVertexAttrib2dAPPLE(int index,
                                              int size,
                                              double u1,
                                              double u2,
                                              int ustride,
                                              int uorder,
                                              double v1,
                                              double v2,
                                              int vstride,
                                              int vorder,
                                              DoubleBuffer points) {
    gl().glMapVertexAttrib2dAPPLE(
      index,
      size,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points);
  }

  public static void glMapVertexAttrib2dAPPLE(int index,
                                              int size,
                                              double u1,
                                              double u2,
                                              int ustride,
                                              int uorder,
                                              double v1,
                                              double v2,
                                              int vstride,
                                              int vorder,
                                              double[] points,
                                              int points_offset) {
    gl().glMapVertexAttrib2dAPPLE(
      index,
      size,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points,
      points_offset);
  }

  public static void glMapVertexAttrib2fAPPLE(int index,
                                              int size,
                                              float u1,
                                              float u2,
                                              int ustride,
                                              int uorder,
                                              float v1,
                                              float v2,
                                              int vstride,
                                              int vorder,
                                              FloatBuffer points) {
    gl().glMapVertexAttrib2fAPPLE(
      index,
      size,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points);
  }

  public static void glMapVertexAttrib2fAPPLE(int index,
                                              int size,
                                              float u1,
                                              float u2,
                                              int ustride,
                                              int uorder,
                                              float v1,
                                              float v2,
                                              int vstride,
                                              int vorder,
                                              float[] points,
                                              int points_offset) {
    gl().glMapVertexAttrib2fAPPLE(
      index,
      size,
      u1,
      u2,
      ustride,
      uorder,
      v1,
      v2,
      vstride,
      vorder,
      points,
      points_offset);
  }

  public static void glMaterialf(int face,
                                 int pname,
                                 float param) {
    gl().glMaterialf(
      face,
      pname,
      param);
  }

  public static void glMaterialfv(int face,
                                  int pname,
                                  FloatBuffer params) {
    gl().glMaterialfv(
      face,
      pname,
      params);
  }

  public static void glMaterialfv(int face,
                                  int pname,
                                  float[] params,
                                  int params_offset) {
    gl().glMaterialfv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glMateriali(int face,
                                 int pname,
                                 int param) {
    gl().glMateriali(
      face,
      pname,
      param);
  }

  public static void glMaterialiv(int face,
                                  int pname,
                                  IntBuffer params) {
    gl().glMaterialiv(
      face,
      pname,
      params);
  }

  public static void glMaterialiv(int face,
                                  int pname,
                                  int[] params,
                                  int params_offset) {
    gl().glMaterialiv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glMatrixIndexPointerARB(int size,
                                             int type,
                                             int stride,
                                             Buffer pointer) {
    gl().glMatrixIndexPointerARB(
      size,
      type,
      stride,
      pointer);
  }

  public static void glMatrixIndexPointerARB(int size,
                                             int type,
                                             int stride,
                                             long pointer_buffer_offset) {
    gl().glMatrixIndexPointerARB(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glMatrixIndexubvARB(int size,
                                         ByteBuffer weights) {
    gl().glMatrixIndexubvARB(
      size,
      weights);
  }

  public static void glMatrixIndexubvARB(int size,
                                         byte[] weights,
                                         int weights_offset) {
    gl().glMatrixIndexubvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glMatrixIndexuivARB(int n,
                                         IntBuffer ids) {
    gl().glMatrixIndexuivARB(
      n,
      ids);
  }

  public static void glMatrixIndexuivARB(int n,
                                         int[] ids,
                                         int ids_offset) {
    gl().glMatrixIndexuivARB(
      n,
      ids,
      ids_offset);
  }

  public static void glMatrixIndexusvARB(int size,
                                         ShortBuffer weights) {
    gl().glMatrixIndexusvARB(
      size,
      weights);
  }

  public static void glMatrixIndexusvARB(int size,
                                         short[] weights,
                                         int weights_offset) {
    gl().glMatrixIndexusvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glMatrixMode(int mode) {
    gl().glMatrixMode(
      mode);
  }

  public static void glMinmax(int target,
                              int internalformat,
                              boolean sink) {
    gl().glMinmax(
      target,
      internalformat,
      sink);
  }

  public static void glMultMatrixd(DoubleBuffer m) {
    gl().glMultMatrixd(
      m);
  }

  public static void glMultMatrixd(double[] m,
                                   int m_offset) {
    gl().glMultMatrixd(
      m,
      m_offset);
  }

  public static void glMultMatrixf(FloatBuffer m) {
    gl().glMultMatrixf(
      m);
  }

  public static void glMultMatrixf(float[] m,
                                   int m_offset) {
    gl().glMultMatrixf(
      m,
      m_offset);
  }

  public static void glMultTransposeMatrixd(DoubleBuffer m) {
    gl().glMultTransposeMatrixd(
      m);
  }

  public static void glMultTransposeMatrixd(double[] m,
                                            int m_offset) {
    gl().glMultTransposeMatrixd(
      m,
      m_offset);
  }

  public static void glMultTransposeMatrixf(FloatBuffer m) {
    gl().glMultTransposeMatrixf(
      m);
  }

  public static void glMultTransposeMatrixf(float[] m,
                                            int m_offset) {
    gl().glMultTransposeMatrixf(
      m,
      m_offset);
  }

  public static void glMultiDrawArrays(int mode,
                                       IntBuffer first,
                                       IntBuffer count,
                                       int primcount) {
    gl().glMultiDrawArrays(
      mode,
      first,
      count,
      primcount);
  }

  public static void glMultiDrawArrays(int mode,
                                       int[] first,
                                       int first_offset,
                                       int[] count,
                                       int count_offset,
                                       int primcount) {
    gl().glMultiDrawArrays(
      mode,
      first,
      first_offset,
      count,
      count_offset,
      primcount);
  }

  public static void glMultiDrawArraysEXT(int mode,
                                          IntBuffer first,
                                          IntBuffer count,
                                          int primcount) {
    gl().glMultiDrawArraysEXT(
      mode,
      first,
      count,
      primcount);
  }

  public static void glMultiDrawArraysEXT(int mode,
                                          int[] first,
                                          int first_offset,
                                          int[] count,
                                          int count_offset,
                                          int primcount) {
    gl().glMultiDrawArraysEXT(
      mode,
      first,
      first_offset,
      count,
      count_offset,
      primcount);
  }

  public static void glMultiDrawElementArrayAPPLE(int mode,
                                                  IntBuffer first,
                                                  IntBuffer count,
                                                  int primcount) {
    gl().glMultiDrawElementArrayAPPLE(
      mode,
      first,
      count,
      primcount);
  }

  public static void glMultiDrawElementArrayAPPLE(int mode,
                                                  int[] first,
                                                  int first_offset,
                                                  int[] count,
                                                  int count_offset,
                                                  int primcount) {
    gl().glMultiDrawElementArrayAPPLE(
      mode,
      first,
      first_offset,
      count,
      count_offset,
      primcount);
  }

  public static void glMultiDrawElements(int mode,
                                         IntBuffer count,
                                         int type,
                                         Buffer[] indices,
                                         int primcount) {
    gl().glMultiDrawElements(
      mode,
      count,
      type,
      indices,
      primcount);
  }

  public static void glMultiDrawElements(int mode,
                                         int[] count,
                                         int count_offset,
                                         int type,
                                         Buffer[] indices,
                                         int primcount) {
    gl().glMultiDrawElements(
      mode,
      count,
      count_offset,
      type,
      indices,
      primcount);
  }

  public static void glMultiDrawElementsEXT(int mode,
                                            IntBuffer count,
                                            int type,
                                            Buffer[] indices,
                                            int primcount) {
    gl().glMultiDrawElementsEXT(
      mode,
      count,
      type,
      indices,
      primcount);
  }

  public static void glMultiDrawElementsEXT(int mode,
                                            int[] count,
                                            int count_offset,
                                            int type,
                                            Buffer[] indices,
                                            int primcount) {
    gl().glMultiDrawElementsEXT(
      mode,
      count,
      count_offset,
      type,
      indices,
      primcount);
  }

  public static void glMultiDrawRangeElementArrayAPPLE(int mode,
                                                       int start,
                                                       int end,
                                                       IntBuffer first,
                                                       IntBuffer count,
                                                       int primcount) {
    gl().glMultiDrawRangeElementArrayAPPLE(
      mode,
      start,
      end,
      first,
      count,
      primcount);
  }

  public static void glMultiDrawRangeElementArrayAPPLE(int mode,
                                                       int start,
                                                       int end,
                                                       int[] first,
                                                       int first_offset,
                                                       int[] count,
                                                       int count_offset,
                                                       int primcount) {
    gl().glMultiDrawRangeElementArrayAPPLE(
      mode,
      start,
      end,
      first,
      first_offset,
      count,
      count_offset,
      primcount);
  }

  public static void glMultiModeDrawArraysIBM(IntBuffer mode,
                                              IntBuffer first,
                                              IntBuffer count,
                                              int primcount,
                                              int modestride) {
    gl().glMultiModeDrawArraysIBM(
      mode,
      first,
      count,
      primcount,
      modestride);
  }

  public static void glMultiModeDrawArraysIBM(int[] mode,
                                              int mode_offset,
                                              int[] first,
                                              int first_offset,
                                              int[] count,
                                              int count_offset,
                                              int primcount,
                                              int modestride) {
    gl().glMultiModeDrawArraysIBM(
      mode,
      mode_offset,
      first,
      first_offset,
      count,
      count_offset,
      primcount,
      modestride);
  }

  public static void glMultiModeDrawElementsIBM(IntBuffer mode,
                                                IntBuffer count,
                                                int type,
                                                Buffer[] indices,
                                                int primcount,
                                                int modestride) {
    gl().glMultiModeDrawElementsIBM(
      mode,
      count,
      type,
      indices,
      primcount,
      modestride);
  }

  public static void glMultiModeDrawElementsIBM(int[] mode,
                                                int mode_offset,
                                                int[] count,
                                                int count_offset,
                                                int type,
                                                Buffer[] indices,
                                                int primcount,
                                                int modestride) {
    gl().glMultiModeDrawElementsIBM(
      mode,
      mode_offset,
      count,
      count_offset,
      type,
      indices,
      primcount,
      modestride);
  }

  public static void glMultiTexCoord1d(int target,
                                       double s) {
    gl().glMultiTexCoord1d(
      target,
      s);
  }

  public static void glMultiTexCoord1dv(int target,
                                        DoubleBuffer v) {
    gl().glMultiTexCoord1dv(
      target,
      v);
  }

  public static void glMultiTexCoord1dv(int target,
                                        double[] v,
                                        int v_offset) {
    gl().glMultiTexCoord1dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1f(int target,
                                       float s) {
    gl().glMultiTexCoord1f(
      target,
      s);
  }

  public static void glMultiTexCoord1fv(int target,
                                        FloatBuffer v) {
    gl().glMultiTexCoord1fv(
      target,
      v);
  }

  public static void glMultiTexCoord1fv(int target,
                                        float[] v,
                                        int v_offset) {
    gl().glMultiTexCoord1fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1hNV(int target,
                                         short s) {
    gl().glMultiTexCoord1hNV(
      target,
      s);
  }

  public static void glMultiTexCoord1hvNV(int index,
                                          ShortBuffer v) {
    gl().glMultiTexCoord1hvNV(
      index,
      v);
  }

  public static void glMultiTexCoord1hvNV(int index,
                                          short[] v,
                                          int v_offset) {
    gl().glMultiTexCoord1hvNV(
      index,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1i(int target,
                                       int s) {
    gl().glMultiTexCoord1i(
      target,
      s);
  }

  public static void glMultiTexCoord1iv(int target,
                                        IntBuffer v) {
    gl().glMultiTexCoord1iv(
      target,
      v);
  }

  public static void glMultiTexCoord1iv(int target,
                                        int[] v,
                                        int v_offset) {
    gl().glMultiTexCoord1iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1s(int target,
                                       short s) {
    gl().glMultiTexCoord1s(
      target,
      s);
  }

  public static void glMultiTexCoord1sv(int target,
                                        ShortBuffer v) {
    gl().glMultiTexCoord1sv(
      target,
      v);
  }

  public static void glMultiTexCoord1sv(int target,
                                        short[] v,
                                        int v_offset) {
    gl().glMultiTexCoord1sv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2d(int target,
                                       double s,
                                       double t) {
    gl().glMultiTexCoord2d(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2dv(int target,
                                        DoubleBuffer v) {
    gl().glMultiTexCoord2dv(
      target,
      v);
  }

  public static void glMultiTexCoord2dv(int target,
                                        double[] v,
                                        int v_offset) {
    gl().glMultiTexCoord2dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2f(int target,
                                       float s,
                                       float t) {
    gl().glMultiTexCoord2f(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2fv(int target,
                                        FloatBuffer v) {
    gl().glMultiTexCoord2fv(
      target,
      v);
  }

  public static void glMultiTexCoord2fv(int target,
                                        float[] v,
                                        int v_offset) {
    gl().glMultiTexCoord2fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2hNV(int target,
                                         short s,
                                         short t) {
    gl().glMultiTexCoord2hNV(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2hvNV(int index,
                                          ShortBuffer v) {
    gl().glMultiTexCoord2hvNV(
      index,
      v);
  }

  public static void glMultiTexCoord2hvNV(int index,
                                          short[] v,
                                          int v_offset) {
    gl().glMultiTexCoord2hvNV(
      index,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2i(int target,
                                       int s,
                                       int t) {
    gl().glMultiTexCoord2i(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2iv(int target,
                                        IntBuffer v) {
    gl().glMultiTexCoord2iv(
      target,
      v);
  }

  public static void glMultiTexCoord2iv(int target,
                                        int[] v,
                                        int v_offset) {
    gl().glMultiTexCoord2iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2s(int target,
                                       short s,
                                       short t) {
    gl().glMultiTexCoord2s(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2sv(int target,
                                        ShortBuffer v) {
    gl().glMultiTexCoord2sv(
      target,
      v);
  }

  public static void glMultiTexCoord2sv(int target,
                                        short[] v,
                                        int v_offset) {
    gl().glMultiTexCoord2sv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3d(int target,
                                       double s,
                                       double t,
                                       double r) {
    gl().glMultiTexCoord3d(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3dv(int target,
                                        DoubleBuffer v) {
    gl().glMultiTexCoord3dv(
      target,
      v);
  }

  public static void glMultiTexCoord3dv(int target,
                                        double[] v,
                                        int v_offset) {
    gl().glMultiTexCoord3dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3f(int target,
                                       float s,
                                       float t,
                                       float r) {
    gl().glMultiTexCoord3f(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3fv(int target,
                                        FloatBuffer v) {
    gl().glMultiTexCoord3fv(
      target,
      v);
  }

  public static void glMultiTexCoord3fv(int target,
                                        float[] v,
                                        int v_offset) {
    gl().glMultiTexCoord3fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3hNV(int target,
                                         short s,
                                         short t,
                                         short r) {
    gl().glMultiTexCoord3hNV(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3hvNV(int index,
                                          ShortBuffer v) {
    gl().glMultiTexCoord3hvNV(
      index,
      v);
  }

  public static void glMultiTexCoord3hvNV(int index,
                                          short[] v,
                                          int v_offset) {
    gl().glMultiTexCoord3hvNV(
      index,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3i(int target,
                                       int s,
                                       int t,
                                       int r) {
    gl().glMultiTexCoord3i(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3iv(int target,
                                        IntBuffer v) {
    gl().glMultiTexCoord3iv(
      target,
      v);
  }

  public static void glMultiTexCoord3iv(int target,
                                        int[] v,
                                        int v_offset) {
    gl().glMultiTexCoord3iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3s(int target,
                                       short s,
                                       short t,
                                       short r) {
    gl().glMultiTexCoord3s(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3sv(int target,
                                        ShortBuffer v) {
    gl().glMultiTexCoord3sv(
      target,
      v);
  }

  public static void glMultiTexCoord3sv(int target,
                                        short[] v,
                                        int v_offset) {
    gl().glMultiTexCoord3sv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4d(int target,
                                       double s,
                                       double t,
                                       double r,
                                       double q) {
    gl().glMultiTexCoord4d(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glMultiTexCoord4dv(int target,
                                        DoubleBuffer v) {
    gl().glMultiTexCoord4dv(
      target,
      v);
  }

  public static void glMultiTexCoord4dv(int target,
                                        double[] v,
                                        int v_offset) {
    gl().glMultiTexCoord4dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4f(int target,
                                       float s,
                                       float t,
                                       float r,
                                       float q) {
    gl().glMultiTexCoord4f(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glMultiTexCoord4fv(int target,
                                        FloatBuffer v) {
    gl().glMultiTexCoord4fv(
      target,
      v);
  }

  public static void glMultiTexCoord4fv(int target,
                                        float[] v,
                                        int v_offset) {
    gl().glMultiTexCoord4fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4hNV(int target,
                                         short s,
                                         short t,
                                         short r,
                                         short q) {
    gl().glMultiTexCoord4hNV(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glMultiTexCoord4hvNV(int index,
                                          ShortBuffer v) {
    gl().glMultiTexCoord4hvNV(
      index,
      v);
  }

  public static void glMultiTexCoord4hvNV(int index,
                                          short[] v,
                                          int v_offset) {
    gl().glMultiTexCoord4hvNV(
      index,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4i(int target,
                                       int start,
                                       int x,
                                       int y,
                                       int width) {
    gl().glMultiTexCoord4i(
      target,
      start,
      x,
      y,
      width);
  }

  public static void glMultiTexCoord4iv(int target,
                                        IntBuffer v) {
    gl().glMultiTexCoord4iv(
      target,
      v);
  }

  public static void glMultiTexCoord4iv(int target,
                                        int[] v,
                                        int v_offset) {
    gl().glMultiTexCoord4iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4s(int target,
                                       short s,
                                       short t,
                                       short r,
                                       short q) {
    gl().glMultiTexCoord4s(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glMultiTexCoord4sv(int target,
                                        ShortBuffer v) {
    gl().glMultiTexCoord4sv(
      target,
      v);
  }

  public static void glMultiTexCoord4sv(int target,
                                        short[] v,
                                        int v_offset) {
    gl().glMultiTexCoord4sv(
      target,
      v,
      v_offset);
  }

  public static int glNewBufferRegion(int type) {
    return gl().glNewBufferRegion(
      type);
  }

  public static void glNewList(int list,
                               int mode) {
    gl().glNewList(
      list,
      mode);
  }

  public static int glNewObjectBufferATI(int size,
                                         Buffer pointer,
                                         int usage) {
    return gl().glNewObjectBufferATI(
      size,
      pointer,
      usage);
  }

  public static void glNormal3b(byte nx,
                                byte ny,
                                byte nz) {
    gl().glNormal3b(
      nx,
      ny,
      nz);
  }

  public static void glNormal3bv(ByteBuffer v) {
    gl().glNormal3bv(
      v);
  }

  public static void glNormal3bv(byte[] v,
                                 int v_offset) {
    gl().glNormal3bv(
      v,
      v_offset);
  }

  public static void glNormal3d(double nx,
                                double ny,
                                double nz) {
    gl().glNormal3d(
      nx,
      ny,
      nz);
  }

  public static void glNormal3dv(DoubleBuffer v) {
    gl().glNormal3dv(
      v);
  }

  public static void glNormal3dv(double[] v,
                                 int v_offset) {
    gl().glNormal3dv(
      v,
      v_offset);
  }

  public static void glNormal3f(float nx,
                                float ny,
                                float nz) {
    gl().glNormal3f(
      nx,
      ny,
      nz);
  }

  public static void glNormal3fVertex3fSUN(float r,
                                           float g,
                                           float b,
                                           float x,
                                           float y,
                                           float z) {
    gl().glNormal3fVertex3fSUN(
      r,
      g,
      b,
      x,
      y,
      z);
  }

  public static void glNormal3fVertex3fvSUN(FloatBuffer c,
                                            FloatBuffer v) {
    gl().glNormal3fVertex3fvSUN(
      c,
      v);
  }

  public static void glNormal3fVertex3fvSUN(float[] c,
                                            int c_offset,
                                            float[] v,
                                            int v_offset) {
    gl().glNormal3fVertex3fvSUN(
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glNormal3fv(FloatBuffer v) {
    gl().glNormal3fv(
      v);
  }

  public static void glNormal3fv(float[] v,
                                 int v_offset) {
    gl().glNormal3fv(
      v,
      v_offset);
  }

  public static void glNormal3hNV(short red,
                                  short green,
                                  short blue) {
    gl().glNormal3hNV(
      red,
      green,
      blue);
  }

  public static void glNormal3hvNV(ShortBuffer v) {
    gl().glNormal3hvNV(
      v);
  }

  public static void glNormal3hvNV(short[] v,
                                   int v_offset) {
    gl().glNormal3hvNV(
      v,
      v_offset);
  }

  public static void glNormal3i(int nx,
                                int ny,
                                int nz) {
    gl().glNormal3i(
      nx,
      ny,
      nz);
  }

  public static void glNormal3iv(IntBuffer v) {
    gl().glNormal3iv(
      v);
  }

  public static void glNormal3iv(int[] v,
                                 int v_offset) {
    gl().glNormal3iv(
      v,
      v_offset);
  }

  public static void glNormal3s(short nx,
                                short ny,
                                short nz) {
    gl().glNormal3s(
      nx,
      ny,
      nz);
  }

  public static void glNormal3sv(ShortBuffer v) {
    gl().glNormal3sv(
      v);
  }

  public static void glNormal3sv(short[] v,
                                 int v_offset) {
    gl().glNormal3sv(
      v,
      v_offset);
  }

  public static void glNormalPointer(int type,
                                     int stride,
                                     Buffer ptr) {
    gl().glNormalPointer(
      type,
      stride,
      ptr);
  }

  public static void glNormalPointer(int type,
                                     int stride,
                                     long ptr_buffer_offset) {
    gl().glNormalPointer(
      type,
      stride,
      ptr_buffer_offset);
  }

  public static void glNormalStream3bATI(int stream,
                                         byte nx,
                                         byte ny,
                                         byte nz) {
    gl().glNormalStream3bATI(
      stream,
      nx,
      ny,
      nz);
  }

  public static void glNormalStream3bvATI(int index,
                                          ByteBuffer v) {
    gl().glNormalStream3bvATI(
      index,
      v);
  }

  public static void glNormalStream3bvATI(int index,
                                          byte[] v,
                                          int v_offset) {
    gl().glNormalStream3bvATI(
      index,
      v,
      v_offset);
  }

  public static void glNormalStream3dATI(int target,
                                         double s,
                                         double t,
                                         double r) {
    gl().glNormalStream3dATI(
      target,
      s,
      t,
      r);
  }

  public static void glNormalStream3dvATI(int target,
                                          DoubleBuffer v) {
    gl().glNormalStream3dvATI(
      target,
      v);
  }

  public static void glNormalStream3dvATI(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glNormalStream3dvATI(
      target,
      v,
      v_offset);
  }

  public static void glNormalStream3fATI(int target,
                                         float s,
                                         float t,
                                         float r) {
    gl().glNormalStream3fATI(
      target,
      s,
      t,
      r);
  }

  public static void glNormalStream3fvATI(int target,
                                          FloatBuffer v) {
    gl().glNormalStream3fvATI(
      target,
      v);
  }

  public static void glNormalStream3fvATI(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glNormalStream3fvATI(
      target,
      v,
      v_offset);
  }

  public static void glNormalStream3iATI(int target,
                                         int s,
                                         int t,
                                         int r) {
    gl().glNormalStream3iATI(
      target,
      s,
      t,
      r);
  }

  public static void glNormalStream3ivATI(int target,
                                          IntBuffer v) {
    gl().glNormalStream3ivATI(
      target,
      v);
  }

  public static void glNormalStream3ivATI(int target,
                                          int[] v,
                                          int v_offset) {
    gl().glNormalStream3ivATI(
      target,
      v,
      v_offset);
  }

  public static void glNormalStream3sATI(int target,
                                         short s,
                                         short t,
                                         short r) {
    gl().glNormalStream3sATI(
      target,
      s,
      t,
      r);
  }

  public static void glNormalStream3svATI(int target,
                                          ShortBuffer v) {
    gl().glNormalStream3svATI(
      target,
      v);
  }

  public static void glNormalStream3svATI(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glNormalStream3svATI(
      target,
      v,
      v_offset);
  }

  public static void glOrtho(double left,
                             double right,
                             double bottom,
                             double top,
                             double near_val,
                             double far_val) {
    gl().glOrtho(
      left,
      right,
      bottom,
      top,
      near_val,
      far_val);
  }

  public static void glPNTrianglesfATI(int target,
                                       float s) {
    gl().glPNTrianglesfATI(
      target,
      s);
  }

  public static void glPNTrianglesiATI(int target,
                                       int s) {
    gl().glPNTrianglesiATI(
      target,
      s);
  }

  public static void glPassTexCoordATI(int red,
                                       int green,
                                       int blue) {
    gl().glPassTexCoordATI(
      red,
      green,
      blue);
  }

  public static void glPassThrough(float token) {
    gl().glPassThrough(
      token);
  }

  public static void glPixelDataRangeNV(int target,
                                        int level,
                                        Buffer img) {
    gl().glPixelDataRangeNV(
      target,
      level,
      img);
  }

  public static void glPixelMapfv(int map,
                                  int mapsize,
                                  FloatBuffer values) {
    gl().glPixelMapfv(
      map,
      mapsize,
      values);
  }

  public static void glPixelMapfv(int map,
                                  int mapsize,
                                  float[] values,
                                  int values_offset) {
    gl().glPixelMapfv(
      map,
      mapsize,
      values,
      values_offset);
  }

  public static void glPixelMapfv(int map,
                                  int mapsize,
                                  long values_buffer_offset) {
    gl().glPixelMapfv(
      map,
      mapsize,
      values_buffer_offset);
  }

  public static void glPixelMapuiv(int map,
                                   int mapsize,
                                   IntBuffer values) {
    gl().glPixelMapuiv(
      map,
      mapsize,
      values);
  }

  public static void glPixelMapuiv(int map,
                                   int mapsize,
                                   int[] values,
                                   int values_offset) {
    gl().glPixelMapuiv(
      map,
      mapsize,
      values,
      values_offset);
  }

  public static void glPixelMapuiv(int map,
                                   int mapsize,
                                   long values_buffer_offset) {
    gl().glPixelMapuiv(
      map,
      mapsize,
      values_buffer_offset);
  }

  public static void glPixelMapusv(int map,
                                   int mapsize,
                                   ShortBuffer values) {
    gl().glPixelMapusv(
      map,
      mapsize,
      values);
  }

  public static void glPixelMapusv(int map,
                                   int mapsize,
                                   short[] values,
                                   int values_offset) {
    gl().glPixelMapusv(
      map,
      mapsize,
      values,
      values_offset);
  }

  public static void glPixelMapusv(int map,
                                   int mapsize,
                                   long values_buffer_offset) {
    gl().glPixelMapusv(
      map,
      mapsize,
      values_buffer_offset);
  }

  public static void glPixelStoref(int pname,
                                   float param) {
    gl().glPixelStoref(
      pname,
      param);
  }

  public static void glPixelStorei(int pname,
                                   int param) {
    gl().glPixelStorei(
      pname,
      param);
  }

  public static void glPixelTexGenParameterfSGIS(int target,
                                                 float s) {
    gl().glPixelTexGenParameterfSGIS(
      target,
      s);
  }

  public static void glPixelTexGenParameterfvSGIS(int target,
                                                  FloatBuffer v) {
    gl().glPixelTexGenParameterfvSGIS(
      target,
      v);
  }

  public static void glPixelTexGenParameterfvSGIS(int target,
                                                  float[] v,
                                                  int v_offset) {
    gl().glPixelTexGenParameterfvSGIS(
      target,
      v,
      v_offset);
  }

  public static void glPixelTexGenParameteriSGIS(int target,
                                                 int s) {
    gl().glPixelTexGenParameteriSGIS(
      target,
      s);
  }

  public static void glPixelTexGenParameterivSGIS(int target,
                                                  IntBuffer v) {
    gl().glPixelTexGenParameterivSGIS(
      target,
      v);
  }

  public static void glPixelTexGenParameterivSGIS(int target,
                                                  int[] v,
                                                  int v_offset) {
    gl().glPixelTexGenParameterivSGIS(
      target,
      v,
      v_offset);
  }

  public static void glPixelTexGenSGIX(int mode) {
    gl().glPixelTexGenSGIX(
      mode);
  }

  public static void glPixelTransferf(int pname,
                                      float param) {
    gl().glPixelTransferf(
      pname,
      param);
  }

  public static void glPixelTransferi(int pname,
                                      int param) {
    gl().glPixelTransferi(
      pname,
      param);
  }

  public static void glPixelTransformParameterfEXT(int target,
                                                   int pname,
                                                   float params) {
    gl().glPixelTransformParameterfEXT(
      target,
      pname,
      params);
  }

  public static void glPixelTransformParameterfvEXT(int target,
                                                    int pname,
                                                    FloatBuffer params) {
    gl().glPixelTransformParameterfvEXT(
      target,
      pname,
      params);
  }

  public static void glPixelTransformParameterfvEXT(int target,
                                                    int pname,
                                                    float[] params,
                                                    int params_offset) {
    gl().glPixelTransformParameterfvEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glPixelTransformParameteriEXT(int target,
                                                   int pname,
                                                   int params) {
    gl().glPixelTransformParameteriEXT(
      target,
      pname,
      params);
  }

  public static void glPixelTransformParameterivEXT(int target,
                                                    int pname,
                                                    IntBuffer params) {
    gl().glPixelTransformParameterivEXT(
      target,
      pname,
      params);
  }

  public static void glPixelTransformParameterivEXT(int target,
                                                    int pname,
                                                    int[] params,
                                                    int params_offset) {
    gl().glPixelTransformParameterivEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glPixelZoom(float xfactor,
                                 float yfactor) {
    gl().glPixelZoom(
      xfactor,
      yfactor);
  }

  public static void glPointParameterf(int target,
                                       float s) {
    gl().glPointParameterf(
      target,
      s);
  }

  public static void glPointParameterfARB(int target,
                                          float s) {
    gl().glPointParameterfARB(
      target,
      s);
  }

  public static void glPointParameterfEXT(int target,
                                          float s) {
    gl().glPointParameterfEXT(
      target,
      s);
  }

  public static void glPointParameterfSGIS(int target,
                                           float s) {
    gl().glPointParameterfSGIS(
      target,
      s);
  }

  public static void glPointParameterfv(int target,
                                        FloatBuffer v) {
    gl().glPointParameterfv(
      target,
      v);
  }

  public static void glPointParameterfv(int target,
                                        float[] v,
                                        int v_offset) {
    gl().glPointParameterfv(
      target,
      v,
      v_offset);
  }

  public static void glPointParameterfvARB(int target,
                                           FloatBuffer v) {
    gl().glPointParameterfvARB(
      target,
      v);
  }

  public static void glPointParameterfvARB(int target,
                                           float[] v,
                                           int v_offset) {
    gl().glPointParameterfvARB(
      target,
      v,
      v_offset);
  }

  public static void glPointParameterfvEXT(int target,
                                           FloatBuffer v) {
    gl().glPointParameterfvEXT(
      target,
      v);
  }

  public static void glPointParameterfvEXT(int target,
                                           float[] v,
                                           int v_offset) {
    gl().glPointParameterfvEXT(
      target,
      v,
      v_offset);
  }

  public static void glPointParameterfvSGIS(int target,
                                            FloatBuffer v) {
    gl().glPointParameterfvSGIS(
      target,
      v);
  }

  public static void glPointParameterfvSGIS(int target,
                                            float[] v,
                                            int v_offset) {
    gl().glPointParameterfvSGIS(
      target,
      v,
      v_offset);
  }

  public static void glPointParameteri(int target,
                                       int s) {
    gl().glPointParameteri(
      target,
      s);
  }

  public static void glPointParameteriNV(int target,
                                         int s) {
    gl().glPointParameteriNV(
      target,
      s);
  }

  public static void glPointParameteriv(int target,
                                        IntBuffer v) {
    gl().glPointParameteriv(
      target,
      v);
  }

  public static void glPointParameteriv(int target,
                                        int[] v,
                                        int v_offset) {
    gl().glPointParameteriv(
      target,
      v,
      v_offset);
  }

  public static void glPointParameterivNV(int target,
                                          IntBuffer v) {
    gl().glPointParameterivNV(
      target,
      v);
  }

  public static void glPointParameterivNV(int target,
                                          int[] v,
                                          int v_offset) {
    gl().glPointParameterivNV(
      target,
      v,
      v_offset);
  }

  public static void glPointSize(float size) {
    gl().glPointSize(
      size);
  }

  public static int glPollAsyncSGIX(IntBuffer markerp) {
    return gl().glPollAsyncSGIX(
      markerp);
  }

  public static int glPollAsyncSGIX(int[] markerp,
                                    int markerp_offset) {
    return gl().glPollAsyncSGIX(
      markerp,
      markerp_offset);
  }

  public static int glPollInstrumentsSGIX(IntBuffer marker_p) {
    return gl().glPollInstrumentsSGIX(
      marker_p);
  }

  public static int glPollInstrumentsSGIX(int[] marker_p,
                                          int marker_p_offset) {
    return gl().glPollInstrumentsSGIX(
      marker_p,
      marker_p_offset);
  }

  public static void glPolygonMode(int face,
                                   int mode) {
    gl().glPolygonMode(
      face,
      mode);
  }

  public static void glPolygonOffset(float factor,
                                     float units) {
    gl().glPolygonOffset(
      factor,
      units);
  }

  public static void glPolygonStipple(ByteBuffer mask) {
    gl().glPolygonStipple(
      mask);
  }

  public static void glPolygonStipple(byte[] mask,
                                      int mask_offset) {
    gl().glPolygonStipple(
      mask,
      mask_offset);
  }

  public static void glPolygonStipple(long mask_buffer_offset) {
    gl().glPolygonStipple(
      mask_buffer_offset);
  }

  public static void glPopAttrib() {
    gl().glPopAttrib();
  }

  public static void glPopClientAttrib() {
    gl().glPopClientAttrib();
  }

  public static void glPopMatrix() {
    gl().glPopMatrix();
  }

  public static void glPopName() {
    gl().glPopName();
  }

  public static void glPrimitiveRestartIndexNV(int mode) {
    gl().glPrimitiveRestartIndexNV(
      mode);
  }

  public static void glPrimitiveRestartNV() {
    gl().glPrimitiveRestartNV();
  }

  public static void glPrioritizeTextures(int n,
                                          IntBuffer textures,
                                          FloatBuffer priorities) {
    gl().glPrioritizeTextures(
      n,
      textures,
      priorities);
  }

  public static void glPrioritizeTextures(int n,
                                          int[] textures,
                                          int textures_offset,
                                          float[] priorities,
                                          int priorities_offset) {
    gl().glPrioritizeTextures(
      n,
      textures,
      textures_offset,
      priorities,
      priorities_offset);
  }

  public static void glProgramEnvParameter4dARB(int target,
                                                int index,
                                                double x,
                                                double y,
                                                double z,
                                                double w) {
    gl().glProgramEnvParameter4dARB(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramEnvParameter4dvARB(int target,
                                                 int index,
                                                 DoubleBuffer params) {
    gl().glProgramEnvParameter4dvARB(
      target,
      index,
      params);
  }

  public static void glProgramEnvParameter4dvARB(int target,
                                                 int index,
                                                 double[] params,
                                                 int params_offset) {
    gl().glProgramEnvParameter4dvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramEnvParameter4fARB(int target,
                                                int index,
                                                float x,
                                                float y,
                                                float z,
                                                float w) {
    gl().glProgramEnvParameter4fARB(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramEnvParameter4fvARB(int target,
                                                 int pname,
                                                 FloatBuffer params) {
    gl().glProgramEnvParameter4fvARB(
      target,
      pname,
      params);
  }

  public static void glProgramEnvParameter4fvARB(int target,
                                                 int pname,
                                                 float[] params,
                                                 int params_offset) {
    gl().glProgramEnvParameter4fvARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glProgramLocalParameter4dARB(int target,
                                                  int index,
                                                  double x,
                                                  double y,
                                                  double z,
                                                  double w) {
    gl().glProgramLocalParameter4dARB(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramLocalParameter4dvARB(int target,
                                                   int index,
                                                   DoubleBuffer params) {
    gl().glProgramLocalParameter4dvARB(
      target,
      index,
      params);
  }

  public static void glProgramLocalParameter4dvARB(int target,
                                                   int index,
                                                   double[] params,
                                                   int params_offset) {
    gl().glProgramLocalParameter4dvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramLocalParameter4fARB(int target,
                                                  int index,
                                                  float x,
                                                  float y,
                                                  float z,
                                                  float w) {
    gl().glProgramLocalParameter4fARB(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramLocalParameter4fvARB(int target,
                                                   int pname,
                                                   FloatBuffer params) {
    gl().glProgramLocalParameter4fvARB(
      target,
      pname,
      params);
  }

  public static void glProgramLocalParameter4fvARB(int target,
                                                   int pname,
                                                   float[] params,
                                                   int params_offset) {
    gl().glProgramLocalParameter4fvARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glProgramNamedParameter4dNV(int id,
                                                 int len,
                                                 String name,
                                                 double x,
                                                 double y,
                                                 double z,
                                                 double w) {
    gl().glProgramNamedParameter4dNV(
      id,
      len,
      name,
      x,
      y,
      z,
      w);
  }

  public static void glProgramNamedParameter4dvNV(int id,
                                                  int len,
                                                  String name,
                                                  DoubleBuffer v) {
    gl().glProgramNamedParameter4dvNV(
      id,
      len,
      name,
      v);
  }

  public static void glProgramNamedParameter4dvNV(int id,
                                                  int len,
                                                  String name,
                                                  double[] v,
                                                  int v_offset) {
    gl().glProgramNamedParameter4dvNV(
      id,
      len,
      name,
      v,
      v_offset);
  }

  public static void glProgramNamedParameter4fNV(int id,
                                                 int len,
                                                 String name,
                                                 float x,
                                                 float y,
                                                 float z,
                                                 float w) {
    gl().glProgramNamedParameter4fNV(
      id,
      len,
      name,
      x,
      y,
      z,
      w);
  }

  public static void glProgramNamedParameter4fvNV(int id,
                                                  int len,
                                                  String name,
                                                  FloatBuffer v) {
    gl().glProgramNamedParameter4fvNV(
      id,
      len,
      name,
      v);
  }

  public static void glProgramNamedParameter4fvNV(int id,
                                                  int len,
                                                  String name,
                                                  float[] v,
                                                  int v_offset) {
    gl().glProgramNamedParameter4fvNV(
      id,
      len,
      name,
      v,
      v_offset);
  }

  public static void glProgramParameter4dNV(int target,
                                            int index,
                                            double x,
                                            double y,
                                            double z,
                                            double w) {
    gl().glProgramParameter4dNV(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramParameter4dvNV(int target,
                                             int index,
                                             DoubleBuffer params) {
    gl().glProgramParameter4dvNV(
      target,
      index,
      params);
  }

  public static void glProgramParameter4dvNV(int target,
                                             int index,
                                             double[] params,
                                             int params_offset) {
    gl().glProgramParameter4dvNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramParameter4fNV(int target,
                                            int index,
                                            float x,
                                            float y,
                                            float z,
                                            float w) {
    gl().glProgramParameter4fNV(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramParameter4fvNV(int target,
                                             int pname,
                                             FloatBuffer params) {
    gl().glProgramParameter4fvNV(
      target,
      pname,
      params);
  }

  public static void glProgramParameter4fvNV(int target,
                                             int pname,
                                             float[] params,
                                             int params_offset) {
    gl().glProgramParameter4fvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glProgramParameters4dvNV(int target,
                                              int index,
                                              int count,
                                              DoubleBuffer v) {
    gl().glProgramParameters4dvNV(
      target,
      index,
      count,
      v);
  }

  public static void glProgramParameters4dvNV(int target,
                                              int index,
                                              int count,
                                              double[] v,
                                              int v_offset) {
    gl().glProgramParameters4dvNV(
      target,
      index,
      count,
      v,
      v_offset);
  }

  public static void glProgramParameters4fvNV(int target,
                                              int index,
                                              int count,
                                              FloatBuffer v) {
    gl().glProgramParameters4fvNV(
      target,
      index,
      count,
      v);
  }

  public static void glProgramParameters4fvNV(int target,
                                              int index,
                                              int count,
                                              float[] v,
                                              int v_offset) {
    gl().glProgramParameters4fvNV(
      target,
      index,
      count,
      v,
      v_offset);
  }

  public static void glProgramStringARB(int target,
                                        int format,
                                        int len,
                                        String string) {
    gl().glProgramStringARB(
      target,
      format,
      len,
      string);
  }

  public static void glPushAttrib(int mask) {
    gl().glPushAttrib(
      mask);
  }

  public static void glPushClientAttrib(int mask) {
    gl().glPushClientAttrib(
      mask);
  }

  public static void glPushMatrix() {
    gl().glPushMatrix();
  }

  public static void glPushName(int name) {
    gl().glPushName(
      name);
  }

  public static void glRasterPos2d(double x,
                                   double y) {
    gl().glRasterPos2d(
      x,
      y);
  }

  public static void glRasterPos2dv(DoubleBuffer v) {
    gl().glRasterPos2dv(
      v);
  }

  public static void glRasterPos2dv(double[] v,
                                    int v_offset) {
    gl().glRasterPos2dv(
      v,
      v_offset);
  }

  public static void glRasterPos2f(float x,
                                   float y) {
    gl().glRasterPos2f(
      x,
      y);
  }

  public static void glRasterPos2fv(FloatBuffer v) {
    gl().glRasterPos2fv(
      v);
  }

  public static void glRasterPos2fv(float[] v,
                                    int v_offset) {
    gl().glRasterPos2fv(
      v,
      v_offset);
  }

  public static void glRasterPos2i(int x,
                                   int y) {
    gl().glRasterPos2i(
      x,
      y);
  }

  public static void glRasterPos2iv(IntBuffer v) {
    gl().glRasterPos2iv(
      v);
  }

  public static void glRasterPos2iv(int[] v,
                                    int v_offset) {
    gl().glRasterPos2iv(
      v,
      v_offset);
  }

  public static void glRasterPos2s(short x,
                                   short y) {
    gl().glRasterPos2s(
      x,
      y);
  }

  public static void glRasterPos2sv(ShortBuffer v) {
    gl().glRasterPos2sv(
      v);
  }

  public static void glRasterPos2sv(short[] v,
                                    int v_offset) {
    gl().glRasterPos2sv(
      v,
      v_offset);
  }

  public static void glRasterPos3d(double x,
                                   double y,
                                   double z) {
    gl().glRasterPos3d(
      x,
      y,
      z);
  }

  public static void glRasterPos3dv(DoubleBuffer v) {
    gl().glRasterPos3dv(
      v);
  }

  public static void glRasterPos3dv(double[] v,
                                    int v_offset) {
    gl().glRasterPos3dv(
      v,
      v_offset);
  }

  public static void glRasterPos3f(float x,
                                   float y,
                                   float z) {
    gl().glRasterPos3f(
      x,
      y,
      z);
  }

  public static void glRasterPos3fv(FloatBuffer v) {
    gl().glRasterPos3fv(
      v);
  }

  public static void glRasterPos3fv(float[] v,
                                    int v_offset) {
    gl().glRasterPos3fv(
      v,
      v_offset);
  }

  public static void glRasterPos3i(int x,
                                   int y,
                                   int z) {
    gl().glRasterPos3i(
      x,
      y,
      z);
  }

  public static void glRasterPos3iv(IntBuffer v) {
    gl().glRasterPos3iv(
      v);
  }

  public static void glRasterPos3iv(int[] v,
                                    int v_offset) {
    gl().glRasterPos3iv(
      v,
      v_offset);
  }

  public static void glRasterPos3s(short x,
                                   short y,
                                   short z) {
    gl().glRasterPos3s(
      x,
      y,
      z);
  }

  public static void glRasterPos3sv(ShortBuffer v) {
    gl().glRasterPos3sv(
      v);
  }

  public static void glRasterPos3sv(short[] v,
                                    int v_offset) {
    gl().glRasterPos3sv(
      v,
      v_offset);
  }

  public static void glRasterPos4d(double x,
                                   double y,
                                   double z,
                                   double w) {
    gl().glRasterPos4d(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4dv(DoubleBuffer v) {
    gl().glRasterPos4dv(
      v);
  }

  public static void glRasterPos4dv(double[] v,
                                    int v_offset) {
    gl().glRasterPos4dv(
      v,
      v_offset);
  }

  public static void glRasterPos4f(float x,
                                   float y,
                                   float z,
                                   float w) {
    gl().glRasterPos4f(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4fv(FloatBuffer v) {
    gl().glRasterPos4fv(
      v);
  }

  public static void glRasterPos4fv(float[] v,
                                    int v_offset) {
    gl().glRasterPos4fv(
      v,
      v_offset);
  }

  public static void glRasterPos4i(int x,
                                   int y,
                                   int z,
                                   int w) {
    gl().glRasterPos4i(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4iv(IntBuffer v) {
    gl().glRasterPos4iv(
      v);
  }

  public static void glRasterPos4iv(int[] v,
                                    int v_offset) {
    gl().glRasterPos4iv(
      v,
      v_offset);
  }

  public static void glRasterPos4s(short x,
                                   short y,
                                   short z,
                                   short w) {
    gl().glRasterPos4s(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4sv(ShortBuffer v) {
    gl().glRasterPos4sv(
      v);
  }

  public static void glRasterPos4sv(short[] v,
                                    int v_offset) {
    gl().glRasterPos4sv(
      v,
      v_offset);
  }

  public static void glReadBuffer(int mode) {
    gl().glReadBuffer(
      mode);
  }

  public static void glReadBufferRegion(int target,
                                        int start,
                                        int x,
                                        int y,
                                        int width) {
    gl().glReadBufferRegion(
      target,
      start,
      x,
      y,
      width);
  }

  public static void glReadInstrumentsSGIX(int count) {
    gl().glReadInstrumentsSGIX(
      count);
  }

  public static void glReadPixels(int x,
                                  int y,
                                  int width,
                                  int height,
                                  int format,
                                  int type,
                                  Buffer pixels) {
    gl().glReadPixels(
      x,
      y,
      width,
      height,
      format,
      type,
      pixels);
  }

  public static void glReadPixels(int x,
                                  int y,
                                  int width,
                                  int height,
                                  int format,
                                  int type,
                                  long pixels_buffer_offset) {
    gl().glReadPixels(
      x,
      y,
      width,
      height,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glRectd(double x1,
                             double y1,
                             double x2,
                             double y2) {
    gl().glRectd(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectdv(DoubleBuffer v1,
                              DoubleBuffer v2) {
    gl().glRectdv(
      v1,
      v2);
  }

  public static void glRectdv(double[] v1,
                              int v1_offset,
                              double[] v2,
                              int v2_offset) {
    gl().glRectdv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static void glRectf(float x1,
                             float y1,
                             float x2,
                             float y2) {
    gl().glRectf(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectfv(FloatBuffer v1,
                              FloatBuffer v2) {
    gl().glRectfv(
      v1,
      v2);
  }

  public static void glRectfv(float[] v1,
                              int v1_offset,
                              float[] v2,
                              int v2_offset) {
    gl().glRectfv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static void glRecti(int x1,
                             int y1,
                             int x2,
                             int y2) {
    gl().glRecti(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectiv(IntBuffer v1,
                              IntBuffer v2) {
    gl().glRectiv(
      v1,
      v2);
  }

  public static void glRectiv(int[] v1,
                              int v1_offset,
                              int[] v2,
                              int v2_offset) {
    gl().glRectiv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static void glRects(short x1,
                             short y1,
                             short x2,
                             short y2) {
    gl().glRects(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectsv(ShortBuffer v1,
                              ShortBuffer v2) {
    gl().glRectsv(
      v1,
      v2);
  }

  public static void glRectsv(short[] v1,
                              int v1_offset,
                              short[] v2,
                              int v2_offset) {
    gl().glRectsv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static void glReferencePlaneSGIX(DoubleBuffer m) {
    gl().glReferencePlaneSGIX(
      m);
  }

  public static void glReferencePlaneSGIX(double[] m,
                                          int m_offset) {
    gl().glReferencePlaneSGIX(
      m,
      m_offset);
  }

  public static int glRenderMode(int mode) {
    return gl().glRenderMode(
      mode);
  }

  public static void glRenderbufferStorageEXT(int target,
                                              int internalformat,
                                              int width,
                                              int height) {
    gl().glRenderbufferStorageEXT(
      target,
      internalformat,
      width,
      height);
  }

  public static void glReplacementCodeuiColor3fVertex3fSUN(int rc,
                                                           float r,
                                                           float g,
                                                           float b,
                                                           float x,
                                                           float y,
                                                           float z) {
    gl().glReplacementCodeuiColor3fVertex3fSUN(
      rc,
      r,
      g,
      b,
      x,
      y,
      z);
  }

  public static void glReplacementCodeuiColor3fVertex3fvSUN(IntBuffer rc,
                                                            FloatBuffer c,
                                                            FloatBuffer v) {
    gl().glReplacementCodeuiColor3fVertex3fvSUN(
      rc,
      c,
      v);
  }

  public static void glReplacementCodeuiColor3fVertex3fvSUN(int[] rc,
                                                            int rc_offset,
                                                            float[] c,
                                                            int c_offset,
                                                            float[] v,
                                                            int v_offset) {
    gl().glReplacementCodeuiColor3fVertex3fvSUN(
      rc,
      rc_offset,
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glReplacementCodeuiColor4fNormal3fVertex3fSUN(int rc,
                                                                   float r,
                                                                   float g,
                                                                   float b,
                                                                   float a,
                                                                   float nx,
                                                                   float ny,
                                                                   float nz,
                                                                   float x,
                                                                   float y,
                                                                   float z) {
    gl().glReplacementCodeuiColor4fNormal3fVertex3fSUN(
      rc,
      r,
      g,
      b,
      a,
      nx,
      ny,
      nz,
      x,
      y,
      z);
  }

  public static void glReplacementCodeuiColor4fNormal3fVertex3fvSUN(IntBuffer rc,
                                                                    FloatBuffer c,
                                                                    FloatBuffer n,
                                                                    FloatBuffer v) {
    gl().glReplacementCodeuiColor4fNormal3fVertex3fvSUN(
      rc,
      c,
      n,
      v);
  }

  public static void glReplacementCodeuiColor4fNormal3fVertex3fvSUN(int[] rc,
                                                                    int rc_offset,
                                                                    float[] c,
                                                                    int c_offset,
                                                                    float[] n,
                                                                    int n_offset,
                                                                    float[] v,
                                                                    int v_offset) {
    gl().glReplacementCodeuiColor4fNormal3fVertex3fvSUN(
      rc,
      rc_offset,
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glReplacementCodeuiColor4ubVertex3fSUN(int rc,
                                                            byte r,
                                                            byte g,
                                                            byte b,
                                                            byte a,
                                                            float x,
                                                            float y,
                                                            float z) {
    gl().glReplacementCodeuiColor4ubVertex3fSUN(
      rc,
      r,
      g,
      b,
      a,
      x,
      y,
      z);
  }

  public static void glReplacementCodeuiColor4ubVertex3fvSUN(IntBuffer rc,
                                                             ByteBuffer c,
                                                             FloatBuffer v) {
    gl().glReplacementCodeuiColor4ubVertex3fvSUN(
      rc,
      c,
      v);
  }

  public static void glReplacementCodeuiColor4ubVertex3fvSUN(int[] rc,
                                                             int rc_offset,
                                                             byte[] c,
                                                             int c_offset,
                                                             float[] v,
                                                             int v_offset) {
    gl().glReplacementCodeuiColor4ubVertex3fvSUN(
      rc,
      rc_offset,
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glReplacementCodeuiNormal3fVertex3fSUN(int rc,
                                                            float r,
                                                            float g,
                                                            float b,
                                                            float x,
                                                            float y,
                                                            float z) {
    gl().glReplacementCodeuiNormal3fVertex3fSUN(
      rc,
      r,
      g,
      b,
      x,
      y,
      z);
  }

  public static void glReplacementCodeuiNormal3fVertex3fvSUN(IntBuffer rc,
                                                             FloatBuffer c,
                                                             FloatBuffer v) {
    gl().glReplacementCodeuiNormal3fVertex3fvSUN(
      rc,
      c,
      v);
  }

  public static void glReplacementCodeuiNormal3fVertex3fvSUN(int[] rc,
                                                             int rc_offset,
                                                             float[] c,
                                                             int c_offset,
                                                             float[] v,
                                                             int v_offset) {
    gl().glReplacementCodeuiNormal3fVertex3fvSUN(
      rc,
      rc_offset,
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glReplacementCodeuiTexCoord2fColor4fNormal3fVertex3fSUN(int rc,
                                                                             float s,
                                                                             float t,
                                                                             float r,
                                                                             float g,
                                                                             float b,
                                                                             float a,
                                                                             float nx,
                                                                             float ny,
                                                                             float nz,
                                                                             float x,
                                                                             float y,
                                                                             float z) {
    gl().glReplacementCodeuiTexCoord2fColor4fNormal3fVertex3fSUN(
      rc,
      s,
      t,
      r,
      g,
      b,
      a,
      nx,
      ny,
      nz,
      x,
      y,
      z);
  }

  public static void glReplacementCodeuiTexCoord2fColor4fNormal3fVertex3fvSUN(IntBuffer rc,
                                                                              FloatBuffer tc,
                                                                              FloatBuffer c,
                                                                              FloatBuffer n,
                                                                              FloatBuffer v) {
    gl().glReplacementCodeuiTexCoord2fColor4fNormal3fVertex3fvSUN(
      rc,
      tc,
      c,
      n,
      v);
  }

  public static void glReplacementCodeuiTexCoord2fColor4fNormal3fVertex3fvSUN(int[] rc,
                                                                              int rc_offset,
                                                                              float[] tc,
                                                                              int tc_offset,
                                                                              float[] c,
                                                                              int c_offset,
                                                                              float[] n,
                                                                              int n_offset,
                                                                              float[] v,
                                                                              int v_offset) {
    gl().glReplacementCodeuiTexCoord2fColor4fNormal3fVertex3fvSUN(
      rc,
      rc_offset,
      tc,
      tc_offset,
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glReplacementCodeuiTexCoord2fNormal3fVertex3fSUN(int rc,
                                                                      float s,
                                                                      float t,
                                                                      float nx,
                                                                      float ny,
                                                                      float nz,
                                                                      float x,
                                                                      float y,
                                                                      float z) {
    gl().glReplacementCodeuiTexCoord2fNormal3fVertex3fSUN(
      rc,
      s,
      t,
      nx,
      ny,
      nz,
      x,
      y,
      z);
  }

  public static void glReplacementCodeuiTexCoord2fNormal3fVertex3fvSUN(IntBuffer rc,
                                                                       FloatBuffer c,
                                                                       FloatBuffer n,
                                                                       FloatBuffer v) {
    gl().glReplacementCodeuiTexCoord2fNormal3fVertex3fvSUN(
      rc,
      c,
      n,
      v);
  }

  public static void glReplacementCodeuiTexCoord2fNormal3fVertex3fvSUN(int[] rc,
                                                                       int rc_offset,
                                                                       float[] c,
                                                                       int c_offset,
                                                                       float[] n,
                                                                       int n_offset,
                                                                       float[] v,
                                                                       int v_offset) {
    gl().glReplacementCodeuiTexCoord2fNormal3fVertex3fvSUN(
      rc,
      rc_offset,
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glReplacementCodeuiTexCoord2fVertex3fSUN(int rc,
                                                              float s,
                                                              float t,
                                                              float x,
                                                              float y,
                                                              float z) {
    gl().glReplacementCodeuiTexCoord2fVertex3fSUN(
      rc,
      s,
      t,
      x,
      y,
      z);
  }

  public static void glReplacementCodeuiTexCoord2fVertex3fvSUN(IntBuffer rc,
                                                               FloatBuffer c,
                                                               FloatBuffer v) {
    gl().glReplacementCodeuiTexCoord2fVertex3fvSUN(
      rc,
      c,
      v);
  }

  public static void glReplacementCodeuiTexCoord2fVertex3fvSUN(int[] rc,
                                                               int rc_offset,
                                                               float[] c,
                                                               int c_offset,
                                                               float[] v,
                                                               int v_offset) {
    gl().glReplacementCodeuiTexCoord2fVertex3fvSUN(
      rc,
      rc_offset,
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glReplacementCodeuiVertex3fSUN(int target,
                                                    float s,
                                                    float t,
                                                    float r) {
    gl().glReplacementCodeuiVertex3fSUN(
      target,
      s,
      t,
      r);
  }

  public static void glReplacementCodeuiVertex3fvSUN(IntBuffer rc,
                                                     FloatBuffer v) {
    gl().glReplacementCodeuiVertex3fvSUN(
      rc,
      v);
  }

  public static void glReplacementCodeuiVertex3fvSUN(int[] rc,
                                                     int rc_offset,
                                                     float[] v,
                                                     int v_offset) {
    gl().glReplacementCodeuiVertex3fvSUN(
      rc,
      rc_offset,
      v,
      v_offset);
  }

  public static void glRequestResidentProgramsNV(int n,
                                                 IntBuffer ids) {
    gl().glRequestResidentProgramsNV(
      n,
      ids);
  }

  public static void glRequestResidentProgramsNV(int n,
                                                 int[] ids,
                                                 int ids_offset) {
    gl().glRequestResidentProgramsNV(
      n,
      ids,
      ids_offset);
  }

  public static void glResetHistogram(int mode) {
    gl().glResetHistogram(
      mode);
  }

  public static void glResetMinmax(int mode) {
    gl().glResetMinmax(
      mode);
  }

  public static void glResizeBuffersMESA() {
    gl().glResizeBuffersMESA();
  }

  public static void glRotated(double angle,
                               double x,
                               double y,
                               double z) {
    gl().glRotated(
      angle,
      x,
      y,
      z);
  }

  public static void glRotatef(float angle,
                               float x,
                               float y,
                               float z) {
    gl().glRotatef(
      angle,
      x,
      y,
      z);
  }

  public static void glSampleCoverage(float value,
                                      boolean invert) {
    gl().glSampleCoverage(
      value,
      invert);
  }

  public static void glSampleMapATI(int red,
                                    int green,
                                    int blue) {
    gl().glSampleMapATI(
      red,
      green,
      blue);
  }

  public static void glSampleMaskEXT(float value,
                                     boolean invert) {
    gl().glSampleMaskEXT(
      value,
      invert);
  }

  public static void glSampleMaskSGIS(float value,
                                      boolean invert) {
    gl().glSampleMaskSGIS(
      value,
      invert);
  }

  public static void glSamplePatternEXT(int mode) {
    gl().glSamplePatternEXT(
      mode);
  }

  public static void glSamplePatternSGIS(int mode) {
    gl().glSamplePatternSGIS(
      mode);
  }

  public static void glScaled(double x,
                              double y,
                              double z) {
    gl().glScaled(
      x,
      y,
      z);
  }

  public static void glScalef(float x,
                              float y,
                              float z) {
    gl().glScalef(
      x,
      y,
      z);
  }

  public static void glScissor(int x,
                               int y,
                               int width,
                               int height) {
    gl().glScissor(
      x,
      y,
      width,
      height);
  }

  public static void glSecondaryColor3b(byte red,
                                        byte green,
                                        byte blue) {
    gl().glSecondaryColor3b(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3bEXT(byte red,
                                           byte green,
                                           byte blue) {
    gl().glSecondaryColor3bEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3bv(ByteBuffer v) {
    gl().glSecondaryColor3bv(
      v);
  }

  public static void glSecondaryColor3bv(byte[] v,
                                         int v_offset) {
    gl().glSecondaryColor3bv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3bvEXT(ByteBuffer v) {
    gl().glSecondaryColor3bvEXT(
      v);
  }

  public static void glSecondaryColor3bvEXT(byte[] v,
                                            int v_offset) {
    gl().glSecondaryColor3bvEXT(
      v,
      v_offset);
  }

  public static void glSecondaryColor3d(double red,
                                        double green,
                                        double blue) {
    gl().glSecondaryColor3d(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3dEXT(double red,
                                           double green,
                                           double blue) {
    gl().glSecondaryColor3dEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3dv(DoubleBuffer m) {
    gl().glSecondaryColor3dv(
      m);
  }

  public static void glSecondaryColor3dv(double[] m,
                                         int m_offset) {
    gl().glSecondaryColor3dv(
      m,
      m_offset);
  }

  public static void glSecondaryColor3dvEXT(DoubleBuffer m) {
    gl().glSecondaryColor3dvEXT(
      m);
  }

  public static void glSecondaryColor3dvEXT(double[] m,
                                            int m_offset) {
    gl().glSecondaryColor3dvEXT(
      m,
      m_offset);
  }

  public static void glSecondaryColor3f(float red,
                                        float green,
                                        float blue) {
    gl().glSecondaryColor3f(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3fEXT(float red,
                                           float green,
                                           float blue) {
    gl().glSecondaryColor3fEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3fv(FloatBuffer m) {
    gl().glSecondaryColor3fv(
      m);
  }

  public static void glSecondaryColor3fv(float[] m,
                                         int m_offset) {
    gl().glSecondaryColor3fv(
      m,
      m_offset);
  }

  public static void glSecondaryColor3fvEXT(FloatBuffer m) {
    gl().glSecondaryColor3fvEXT(
      m);
  }

  public static void glSecondaryColor3fvEXT(float[] m,
                                            int m_offset) {
    gl().glSecondaryColor3fvEXT(
      m,
      m_offset);
  }

  public static void glSecondaryColor3hNV(short red,
                                          short green,
                                          short blue) {
    gl().glSecondaryColor3hNV(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3hvNV(ShortBuffer v) {
    gl().glSecondaryColor3hvNV(
      v);
  }

  public static void glSecondaryColor3hvNV(short[] v,
                                           int v_offset) {
    gl().glSecondaryColor3hvNV(
      v,
      v_offset);
  }

  public static void glSecondaryColor3i(int red,
                                        int green,
                                        int blue) {
    gl().glSecondaryColor3i(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3iEXT(int red,
                                           int green,
                                           int blue) {
    gl().glSecondaryColor3iEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3iv(IntBuffer v) {
    gl().glSecondaryColor3iv(
      v);
  }

  public static void glSecondaryColor3iv(int[] v,
                                         int v_offset) {
    gl().glSecondaryColor3iv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3ivEXT(IntBuffer v) {
    gl().glSecondaryColor3ivEXT(
      v);
  }

  public static void glSecondaryColor3ivEXT(int[] v,
                                            int v_offset) {
    gl().glSecondaryColor3ivEXT(
      v,
      v_offset);
  }

  public static void glSecondaryColor3s(short red,
                                        short green,
                                        short blue) {
    gl().glSecondaryColor3s(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3sEXT(short red,
                                           short green,
                                           short blue) {
    gl().glSecondaryColor3sEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3sv(ShortBuffer v) {
    gl().glSecondaryColor3sv(
      v);
  }

  public static void glSecondaryColor3sv(short[] v,
                                         int v_offset) {
    gl().glSecondaryColor3sv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3svEXT(ShortBuffer v) {
    gl().glSecondaryColor3svEXT(
      v);
  }

  public static void glSecondaryColor3svEXT(short[] v,
                                            int v_offset) {
    gl().glSecondaryColor3svEXT(
      v,
      v_offset);
  }

  public static void glSecondaryColor3ub(byte red,
                                         byte green,
                                         byte blue) {
    gl().glSecondaryColor3ub(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3ubEXT(byte red,
                                            byte green,
                                            byte blue) {
    gl().glSecondaryColor3ubEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3ubv(ByteBuffer v) {
    gl().glSecondaryColor3ubv(
      v);
  }

  public static void glSecondaryColor3ubv(byte[] v,
                                          int v_offset) {
    gl().glSecondaryColor3ubv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3ubvEXT(ByteBuffer v) {
    gl().glSecondaryColor3ubvEXT(
      v);
  }

  public static void glSecondaryColor3ubvEXT(byte[] v,
                                             int v_offset) {
    gl().glSecondaryColor3ubvEXT(
      v,
      v_offset);
  }

  public static void glSecondaryColor3ui(int red,
                                         int green,
                                         int blue) {
    gl().glSecondaryColor3ui(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3uiEXT(int red,
                                            int green,
                                            int blue) {
    gl().glSecondaryColor3uiEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3uiv(IntBuffer v) {
    gl().glSecondaryColor3uiv(
      v);
  }

  public static void glSecondaryColor3uiv(int[] v,
                                          int v_offset) {
    gl().glSecondaryColor3uiv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3uivEXT(IntBuffer v) {
    gl().glSecondaryColor3uivEXT(
      v);
  }

  public static void glSecondaryColor3uivEXT(int[] v,
                                             int v_offset) {
    gl().glSecondaryColor3uivEXT(
      v,
      v_offset);
  }

  public static void glSecondaryColor3us(short red,
                                         short green,
                                         short blue) {
    gl().glSecondaryColor3us(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3usEXT(short red,
                                            short green,
                                            short blue) {
    gl().glSecondaryColor3usEXT(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3usv(ShortBuffer v) {
    gl().glSecondaryColor3usv(
      v);
  }

  public static void glSecondaryColor3usv(short[] v,
                                          int v_offset) {
    gl().glSecondaryColor3usv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3usvEXT(ShortBuffer v) {
    gl().glSecondaryColor3usvEXT(
      v);
  }

  public static void glSecondaryColor3usvEXT(short[] v,
                                             int v_offset) {
    gl().glSecondaryColor3usvEXT(
      v,
      v_offset);
  }

  public static void glSecondaryColorPointer(int size,
                                             int type,
                                             int stride,
                                             Buffer pointer) {
    gl().glSecondaryColorPointer(
      size,
      type,
      stride,
      pointer);
  }

  public static void glSecondaryColorPointer(int size,
                                             int type,
                                             int stride,
                                             long pointer_buffer_offset) {
    gl().glSecondaryColorPointer(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glSecondaryColorPointerEXT(int size,
                                                int type,
                                                int stride,
                                                Buffer pointer) {
    gl().glSecondaryColorPointerEXT(
      size,
      type,
      stride,
      pointer);
  }

  public static void glSecondaryColorPointerEXT(int size,
                                                int type,
                                                int stride,
                                                long pointer_buffer_offset) {
    gl().glSecondaryColorPointerEXT(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glSelectBuffer(int size,
                                    IntBuffer buffer) {
    gl().glSelectBuffer(
      size,
      buffer);
  }

  public static void glSeparableFilter2D(int target,
                                         int internalformat,
                                         int width,
                                         int height,
                                         int format,
                                         int type,
                                         Buffer row,
                                         Buffer column) {
    gl().glSeparableFilter2D(
      target,
      internalformat,
      width,
      height,
      format,
      type,
      row,
      column);
  }

  public static void glSeparableFilter2D(int target,
                                         int internalformat,
                                         int width,
                                         int height,
                                         int format,
                                         int type,
                                         long row_buffer_offset,
                                         long column_buffer_offset) {
    gl().glSeparableFilter2D(
      target,
      internalformat,
      width,
      height,
      format,
      type,
      row_buffer_offset,
      column_buffer_offset);
  }

  public static void glSetFenceAPPLE(int mode) {
    gl().glSetFenceAPPLE(
      mode);
  }

  public static void glSetFenceNV(int target,
                                  int id) {
    gl().glSetFenceNV(
      target,
      id);
  }

  public static void glSetFragmentShaderConstantATI(int target,
                                                    FloatBuffer v) {
    gl().glSetFragmentShaderConstantATI(
      target,
      v);
  }

  public static void glSetFragmentShaderConstantATI(int target,
                                                    float[] v,
                                                    int v_offset) {
    gl().glSetFragmentShaderConstantATI(
      target,
      v,
      v_offset);
  }

  public static void glSetInvariantEXT(int id,
                                       int type,
                                       Buffer addr) {
    gl().glSetInvariantEXT(
      id,
      type,
      addr);
  }

  public static void glSetLocalConstantEXT(int id,
                                           int type,
                                           Buffer addr) {
    gl().glSetLocalConstantEXT(
      id,
      type,
      addr);
  }

  public static void glShadeModel(int mode) {
    gl().glShadeModel(
      mode);
  }

  public static void glShaderOp1EXT(int red,
                                    int green,
                                    int blue) {
    gl().glShaderOp1EXT(
      red,
      green,
      blue);
  }

  public static void glShaderOp2EXT(int sfactorRGB,
                                    int dfactorRGB,
                                    int sfactorAlpha,
                                    int dfactorAlpha) {
    gl().glShaderOp2EXT(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glShaderOp3EXT(int op,
                                    int res,
                                    int arg1,
                                    int arg2,
                                    int arg3) {
    gl().glShaderOp3EXT(
      op,
      res,
      arg1,
      arg2,
      arg3);
  }

  public static void glShaderSource(int shader,
                                    int count,
                                    String[] string,
                                    IntBuffer length) {
    gl().glShaderSource(
      shader,
      count,
      string,
      length);
  }

  public static void glShaderSource(int shader,
                                    int count,
                                    String[] string,
                                    int[] length,
                                    int length_offset) {
    gl().glShaderSource(
      shader,
      count,
      string,
      length,
      length_offset);
  }

  public static void glShaderSourceARB(int shader,
                                       int count,
                                       String[] string,
                                       IntBuffer length) {
    gl().glShaderSourceARB(
      shader,
      count,
      string,
      length);
  }

  public static void glShaderSourceARB(int shader,
                                       int count,
                                       String[] string,
                                       int[] length,
                                       int length_offset) {
    gl().glShaderSourceARB(
      shader,
      count,
      string,
      length,
      length_offset);
  }

  public static void glSharpenTexFuncSGIS(int target,
                                          int n,
                                          FloatBuffer points) {
    gl().glSharpenTexFuncSGIS(
      target,
      n,
      points);
  }

  public static void glSharpenTexFuncSGIS(int target,
                                          int n,
                                          float[] points,
                                          int points_offset) {
    gl().glSharpenTexFuncSGIS(
      target,
      n,
      points,
      points_offset);
  }

  public static void glSpriteParameterfSGIX(int target,
                                            float s) {
    gl().glSpriteParameterfSGIX(
      target,
      s);
  }

  public static void glSpriteParameterfvSGIX(int target,
                                             FloatBuffer v) {
    gl().glSpriteParameterfvSGIX(
      target,
      v);
  }

  public static void glSpriteParameterfvSGIX(int target,
                                             float[] v,
                                             int v_offset) {
    gl().glSpriteParameterfvSGIX(
      target,
      v,
      v_offset);
  }

  public static void glSpriteParameteriSGIX(int target,
                                            int s) {
    gl().glSpriteParameteriSGIX(
      target,
      s);
  }

  public static void glSpriteParameterivSGIX(int target,
                                             IntBuffer v) {
    gl().glSpriteParameterivSGIX(
      target,
      v);
  }

  public static void glSpriteParameterivSGIX(int target,
                                             int[] v,
                                             int v_offset) {
    gl().glSpriteParameterivSGIX(
      target,
      v,
      v_offset);
  }

  public static void glStartInstrumentsSGIX() {
    gl().glStartInstrumentsSGIX();
  }

  public static void glStencilFunc(int func,
                                   int ref,
                                   int mask) {
    gl().glStencilFunc(
      func,
      ref,
      mask);
  }

  public static void glStencilFuncSeparate(int frontfunc,
                                           int backfunc,
                                           int ref,
                                           int mask) {
    gl().glStencilFuncSeparate(
      frontfunc,
      backfunc,
      ref,
      mask);
  }

  public static void glStencilFuncSeparateATI(int frontfunc,
                                              int backfunc,
                                              int ref,
                                              int mask) {
    gl().glStencilFuncSeparateATI(
      frontfunc,
      backfunc,
      ref,
      mask);
  }

  public static void glStencilMask(int mask) {
    gl().glStencilMask(
      mask);
  }

  public static void glStencilMaskSeparate(int target,
                                           int id) {
    gl().glStencilMaskSeparate(
      target,
      id);
  }

  public static void glStencilOp(int fail,
                                 int zfail,
                                 int zpass) {
    gl().glStencilOp(
      fail,
      zfail,
      zpass);
  }

  public static void glStencilOpSeparate(int sfactorRGB,
                                         int dfactorRGB,
                                         int sfactorAlpha,
                                         int dfactorAlpha) {
    gl().glStencilOpSeparate(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glStencilOpSeparateATI(int sfactorRGB,
                                            int dfactorRGB,
                                            int sfactorAlpha,
                                            int dfactorAlpha) {
    gl().glStencilOpSeparateATI(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glStopInstrumentsSGIX(int count) {
    gl().glStopInstrumentsSGIX(
      count);
  }

  public static void glStringMarkerGREMEDY(int length,
                                           Buffer pointer) {
    gl().glStringMarkerGREMEDY(
      length,
      pointer);
  }

  public static void glSwapAPPLE() {
    gl().glSwapAPPLE();
  }

  public static void glSwizzleEXT(int stage,
                                  int portion,
                                  int variable,
                                  int input,
                                  int mapping,
                                  int componentUsage) {
    gl().glSwizzleEXT(
      stage,
      portion,
      variable,
      input,
      mapping,
      componentUsage);
  }

  public static void glTagSampleBufferSGIX() {
    gl().glTagSampleBufferSGIX();
  }

  public static void glTbufferMask3DFX(int mode) {
    gl().glTbufferMask3DFX(
      mode);
  }

  public static boolean glTestFenceAPPLE(int id) {
    return gl().glTestFenceAPPLE(
      id);
  }

  public static boolean glTestFenceNV(int id) {
    return gl().glTestFenceNV(
      id);
  }

  public static boolean glTestObjectAPPLE(int id,
                                          int cap) {
    return gl().glTestObjectAPPLE(
      id,
      cap);
  }

  public static void glTexBumpParameterfvATI(int target,
                                             FloatBuffer v) {
    gl().glTexBumpParameterfvATI(
      target,
      v);
  }

  public static void glTexBumpParameterfvATI(int target,
                                             float[] v,
                                             int v_offset) {
    gl().glTexBumpParameterfvATI(
      target,
      v,
      v_offset);
  }

  public static void glTexBumpParameterivATI(int target,
                                             IntBuffer v) {
    gl().glTexBumpParameterivATI(
      target,
      v);
  }

  public static void glTexBumpParameterivATI(int target,
                                             int[] v,
                                             int v_offset) {
    gl().glTexBumpParameterivATI(
      target,
      v,
      v_offset);
  }

  public static void glTexCoord1d(double s) {
    gl().glTexCoord1d(
      s);
  }

  public static void glTexCoord1dv(DoubleBuffer v) {
    gl().glTexCoord1dv(
      v);
  }

  public static void glTexCoord1dv(double[] v,
                                   int v_offset) {
    gl().glTexCoord1dv(
      v,
      v_offset);
  }

  public static void glTexCoord1f(float s) {
    gl().glTexCoord1f(
      s);
  }

  public static void glTexCoord1fv(FloatBuffer v) {
    gl().glTexCoord1fv(
      v);
  }

  public static void glTexCoord1fv(float[] v,
                                   int v_offset) {
    gl().glTexCoord1fv(
      v,
      v_offset);
  }

  public static void glTexCoord1hNV(short factor) {
    gl().glTexCoord1hNV(
      factor);
  }

  public static void glTexCoord1hvNV(ShortBuffer v) {
    gl().glTexCoord1hvNV(
      v);
  }

  public static void glTexCoord1hvNV(short[] v,
                                     int v_offset) {
    gl().glTexCoord1hvNV(
      v,
      v_offset);
  }

  public static void glTexCoord1i(int s) {
    gl().glTexCoord1i(
      s);
  }

  public static void glTexCoord1iv(IntBuffer v) {
    gl().glTexCoord1iv(
      v);
  }

  public static void glTexCoord1iv(int[] v,
                                   int v_offset) {
    gl().glTexCoord1iv(
      v,
      v_offset);
  }

  public static void glTexCoord1s(short s) {
    gl().glTexCoord1s(
      s);
  }

  public static void glTexCoord1sv(ShortBuffer v) {
    gl().glTexCoord1sv(
      v);
  }

  public static void glTexCoord1sv(short[] v,
                                   int v_offset) {
    gl().glTexCoord1sv(
      v,
      v_offset);
  }

  public static void glTexCoord2d(double s,
                                  double t) {
    gl().glTexCoord2d(
      s,
      t);
  }

  public static void glTexCoord2dv(DoubleBuffer v) {
    gl().glTexCoord2dv(
      v);
  }

  public static void glTexCoord2dv(double[] v,
                                   int v_offset) {
    gl().glTexCoord2dv(
      v,
      v_offset);
  }

  public static void glTexCoord2f(float s,
                                  float t) {
    gl().glTexCoord2f(
      s,
      t);
  }

  public static void glTexCoord2fColor3fVertex3fSUN(float s,
                                                    float t,
                                                    float p,
                                                    float q,
                                                    float x,
                                                    float y,
                                                    float z,
                                                    float w) {
    gl().glTexCoord2fColor3fVertex3fSUN(
      s,
      t,
      p,
      q,
      x,
      y,
      z,
      w);
  }

  public static void glTexCoord2fColor3fVertex3fvSUN(FloatBuffer c,
                                                     FloatBuffer n,
                                                     FloatBuffer v) {
    gl().glTexCoord2fColor3fVertex3fvSUN(
      c,
      n,
      v);
  }

  public static void glTexCoord2fColor3fVertex3fvSUN(float[] c,
                                                     int c_offset,
                                                     float[] n,
                                                     int n_offset,
                                                     float[] v,
                                                     int v_offset) {
    gl().glTexCoord2fColor3fVertex3fvSUN(
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glTexCoord2fColor4fNormal3fVertex3fSUN(float s,
                                                            float t,
                                                            float r,
                                                            float g,
                                                            float b,
                                                            float a,
                                                            float nx,
                                                            float ny,
                                                            float nz,
                                                            float x,
                                                            float y,
                                                            float z) {
    gl().glTexCoord2fColor4fNormal3fVertex3fSUN(
      s,
      t,
      r,
      g,
      b,
      a,
      nx,
      ny,
      nz,
      x,
      y,
      z);
  }

  public static void glTexCoord2fColor4fNormal3fVertex3fvSUN(FloatBuffer tc,
                                                             FloatBuffer c,
                                                             FloatBuffer n,
                                                             FloatBuffer v) {
    gl().glTexCoord2fColor4fNormal3fVertex3fvSUN(
      tc,
      c,
      n,
      v);
  }

  public static void glTexCoord2fColor4fNormal3fVertex3fvSUN(float[] tc,
                                                             int tc_offset,
                                                             float[] c,
                                                             int c_offset,
                                                             float[] n,
                                                             int n_offset,
                                                             float[] v,
                                                             int v_offset) {
    gl().glTexCoord2fColor4fNormal3fVertex3fvSUN(
      tc,
      tc_offset,
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glTexCoord2fColor4ubVertex3fSUN(float s,
                                                     float t,
                                                     byte r,
                                                     byte g,
                                                     byte b,
                                                     byte a,
                                                     float x,
                                                     float y,
                                                     float z) {
    gl().glTexCoord2fColor4ubVertex3fSUN(
      s,
      t,
      r,
      g,
      b,
      a,
      x,
      y,
      z);
  }

  public static void glTexCoord2fColor4ubVertex3fvSUN(FloatBuffer tc,
                                                      ByteBuffer c,
                                                      FloatBuffer v) {
    gl().glTexCoord2fColor4ubVertex3fvSUN(
      tc,
      c,
      v);
  }

  public static void glTexCoord2fColor4ubVertex3fvSUN(float[] tc,
                                                      int tc_offset,
                                                      byte[] c,
                                                      int c_offset,
                                                      float[] v,
                                                      int v_offset) {
    gl().glTexCoord2fColor4ubVertex3fvSUN(
      tc,
      tc_offset,
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glTexCoord2fNormal3fVertex3fSUN(float s,
                                                     float t,
                                                     float p,
                                                     float q,
                                                     float x,
                                                     float y,
                                                     float z,
                                                     float w) {
    gl().glTexCoord2fNormal3fVertex3fSUN(
      s,
      t,
      p,
      q,
      x,
      y,
      z,
      w);
  }

  public static void glTexCoord2fNormal3fVertex3fvSUN(FloatBuffer c,
                                                      FloatBuffer n,
                                                      FloatBuffer v) {
    gl().glTexCoord2fNormal3fVertex3fvSUN(
      c,
      n,
      v);
  }

  public static void glTexCoord2fNormal3fVertex3fvSUN(float[] c,
                                                      int c_offset,
                                                      float[] n,
                                                      int n_offset,
                                                      float[] v,
                                                      int v_offset) {
    gl().glTexCoord2fNormal3fVertex3fvSUN(
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glTexCoord2fVertex3fSUN(float s,
                                             float t,
                                             float x,
                                             float y,
                                             float z) {
    gl().glTexCoord2fVertex3fSUN(
      s,
      t,
      x,
      y,
      z);
  }

  public static void glTexCoord2fVertex3fvSUN(FloatBuffer c,
                                              FloatBuffer v) {
    gl().glTexCoord2fVertex3fvSUN(
      c,
      v);
  }

  public static void glTexCoord2fVertex3fvSUN(float[] c,
                                              int c_offset,
                                              float[] v,
                                              int v_offset) {
    gl().glTexCoord2fVertex3fvSUN(
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glTexCoord2fv(FloatBuffer v) {
    gl().glTexCoord2fv(
      v);
  }

  public static void glTexCoord2fv(float[] v,
                                   int v_offset) {
    gl().glTexCoord2fv(
      v,
      v_offset);
  }

  public static void glTexCoord2hNV(short x,
                                    short y) {
    gl().glTexCoord2hNV(
      x,
      y);
  }

  public static void glTexCoord2hvNV(ShortBuffer v) {
    gl().glTexCoord2hvNV(
      v);
  }

  public static void glTexCoord2hvNV(short[] v,
                                     int v_offset) {
    gl().glTexCoord2hvNV(
      v,
      v_offset);
  }

  public static void glTexCoord2i(int s,
                                  int t) {
    gl().glTexCoord2i(
      s,
      t);
  }

  public static void glTexCoord2iv(IntBuffer v) {
    gl().glTexCoord2iv(
      v);
  }

  public static void glTexCoord2iv(int[] v,
                                   int v_offset) {
    gl().glTexCoord2iv(
      v,
      v_offset);
  }

  public static void glTexCoord2s(short s,
                                  short t) {
    gl().glTexCoord2s(
      s,
      t);
  }

  public static void glTexCoord2sv(ShortBuffer v) {
    gl().glTexCoord2sv(
      v);
  }

  public static void glTexCoord2sv(short[] v,
                                   int v_offset) {
    gl().glTexCoord2sv(
      v,
      v_offset);
  }

  public static void glTexCoord3d(double s,
                                  double t,
                                  double r) {
    gl().glTexCoord3d(
      s,
      t,
      r);
  }

  public static void glTexCoord3dv(DoubleBuffer v) {
    gl().glTexCoord3dv(
      v);
  }

  public static void glTexCoord3dv(double[] v,
                                   int v_offset) {
    gl().glTexCoord3dv(
      v,
      v_offset);
  }

  public static void glTexCoord3f(float s,
                                  float t,
                                  float r) {
    gl().glTexCoord3f(
      s,
      t,
      r);
  }

  public static void glTexCoord3fv(FloatBuffer v) {
    gl().glTexCoord3fv(
      v);
  }

  public static void glTexCoord3fv(float[] v,
                                   int v_offset) {
    gl().glTexCoord3fv(
      v,
      v_offset);
  }

  public static void glTexCoord3hNV(short red,
                                    short green,
                                    short blue) {
    gl().glTexCoord3hNV(
      red,
      green,
      blue);
  }

  public static void glTexCoord3hvNV(ShortBuffer v) {
    gl().glTexCoord3hvNV(
      v);
  }

  public static void glTexCoord3hvNV(short[] v,
                                     int v_offset) {
    gl().glTexCoord3hvNV(
      v,
      v_offset);
  }

  public static void glTexCoord3i(int s,
                                  int t,
                                  int r) {
    gl().glTexCoord3i(
      s,
      t,
      r);
  }

  public static void glTexCoord3iv(IntBuffer v) {
    gl().glTexCoord3iv(
      v);
  }

  public static void glTexCoord3iv(int[] v,
                                   int v_offset) {
    gl().glTexCoord3iv(
      v,
      v_offset);
  }

  public static void glTexCoord3s(short s,
                                  short t,
                                  short r) {
    gl().glTexCoord3s(
      s,
      t,
      r);
  }

  public static void glTexCoord3sv(ShortBuffer v) {
    gl().glTexCoord3sv(
      v);
  }

  public static void glTexCoord3sv(short[] v,
                                   int v_offset) {
    gl().glTexCoord3sv(
      v,
      v_offset);
  }

  public static void glTexCoord4d(double s,
                                  double t,
                                  double r,
                                  double q) {
    gl().glTexCoord4d(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4dv(DoubleBuffer v) {
    gl().glTexCoord4dv(
      v);
  }

  public static void glTexCoord4dv(double[] v,
                                   int v_offset) {
    gl().glTexCoord4dv(
      v,
      v_offset);
  }

  public static void glTexCoord4f(float s,
                                  float t,
                                  float r,
                                  float q) {
    gl().glTexCoord4f(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4fColor4fNormal3fVertex4fSUN(float s,
                                                            float t,
                                                            float p,
                                                            float q,
                                                            float r,
                                                            float g,
                                                            float b,
                                                            float a,
                                                            float nx,
                                                            float ny,
                                                            float nz,
                                                            float x,
                                                            float y,
                                                            float z,
                                                            float w) {
    gl().glTexCoord4fColor4fNormal3fVertex4fSUN(
      s,
      t,
      p,
      q,
      r,
      g,
      b,
      a,
      nx,
      ny,
      nz,
      x,
      y,
      z,
      w);
  }

  public static void glTexCoord4fColor4fNormal3fVertex4fvSUN(FloatBuffer tc,
                                                             FloatBuffer c,
                                                             FloatBuffer n,
                                                             FloatBuffer v) {
    gl().glTexCoord4fColor4fNormal3fVertex4fvSUN(
      tc,
      c,
      n,
      v);
  }

  public static void glTexCoord4fColor4fNormal3fVertex4fvSUN(float[] tc,
                                                             int tc_offset,
                                                             float[] c,
                                                             int c_offset,
                                                             float[] n,
                                                             int n_offset,
                                                             float[] v,
                                                             int v_offset) {
    gl().glTexCoord4fColor4fNormal3fVertex4fvSUN(
      tc,
      tc_offset,
      c,
      c_offset,
      n,
      n_offset,
      v,
      v_offset);
  }

  public static void glTexCoord4fVertex4fSUN(float s,
                                             float t,
                                             float p,
                                             float q,
                                             float x,
                                             float y,
                                             float z,
                                             float w) {
    gl().glTexCoord4fVertex4fSUN(
      s,
      t,
      p,
      q,
      x,
      y,
      z,
      w);
  }

  public static void glTexCoord4fVertex4fvSUN(FloatBuffer c,
                                              FloatBuffer v) {
    gl().glTexCoord4fVertex4fvSUN(
      c,
      v);
  }

  public static void glTexCoord4fVertex4fvSUN(float[] c,
                                              int c_offset,
                                              float[] v,
                                              int v_offset) {
    gl().glTexCoord4fVertex4fvSUN(
      c,
      c_offset,
      v,
      v_offset);
  }

  public static void glTexCoord4fv(FloatBuffer v) {
    gl().glTexCoord4fv(
      v);
  }

  public static void glTexCoord4fv(float[] v,
                                   int v_offset) {
    gl().glTexCoord4fv(
      v,
      v_offset);
  }

  public static void glTexCoord4hNV(short x,
                                    short y,
                                    short z,
                                    short w) {
    gl().glTexCoord4hNV(
      x,
      y,
      z,
      w);
  }

  public static void glTexCoord4hvNV(ShortBuffer v) {
    gl().glTexCoord4hvNV(
      v);
  }

  public static void glTexCoord4hvNV(short[] v,
                                     int v_offset) {
    gl().glTexCoord4hvNV(
      v,
      v_offset);
  }

  public static void glTexCoord4i(int s,
                                  int t,
                                  int r,
                                  int q) {
    gl().glTexCoord4i(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4iv(IntBuffer v) {
    gl().glTexCoord4iv(
      v);
  }

  public static void glTexCoord4iv(int[] v,
                                   int v_offset) {
    gl().glTexCoord4iv(
      v,
      v_offset);
  }

  public static void glTexCoord4s(short s,
                                  short t,
                                  short r,
                                  short q) {
    gl().glTexCoord4s(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4sv(ShortBuffer v) {
    gl().glTexCoord4sv(
      v);
  }

  public static void glTexCoord4sv(short[] v,
                                   int v_offset) {
    gl().glTexCoord4sv(
      v,
      v_offset);
  }

  public static void glTexCoordPointer(int size,
                                       int type,
                                       int stride,
                                       Buffer ptr) {
    gl().glTexCoordPointer(
      size,
      type,
      stride,
      ptr);
  }

  public static void glTexCoordPointer(int size,
                                       int type,
                                       int stride,
                                       long ptr_buffer_offset) {
    gl().glTexCoordPointer(
      size,
      type,
      stride,
      ptr_buffer_offset);
  }

  public static void glTexEnvf(int target,
                               int pname,
                               float param) {
    gl().glTexEnvf(
      target,
      pname,
      param);
  }

  public static void glTexEnvfv(int target,
                                int pname,
                                FloatBuffer params) {
    gl().glTexEnvfv(
      target,
      pname,
      params);
  }

  public static void glTexEnvfv(int target,
                                int pname,
                                float[] params,
                                int params_offset) {
    gl().glTexEnvfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexEnvi(int target,
                               int pname,
                               int param) {
    gl().glTexEnvi(
      target,
      pname,
      param);
  }

  public static void glTexEnviv(int target,
                                int pname,
                                IntBuffer params) {
    gl().glTexEnviv(
      target,
      pname,
      params);
  }

  public static void glTexEnviv(int target,
                                int pname,
                                int[] params,
                                int params_offset) {
    gl().glTexEnviv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexFilterFuncSGIS(int target,
                                         int filter,
                                         int n,
                                         FloatBuffer weights) {
    gl().glTexFilterFuncSGIS(
      target,
      filter,
      n,
      weights);
  }

  public static void glTexFilterFuncSGIS(int target,
                                         int filter,
                                         int n,
                                         float[] weights,
                                         int weights_offset) {
    gl().glTexFilterFuncSGIS(
      target,
      filter,
      n,
      weights,
      weights_offset);
  }

  public static void glTexGend(int coord,
                               int pname,
                               double param) {
    gl().glTexGend(
      coord,
      pname,
      param);
  }

  public static void glTexGendv(int coord,
                                int pname,
                                DoubleBuffer params) {
    gl().glTexGendv(
      coord,
      pname,
      params);
  }

  public static void glTexGendv(int coord,
                                int pname,
                                double[] params,
                                int params_offset) {
    gl().glTexGendv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glTexGenf(int coord,
                               int pname,
                               float param) {
    gl().glTexGenf(
      coord,
      pname,
      param);
  }

  public static void glTexGenfv(int coord,
                                int pname,
                                FloatBuffer params) {
    gl().glTexGenfv(
      coord,
      pname,
      params);
  }

  public static void glTexGenfv(int coord,
                                int pname,
                                float[] params,
                                int params_offset) {
    gl().glTexGenfv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glTexGeni(int coord,
                               int pname,
                               int param) {
    gl().glTexGeni(
      coord,
      pname,
      param);
  }

  public static void glTexGeniv(int coord,
                                int pname,
                                IntBuffer params) {
    gl().glTexGeniv(
      coord,
      pname,
      params);
  }

  public static void glTexGeniv(int coord,
                                int pname,
                                int[] params,
                                int params_offset) {
    gl().glTexGeniv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glTexImage1D(int target,
                                  int level,
                                  int internalFormat,
                                  int width,
                                  int border,
                                  int format,
                                  int type,
                                  Buffer pixels) {
    gl().glTexImage1D(
      target,
      level,
      internalFormat,
      width,
      border,
      format,
      type,
      pixels);
  }

  public static void glTexImage1D(int target,
                                  int level,
                                  int internalFormat,
                                  int width,
                                  int border,
                                  int format,
                                  int type,
                                  long pixels_buffer_offset) {
    gl().glTexImage1D(
      target,
      level,
      internalFormat,
      width,
      border,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glTexImage2D(int target,
                                  int level,
                                  int internalFormat,
                                  int width,
                                  int height,
                                  int border,
                                  int format,
                                  int type,
                                  Buffer pixels) {
    gl().glTexImage2D(
      target,
      level,
      internalFormat,
      width,
      height,
      border,
      format,
      type,
      pixels);
  }

  public static void glTexImage2D(int target,
                                  int level,
                                  int internalFormat,
                                  int width,
                                  int height,
                                  int border,
                                  int format,
                                  int type,
                                  long pixels_buffer_offset) {
    gl().glTexImage2D(
      target,
      level,
      internalFormat,
      width,
      height,
      border,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glTexImage3D(int target,
                                  int level,
                                  int internalformat,
                                  int width,
                                  int height,
                                  int depth,
                                  int border,
                                  int format,
                                  int type,
                                  Buffer pixels) {
    gl().glTexImage3D(
      target,
      level,
      internalformat,
      width,
      height,
      depth,
      border,
      format,
      type,
      pixels);
  }

  public static void glTexImage3D(int target,
                                  int level,
                                  int internalformat,
                                  int width,
                                  int height,
                                  int depth,
                                  int border,
                                  int format,
                                  int type,
                                  long pixels_buffer_offset) {
    gl().glTexImage3D(
      target,
      level,
      internalformat,
      width,
      height,
      depth,
      border,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glTexImage4DSGIS(int target,
                                      int level,
                                      int internalformat,
                                      int width,
                                      int height,
                                      int depth,
                                      int size4d,
                                      int border,
                                      int format,
                                      int type,
                                      Buffer pixels) {
    gl().glTexImage4DSGIS(
      target,
      level,
      internalformat,
      width,
      height,
      depth,
      size4d,
      border,
      format,
      type,
      pixels);
  }

  public static void glTexParameterf(int target,
                                     int pname,
                                     float param) {
    gl().glTexParameterf(
      target,
      pname,
      param);
  }

  public static void glTexParameterfv(int target,
                                      int pname,
                                      FloatBuffer params) {
    gl().glTexParameterfv(
      target,
      pname,
      params);
  }

  public static void glTexParameterfv(int target,
                                      int pname,
                                      float[] params,
                                      int params_offset) {
    gl().glTexParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexParameteri(int target,
                                     int pname,
                                     int param) {
    gl().glTexParameteri(
      target,
      pname,
      param);
  }

  public static void glTexParameteriv(int target,
                                      int pname,
                                      IntBuffer params) {
    gl().glTexParameteriv(
      target,
      pname,
      params);
  }

  public static void glTexParameteriv(int target,
                                      int pname,
                                      int[] params,
                                      int params_offset) {
    gl().glTexParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexSubImage1D(int target,
                                     int level,
                                     int xoffset,
                                     int width,
                                     int format,
                                     int type,
                                     Buffer pixels) {
    gl().glTexSubImage1D(
      target,
      level,
      xoffset,
      width,
      format,
      type,
      pixels);
  }

  public static void glTexSubImage1D(int target,
                                     int level,
                                     int xoffset,
                                     int width,
                                     int format,
                                     int type,
                                     long pixels_buffer_offset) {
    gl().glTexSubImage1D(
      target,
      level,
      xoffset,
      width,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glTexSubImage2D(int target,
                                     int level,
                                     int xoffset,
                                     int yoffset,
                                     int width,
                                     int height,
                                     int format,
                                     int type,
                                     Buffer pixels) {
    gl().glTexSubImage2D(
      target,
      level,
      xoffset,
      yoffset,
      width,
      height,
      format,
      type,
      pixels);
  }

  public static void glTexSubImage2D(int target,
                                     int level,
                                     int xoffset,
                                     int yoffset,
                                     int width,
                                     int height,
                                     int format,
                                     int type,
                                     long pixels_buffer_offset) {
    gl().glTexSubImage2D(
      target,
      level,
      xoffset,
      yoffset,
      width,
      height,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glTexSubImage3D(int target,
                                     int level,
                                     int xoffset,
                                     int yoffset,
                                     int zoffset,
                                     int width,
                                     int height,
                                     int depth,
                                     int format,
                                     int type,
                                     Buffer pixels) {
    gl().glTexSubImage3D(
      target,
      level,
      xoffset,
      yoffset,
      zoffset,
      width,
      height,
      depth,
      format,
      type,
      pixels);
  }

  public static void glTexSubImage3D(int target,
                                     int level,
                                     int xoffset,
                                     int yoffset,
                                     int zoffset,
                                     int width,
                                     int height,
                                     int depth,
                                     int format,
                                     int type,
                                     long pixels_buffer_offset) {
    gl().glTexSubImage3D(
      target,
      level,
      xoffset,
      yoffset,
      zoffset,
      width,
      height,
      depth,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glTexSubImage4DSGIS(int target,
                                         int level,
                                         int xoffset,
                                         int yoffset,
                                         int zoffset,
                                         int woffset,
                                         int width,
                                         int height,
                                         int depth,
                                         int size4d,
                                         int format,
                                         int type,
                                         Buffer pixels) {
    gl().glTexSubImage4DSGIS(
      target,
      level,
      xoffset,
      yoffset,
      zoffset,
      woffset,
      width,
      height,
      depth,
      size4d,
      format,
      type,
      pixels);
  }

  public static void glTextureColorMaskSGIS(boolean red,
                                            boolean green,
                                            boolean blue,
                                            boolean alpha) {
    gl().glTextureColorMaskSGIS(
      red,
      green,
      blue,
      alpha);
  }

  public static void glTextureLightEXT(int mode) {
    gl().glTextureLightEXT(
      mode);
  }

  public static void glTextureMaterialEXT(int target,
                                          int id) {
    gl().glTextureMaterialEXT(
      target,
      id);
  }

  public static void glTextureNormalEXT(int mode) {
    gl().glTextureNormalEXT(
      mode);
  }

  public static void glTextureRangeAPPLE(int target,
                                         int length,
                                         Buffer pointer) {
    gl().glTextureRangeAPPLE(
      target,
      length,
      pointer);
  }

  public static void glTrackMatrixNV(int sfactorRGB,
                                     int dfactorRGB,
                                     int sfactorAlpha,
                                     int dfactorAlpha) {
    gl().glTrackMatrixNV(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glTranslated(double x,
                                  double y,
                                  double z) {
    gl().glTranslated(
      x,
      y,
      z);
  }

  public static void glTranslatef(float x,
                                  float y,
                                  float z) {
    gl().glTranslatef(
      x,
      y,
      z);
  }

  public static void glUniform1f(int location,
                                 float v0) {
    gl().glUniform1f(
      location,
      v0);
  }

  public static void glUniform1fARB(int location,
                                    float v0) {
    gl().glUniform1fARB(
      location,
      v0);
  }

  public static void glUniform1fv(int location,
                                  int count,
                                  FloatBuffer value) {
    gl().glUniform1fv(
      location,
      count,
      value);
  }

  public static void glUniform1fv(int location,
                                  int count,
                                  float[] value,
                                  int value_offset) {
    gl().glUniform1fv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform1fvARB(int location,
                                     int count,
                                     FloatBuffer value) {
    gl().glUniform1fvARB(
      location,
      count,
      value);
  }

  public static void glUniform1fvARB(int location,
                                     int count,
                                     float[] value,
                                     int value_offset) {
    gl().glUniform1fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform1i(int x,
                                 int y) {
    gl().glUniform1i(
      x,
      y);
  }

  public static void glUniform1iARB(int x,
                                    int y) {
    gl().glUniform1iARB(
      x,
      y);
  }

  public static void glUniform1iv(int location,
                                  int count,
                                  IntBuffer value) {
    gl().glUniform1iv(
      location,
      count,
      value);
  }

  public static void glUniform1iv(int location,
                                  int count,
                                  int[] value,
                                  int value_offset) {
    gl().glUniform1iv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform1ivARB(int location,
                                     int count,
                                     IntBuffer value) {
    gl().glUniform1ivARB(
      location,
      count,
      value);
  }

  public static void glUniform1ivARB(int location,
                                     int count,
                                     int[] value,
                                     int value_offset) {
    gl().glUniform1ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform2f(int location,
                                 float v0,
                                 float v1) {
    gl().glUniform2f(
      location,
      v0,
      v1);
  }

  public static void glUniform2fARB(int location,
                                    float v0,
                                    float v1) {
    gl().glUniform2fARB(
      location,
      v0,
      v1);
  }

  public static void glUniform2fv(int location,
                                  int count,
                                  FloatBuffer value) {
    gl().glUniform2fv(
      location,
      count,
      value);
  }

  public static void glUniform2fv(int location,
                                  int count,
                                  float[] value,
                                  int value_offset) {
    gl().glUniform2fv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform2fvARB(int location,
                                     int count,
                                     FloatBuffer value) {
    gl().glUniform2fvARB(
      location,
      count,
      value);
  }

  public static void glUniform2fvARB(int location,
                                     int count,
                                     float[] value,
                                     int value_offset) {
    gl().glUniform2fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform2i(int red,
                                 int green,
                                 int blue) {
    gl().glUniform2i(
      red,
      green,
      blue);
  }

  public static void glUniform2iARB(int red,
                                    int green,
                                    int blue) {
    gl().glUniform2iARB(
      red,
      green,
      blue);
  }

  public static void glUniform2iv(int location,
                                  int count,
                                  IntBuffer value) {
    gl().glUniform2iv(
      location,
      count,
      value);
  }

  public static void glUniform2iv(int location,
                                  int count,
                                  int[] value,
                                  int value_offset) {
    gl().glUniform2iv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform2ivARB(int location,
                                     int count,
                                     IntBuffer value) {
    gl().glUniform2ivARB(
      location,
      count,
      value);
  }

  public static void glUniform2ivARB(int location,
                                     int count,
                                     int[] value,
                                     int value_offset) {
    gl().glUniform2ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform3f(int location,
                                 float v0,
                                 float v1,
                                 float v2) {
    gl().glUniform3f(
      location,
      v0,
      v1,
      v2);
  }

  public static void glUniform3fARB(int location,
                                    float v0,
                                    float v1,
                                    float v2) {
    gl().glUniform3fARB(
      location,
      v0,
      v1,
      v2);
  }

  public static void glUniform3fv(int location,
                                  int count,
                                  FloatBuffer value) {
    gl().glUniform3fv(
      location,
      count,
      value);
  }

  public static void glUniform3fv(int location,
                                  int count,
                                  float[] value,
                                  int value_offset) {
    gl().glUniform3fv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform3fvARB(int location,
                                     int count,
                                     FloatBuffer value) {
    gl().glUniform3fvARB(
      location,
      count,
      value);
  }

  public static void glUniform3fvARB(int location,
                                     int count,
                                     float[] value,
                                     int value_offset) {
    gl().glUniform3fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform3i(int location,
                                 int v0,
                                 int v1,
                                 int v2) {
    gl().glUniform3i(
      location,
      v0,
      v1,
      v2);
  }

  public static void glUniform3iARB(int location,
                                    int v0,
                                    int v1,
                                    int v2) {
    gl().glUniform3iARB(
      location,
      v0,
      v1,
      v2);
  }

  public static void glUniform3iv(int location,
                                  int count,
                                  IntBuffer value) {
    gl().glUniform3iv(
      location,
      count,
      value);
  }

  public static void glUniform3iv(int location,
                                  int count,
                                  int[] value,
                                  int value_offset) {
    gl().glUniform3iv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform3ivARB(int location,
                                     int count,
                                     IntBuffer value) {
    gl().glUniform3ivARB(
      location,
      count,
      value);
  }

  public static void glUniform3ivARB(int location,
                                     int count,
                                     int[] value,
                                     int value_offset) {
    gl().glUniform3ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform4f(int location,
                                 float v0,
                                 float v1,
                                 float v2,
                                 float v3) {
    gl().glUniform4f(
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glUniform4fARB(int location,
                                    float v0,
                                    float v1,
                                    float v2,
                                    float v3) {
    gl().glUniform4fARB(
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glUniform4fv(int location,
                                  int count,
                                  FloatBuffer value) {
    gl().glUniform4fv(
      location,
      count,
      value);
  }

  public static void glUniform4fv(int location,
                                  int count,
                                  float[] value,
                                  int value_offset) {
    gl().glUniform4fv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform4fvARB(int location,
                                     int count,
                                     FloatBuffer value) {
    gl().glUniform4fvARB(
      location,
      count,
      value);
  }

  public static void glUniform4fvARB(int location,
                                     int count,
                                     float[] value,
                                     int value_offset) {
    gl().glUniform4fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform4i(int location,
                                 int v0,
                                 int v1,
                                 int v2,
                                 int v3) {
    gl().glUniform4i(
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glUniform4iARB(int location,
                                    int v0,
                                    int v1,
                                    int v2,
                                    int v3) {
    gl().glUniform4iARB(
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glUniform4iv(int location,
                                  int count,
                                  IntBuffer value) {
    gl().glUniform4iv(
      location,
      count,
      value);
  }

  public static void glUniform4iv(int location,
                                  int count,
                                  int[] value,
                                  int value_offset) {
    gl().glUniform4iv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform4ivARB(int location,
                                     int count,
                                     IntBuffer value) {
    gl().glUniform4ivARB(
      location,
      count,
      value);
  }

  public static void glUniform4ivARB(int location,
                                     int count,
                                     int[] value,
                                     int value_offset) {
    gl().glUniform4ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniformMatrix2fv(int location,
                                        int count,
                                        boolean transpose,
                                        FloatBuffer value) {
    gl().glUniformMatrix2fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix2fv(int location,
                                        int count,
                                        boolean transpose,
                                        float[] value,
                                        int value_offset) {
    gl().glUniformMatrix2fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix2fvARB(int location,
                                           int count,
                                           boolean transpose,
                                           FloatBuffer value) {
    gl().glUniformMatrix2fvARB(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix2fvARB(int location,
                                           int count,
                                           boolean transpose,
                                           float[] value,
                                           int value_offset) {
    gl().glUniformMatrix2fvARB(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix3fv(int location,
                                        int count,
                                        boolean transpose,
                                        FloatBuffer value) {
    gl().glUniformMatrix3fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix3fv(int location,
                                        int count,
                                        boolean transpose,
                                        float[] value,
                                        int value_offset) {
    gl().glUniformMatrix3fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix3fvARB(int location,
                                           int count,
                                           boolean transpose,
                                           FloatBuffer value) {
    gl().glUniformMatrix3fvARB(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix3fvARB(int location,
                                           int count,
                                           boolean transpose,
                                           float[] value,
                                           int value_offset) {
    gl().glUniformMatrix3fvARB(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix4fv(int location,
                                        int count,
                                        boolean transpose,
                                        FloatBuffer value) {
    gl().glUniformMatrix4fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix4fv(int location,
                                        int count,
                                        boolean transpose,
                                        float[] value,
                                        int value_offset) {
    gl().glUniformMatrix4fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix4fvARB(int location,
                                           int count,
                                           boolean transpose,
                                           FloatBuffer value) {
    gl().glUniformMatrix4fvARB(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix4fvARB(int location,
                                           int count,
                                           boolean transpose,
                                           float[] value,
                                           int value_offset) {
    gl().glUniformMatrix4fvARB(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUnlockArraysEXT() {
    gl().glUnlockArraysEXT();
  }

  public static boolean glUnmapBuffer(int id) {
    return gl().glUnmapBuffer(
      id);
  }

  public static boolean glUnmapBufferARB(int id) {
    return gl().glUnmapBufferARB(
      id);
  }

  public static void glUpdateObjectBufferATI(int buffer,
                                             int offset,
                                             int size,
                                             Buffer pointer,
                                             int preserve) {
    gl().glUpdateObjectBufferATI(
      buffer,
      offset,
      size,
      pointer,
      preserve);
  }

  public static void glUseProgram(int mode) {
    gl().glUseProgram(
      mode);
  }

  public static void glUseProgramObjectARB(int mode) {
    gl().glUseProgramObjectARB(
      mode);
  }

  public static void glValidateProgram(int mode) {
    gl().glValidateProgram(
      mode);
  }

  public static void glValidateProgramARB(int mode) {
    gl().glValidateProgramARB(
      mode);
  }

  public static void glVariantArrayObjectATI(int id,
                                             int type,
                                             int stride,
                                             int buffer,
                                             int offset) {
    gl().glVariantArrayObjectATI(
      id,
      type,
      stride,
      buffer,
      offset);
  }

  public static void glVariantPointerEXT(int id,
                                         int type,
                                         int stride,
                                         Buffer addr) {
    gl().glVariantPointerEXT(
      id,
      type,
      stride,
      addr);
  }

  public static void glVariantPointerEXT(int id,
                                         int type,
                                         int stride,
                                         long addr_buffer_offset) {
    gl().glVariantPointerEXT(
      id,
      type,
      stride,
      addr_buffer_offset);
  }

  public static void glVariantbvEXT(int index,
                                    ByteBuffer v) {
    gl().glVariantbvEXT(
      index,
      v);
  }

  public static void glVariantbvEXT(int index,
                                    byte[] v,
                                    int v_offset) {
    gl().glVariantbvEXT(
      index,
      v,
      v_offset);
  }

  public static void glVariantdvEXT(int target,
                                    DoubleBuffer v) {
    gl().glVariantdvEXT(
      target,
      v);
  }

  public static void glVariantdvEXT(int target,
                                    double[] v,
                                    int v_offset) {
    gl().glVariantdvEXT(
      target,
      v,
      v_offset);
  }

  public static void glVariantfvEXT(int target,
                                    FloatBuffer v) {
    gl().glVariantfvEXT(
      target,
      v);
  }

  public static void glVariantfvEXT(int target,
                                    float[] v,
                                    int v_offset) {
    gl().glVariantfvEXT(
      target,
      v,
      v_offset);
  }

  public static void glVariantivEXT(int target,
                                    IntBuffer v) {
    gl().glVariantivEXT(
      target,
      v);
  }

  public static void glVariantivEXT(int target,
                                    int[] v,
                                    int v_offset) {
    gl().glVariantivEXT(
      target,
      v,
      v_offset);
  }

  public static void glVariantsvEXT(int target,
                                    ShortBuffer v) {
    gl().glVariantsvEXT(
      target,
      v);
  }

  public static void glVariantsvEXT(int target,
                                    short[] v,
                                    int v_offset) {
    gl().glVariantsvEXT(
      target,
      v,
      v_offset);
  }

  public static void glVariantubvEXT(int index,
                                     ByteBuffer v) {
    gl().glVariantubvEXT(
      index,
      v);
  }

  public static void glVariantubvEXT(int index,
                                     byte[] v,
                                     int v_offset) {
    gl().glVariantubvEXT(
      index,
      v,
      v_offset);
  }

  public static void glVariantuivEXT(int index,
                                     IntBuffer v) {
    gl().glVariantuivEXT(
      index,
      v);
  }

  public static void glVariantuivEXT(int index,
                                     int[] v,
                                     int v_offset) {
    gl().glVariantuivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVariantusvEXT(int index,
                                     ShortBuffer v) {
    gl().glVariantusvEXT(
      index,
      v);
  }

  public static void glVariantusvEXT(int index,
                                     short[] v,
                                     int v_offset) {
    gl().glVariantusvEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertex2d(double x,
                                double y) {
    gl().glVertex2d(
      x,
      y);
  }

  public static void glVertex2dv(DoubleBuffer v) {
    gl().glVertex2dv(
      v);
  }

  public static void glVertex2dv(double[] v,
                                 int v_offset) {
    gl().glVertex2dv(
      v,
      v_offset);
  }

  public static void glVertex2f(float x,
                                float y) {
    gl().glVertex2f(
      x,
      y);
  }

  public static void glVertex2fv(FloatBuffer v) {
    gl().glVertex2fv(
      v);
  }

  public static void glVertex2fv(float[] v,
                                 int v_offset) {
    gl().glVertex2fv(
      v,
      v_offset);
  }

  public static void glVertex2hNV(short x,
                                  short y) {
    gl().glVertex2hNV(
      x,
      y);
  }

  public static void glVertex2hvNV(ShortBuffer v) {
    gl().glVertex2hvNV(
      v);
  }

  public static void glVertex2hvNV(short[] v,
                                   int v_offset) {
    gl().glVertex2hvNV(
      v,
      v_offset);
  }

  public static void glVertex2i(int x,
                                int y) {
    gl().glVertex2i(
      x,
      y);
  }

  public static void glVertex2iv(IntBuffer v) {
    gl().glVertex2iv(
      v);
  }

  public static void glVertex2iv(int[] v,
                                 int v_offset) {
    gl().glVertex2iv(
      v,
      v_offset);
  }

  public static void glVertex2s(short x,
                                short y) {
    gl().glVertex2s(
      x,
      y);
  }

  public static void glVertex2sv(ShortBuffer v) {
    gl().glVertex2sv(
      v);
  }

  public static void glVertex2sv(short[] v,
                                 int v_offset) {
    gl().glVertex2sv(
      v,
      v_offset);
  }

  public static void glVertex3d(double x,
                                double y,
                                double z) {
    gl().glVertex3d(
      x,
      y,
      z);
  }

  public static void glVertex3dv(DoubleBuffer v) {
    gl().glVertex3dv(
      v);
  }

  public static void glVertex3dv(double[] v,
                                 int v_offset) {
    gl().glVertex3dv(
      v,
      v_offset);
  }

  public static void glVertex3f(float x,
                                float y,
                                float z) {
    gl().glVertex3f(
      x,
      y,
      z);
  }

  public static void glVertex3fv(FloatBuffer v) {
    gl().glVertex3fv(
      v);
  }

  public static void glVertex3fv(float[] v,
                                 int v_offset) {
    gl().glVertex3fv(
      v,
      v_offset);
  }

  public static void glVertex3hNV(short red,
                                  short green,
                                  short blue) {
    gl().glVertex3hNV(
      red,
      green,
      blue);
  }

  public static void glVertex3hvNV(ShortBuffer v) {
    gl().glVertex3hvNV(
      v);
  }

  public static void glVertex3hvNV(short[] v,
                                   int v_offset) {
    gl().glVertex3hvNV(
      v,
      v_offset);
  }

  public static void glVertex3i(int x,
                                int y,
                                int z) {
    gl().glVertex3i(
      x,
      y,
      z);
  }

  public static void glVertex3iv(IntBuffer v) {
    gl().glVertex3iv(
      v);
  }

  public static void glVertex3iv(int[] v,
                                 int v_offset) {
    gl().glVertex3iv(
      v,
      v_offset);
  }

  public static void glVertex3s(short x,
                                short y,
                                short z) {
    gl().glVertex3s(
      x,
      y,
      z);
  }

  public static void glVertex3sv(ShortBuffer v) {
    gl().glVertex3sv(
      v);
  }

  public static void glVertex3sv(short[] v,
                                 int v_offset) {
    gl().glVertex3sv(
      v,
      v_offset);
  }

  public static void glVertex4d(double x,
                                double y,
                                double z,
                                double w) {
    gl().glVertex4d(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4dv(DoubleBuffer v) {
    gl().glVertex4dv(
      v);
  }

  public static void glVertex4dv(double[] v,
                                 int v_offset) {
    gl().glVertex4dv(
      v,
      v_offset);
  }

  public static void glVertex4f(float x,
                                float y,
                                float z,
                                float w) {
    gl().glVertex4f(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4fv(FloatBuffer v) {
    gl().glVertex4fv(
      v);
  }

  public static void glVertex4fv(float[] v,
                                 int v_offset) {
    gl().glVertex4fv(
      v,
      v_offset);
  }

  public static void glVertex4hNV(short x,
                                  short y,
                                  short z,
                                  short w) {
    gl().glVertex4hNV(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4hvNV(ShortBuffer v) {
    gl().glVertex4hvNV(
      v);
  }

  public static void glVertex4hvNV(short[] v,
                                   int v_offset) {
    gl().glVertex4hvNV(
      v,
      v_offset);
  }

  public static void glVertex4i(int x,
                                int y,
                                int z,
                                int w) {
    gl().glVertex4i(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4iv(IntBuffer v) {
    gl().glVertex4iv(
      v);
  }

  public static void glVertex4iv(int[] v,
                                 int v_offset) {
    gl().glVertex4iv(
      v,
      v_offset);
  }

  public static void glVertex4s(short x,
                                short y,
                                short z,
                                short w) {
    gl().glVertex4s(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4sv(ShortBuffer v) {
    gl().glVertex4sv(
      v);
  }

  public static void glVertex4sv(short[] v,
                                 int v_offset) {
    gl().glVertex4sv(
      v,
      v_offset);
  }

  public static void glVertexArrayParameteriAPPLE(int target,
                                                  int s) {
    gl().glVertexArrayParameteriAPPLE(
      target,
      s);
  }

  public static void glVertexArrayRangeAPPLE(int length,
                                             Buffer pointer) {
    gl().glVertexArrayRangeAPPLE(
      length,
      pointer);
  }

  public static void glVertexArrayRangeNV(int length,
                                          Buffer pointer) {
    gl().glVertexArrayRangeNV(
      length,
      pointer);
  }

  public static void glVertexAttrib1d(int target,
                                      double s) {
    gl().glVertexAttrib1d(
      target,
      s);
  }

  public static void glVertexAttrib1dARB(int target,
                                         double s) {
    gl().glVertexAttrib1dARB(
      target,
      s);
  }

  public static void glVertexAttrib1dNV(int target,
                                        double s) {
    gl().glVertexAttrib1dNV(
      target,
      s);
  }

  public static void glVertexAttrib1dv(int target,
                                       DoubleBuffer v) {
    gl().glVertexAttrib1dv(
      target,
      v);
  }

  public static void glVertexAttrib1dv(int target,
                                       double[] v,
                                       int v_offset) {
    gl().glVertexAttrib1dv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1dvARB(int target,
                                          DoubleBuffer v) {
    gl().glVertexAttrib1dvARB(
      target,
      v);
  }

  public static void glVertexAttrib1dvARB(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttrib1dvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1dvNV(int target,
                                         DoubleBuffer v) {
    gl().glVertexAttrib1dvNV(
      target,
      v);
  }

  public static void glVertexAttrib1dvNV(int target,
                                         double[] v,
                                         int v_offset) {
    gl().glVertexAttrib1dvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1f(int target,
                                      float s) {
    gl().glVertexAttrib1f(
      target,
      s);
  }

  public static void glVertexAttrib1fARB(int target,
                                         float s) {
    gl().glVertexAttrib1fARB(
      target,
      s);
  }

  public static void glVertexAttrib1fNV(int target,
                                        float s) {
    gl().glVertexAttrib1fNV(
      target,
      s);
  }

  public static void glVertexAttrib1fv(int target,
                                       FloatBuffer v) {
    gl().glVertexAttrib1fv(
      target,
      v);
  }

  public static void glVertexAttrib1fv(int target,
                                       float[] v,
                                       int v_offset) {
    gl().glVertexAttrib1fv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1fvARB(int target,
                                          FloatBuffer v) {
    gl().glVertexAttrib1fvARB(
      target,
      v);
  }

  public static void glVertexAttrib1fvARB(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexAttrib1fvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1fvNV(int target,
                                         FloatBuffer v) {
    gl().glVertexAttrib1fvNV(
      target,
      v);
  }

  public static void glVertexAttrib1fvNV(int target,
                                         float[] v,
                                         int v_offset) {
    gl().glVertexAttrib1fvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1hNV(int target,
                                        short s) {
    gl().glVertexAttrib1hNV(
      target,
      s);
  }

  public static void glVertexAttrib1hvNV(int index,
                                         ShortBuffer v) {
    gl().glVertexAttrib1hvNV(
      index,
      v);
  }

  public static void glVertexAttrib1hvNV(int index,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib1hvNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib1s(int target,
                                      short s) {
    gl().glVertexAttrib1s(
      target,
      s);
  }

  public static void glVertexAttrib1sARB(int target,
                                         short s) {
    gl().glVertexAttrib1sARB(
      target,
      s);
  }

  public static void glVertexAttrib1sNV(int target,
                                        short s) {
    gl().glVertexAttrib1sNV(
      target,
      s);
  }

  public static void glVertexAttrib1sv(int target,
                                       ShortBuffer v) {
    gl().glVertexAttrib1sv(
      target,
      v);
  }

  public static void glVertexAttrib1sv(int target,
                                       short[] v,
                                       int v_offset) {
    gl().glVertexAttrib1sv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1svARB(int target,
                                          ShortBuffer v) {
    gl().glVertexAttrib1svARB(
      target,
      v);
  }

  public static void glVertexAttrib1svARB(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttrib1svARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib1svNV(int target,
                                         ShortBuffer v) {
    gl().glVertexAttrib1svNV(
      target,
      v);
  }

  public static void glVertexAttrib1svNV(int target,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib1svNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2d(int target,
                                      double s,
                                      double t) {
    gl().glVertexAttrib2d(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2dARB(int target,
                                         double s,
                                         double t) {
    gl().glVertexAttrib2dARB(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2dNV(int target,
                                        double s,
                                        double t) {
    gl().glVertexAttrib2dNV(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2dv(int target,
                                       DoubleBuffer v) {
    gl().glVertexAttrib2dv(
      target,
      v);
  }

  public static void glVertexAttrib2dv(int target,
                                       double[] v,
                                       int v_offset) {
    gl().glVertexAttrib2dv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2dvARB(int target,
                                          DoubleBuffer v) {
    gl().glVertexAttrib2dvARB(
      target,
      v);
  }

  public static void glVertexAttrib2dvARB(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttrib2dvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2dvNV(int target,
                                         DoubleBuffer v) {
    gl().glVertexAttrib2dvNV(
      target,
      v);
  }

  public static void glVertexAttrib2dvNV(int target,
                                         double[] v,
                                         int v_offset) {
    gl().glVertexAttrib2dvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2f(int target,
                                      float s,
                                      float t) {
    gl().glVertexAttrib2f(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2fARB(int target,
                                         float s,
                                         float t) {
    gl().glVertexAttrib2fARB(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2fNV(int target,
                                        float s,
                                        float t) {
    gl().glVertexAttrib2fNV(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2fv(int target,
                                       FloatBuffer v) {
    gl().glVertexAttrib2fv(
      target,
      v);
  }

  public static void glVertexAttrib2fv(int target,
                                       float[] v,
                                       int v_offset) {
    gl().glVertexAttrib2fv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2fvARB(int target,
                                          FloatBuffer v) {
    gl().glVertexAttrib2fvARB(
      target,
      v);
  }

  public static void glVertexAttrib2fvARB(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexAttrib2fvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2fvNV(int target,
                                         FloatBuffer v) {
    gl().glVertexAttrib2fvNV(
      target,
      v);
  }

  public static void glVertexAttrib2fvNV(int target,
                                         float[] v,
                                         int v_offset) {
    gl().glVertexAttrib2fvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2hNV(int target,
                                        short s,
                                        short t) {
    gl().glVertexAttrib2hNV(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2hvNV(int index,
                                         ShortBuffer v) {
    gl().glVertexAttrib2hvNV(
      index,
      v);
  }

  public static void glVertexAttrib2hvNV(int index,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib2hvNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib2s(int target,
                                      short s,
                                      short t) {
    gl().glVertexAttrib2s(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2sARB(int target,
                                         short s,
                                         short t) {
    gl().glVertexAttrib2sARB(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2sNV(int target,
                                        short s,
                                        short t) {
    gl().glVertexAttrib2sNV(
      target,
      s,
      t);
  }

  public static void glVertexAttrib2sv(int target,
                                       ShortBuffer v) {
    gl().glVertexAttrib2sv(
      target,
      v);
  }

  public static void glVertexAttrib2sv(int target,
                                       short[] v,
                                       int v_offset) {
    gl().glVertexAttrib2sv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2svARB(int target,
                                          ShortBuffer v) {
    gl().glVertexAttrib2svARB(
      target,
      v);
  }

  public static void glVertexAttrib2svARB(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttrib2svARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib2svNV(int target,
                                         ShortBuffer v) {
    gl().glVertexAttrib2svNV(
      target,
      v);
  }

  public static void glVertexAttrib2svNV(int target,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib2svNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3d(int target,
                                      double s,
                                      double t,
                                      double r) {
    gl().glVertexAttrib3d(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3dARB(int target,
                                         double s,
                                         double t,
                                         double r) {
    gl().glVertexAttrib3dARB(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3dNV(int target,
                                        double s,
                                        double t,
                                        double r) {
    gl().glVertexAttrib3dNV(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3dv(int target,
                                       DoubleBuffer v) {
    gl().glVertexAttrib3dv(
      target,
      v);
  }

  public static void glVertexAttrib3dv(int target,
                                       double[] v,
                                       int v_offset) {
    gl().glVertexAttrib3dv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3dvARB(int target,
                                          DoubleBuffer v) {
    gl().glVertexAttrib3dvARB(
      target,
      v);
  }

  public static void glVertexAttrib3dvARB(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttrib3dvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3dvNV(int target,
                                         DoubleBuffer v) {
    gl().glVertexAttrib3dvNV(
      target,
      v);
  }

  public static void glVertexAttrib3dvNV(int target,
                                         double[] v,
                                         int v_offset) {
    gl().glVertexAttrib3dvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3f(int target,
                                      float s,
                                      float t,
                                      float r) {
    gl().glVertexAttrib3f(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3fARB(int target,
                                         float s,
                                         float t,
                                         float r) {
    gl().glVertexAttrib3fARB(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3fNV(int target,
                                        float s,
                                        float t,
                                        float r) {
    gl().glVertexAttrib3fNV(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3fv(int target,
                                       FloatBuffer v) {
    gl().glVertexAttrib3fv(
      target,
      v);
  }

  public static void glVertexAttrib3fv(int target,
                                       float[] v,
                                       int v_offset) {
    gl().glVertexAttrib3fv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3fvARB(int target,
                                          FloatBuffer v) {
    gl().glVertexAttrib3fvARB(
      target,
      v);
  }

  public static void glVertexAttrib3fvARB(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexAttrib3fvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3fvNV(int target,
                                         FloatBuffer v) {
    gl().glVertexAttrib3fvNV(
      target,
      v);
  }

  public static void glVertexAttrib3fvNV(int target,
                                         float[] v,
                                         int v_offset) {
    gl().glVertexAttrib3fvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3hNV(int target,
                                        short s,
                                        short t,
                                        short r) {
    gl().glVertexAttrib3hNV(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3hvNV(int index,
                                         ShortBuffer v) {
    gl().glVertexAttrib3hvNV(
      index,
      v);
  }

  public static void glVertexAttrib3hvNV(int index,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib3hvNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib3s(int target,
                                      short s,
                                      short t,
                                      short r) {
    gl().glVertexAttrib3s(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3sARB(int target,
                                         short s,
                                         short t,
                                         short r) {
    gl().glVertexAttrib3sARB(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3sNV(int target,
                                        short s,
                                        short t,
                                        short r) {
    gl().glVertexAttrib3sNV(
      target,
      s,
      t,
      r);
  }

  public static void glVertexAttrib3sv(int target,
                                       ShortBuffer v) {
    gl().glVertexAttrib3sv(
      target,
      v);
  }

  public static void glVertexAttrib3sv(int target,
                                       short[] v,
                                       int v_offset) {
    gl().glVertexAttrib3sv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3svARB(int target,
                                          ShortBuffer v) {
    gl().glVertexAttrib3svARB(
      target,
      v);
  }

  public static void glVertexAttrib3svARB(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttrib3svARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib3svNV(int target,
                                         ShortBuffer v) {
    gl().glVertexAttrib3svNV(
      target,
      v);
  }

  public static void glVertexAttrib3svNV(int target,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib3svNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nbv(int index,
                                        ByteBuffer v) {
    gl().glVertexAttrib4Nbv(
      index,
      v);
  }

  public static void glVertexAttrib4Nbv(int index,
                                        byte[] v,
                                        int v_offset) {
    gl().glVertexAttrib4Nbv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NbvARB(int index,
                                           ByteBuffer v) {
    gl().glVertexAttrib4NbvARB(
      index,
      v);
  }

  public static void glVertexAttrib4NbvARB(int index,
                                           byte[] v,
                                           int v_offset) {
    gl().glVertexAttrib4NbvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Niv(int target,
                                        IntBuffer v) {
    gl().glVertexAttrib4Niv(
      target,
      v);
  }

  public static void glVertexAttrib4Niv(int target,
                                        int[] v,
                                        int v_offset) {
    gl().glVertexAttrib4Niv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NivARB(int target,
                                           IntBuffer v) {
    gl().glVertexAttrib4NivARB(
      target,
      v);
  }

  public static void glVertexAttrib4NivARB(int target,
                                           int[] v,
                                           int v_offset) {
    gl().glVertexAttrib4NivARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nsv(int target,
                                        ShortBuffer v) {
    gl().glVertexAttrib4Nsv(
      target,
      v);
  }

  public static void glVertexAttrib4Nsv(int target,
                                        short[] v,
                                        int v_offset) {
    gl().glVertexAttrib4Nsv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NsvARB(int target,
                                           ShortBuffer v) {
    gl().glVertexAttrib4NsvARB(
      target,
      v);
  }

  public static void glVertexAttrib4NsvARB(int target,
                                           short[] v,
                                           int v_offset) {
    gl().glVertexAttrib4NsvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nub(int index,
                                        byte x,
                                        byte y,
                                        byte z,
                                        byte w) {
    gl().glVertexAttrib4Nub(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4NubARB(int index,
                                           byte x,
                                           byte y,
                                           byte z,
                                           byte w) {
    gl().glVertexAttrib4NubARB(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4Nubv(int index,
                                         ByteBuffer v) {
    gl().glVertexAttrib4Nubv(
      index,
      v);
  }

  public static void glVertexAttrib4Nubv(int index,
                                         byte[] v,
                                         int v_offset) {
    gl().glVertexAttrib4Nubv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NubvARB(int index,
                                            ByteBuffer v) {
    gl().glVertexAttrib4NubvARB(
      index,
      v);
  }

  public static void glVertexAttrib4NubvARB(int index,
                                            byte[] v,
                                            int v_offset) {
    gl().glVertexAttrib4NubvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nuiv(int index,
                                         IntBuffer v) {
    gl().glVertexAttrib4Nuiv(
      index,
      v);
  }

  public static void glVertexAttrib4Nuiv(int index,
                                         int[] v,
                                         int v_offset) {
    gl().glVertexAttrib4Nuiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NuivARB(int index,
                                            IntBuffer v) {
    gl().glVertexAttrib4NuivARB(
      index,
      v);
  }

  public static void glVertexAttrib4NuivARB(int index,
                                            int[] v,
                                            int v_offset) {
    gl().glVertexAttrib4NuivARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nusv(int index,
                                         ShortBuffer v) {
    gl().glVertexAttrib4Nusv(
      index,
      v);
  }

  public static void glVertexAttrib4Nusv(int index,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib4Nusv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NusvARB(int index,
                                            ShortBuffer v) {
    gl().glVertexAttrib4NusvARB(
      index,
      v);
  }

  public static void glVertexAttrib4NusvARB(int index,
                                            short[] v,
                                            int v_offset) {
    gl().glVertexAttrib4NusvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4bv(int index,
                                       ByteBuffer v) {
    gl().glVertexAttrib4bv(
      index,
      v);
  }

  public static void glVertexAttrib4bv(int index,
                                       byte[] v,
                                       int v_offset) {
    gl().glVertexAttrib4bv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4bvARB(int index,
                                          ByteBuffer v) {
    gl().glVertexAttrib4bvARB(
      index,
      v);
  }

  public static void glVertexAttrib4bvARB(int index,
                                          byte[] v,
                                          int v_offset) {
    gl().glVertexAttrib4bvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4d(int target,
                                      double s,
                                      double t,
                                      double r,
                                      double q) {
    gl().glVertexAttrib4d(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4dARB(int target,
                                         double s,
                                         double t,
                                         double r,
                                         double q) {
    gl().glVertexAttrib4dARB(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4dNV(int target,
                                        double s,
                                        double t,
                                        double r,
                                        double q) {
    gl().glVertexAttrib4dNV(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4dv(int target,
                                       DoubleBuffer v) {
    gl().glVertexAttrib4dv(
      target,
      v);
  }

  public static void glVertexAttrib4dv(int target,
                                       double[] v,
                                       int v_offset) {
    gl().glVertexAttrib4dv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4dvARB(int target,
                                          DoubleBuffer v) {
    gl().glVertexAttrib4dvARB(
      target,
      v);
  }

  public static void glVertexAttrib4dvARB(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttrib4dvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4dvNV(int target,
                                         DoubleBuffer v) {
    gl().glVertexAttrib4dvNV(
      target,
      v);
  }

  public static void glVertexAttrib4dvNV(int target,
                                         double[] v,
                                         int v_offset) {
    gl().glVertexAttrib4dvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4f(int target,
                                      float s,
                                      float t,
                                      float r,
                                      float q) {
    gl().glVertexAttrib4f(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4fARB(int target,
                                         float s,
                                         float t,
                                         float r,
                                         float q) {
    gl().glVertexAttrib4fARB(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4fNV(int target,
                                        float s,
                                        float t,
                                        float r,
                                        float q) {
    gl().glVertexAttrib4fNV(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4fv(int target,
                                       FloatBuffer v) {
    gl().glVertexAttrib4fv(
      target,
      v);
  }

  public static void glVertexAttrib4fv(int target,
                                       float[] v,
                                       int v_offset) {
    gl().glVertexAttrib4fv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4fvARB(int target,
                                          FloatBuffer v) {
    gl().glVertexAttrib4fvARB(
      target,
      v);
  }

  public static void glVertexAttrib4fvARB(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexAttrib4fvARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4fvNV(int target,
                                         FloatBuffer v) {
    gl().glVertexAttrib4fvNV(
      target,
      v);
  }

  public static void glVertexAttrib4fvNV(int target,
                                         float[] v,
                                         int v_offset) {
    gl().glVertexAttrib4fvNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4hNV(int target,
                                        short s,
                                        short t,
                                        short r,
                                        short q) {
    gl().glVertexAttrib4hNV(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4hvNV(int index,
                                         ShortBuffer v) {
    gl().glVertexAttrib4hvNV(
      index,
      v);
  }

  public static void glVertexAttrib4hvNV(int index,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib4hvNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4iv(int target,
                                       IntBuffer v) {
    gl().glVertexAttrib4iv(
      target,
      v);
  }

  public static void glVertexAttrib4iv(int target,
                                       int[] v,
                                       int v_offset) {
    gl().glVertexAttrib4iv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4ivARB(int target,
                                          IntBuffer v) {
    gl().glVertexAttrib4ivARB(
      target,
      v);
  }

  public static void glVertexAttrib4ivARB(int target,
                                          int[] v,
                                          int v_offset) {
    gl().glVertexAttrib4ivARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4s(int target,
                                      short s,
                                      short t,
                                      short r,
                                      short q) {
    gl().glVertexAttrib4s(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4sARB(int target,
                                         short s,
                                         short t,
                                         short r,
                                         short q) {
    gl().glVertexAttrib4sARB(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4sNV(int target,
                                        short s,
                                        short t,
                                        short r,
                                        short q) {
    gl().glVertexAttrib4sNV(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexAttrib4sv(int target,
                                       ShortBuffer v) {
    gl().glVertexAttrib4sv(
      target,
      v);
  }

  public static void glVertexAttrib4sv(int target,
                                       short[] v,
                                       int v_offset) {
    gl().glVertexAttrib4sv(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4svARB(int target,
                                          ShortBuffer v) {
    gl().glVertexAttrib4svARB(
      target,
      v);
  }

  public static void glVertexAttrib4svARB(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttrib4svARB(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4svNV(int target,
                                         ShortBuffer v) {
    gl().glVertexAttrib4svNV(
      target,
      v);
  }

  public static void glVertexAttrib4svNV(int target,
                                         short[] v,
                                         int v_offset) {
    gl().glVertexAttrib4svNV(
      target,
      v,
      v_offset);
  }

  public static void glVertexAttrib4ubNV(int index,
                                         byte x,
                                         byte y,
                                         byte z,
                                         byte w) {
    gl().glVertexAttrib4ubNV(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4ubv(int index,
                                        ByteBuffer v) {
    gl().glVertexAttrib4ubv(
      index,
      v);
  }

  public static void glVertexAttrib4ubv(int index,
                                        byte[] v,
                                        int v_offset) {
    gl().glVertexAttrib4ubv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4ubvARB(int index,
                                           ByteBuffer v) {
    gl().glVertexAttrib4ubvARB(
      index,
      v);
  }

  public static void glVertexAttrib4ubvARB(int index,
                                           byte[] v,
                                           int v_offset) {
    gl().glVertexAttrib4ubvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4ubvNV(int index,
                                          ByteBuffer v) {
    gl().glVertexAttrib4ubvNV(
      index,
      v);
  }

  public static void glVertexAttrib4ubvNV(int index,
                                          byte[] v,
                                          int v_offset) {
    gl().glVertexAttrib4ubvNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4uiv(int index,
                                        IntBuffer v) {
    gl().glVertexAttrib4uiv(
      index,
      v);
  }

  public static void glVertexAttrib4uiv(int index,
                                        int[] v,
                                        int v_offset) {
    gl().glVertexAttrib4uiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4uivARB(int index,
                                           IntBuffer v) {
    gl().glVertexAttrib4uivARB(
      index,
      v);
  }

  public static void glVertexAttrib4uivARB(int index,
                                           int[] v,
                                           int v_offset) {
    gl().glVertexAttrib4uivARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4usv(int index,
                                        ShortBuffer v) {
    gl().glVertexAttrib4usv(
      index,
      v);
  }

  public static void glVertexAttrib4usv(int index,
                                        short[] v,
                                        int v_offset) {
    gl().glVertexAttrib4usv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4usvARB(int index,
                                           ShortBuffer v) {
    gl().glVertexAttrib4usvARB(
      index,
      v);
  }

  public static void glVertexAttrib4usvARB(int index,
                                           short[] v,
                                           int v_offset) {
    gl().glVertexAttrib4usvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribArrayObjectATI(int index,
                                                  int size,
                                                  int type,
                                                  boolean normalized,
                                                  int stride,
                                                  int buffer,
                                                  int offset) {
    gl().glVertexAttribArrayObjectATI(
      index,
      size,
      type,
      normalized,
      stride,
      buffer,
      offset);
  }

  public static void glVertexAttribPointer(int index,
                                           int size,
                                           int type,
                                           boolean normalized,
                                           int stride,
                                           Buffer pointer) {
    gl().glVertexAttribPointer(
      index,
      size,
      type,
      normalized,
      stride,
      pointer);
  }

  public static void glVertexAttribPointer(int index,
                                           int size,
                                           int type,
                                           boolean normalized,
                                           int stride,
                                           long pointer_buffer_offset) {
    gl().glVertexAttribPointer(
      index,
      size,
      type,
      normalized,
      stride,
      pointer_buffer_offset);
  }

  public static void glVertexAttribPointerARB(int index,
                                              int size,
                                              int type,
                                              boolean normalized,
                                              int stride,
                                              Buffer pointer) {
    gl().glVertexAttribPointerARB(
      index,
      size,
      type,
      normalized,
      stride,
      pointer);
  }

  public static void glVertexAttribPointerARB(int index,
                                              int size,
                                              int type,
                                              boolean normalized,
                                              int stride,
                                              long pointer_buffer_offset) {
    gl().glVertexAttribPointerARB(
      index,
      size,
      type,
      normalized,
      stride,
      pointer_buffer_offset);
  }

  public static void glVertexAttribPointerNV(int index,
                                             int fsize,
                                             int type,
                                             int stride,
                                             Buffer pointer) {
    gl().glVertexAttribPointerNV(
      index,
      fsize,
      type,
      stride,
      pointer);
  }

  public static void glVertexAttribPointerNV(int index,
                                             int fsize,
                                             int type,
                                             int stride,
                                             long pointer_buffer_offset) {
    gl().glVertexAttribPointerNV(
      index,
      fsize,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glVertexAttribs1dvNV(int index,
                                          int count,
                                          DoubleBuffer v) {
    gl().glVertexAttribs1dvNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs1dvNV(int index,
                                          int count,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttribs1dvNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs1fvNV(int target,
                                          int n,
                                          FloatBuffer points) {
    gl().glVertexAttribs1fvNV(
      target,
      n,
      points);
  }

  public static void glVertexAttribs1fvNV(int target,
                                          int n,
                                          float[] points,
                                          int points_offset) {
    gl().glVertexAttribs1fvNV(
      target,
      n,
      points,
      points_offset);
  }

  public static void glVertexAttribs1hvNV(int index,
                                          int n,
                                          ShortBuffer v) {
    gl().glVertexAttribs1hvNV(
      index,
      n,
      v);
  }

  public static void glVertexAttribs1hvNV(int index,
                                          int n,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs1hvNV(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexAttribs1svNV(int index,
                                          int count,
                                          ShortBuffer v) {
    gl().glVertexAttribs1svNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs1svNV(int index,
                                          int count,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs1svNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs2dvNV(int index,
                                          int count,
                                          DoubleBuffer v) {
    gl().glVertexAttribs2dvNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs2dvNV(int index,
                                          int count,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttribs2dvNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs2fvNV(int target,
                                          int n,
                                          FloatBuffer points) {
    gl().glVertexAttribs2fvNV(
      target,
      n,
      points);
  }

  public static void glVertexAttribs2fvNV(int target,
                                          int n,
                                          float[] points,
                                          int points_offset) {
    gl().glVertexAttribs2fvNV(
      target,
      n,
      points,
      points_offset);
  }

  public static void glVertexAttribs2hvNV(int index,
                                          int n,
                                          ShortBuffer v) {
    gl().glVertexAttribs2hvNV(
      index,
      n,
      v);
  }

  public static void glVertexAttribs2hvNV(int index,
                                          int n,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs2hvNV(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexAttribs2svNV(int index,
                                          int count,
                                          ShortBuffer v) {
    gl().glVertexAttribs2svNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs2svNV(int index,
                                          int count,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs2svNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs3dvNV(int index,
                                          int count,
                                          DoubleBuffer v) {
    gl().glVertexAttribs3dvNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs3dvNV(int index,
                                          int count,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttribs3dvNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs3fvNV(int target,
                                          int n,
                                          FloatBuffer points) {
    gl().glVertexAttribs3fvNV(
      target,
      n,
      points);
  }

  public static void glVertexAttribs3fvNV(int target,
                                          int n,
                                          float[] points,
                                          int points_offset) {
    gl().glVertexAttribs3fvNV(
      target,
      n,
      points,
      points_offset);
  }

  public static void glVertexAttribs3hvNV(int index,
                                          int n,
                                          ShortBuffer v) {
    gl().glVertexAttribs3hvNV(
      index,
      n,
      v);
  }

  public static void glVertexAttribs3hvNV(int index,
                                          int n,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs3hvNV(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexAttribs3svNV(int index,
                                          int count,
                                          ShortBuffer v) {
    gl().glVertexAttribs3svNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs3svNV(int index,
                                          int count,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs3svNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs4dvNV(int index,
                                          int count,
                                          DoubleBuffer v) {
    gl().glVertexAttribs4dvNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs4dvNV(int index,
                                          int count,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexAttribs4dvNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs4fvNV(int target,
                                          int n,
                                          FloatBuffer points) {
    gl().glVertexAttribs4fvNV(
      target,
      n,
      points);
  }

  public static void glVertexAttribs4fvNV(int target,
                                          int n,
                                          float[] points,
                                          int points_offset) {
    gl().glVertexAttribs4fvNV(
      target,
      n,
      points,
      points_offset);
  }

  public static void glVertexAttribs4hvNV(int index,
                                          int n,
                                          ShortBuffer v) {
    gl().glVertexAttribs4hvNV(
      index,
      n,
      v);
  }

  public static void glVertexAttribs4hvNV(int index,
                                          int n,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs4hvNV(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexAttribs4svNV(int index,
                                          int count,
                                          ShortBuffer v) {
    gl().glVertexAttribs4svNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs4svNV(int index,
                                          int count,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexAttribs4svNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexAttribs4ubvNV(int index,
                                           int count,
                                           ByteBuffer v) {
    gl().glVertexAttribs4ubvNV(
      index,
      count,
      v);
  }

  public static void glVertexAttribs4ubvNV(int index,
                                           int count,
                                           byte[] v,
                                           int v_offset) {
    gl().glVertexAttribs4ubvNV(
      index,
      count,
      v,
      v_offset);
  }

  public static void glVertexBlendARB(int count) {
    gl().glVertexBlendARB(
      count);
  }

  public static void glVertexBlendEnvfATI(int target,
                                          float s) {
    gl().glVertexBlendEnvfATI(
      target,
      s);
  }

  public static void glVertexBlendEnviATI(int target,
                                          int s) {
    gl().glVertexBlendEnviATI(
      target,
      s);
  }

  public static void glVertexPointer(int size,
                                     int type,
                                     int stride,
                                     Buffer ptr) {
    gl().glVertexPointer(
      size,
      type,
      stride,
      ptr);
  }

  public static void glVertexPointer(int size,
                                     int type,
                                     int stride,
                                     long ptr_buffer_offset) {
    gl().glVertexPointer(
      size,
      type,
      stride,
      ptr_buffer_offset);
  }

  public static void glVertexStream1dATI(int target,
                                         double s) {
    gl().glVertexStream1dATI(
      target,
      s);
  }

  public static void glVertexStream1dvATI(int target,
                                          DoubleBuffer v) {
    gl().glVertexStream1dvATI(
      target,
      v);
  }

  public static void glVertexStream1dvATI(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexStream1dvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream1fATI(int target,
                                         float s) {
    gl().glVertexStream1fATI(
      target,
      s);
  }

  public static void glVertexStream1fvATI(int target,
                                          FloatBuffer v) {
    gl().glVertexStream1fvATI(
      target,
      v);
  }

  public static void glVertexStream1fvATI(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexStream1fvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream1iATI(int target,
                                         int s) {
    gl().glVertexStream1iATI(
      target,
      s);
  }

  public static void glVertexStream1ivATI(int target,
                                          IntBuffer v) {
    gl().glVertexStream1ivATI(
      target,
      v);
  }

  public static void glVertexStream1ivATI(int target,
                                          int[] v,
                                          int v_offset) {
    gl().glVertexStream1ivATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream1sATI(int target,
                                         short s) {
    gl().glVertexStream1sATI(
      target,
      s);
  }

  public static void glVertexStream1svATI(int target,
                                          ShortBuffer v) {
    gl().glVertexStream1svATI(
      target,
      v);
  }

  public static void glVertexStream1svATI(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexStream1svATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream2dATI(int target,
                                         double s,
                                         double t) {
    gl().glVertexStream2dATI(
      target,
      s,
      t);
  }

  public static void glVertexStream2dvATI(int target,
                                          DoubleBuffer v) {
    gl().glVertexStream2dvATI(
      target,
      v);
  }

  public static void glVertexStream2dvATI(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexStream2dvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream2fATI(int target,
                                         float s,
                                         float t) {
    gl().glVertexStream2fATI(
      target,
      s,
      t);
  }

  public static void glVertexStream2fvATI(int target,
                                          FloatBuffer v) {
    gl().glVertexStream2fvATI(
      target,
      v);
  }

  public static void glVertexStream2fvATI(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexStream2fvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream2iATI(int target,
                                         int s,
                                         int t) {
    gl().glVertexStream2iATI(
      target,
      s,
      t);
  }

  public static void glVertexStream2ivATI(int target,
                                          IntBuffer v) {
    gl().glVertexStream2ivATI(
      target,
      v);
  }

  public static void glVertexStream2ivATI(int target,
                                          int[] v,
                                          int v_offset) {
    gl().glVertexStream2ivATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream2sATI(int target,
                                         short s,
                                         short t) {
    gl().glVertexStream2sATI(
      target,
      s,
      t);
  }

  public static void glVertexStream2svATI(int target,
                                          ShortBuffer v) {
    gl().glVertexStream2svATI(
      target,
      v);
  }

  public static void glVertexStream2svATI(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexStream2svATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream3dATI(int target,
                                         double s,
                                         double t,
                                         double r) {
    gl().glVertexStream3dATI(
      target,
      s,
      t,
      r);
  }

  public static void glVertexStream3dvATI(int target,
                                          DoubleBuffer v) {
    gl().glVertexStream3dvATI(
      target,
      v);
  }

  public static void glVertexStream3dvATI(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexStream3dvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream3fATI(int target,
                                         float s,
                                         float t,
                                         float r) {
    gl().glVertexStream3fATI(
      target,
      s,
      t,
      r);
  }

  public static void glVertexStream3fvATI(int target,
                                          FloatBuffer v) {
    gl().glVertexStream3fvATI(
      target,
      v);
  }

  public static void glVertexStream3fvATI(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexStream3fvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream3iATI(int target,
                                         int s,
                                         int t,
                                         int r) {
    gl().glVertexStream3iATI(
      target,
      s,
      t,
      r);
  }

  public static void glVertexStream3ivATI(int target,
                                          IntBuffer v) {
    gl().glVertexStream3ivATI(
      target,
      v);
  }

  public static void glVertexStream3ivATI(int target,
                                          int[] v,
                                          int v_offset) {
    gl().glVertexStream3ivATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream3sATI(int target,
                                         short s,
                                         short t,
                                         short r) {
    gl().glVertexStream3sATI(
      target,
      s,
      t,
      r);
  }

  public static void glVertexStream3svATI(int target,
                                          ShortBuffer v) {
    gl().glVertexStream3svATI(
      target,
      v);
  }

  public static void glVertexStream3svATI(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexStream3svATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream4dATI(int target,
                                         double s,
                                         double t,
                                         double r,
                                         double q) {
    gl().glVertexStream4dATI(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexStream4dvATI(int target,
                                          DoubleBuffer v) {
    gl().glVertexStream4dvATI(
      target,
      v);
  }

  public static void glVertexStream4dvATI(int target,
                                          double[] v,
                                          int v_offset) {
    gl().glVertexStream4dvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream4fATI(int target,
                                         float s,
                                         float t,
                                         float r,
                                         float q) {
    gl().glVertexStream4fATI(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexStream4fvATI(int target,
                                          FloatBuffer v) {
    gl().glVertexStream4fvATI(
      target,
      v);
  }

  public static void glVertexStream4fvATI(int target,
                                          float[] v,
                                          int v_offset) {
    gl().glVertexStream4fvATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream4iATI(int target,
                                         int start,
                                         int x,
                                         int y,
                                         int width) {
    gl().glVertexStream4iATI(
      target,
      start,
      x,
      y,
      width);
  }

  public static void glVertexStream4ivATI(int target,
                                          IntBuffer v) {
    gl().glVertexStream4ivATI(
      target,
      v);
  }

  public static void glVertexStream4ivATI(int target,
                                          int[] v,
                                          int v_offset) {
    gl().glVertexStream4ivATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexStream4sATI(int target,
                                         short s,
                                         short t,
                                         short r,
                                         short q) {
    gl().glVertexStream4sATI(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glVertexStream4svATI(int target,
                                          ShortBuffer v) {
    gl().glVertexStream4svATI(
      target,
      v);
  }

  public static void glVertexStream4svATI(int target,
                                          short[] v,
                                          int v_offset) {
    gl().glVertexStream4svATI(
      target,
      v,
      v_offset);
  }

  public static void glVertexWeightPointerEXT(int size,
                                              int type,
                                              int stride,
                                              Buffer pointer) {
    gl().glVertexWeightPointerEXT(
      size,
      type,
      stride,
      pointer);
  }

  public static void glVertexWeightPointerEXT(int size,
                                              int type,
                                              int stride,
                                              long pointer_buffer_offset) {
    gl().glVertexWeightPointerEXT(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glVertexWeightfEXT(float coord) {
    gl().glVertexWeightfEXT(
      coord);
  }

  public static void glVertexWeightfvEXT(FloatBuffer m) {
    gl().glVertexWeightfvEXT(
      m);
  }

  public static void glVertexWeightfvEXT(float[] m,
                                         int m_offset) {
    gl().glVertexWeightfvEXT(
      m,
      m_offset);
  }

  public static void glVertexWeighthNV(short factor) {
    gl().glVertexWeighthNV(
      factor);
  }

  public static void glVertexWeighthvNV(ShortBuffer v) {
    gl().glVertexWeighthvNV(
      v);
  }

  public static void glVertexWeighthvNV(short[] v,
                                        int v_offset) {
    gl().glVertexWeighthvNV(
      v,
      v_offset);
  }

  public static void glViewport(int x,
                                int y,
                                int width,
                                int height) {
    gl().glViewport(
      x,
      y,
      width,
      height);
  }

  public static void glWeightPointerARB(int size,
                                        int type,
                                        int stride,
                                        Buffer pointer) {
    gl().glWeightPointerARB(
      size,
      type,
      stride,
      pointer);
  }

  public static void glWeightPointerARB(int size,
                                        int type,
                                        int stride,
                                        long pointer_buffer_offset) {
    gl().glWeightPointerARB(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glWeightbvARB(int size,
                                   ByteBuffer weights) {
    gl().glWeightbvARB(
      size,
      weights);
  }

  public static void glWeightbvARB(int size,
                                   byte[] weights,
                                   int weights_offset) {
    gl().glWeightbvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightdvARB(int size,
                                   DoubleBuffer weights) {
    gl().glWeightdvARB(
      size,
      weights);
  }

  public static void glWeightdvARB(int size,
                                   double[] weights,
                                   int weights_offset) {
    gl().glWeightdvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightfvARB(int size,
                                   FloatBuffer weights) {
    gl().glWeightfvARB(
      size,
      weights);
  }

  public static void glWeightfvARB(int size,
                                   float[] weights,
                                   int weights_offset) {
    gl().glWeightfvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightivARB(int size,
                                   IntBuffer weights) {
    gl().glWeightivARB(
      size,
      weights);
  }

  public static void glWeightivARB(int size,
                                   int[] weights,
                                   int weights_offset) {
    gl().glWeightivARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightsvARB(int size,
                                   ShortBuffer weights) {
    gl().glWeightsvARB(
      size,
      weights);
  }

  public static void glWeightsvARB(int size,
                                   short[] weights,
                                   int weights_offset) {
    gl().glWeightsvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightubvARB(int size,
                                    ByteBuffer weights) {
    gl().glWeightubvARB(
      size,
      weights);
  }

  public static void glWeightubvARB(int size,
                                    byte[] weights,
                                    int weights_offset) {
    gl().glWeightubvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightuivARB(int n,
                                    IntBuffer ids) {
    gl().glWeightuivARB(
      n,
      ids);
  }

  public static void glWeightuivARB(int n,
                                    int[] ids,
                                    int ids_offset) {
    gl().glWeightuivARB(
      n,
      ids,
      ids_offset);
  }

  public static void glWeightusvARB(int size,
                                    ShortBuffer weights) {
    gl().glWeightusvARB(
      size,
      weights);
  }

  public static void glWeightusvARB(int size,
                                    short[] weights,
                                    int weights_offset) {
    gl().glWeightusvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWindowPos2d(double x,
                                   double y) {
    gl().glWindowPos2d(
      x,
      y);
  }

  public static void glWindowPos2dARB(double x,
                                      double y) {
    gl().glWindowPos2dARB(
      x,
      y);
  }

  public static void glWindowPos2dMESA(double x,
                                       double y) {
    gl().glWindowPos2dMESA(
      x,
      y);
  }

  public static void glWindowPos2dv(DoubleBuffer m) {
    gl().glWindowPos2dv(
      m);
  }

  public static void glWindowPos2dv(double[] m,
                                    int m_offset) {
    gl().glWindowPos2dv(
      m,
      m_offset);
  }

  public static void glWindowPos2dvARB(DoubleBuffer m) {
    gl().glWindowPos2dvARB(
      m);
  }

  public static void glWindowPos2dvARB(double[] m,
                                       int m_offset) {
    gl().glWindowPos2dvARB(
      m,
      m_offset);
  }

  public static void glWindowPos2dvMESA(DoubleBuffer m) {
    gl().glWindowPos2dvMESA(
      m);
  }

  public static void glWindowPos2dvMESA(double[] m,
                                        int m_offset) {
    gl().glWindowPos2dvMESA(
      m,
      m_offset);
  }

  public static void glWindowPos2f(float x,
                                   float y) {
    gl().glWindowPos2f(
      x,
      y);
  }

  public static void glWindowPos2fARB(float x,
                                      float y) {
    gl().glWindowPos2fARB(
      x,
      y);
  }

  public static void glWindowPos2fMESA(float x,
                                       float y) {
    gl().glWindowPos2fMESA(
      x,
      y);
  }

  public static void glWindowPos2fv(FloatBuffer m) {
    gl().glWindowPos2fv(
      m);
  }

  public static void glWindowPos2fv(float[] m,
                                    int m_offset) {
    gl().glWindowPos2fv(
      m,
      m_offset);
  }

  public static void glWindowPos2fvARB(FloatBuffer m) {
    gl().glWindowPos2fvARB(
      m);
  }

  public static void glWindowPos2fvARB(float[] m,
                                       int m_offset) {
    gl().glWindowPos2fvARB(
      m,
      m_offset);
  }

  public static void glWindowPos2fvMESA(FloatBuffer m) {
    gl().glWindowPos2fvMESA(
      m);
  }

  public static void glWindowPos2fvMESA(float[] m,
                                        int m_offset) {
    gl().glWindowPos2fvMESA(
      m,
      m_offset);
  }

  public static void glWindowPos2i(int x,
                                   int y) {
    gl().glWindowPos2i(
      x,
      y);
  }

  public static void glWindowPos2iARB(int x,
                                      int y) {
    gl().glWindowPos2iARB(
      x,
      y);
  }

  public static void glWindowPos2iMESA(int x,
                                       int y) {
    gl().glWindowPos2iMESA(
      x,
      y);
  }

  public static void glWindowPos2iv(IntBuffer v) {
    gl().glWindowPos2iv(
      v);
  }

  public static void glWindowPos2iv(int[] v,
                                    int v_offset) {
    gl().glWindowPos2iv(
      v,
      v_offset);
  }

  public static void glWindowPos2ivARB(IntBuffer v) {
    gl().glWindowPos2ivARB(
      v);
  }

  public static void glWindowPos2ivARB(int[] v,
                                       int v_offset) {
    gl().glWindowPos2ivARB(
      v,
      v_offset);
  }

  public static void glWindowPos2ivMESA(IntBuffer v) {
    gl().glWindowPos2ivMESA(
      v);
  }

  public static void glWindowPos2ivMESA(int[] v,
                                        int v_offset) {
    gl().glWindowPos2ivMESA(
      v,
      v_offset);
  }

  public static void glWindowPos2s(short x,
                                   short y) {
    gl().glWindowPos2s(
      x,
      y);
  }

  public static void glWindowPos2sARB(short x,
                                      short y) {
    gl().glWindowPos2sARB(
      x,
      y);
  }

  public static void glWindowPos2sMESA(short x,
                                       short y) {
    gl().glWindowPos2sMESA(
      x,
      y);
  }

  public static void glWindowPos2sv(ShortBuffer v) {
    gl().glWindowPos2sv(
      v);
  }

  public static void glWindowPos2sv(short[] v,
                                    int v_offset) {
    gl().glWindowPos2sv(
      v,
      v_offset);
  }

  public static void glWindowPos2svARB(ShortBuffer v) {
    gl().glWindowPos2svARB(
      v);
  }

  public static void glWindowPos2svARB(short[] v,
                                       int v_offset) {
    gl().glWindowPos2svARB(
      v,
      v_offset);
  }

  public static void glWindowPos2svMESA(ShortBuffer v) {
    gl().glWindowPos2svMESA(
      v);
  }

  public static void glWindowPos2svMESA(short[] v,
                                        int v_offset) {
    gl().glWindowPos2svMESA(
      v,
      v_offset);
  }

  public static void glWindowPos3d(double red,
                                   double green,
                                   double blue) {
    gl().glWindowPos3d(
      red,
      green,
      blue);
  }

  public static void glWindowPos3dARB(double red,
                                      double green,
                                      double blue) {
    gl().glWindowPos3dARB(
      red,
      green,
      blue);
  }

  public static void glWindowPos3dMESA(double red,
                                       double green,
                                       double blue) {
    gl().glWindowPos3dMESA(
      red,
      green,
      blue);
  }

  public static void glWindowPos3dv(DoubleBuffer m) {
    gl().glWindowPos3dv(
      m);
  }

  public static void glWindowPos3dv(double[] m,
                                    int m_offset) {
    gl().glWindowPos3dv(
      m,
      m_offset);
  }

  public static void glWindowPos3dvARB(DoubleBuffer m) {
    gl().glWindowPos3dvARB(
      m);
  }

  public static void glWindowPos3dvARB(double[] m,
                                       int m_offset) {
    gl().glWindowPos3dvARB(
      m,
      m_offset);
  }

  public static void glWindowPos3dvMESA(DoubleBuffer m) {
    gl().glWindowPos3dvMESA(
      m);
  }

  public static void glWindowPos3dvMESA(double[] m,
                                        int m_offset) {
    gl().glWindowPos3dvMESA(
      m,
      m_offset);
  }

  public static void glWindowPos3f(float red,
                                   float green,
                                   float blue) {
    gl().glWindowPos3f(
      red,
      green,
      blue);
  }

  public static void glWindowPos3fARB(float red,
                                      float green,
                                      float blue) {
    gl().glWindowPos3fARB(
      red,
      green,
      blue);
  }

  public static void glWindowPos3fMESA(float red,
                                       float green,
                                       float blue) {
    gl().glWindowPos3fMESA(
      red,
      green,
      blue);
  }

  public static void glWindowPos3fv(FloatBuffer m) {
    gl().glWindowPos3fv(
      m);
  }

  public static void glWindowPos3fv(float[] m,
                                    int m_offset) {
    gl().glWindowPos3fv(
      m,
      m_offset);
  }

  public static void glWindowPos3fvARB(FloatBuffer m) {
    gl().glWindowPos3fvARB(
      m);
  }

  public static void glWindowPos3fvARB(float[] m,
                                       int m_offset) {
    gl().glWindowPos3fvARB(
      m,
      m_offset);
  }

  public static void glWindowPos3fvMESA(FloatBuffer m) {
    gl().glWindowPos3fvMESA(
      m);
  }

  public static void glWindowPos3fvMESA(float[] m,
                                        int m_offset) {
    gl().glWindowPos3fvMESA(
      m,
      m_offset);
  }

  public static void glWindowPos3i(int red,
                                   int green,
                                   int blue) {
    gl().glWindowPos3i(
      red,
      green,
      blue);
  }

  public static void glWindowPos3iARB(int red,
                                      int green,
                                      int blue) {
    gl().glWindowPos3iARB(
      red,
      green,
      blue);
  }

  public static void glWindowPos3iMESA(int red,
                                       int green,
                                       int blue) {
    gl().glWindowPos3iMESA(
      red,
      green,
      blue);
  }

  public static void glWindowPos3iv(IntBuffer v) {
    gl().glWindowPos3iv(
      v);
  }

  public static void glWindowPos3iv(int[] v,
                                    int v_offset) {
    gl().glWindowPos3iv(
      v,
      v_offset);
  }

  public static void glWindowPos3ivARB(IntBuffer v) {
    gl().glWindowPos3ivARB(
      v);
  }

  public static void glWindowPos3ivARB(int[] v,
                                       int v_offset) {
    gl().glWindowPos3ivARB(
      v,
      v_offset);
  }

  public static void glWindowPos3ivMESA(IntBuffer v) {
    gl().glWindowPos3ivMESA(
      v);
  }

  public static void glWindowPos3ivMESA(int[] v,
                                        int v_offset) {
    gl().glWindowPos3ivMESA(
      v,
      v_offset);
  }

  public static void glWindowPos3s(short red,
                                   short green,
                                   short blue) {
    gl().glWindowPos3s(
      red,
      green,
      blue);
  }

  public static void glWindowPos3sARB(short red,
                                      short green,
                                      short blue) {
    gl().glWindowPos3sARB(
      red,
      green,
      blue);
  }

  public static void glWindowPos3sMESA(short red,
                                       short green,
                                       short blue) {
    gl().glWindowPos3sMESA(
      red,
      green,
      blue);
  }

  public static void glWindowPos3sv(ShortBuffer v) {
    gl().glWindowPos3sv(
      v);
  }

  public static void glWindowPos3sv(short[] v,
                                    int v_offset) {
    gl().glWindowPos3sv(
      v,
      v_offset);
  }

  public static void glWindowPos3svARB(ShortBuffer v) {
    gl().glWindowPos3svARB(
      v);
  }

  public static void glWindowPos3svARB(short[] v,
                                       int v_offset) {
    gl().glWindowPos3svARB(
      v,
      v_offset);
  }

  public static void glWindowPos3svMESA(ShortBuffer v) {
    gl().glWindowPos3svMESA(
      v);
  }

  public static void glWindowPos3svMESA(short[] v,
                                        int v_offset) {
    gl().glWindowPos3svMESA(
      v,
      v_offset);
  }

  public static void glWindowPos4dMESA(double x,
                                       double y,
                                       double z,
                                       double w) {
    gl().glWindowPos4dMESA(
      x,
      y,
      z,
      w);
  }

  public static void glWindowPos4dvMESA(DoubleBuffer m) {
    gl().glWindowPos4dvMESA(
      m);
  }

  public static void glWindowPos4dvMESA(double[] m,
                                        int m_offset) {
    gl().glWindowPos4dvMESA(
      m,
      m_offset);
  }

  public static void glWindowPos4fMESA(float red,
                                       float green,
                                       float blue,
                                       float alpha) {
    gl().glWindowPos4fMESA(
      red,
      green,
      blue,
      alpha);
  }

  public static void glWindowPos4fvMESA(FloatBuffer m) {
    gl().glWindowPos4fvMESA(
      m);
  }

  public static void glWindowPos4fvMESA(float[] m,
                                        int m_offset) {
    gl().glWindowPos4fvMESA(
      m,
      m_offset);
  }

  public static void glWindowPos4iMESA(int location,
                                       int v0,
                                       int v1,
                                       int v2) {
    gl().glWindowPos4iMESA(
      location,
      v0,
      v1,
      v2);
  }

  public static void glWindowPos4ivMESA(IntBuffer v) {
    gl().glWindowPos4ivMESA(
      v);
  }

  public static void glWindowPos4ivMESA(int[] v,
                                        int v_offset) {
    gl().glWindowPos4ivMESA(
      v,
      v_offset);
  }

  public static void glWindowPos4sMESA(short x,
                                       short y,
                                       short z,
                                       short w) {
    gl().glWindowPos4sMESA(
      x,
      y,
      z,
      w);
  }

  public static void glWindowPos4svMESA(ShortBuffer v) {
    gl().glWindowPos4svMESA(
      v);
  }

  public static void glWindowPos4svMESA(short[] v,
                                        int v_offset) {
    gl().glWindowPos4svMESA(
      v,
      v_offset);
  }

  public static void glWriteMaskEXT(int stage,
                                    int portion,
                                    int variable,
                                    int input,
                                    int mapping,
                                    int componentUsage) {
    gl().glWriteMaskEXT(
      stage,
      portion,
      variable,
      input,
      mapping,
      componentUsage);
  }

  public static boolean isExtensionAvailable(String extensionName) {
    return gl().isExtensionAvailable(extensionName);
  }

  private static GL gl() {
    return GLContext.getCurrent().getGL();
  }

 ///////////////////////////////////////////////////////////////////////////
  // private

  private Gl() {
  }
}
