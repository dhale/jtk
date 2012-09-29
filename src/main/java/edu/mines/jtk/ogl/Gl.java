/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import java.nio.*;
import javax.media.opengl.*;
import com.jogamp.common.nio.PointerBuffer;

/**
 * OpenGL standard constants and functions.
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.08.17
 */
public class Gl {


  // Generated from GL.html

  public static final int GL_DEPTH_BUFFER_BIT
    = GL2.GL_DEPTH_BUFFER_BIT;

  public static final int GL_STENCIL_BUFFER_BIT
    = GL2.GL_STENCIL_BUFFER_BIT;

  public static final int GL_COLOR_BUFFER_BIT
    = GL2.GL_COLOR_BUFFER_BIT;

  public static final int GL_FALSE
    = GL2.GL_FALSE;

  public static final int GL_TRUE
    = GL2.GL_TRUE;

  public static final int GL_POINTS
    = GL2.GL_POINTS;

  public static final int GL_LINES
    = GL2.GL_LINES;

  public static final int GL_LINE_LOOP
    = GL2.GL_LINE_LOOP;

  public static final int GL_LINE_STRIP
    = GL2.GL_LINE_STRIP;

  public static final int GL_TRIANGLES
    = GL2.GL_TRIANGLES;

  public static final int GL_TRIANGLE_STRIP
    = GL2.GL_TRIANGLE_STRIP;

  public static final int GL_TRIANGLE_FAN
    = GL2.GL_TRIANGLE_FAN;

  public static final int GL_ZERO
    = GL2.GL_ZERO;

  public static final int GL_ONE
    = GL2.GL_ONE;

  public static final int GL_SRC_COLOR
    = GL2.GL_SRC_COLOR;

  public static final int GL_ONE_MINUS_SRC_COLOR
    = GL2.GL_ONE_MINUS_SRC_COLOR;

  public static final int GL_SRC_ALPHA
    = GL2.GL_SRC_ALPHA;

  public static final int GL_ONE_MINUS_SRC_ALPHA
    = GL2.GL_ONE_MINUS_SRC_ALPHA;

  public static final int GL_DST_ALPHA
    = GL2.GL_DST_ALPHA;

  public static final int GL_ONE_MINUS_DST_ALPHA
    = GL2.GL_ONE_MINUS_DST_ALPHA;

  public static final int GL_DST_COLOR
    = GL2.GL_DST_COLOR;

  public static final int GL_ONE_MINUS_DST_COLOR
    = GL2.GL_ONE_MINUS_DST_COLOR;

  public static final int GL_SRC_ALPHA_SATURATE
    = GL2.GL_SRC_ALPHA_SATURATE;

  public static final int GL_FUNC_ADD
    = GL2.GL_FUNC_ADD;

  public static final int GL_BLEND_EQUATION
    = GL2.GL_BLEND_EQUATION;

  public static final int GL_BLEND_EQUATION_RGB
    = GL2.GL_BLEND_EQUATION_RGB;

  public static final int GL_BLEND_EQUATION_ALPHA
    = GL2.GL_BLEND_EQUATION_ALPHA;

  public static final int GL_FUNC_SUBTRACT
    = GL2.GL_FUNC_SUBTRACT;

  public static final int GL_FUNC_REVERSE_SUBTRACT
    = GL2.GL_FUNC_REVERSE_SUBTRACT;

  public static final int GL_BLEND_DST_RGB
    = GL2.GL_BLEND_DST_RGB;

  public static final int GL_BLEND_SRC_RGB
    = GL2.GL_BLEND_SRC_RGB;

  public static final int GL_BLEND_DST_ALPHA
    = GL2.GL_BLEND_DST_ALPHA;

  public static final int GL_BLEND_SRC_ALPHA
    = GL2.GL_BLEND_SRC_ALPHA;

  public static final int GL_ARRAY_BUFFER
    = GL2.GL_ARRAY_BUFFER;

  public static final int GL_ELEMENT_ARRAY_BUFFER
    = GL2.GL_ELEMENT_ARRAY_BUFFER;

  public static final int GL_ARRAY_BUFFER_BINDING
    = GL2.GL_ARRAY_BUFFER_BINDING;

  public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING
    = GL2.GL_ELEMENT_ARRAY_BUFFER_BINDING;

  public static final int GL_STATIC_DRAW
    = GL2.GL_STATIC_DRAW;

  public static final int GL_DYNAMIC_DRAW
    = GL2.GL_DYNAMIC_DRAW;

  public static final int GL_BUFFER_SIZE
    = GL2.GL_BUFFER_SIZE;

  public static final int GL_BUFFER_USAGE
    = GL2.GL_BUFFER_USAGE;

  public static final int GL_FRONT
    = GL2.GL_FRONT;

  public static final int GL_BACK
    = GL2.GL_BACK;

  public static final int GL_FRONT_AND_BACK
    = GL2.GL_FRONT_AND_BACK;

  public static final int GL_TEXTURE_2D
    = GL2.GL_TEXTURE_2D;

  public static final int GL_CULL_FACE
    = GL2.GL_CULL_FACE;

  public static final int GL_BLEND
    = GL2.GL_BLEND;

  public static final int GL_DITHER
    = GL2.GL_DITHER;

  public static final int GL_STENCIL_TEST
    = GL2.GL_STENCIL_TEST;

  public static final int GL_DEPTH_TEST
    = GL2.GL_DEPTH_TEST;

  public static final int GL_SCISSOR_TEST
    = GL2.GL_SCISSOR_TEST;

  public static final int GL_POLYGON_OFFSET_FILL
    = GL2.GL_POLYGON_OFFSET_FILL;

  public static final int GL_SAMPLE_ALPHA_TO_COVERAGE
    = GL2.GL_SAMPLE_ALPHA_TO_COVERAGE;

  public static final int GL_SAMPLE_COVERAGE
    = GL2.GL_SAMPLE_COVERAGE;

  public static final int GL_NO_ERROR
    = GL2.GL_NO_ERROR;

  public static final int GL_INVALID_ENUM
    = GL2.GL_INVALID_ENUM;

  public static final int GL_INVALID_VALUE
    = GL2.GL_INVALID_VALUE;

  public static final int GL_INVALID_OPERATION
    = GL2.GL_INVALID_OPERATION;

  public static final int GL_OUT_OF_MEMORY
    = GL2.GL_OUT_OF_MEMORY;

  public static final int GL_CW
    = GL2.GL_CW;

  public static final int GL_CCW
    = GL2.GL_CCW;

  public static final int GL_LINE_WIDTH
    = GL2.GL_LINE_WIDTH;

  public static final int GL_ALIASED_POINT_SIZE_RANGE
    = GL2.GL_ALIASED_POINT_SIZE_RANGE;

  public static final int GL_ALIASED_LINE_WIDTH_RANGE
    = GL2.GL_ALIASED_LINE_WIDTH_RANGE;

  public static final int GL_CULL_FACE_MODE
    = GL2.GL_CULL_FACE_MODE;

  public static final int GL_FRONT_FACE
    = GL2.GL_FRONT_FACE;

  public static final int GL_DEPTH_RANGE
    = GL2.GL_DEPTH_RANGE;

  public static final int GL_DEPTH_WRITEMASK
    = GL2.GL_DEPTH_WRITEMASK;

  public static final int GL_DEPTH_CLEAR_VALUE
    = GL2.GL_DEPTH_CLEAR_VALUE;

  public static final int GL_DEPTH_FUNC
    = GL2.GL_DEPTH_FUNC;

  public static final int GL_STENCIL_CLEAR_VALUE
    = GL2.GL_STENCIL_CLEAR_VALUE;

  public static final int GL_STENCIL_FUNC
    = GL2.GL_STENCIL_FUNC;

  public static final int GL_STENCIL_FAIL
    = GL2.GL_STENCIL_FAIL;

  public static final int GL_STENCIL_PASS_DEPTH_FAIL
    = GL2.GL_STENCIL_PASS_DEPTH_FAIL;

  public static final int GL_STENCIL_PASS_DEPTH_PASS
    = GL2.GL_STENCIL_PASS_DEPTH_PASS;

  public static final int GL_STENCIL_REF
    = GL2.GL_STENCIL_REF;

  public static final int GL_STENCIL_VALUE_MASK
    = GL2.GL_STENCIL_VALUE_MASK;

  public static final int GL_STENCIL_WRITEMASK
    = GL2.GL_STENCIL_WRITEMASK;

  public static final int GL_VIEWPORT
    = GL2.GL_VIEWPORT;

  public static final int GL_SCISSOR_BOX
    = GL2.GL_SCISSOR_BOX;

  public static final int GL_COLOR_CLEAR_VALUE
    = GL2.GL_COLOR_CLEAR_VALUE;

  public static final int GL_COLOR_WRITEMASK
    = GL2.GL_COLOR_WRITEMASK;

  public static final int GL_UNPACK_ALIGNMENT
    = GL2.GL_UNPACK_ALIGNMENT;

  public static final int GL_PACK_ALIGNMENT
    = GL2.GL_PACK_ALIGNMENT;

  public static final int GL_MAX_TEXTURE_SIZE
    = GL2.GL_MAX_TEXTURE_SIZE;

  public static final int GL_MAX_VIEWPORT_DIMS
    = GL2.GL_MAX_VIEWPORT_DIMS;

  public static final int GL_SUBPIXEL_BITS
    = GL2.GL_SUBPIXEL_BITS;

  public static final int GL_RED_BITS
    = GL2.GL_RED_BITS;

  public static final int GL_GREEN_BITS
    = GL2.GL_GREEN_BITS;

  public static final int GL_BLUE_BITS
    = GL2.GL_BLUE_BITS;

  public static final int GL_ALPHA_BITS
    = GL2.GL_ALPHA_BITS;

  public static final int GL_DEPTH_BITS
    = GL2.GL_DEPTH_BITS;

  public static final int GL_STENCIL_BITS
    = GL2.GL_STENCIL_BITS;

  public static final int GL_POLYGON_OFFSET_UNITS
    = GL2.GL_POLYGON_OFFSET_UNITS;

  public static final int GL_POLYGON_OFFSET_FACTOR
    = GL2.GL_POLYGON_OFFSET_FACTOR;

  public static final int GL_TEXTURE_BINDING_2D
    = GL2.GL_TEXTURE_BINDING_2D;

  public static final int GL_SAMPLE_BUFFERS
    = GL2.GL_SAMPLE_BUFFERS;

  public static final int GL_SAMPLES
    = GL2.GL_SAMPLES;

  public static final int GL_SAMPLE_COVERAGE_VALUE
    = GL2.GL_SAMPLE_COVERAGE_VALUE;

  public static final int GL_SAMPLE_COVERAGE_INVERT
    = GL2.GL_SAMPLE_COVERAGE_INVERT;

  public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS
    = GL2.GL_NUM_COMPRESSED_TEXTURE_FORMATS;

  public static final int GL_COMPRESSED_TEXTURE_FORMATS
    = GL2.GL_COMPRESSED_TEXTURE_FORMATS;

  public static final int GL_DONT_CARE
    = GL2.GL_DONT_CARE;

  public static final int GL_FASTEST
    = GL2.GL_FASTEST;

  public static final int GL_NICEST
    = GL2.GL_NICEST;

  public static final int GL_GENERATE_MIPMAP_HINT
    = GL2.GL_GENERATE_MIPMAP_HINT;

  public static final int GL_BYTE
    = GL2.GL_BYTE;

  public static final int GL_UNSIGNED_BYTE
    = GL2.GL_UNSIGNED_BYTE;

  public static final int GL_SHORT
    = GL2.GL_SHORT;

  public static final int GL_UNSIGNED_SHORT
    = GL2.GL_UNSIGNED_SHORT;

  public static final int GL_UNSIGNED_INT
    = GL2.GL_UNSIGNED_INT;

  public static final int GL_FLOAT
    = GL2.GL_FLOAT;

  public static final int GL_FIXED
    = GL2.GL_FIXED;

  public static final int GL_ALPHA
    = GL2.GL_ALPHA;

  public static final int GL_RGB
    = GL2.GL_RGB;

  public static final int GL_RGBA
    = GL2.GL_RGBA;

  public static final int GL_LUMINANCE
    = GL2.GL_LUMINANCE;

  public static final int GL_LUMINANCE_ALPHA
    = GL2.GL_LUMINANCE_ALPHA;

  public static final int GL_UNSIGNED_SHORT_4_4_4_4
    = GL2.GL_UNSIGNED_SHORT_4_4_4_4;

  public static final int GL_UNSIGNED_SHORT_5_5_5_1
    = GL2.GL_UNSIGNED_SHORT_5_5_5_1;

  public static final int GL_UNSIGNED_SHORT_5_6_5
    = GL2.GL_UNSIGNED_SHORT_5_6_5;

  public static final int GL_NEVER
    = GL2.GL_NEVER;

  public static final int GL_LESS
    = GL2.GL_LESS;

  public static final int GL_EQUAL
    = GL2.GL_EQUAL;

  public static final int GL_LEQUAL
    = GL2.GL_LEQUAL;

  public static final int GL_GREATER
    = GL2.GL_GREATER;

  public static final int GL_NOTEQUAL
    = GL2.GL_NOTEQUAL;

  public static final int GL_GEQUAL
    = GL2.GL_GEQUAL;

  public static final int GL_ALWAYS
    = GL2.GL_ALWAYS;

  public static final int GL_KEEP
    = GL2.GL_KEEP;

  public static final int GL_REPLACE
    = GL2.GL_REPLACE;

  public static final int GL_INCR
    = GL2.GL_INCR;

  public static final int GL_DECR
    = GL2.GL_DECR;

  public static final int GL_INVERT
    = GL2.GL_INVERT;

  public static final int GL_INCR_WRAP
    = GL2.GL_INCR_WRAP;

  public static final int GL_DECR_WRAP
    = GL2.GL_DECR_WRAP;

  public static final int GL_VENDOR
    = GL2.GL_VENDOR;

  public static final int GL_RENDERER
    = GL2.GL_RENDERER;

  public static final int GL_VERSION
    = GL2.GL_VERSION;

  public static final int GL_EXTENSIONS
    = GL2.GL_EXTENSIONS;

  public static final int GL_NEAREST
    = GL2.GL_NEAREST;

  public static final int GL_LINEAR
    = GL2.GL_LINEAR;

  public static final int GL_NEAREST_MIPMAP_NEAREST
    = GL2.GL_NEAREST_MIPMAP_NEAREST;

  public static final int GL_LINEAR_MIPMAP_NEAREST
    = GL2.GL_LINEAR_MIPMAP_NEAREST;

  public static final int GL_NEAREST_MIPMAP_LINEAR
    = GL2.GL_NEAREST_MIPMAP_LINEAR;

  public static final int GL_LINEAR_MIPMAP_LINEAR
    = GL2.GL_LINEAR_MIPMAP_LINEAR;

  public static final int GL_TEXTURE_MAG_FILTER
    = GL2.GL_TEXTURE_MAG_FILTER;

  public static final int GL_TEXTURE_MIN_FILTER
    = GL2.GL_TEXTURE_MIN_FILTER;

  public static final int GL_TEXTURE_WRAP_S
    = GL2.GL_TEXTURE_WRAP_S;

  public static final int GL_TEXTURE_WRAP_T
    = GL2.GL_TEXTURE_WRAP_T;

  public static final int GL_TEXTURE
    = GL2.GL_TEXTURE;

  public static final int GL_TEXTURE_CUBE_MAP
    = GL2.GL_TEXTURE_CUBE_MAP;

  public static final int GL_TEXTURE_BINDING_CUBE_MAP
    = GL2.GL_TEXTURE_BINDING_CUBE_MAP;

  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X
    = GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X
    = GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;

  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y
    = GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;

  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y
    = GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;

  public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z
    = GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;

  public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    = GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;

  public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE
    = GL2.GL_MAX_CUBE_MAP_TEXTURE_SIZE;

  public static final int GL_TEXTURE0
    = GL2.GL_TEXTURE0;

  public static final int GL_TEXTURE1
    = GL2.GL_TEXTURE1;

  public static final int GL_TEXTURE2
    = GL2.GL_TEXTURE2;

  public static final int GL_TEXTURE3
    = GL2.GL_TEXTURE3;

  public static final int GL_TEXTURE4
    = GL2.GL_TEXTURE4;

  public static final int GL_TEXTURE5
    = GL2.GL_TEXTURE5;

  public static final int GL_TEXTURE6
    = GL2.GL_TEXTURE6;

  public static final int GL_TEXTURE7
    = GL2.GL_TEXTURE7;

  public static final int GL_TEXTURE8
    = GL2.GL_TEXTURE8;

  public static final int GL_TEXTURE9
    = GL2.GL_TEXTURE9;

  public static final int GL_TEXTURE10
    = GL2.GL_TEXTURE10;

  public static final int GL_TEXTURE11
    = GL2.GL_TEXTURE11;

  public static final int GL_TEXTURE12
    = GL2.GL_TEXTURE12;

  public static final int GL_TEXTURE13
    = GL2.GL_TEXTURE13;

  public static final int GL_TEXTURE14
    = GL2.GL_TEXTURE14;

  public static final int GL_TEXTURE15
    = GL2.GL_TEXTURE15;

  public static final int GL_TEXTURE16
    = GL2.GL_TEXTURE16;

  public static final int GL_TEXTURE17
    = GL2.GL_TEXTURE17;

  public static final int GL_TEXTURE18
    = GL2.GL_TEXTURE18;

  public static final int GL_TEXTURE19
    = GL2.GL_TEXTURE19;

  public static final int GL_TEXTURE20
    = GL2.GL_TEXTURE20;

  public static final int GL_TEXTURE21
    = GL2.GL_TEXTURE21;

  public static final int GL_TEXTURE22
    = GL2.GL_TEXTURE22;

  public static final int GL_TEXTURE23
    = GL2.GL_TEXTURE23;

  public static final int GL_TEXTURE24
    = GL2.GL_TEXTURE24;

  public static final int GL_TEXTURE25
    = GL2.GL_TEXTURE25;

  public static final int GL_TEXTURE26
    = GL2.GL_TEXTURE26;

  public static final int GL_TEXTURE27
    = GL2.GL_TEXTURE27;

  public static final int GL_TEXTURE28
    = GL2.GL_TEXTURE28;

  public static final int GL_TEXTURE29
    = GL2.GL_TEXTURE29;

  public static final int GL_TEXTURE30
    = GL2.GL_TEXTURE30;

  public static final int GL_TEXTURE31
    = GL2.GL_TEXTURE31;

  public static final int GL_ACTIVE_TEXTURE
    = GL2.GL_ACTIVE_TEXTURE;

  public static final int GL_REPEAT
    = GL2.GL_REPEAT;

  public static final int GL_CLAMP_TO_EDGE
    = GL2.GL_CLAMP_TO_EDGE;

  public static final int GL_MIRRORED_REPEAT
    = GL2.GL_MIRRORED_REPEAT;

  public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE
    = GL2.GL_IMPLEMENTATION_COLOR_READ_TYPE;

  public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT
    = GL2.GL_IMPLEMENTATION_COLOR_READ_FORMAT;

  public static final int GL_FRAMEBUFFER
    = GL2.GL_FRAMEBUFFER;

  public static final int GL_RENDERBUFFER
    = GL2.GL_RENDERBUFFER;

  public static final int GL_RGBA4
    = GL2.GL_RGBA4;

  public static final int GL_RGB5_A1
    = GL2.GL_RGB5_A1;

  public static final int GL_RGB565
    = GL2.GL_RGB565;

  public static final int GL_DEPTH_COMPONENT16
    = GL2.GL_DEPTH_COMPONENT16;

  public static final int GL_STENCIL_INDEX8
    = GL2.GL_STENCIL_INDEX8;

  public static final int GL_RENDERBUFFER_WIDTH
    = GL2.GL_RENDERBUFFER_WIDTH;

  public static final int GL_RENDERBUFFER_HEIGHT
    = GL2.GL_RENDERBUFFER_HEIGHT;

  public static final int GL_RENDERBUFFER_INTERNAL_FORMAT
    = GL2.GL_RENDERBUFFER_INTERNAL_FORMAT;

  public static final int GL_RENDERBUFFER_RED_SIZE
    = GL2.GL_RENDERBUFFER_RED_SIZE;

  public static final int GL_RENDERBUFFER_GREEN_SIZE
    = GL2.GL_RENDERBUFFER_GREEN_SIZE;

  public static final int GL_RENDERBUFFER_BLUE_SIZE
    = GL2.GL_RENDERBUFFER_BLUE_SIZE;

  public static final int GL_RENDERBUFFER_ALPHA_SIZE
    = GL2.GL_RENDERBUFFER_ALPHA_SIZE;

  public static final int GL_RENDERBUFFER_DEPTH_SIZE
    = GL2.GL_RENDERBUFFER_DEPTH_SIZE;

  public static final int GL_RENDERBUFFER_STENCIL_SIZE
    = GL2.GL_RENDERBUFFER_STENCIL_SIZE;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE;

  public static final int GL_COLOR_ATTACHMENT0
    = GL2.GL_COLOR_ATTACHMENT0;

  public static final int GL_DEPTH_ATTACHMENT
    = GL2.GL_DEPTH_ATTACHMENT;

  public static final int GL_STENCIL_ATTACHMENT
    = GL2.GL_STENCIL_ATTACHMENT;

  public static final int GL_NONE
    = GL2.GL_NONE;

  public static final int GL_FRAMEBUFFER_COMPLETE
    = GL2.GL_FRAMEBUFFER_COMPLETE;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_FORMATS;

  public static final int GL_FRAMEBUFFER_UNSUPPORTED
    = GL2.GL_FRAMEBUFFER_UNSUPPORTED;

  public static final int GL_FRAMEBUFFER_BINDING
    = GL2.GL_FRAMEBUFFER_BINDING;

  public static final int GL_RENDERBUFFER_BINDING
    = GL2.GL_RENDERBUFFER_BINDING;

  public static final int GL_MAX_RENDERBUFFER_SIZE
    = GL2.GL_MAX_RENDERBUFFER_SIZE;

  public static final int GL_INVALID_FRAMEBUFFER_OPERATION
    = GL2.GL_INVALID_FRAMEBUFFER_OPERATION;

  public static final int GL_DEPTH_COMPONENT24
    = GL2.GL_DEPTH_COMPONENT24;

  public static final int GL_DEPTH_COMPONENT32
    = GL2.GL_DEPTH_COMPONENT32;

  public static final int GL_WRITE_ONLY
    = GL2.GL_WRITE_ONLY;

  public static final int GL_BUFFER_ACCESS
    = GL2.GL_BUFFER_ACCESS;

  public static final int GL_BUFFER_MAPPED
    = GL2.GL_BUFFER_MAPPED;

  public static final int GL_BUFFER_MAP_POINTER
    = GL2.GL_BUFFER_MAP_POINTER;

  public static final int GL_DEPTH_STENCIL
    = GL2.GL_DEPTH_STENCIL;

  public static final int GL_UNSIGNED_INT_24_8
    = GL2.GL_UNSIGNED_INT_24_8;

  public static final int GL_DEPTH24_STENCIL8
    = GL2.GL_DEPTH24_STENCIL8;

  public static final int GL_RGB8
    = GL2.GL_RGB8;

  public static final int GL_RGBA8
    = GL2.GL_RGBA8;

  public static final int GL_STENCIL_INDEX1
    = GL2.GL_STENCIL_INDEX1;

  public static final int GL_STENCIL_INDEX4
    = GL2.GL_STENCIL_INDEX4;

  public static final int GL_BGRA
    = GL2.GL_BGRA;

  public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV_EXT
    = GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV_EXT;

  public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV_EXT
    = GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV_EXT;

  public static final int GL_GUILTY_CONTEXT_RESET
    = GL2.GL_GUILTY_CONTEXT_RESET;

  public static final int GL_INNOCENT_CONTEXT_RESET
    = GL2.GL_INNOCENT_CONTEXT_RESET;

  public static final int GL_UNKNOWN_CONTEXT_RESET
    = GL2.GL_UNKNOWN_CONTEXT_RESET;

  public static final int GL_RESET_NOTIFICATION_STRATEGY
    = GL2.GL_RESET_NOTIFICATION_STRATEGY;

  public static final int GL_LOSE_CONTEXT_ON_RESET
    = GL2.GL_LOSE_CONTEXT_ON_RESET;

  public static final int GL_NO_RESET_NOTIFICATION
    = GL2.GL_NO_RESET_NOTIFICATION;

  public static final int GL_SRGB
    = GL2.GL_SRGB;

  public static final int GL_SRGB_ALPHA
    = GL2.GL_SRGB_ALPHA;

  public static final int GL_SRGB8_ALPHA8
    = GL2.GL_SRGB8_ALPHA8;

  public static final int GL_COMPRESSED_RGB_S3TC_DXT1_EXT
    = GL2.GL_COMPRESSED_RGB_S3TC_DXT1_EXT;

  public static final int GL_COMPRESSED_RGBA_S3TC_DXT1_EXT
    = GL2.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;

  public static final int GL_TEXTURE_MAX_ANISOTROPY_EXT
    = GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT;

  public static final int GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT
    = GL2.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;

  public static final int GL_TEXTURE_IMMUTABLE_FORMAT
    = GL2.GL_TEXTURE_IMMUTABLE_FORMAT;

  public static final int GL_RGBA32F
    = GL2.GL_RGBA32F;

  public static final int GL_RGB32F
    = GL2.GL_RGB32F;

  public static final int GL_ALPHA32F_ARB
    = GL2.GL_ALPHA32F_ARB;

  public static final int GL_LUMINANCE32F_ARB
    = GL2.GL_LUMINANCE32F_ARB;

  public static final int GL_LUMINANCE_ALPHA32F_ARB
    = GL2.GL_LUMINANCE_ALPHA32F_ARB;

  public static final int GL_ALPHA16F_ARB
    = GL2.GL_ALPHA16F_ARB;

  public static final int GL_LUMINANCE16F_ARB
    = GL2.GL_LUMINANCE16F_ARB;

  public static final int GL_LUMINANCE_ALPHA16F_ARB
    = GL2.GL_LUMINANCE_ALPHA16F_ARB;

  public static final int GL_RGB10_A2
    = GL2.GL_RGB10_A2;

  public static final int GL_RGB10
    = GL2.GL_RGB10;

  public static final int GL_COMPRESSED_RGBA_S3TC_DXT3_EXT
    = GL2.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;

  public static final int GL_COMPRESSED_RGBA_S3TC_DXT5_EXT
    = GL2.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;

  public static final int GL_TEXTURE_2D_ARRAY
    = GL2.GL_TEXTURE_2D_ARRAY;

  public static final int GL_SAMPLER_2D_ARRAY
    = GL2.GL_SAMPLER_2D_ARRAY;

  public static final int GL_TEXTURE_BINDING_2D_ARRAY
    = GL2.GL_TEXTURE_BINDING_2D_ARRAY;

  public static final int GL_MAX_ARRAY_TEXTURE_LAYERS
    = GL2.GL_MAX_ARRAY_TEXTURE_LAYERS;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER;

  public static final int GL_R11F_G11F_B10F
    = GL2.GL_R11F_G11F_B10F;

  public static final int GL_UNSIGNED_INT_10F_11F_11F_REV
    = GL2.GL_UNSIGNED_INT_10F_11F_11F_REV;

  public static final int GL_RGBA_SIGNED_COMPONENTS
    = GL2.GL_RGBA_SIGNED_COMPONENTS;

  public static final int GL_AND
    = GL2.GL_AND;

  public static final int GL_AND_INVERTED
    = GL2.GL_AND_INVERTED;

  public static final int GL_AND_REVERSE
    = GL2.GL_AND_REVERSE;

  public static final int GL_BLEND_DST
    = GL2.GL_BLEND_DST;

  public static final int GL_BLEND_SRC
    = GL2.GL_BLEND_SRC;

  public static final int GL_CLEAR
    = GL2.GL_CLEAR;

  public static final int GL_COLOR_LOGIC_OP
    = GL2.GL_COLOR_LOGIC_OP;

  public static final int GL_COPY
    = GL2.GL_COPY;

  public static final int GL_COPY_INVERTED
    = GL2.GL_COPY_INVERTED;

  public static final int GL_EQUIV
    = GL2.GL_EQUIV;

  public static final int GL_LINE_SMOOTH
    = GL2.GL_LINE_SMOOTH;

  public static final int GL_LINE_SMOOTH_HINT
    = GL2.GL_LINE_SMOOTH_HINT;

  public static final int GL_LOGIC_OP_MODE
    = GL2.GL_LOGIC_OP_MODE;

  public static final int GL_MULTISAMPLE
    = GL2.GL_MULTISAMPLE;

  public static final int GL_NAND
    = GL2.GL_NAND;

  public static final int GL_NOOP
    = GL2.GL_NOOP;

  public static final int GL_NOR
    = GL2.GL_NOR;

  public static final int GL_OR
    = GL2.GL_OR;

  public static final int GL_OR_INVERTED
    = GL2.GL_OR_INVERTED;

  public static final int GL_OR_REVERSE
    = GL2.GL_OR_REVERSE;

  public static final int GL_POINT_FADE_THRESHOLD_SIZE
    = GL2.GL_POINT_FADE_THRESHOLD_SIZE;

  public static final int GL_POINT_SIZE
    = GL2.GL_POINT_SIZE;

  public static final int GL_SAMPLE_ALPHA_TO_ONE
    = GL2.GL_SAMPLE_ALPHA_TO_ONE;

  public static final int GL_SET
    = GL2.GL_SET;

  public static final int GL_SMOOTH_LINE_WIDTH_RANGE
    = GL2.GL_SMOOTH_LINE_WIDTH_RANGE;

  public static final int GL_SMOOTH_POINT_SIZE_RANGE
    = GL2.GL_SMOOTH_POINT_SIZE_RANGE;

  public static final int GL_XOR
    = GL2.GL_XOR;

  public static final int GL_HALF_FLOAT
    = GL2.GL_HALF_FLOAT;

  public static void glActiveTexture(
    int texture) {
    gl().glActiveTexture(
      texture);
  }

  public static void glBindBuffer(
    int target,
    int buffer) {
    gl().glBindBuffer(
      target,
      buffer);
  }

  public static void glBindFramebuffer(
    int target,
    int framebuffer) {
    gl().glBindFramebuffer(
      target,
      framebuffer);
  }

  public static void glBindRenderbuffer(
    int target,
    int renderbuffer) {
    gl().glBindRenderbuffer(
      target,
      renderbuffer);
  }

  public static void glBindTexture(
    int target,
    int texture) {
    gl().glBindTexture(
      target,
      texture);
  }

  public static void glBlendEquation(
    int mode) {
    gl().glBlendEquation(
      mode);
  }

  public static void glBlendEquationSeparate(
    int modeRGB,
    int modeAlpha) {
    gl().glBlendEquationSeparate(
      modeRGB,
      modeAlpha);
  }

  public static void glBlendFunc(
    int sfactor,
    int dfactor) {
    gl().glBlendFunc(
      sfactor,
      dfactor);
  }

  public static void glBlendFuncSeparate(
    int srcRGB,
    int dstRGB,
    int srcAlpha,
    int dstAlpha) {
    gl().glBlendFuncSeparate(
      srcRGB,
      dstRGB,
      srcAlpha,
      dstAlpha);
  }

  public static void glBufferData(
    int target,
    long size,
    Buffer data,
    int usage) {
    gl().glBufferData(
      target,
      size,
      data,
      usage);
  }

  public static void glBufferSubData(
    int target,
    long offset,
    long size,
    Buffer data) {
    gl().glBufferSubData(
      target,
      offset,
      size,
      data);
  }

  public static int glCheckFramebufferStatus(
    int target) {
    return gl().glCheckFramebufferStatus(
      target);
  }

  public static void glClear(
    int mask) {
    gl().glClear(
      mask);
  }

  public static void glClearColor(
    float red,
    float green,
    float blue,
    float alpha) {
    gl().glClearColor(
      red,
      green,
      blue,
      alpha);
  }

  public static void glClearDepthf(
    float depth) {
    gl().glClearDepthf(
      depth);
  }

  public static void glClearStencil(
    int s) {
    gl().glClearStencil(
      s);
  }

  public static void glColorMask(
    boolean red,
    boolean green,
    boolean blue,
    boolean alpha) {
    gl().glColorMask(
      red,
      green,
      blue,
      alpha);
  }

  public static void glCompressedTexImage2D(
    int target,
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

  public static void glCompressedTexImage2D(
    int target,
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

  public static void glCompressedTexSubImage2D(
    int target,
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

  public static void glCompressedTexSubImage2D(
    int target,
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

  public static void glCopyTexImage2D(
    int target,
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

  public static void glCopyTexSubImage2D(
    int target,
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

  public static void glCullFace(
    int mode) {
    gl().glCullFace(
      mode);
  }

  public static void glDeleteBuffers(
    int n,
    IntBuffer buffers) {
    gl().glDeleteBuffers(
      n,
      buffers);
  }

  public static void glDeleteBuffers(
    int n,
    int[] buffers,
    int buffers_offset) {
    gl().glDeleteBuffers(
      n,
      buffers,
      buffers_offset);
  }

  public static void glDeleteFramebuffers(
    int n,
    IntBuffer framebuffers) {
    gl().glDeleteFramebuffers(
      n,
      framebuffers);
  }

  public static void glDeleteFramebuffers(
    int n,
    int[] framebuffers,
    int framebuffers_offset) {
    gl().glDeleteFramebuffers(
      n,
      framebuffers,
      framebuffers_offset);
  }

  public static void glDeleteRenderbuffers(
    int n,
    IntBuffer renderbuffers) {
    gl().glDeleteRenderbuffers(
      n,
      renderbuffers);
  }

  public static void glDeleteRenderbuffers(
    int n,
    int[] renderbuffers,
    int renderbuffers_offset) {
    gl().glDeleteRenderbuffers(
      n,
      renderbuffers,
      renderbuffers_offset);
  }

  public static void glDeleteTextures(
    int n,
    IntBuffer textures) {
    gl().glDeleteTextures(
      n,
      textures);
  }

  public static void glDeleteTextures(
    int n,
    int[] textures,
    int textures_offset) {
    gl().glDeleteTextures(
      n,
      textures,
      textures_offset);
  }

  public static void glDepthFunc(
    int func) {
    gl().glDepthFunc(
      func);
  }

  public static void glDepthMask(
    boolean flag) {
    gl().glDepthMask(
      flag);
  }

  public static void glDepthRangef(
    float zNear,
    float zFar) {
    gl().glDepthRangef(
      zNear,
      zFar);
  }

  public static void glDisable(
    int cap) {
    gl().glDisable(
      cap);
  }

  public static void glDrawArrays(
    int mode,
    int first,
    int count) {
    gl().glDrawArrays(
      mode,
      first,
      count);
  }

  public static void glDrawElements(
    int mode,
    int count,
    int type,
    Buffer indices) {
    gl().glDrawElements(
      mode,
      count,
      type,
      indices);
  }

  public static void glDrawElements(
    int mode,
    int count,
    int type,
    long indices_buffer_offset) {
    gl().glDrawElements(
      mode,
      count,
      type,
      indices_buffer_offset);
  }

  public static void glEnable(
    int cap) {
    gl().glEnable(
      cap);
  }

  public static void glFinish(
    ) {
    gl().glFinish();
  }

  public static void glFlush(
    ) {
    gl().glFlush();
  }

  public static void glFramebufferRenderbuffer(
    int target,
    int attachment,
    int renderbuffertarget,
    int renderbuffer) {
    gl().glFramebufferRenderbuffer(
      target,
      attachment,
      renderbuffertarget,
      renderbuffer);
  }

  public static void glFramebufferTexture2D(
    int target,
    int attachment,
    int textarget,
    int texture,
    int level) {
    gl().glFramebufferTexture2D(
      target,
      attachment,
      textarget,
      texture,
      level);
  }

  public static void glFrontFace(
    int mode) {
    gl().glFrontFace(
      mode);
  }

  public static void glGenBuffers(
    int n,
    IntBuffer buffers) {
    gl().glGenBuffers(
      n,
      buffers);
  }

  public static void glGenBuffers(
    int n,
    int[] buffers,
    int buffers_offset) {
    gl().glGenBuffers(
      n,
      buffers,
      buffers_offset);
  }

  public static void glGenFramebuffers(
    int n,
    IntBuffer framebuffers) {
    gl().glGenFramebuffers(
      n,
      framebuffers);
  }

  public static void glGenFramebuffers(
    int n,
    int[] framebuffers,
    int framebuffers_offset) {
    gl().glGenFramebuffers(
      n,
      framebuffers,
      framebuffers_offset);
  }

  public static void glGenRenderbuffers(
    int n,
    IntBuffer renderbuffers) {
    gl().glGenRenderbuffers(
      n,
      renderbuffers);
  }

  public static void glGenRenderbuffers(
    int n,
    int[] renderbuffers,
    int renderbuffers_offset) {
    gl().glGenRenderbuffers(
      n,
      renderbuffers,
      renderbuffers_offset);
  }

  public static void glGenTextures(
    int n,
    IntBuffer textures) {
    gl().glGenTextures(
      n,
      textures);
  }

  public static void glGenTextures(
    int n,
    int[] textures,
    int textures_offset) {
    gl().glGenTextures(
      n,
      textures,
      textures_offset);
  }

  public static void glGenerateMipmap(
    int target) {
    gl().glGenerateMipmap(
      target);
  }

  public static void glGetBooleanv(
    int pname,
    ByteBuffer params) {
    gl().glGetBooleanv(
      pname,
      params);
  }

  public static void glGetBooleanv(
    int pname,
    byte[] params,
    int params_offset) {
    gl().glGetBooleanv(
      pname,
      params,
      params_offset);
  }

  public static void glGetBufferParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetBufferParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetBufferParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetBufferParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static int glGetError(
    ) {
    return gl().glGetError();
  }

  public static void glGetFloatv(
    int pname,
    FloatBuffer params) {
    gl().glGetFloatv(
      pname,
      params);
  }

  public static void glGetFloatv(
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetFloatv(
      pname,
      params,
      params_offset);
  }

  public static void glGetFramebufferAttachmentParameteriv(
    int target,
    int attachment,
    int pname,
    IntBuffer params) {
    gl().glGetFramebufferAttachmentParameteriv(
      target,
      attachment,
      pname,
      params);
  }

  public static void glGetFramebufferAttachmentParameteriv(
    int target,
    int attachment,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetFramebufferAttachmentParameteriv(
      target,
      attachment,
      pname,
      params,
      params_offset);
  }

  public static int glGetGraphicsResetStatus(
    ) {
    return gl().glGetGraphicsResetStatus();
  }

  public static void glGetIntegerv(
    int pname,
    IntBuffer params) {
    gl().glGetIntegerv(
      pname,
      params);
  }

  public static void glGetIntegerv(
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetIntegerv(
      pname,
      params,
      params_offset);
  }

  public static void glGetRenderbufferParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetRenderbufferParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetRenderbufferParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetRenderbufferParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static String glGetString(
    int name) {
    return gl().glGetString(
      name);
  }

  public static void glGetTexParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetTexParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetTexParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetTexParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetTexParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetTexParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTexParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetnUniformfv(
    int program,
    int location,
    int bufSize,
    FloatBuffer params) {
    gl().glGetnUniformfv(
      program,
      location,
      bufSize,
      params);
  }

  public static void glGetnUniformfv(
    int program,
    int location,
    int bufSize,
    float[] params,
    int params_offset) {
    gl().glGetnUniformfv(
      program,
      location,
      bufSize,
      params,
      params_offset);
  }

  public static void glGetnUniformiv(
    int program,
    int location,
    int bufSize,
    IntBuffer params) {
    gl().glGetnUniformiv(
      program,
      location,
      bufSize,
      params);
  }

  public static void glGetnUniformiv(
    int program,
    int location,
    int bufSize,
    int[] params,
    int params_offset) {
    gl().glGetnUniformiv(
      program,
      location,
      bufSize,
      params,
      params_offset);
  }

  public static void glHint(
    int target,
    int mode) {
    gl().glHint(
      target,
      mode);
  }

  public static boolean glIsBuffer(
    int buffer) {
    return gl().glIsBuffer(
      buffer);
  }

  public static boolean glIsEnabled(
    int cap) {
    return gl().glIsEnabled(
      cap);
  }

  public static boolean glIsFramebuffer(
    int framebuffer) {
    return gl().glIsFramebuffer(
      framebuffer);
  }

  public static boolean glIsRenderbuffer(
    int renderbuffer) {
    return gl().glIsRenderbuffer(
      renderbuffer);
  }

  public static boolean glIsTexture(
    int texture) {
    return gl().glIsTexture(
      texture);
  }

  public static void glLineWidth(
    float width) {
    gl().glLineWidth(
      width);
  }

  public static ByteBuffer glMapBuffer(
    int target,
    int access) {
    return gl().glMapBuffer(
      target,
      access);
  }

  public static void glPixelStorei(
    int pname,
    int param) {
    gl().glPixelStorei(
      pname,
      param);
  }

  public static void glPolygonOffset(
    float factor,
    float units) {
    gl().glPolygonOffset(
      factor,
      units);
  }

  public static void glReadPixels(
    int x,
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

  public static void glReadPixels(
    int x,
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

  public static void glReadnPixels(
    int x,
    int y,
    int width,
    int height,
    int format,
    int type,
    int bufSize,
    Buffer data) {
    gl().glReadnPixels(
      x,
      y,
      width,
      height,
      format,
      type,
      bufSize,
      data);
  }

  public static void glRenderbufferStorage(
    int target,
    int internalformat,
    int width,
    int height) {
    gl().glRenderbufferStorage(
      target,
      internalformat,
      width,
      height);
  }

  public static void glSampleCoverage(
    float value,
    boolean invert) {
    gl().glSampleCoverage(
      value,
      invert);
  }

  public static void glScissor(
    int x,
    int y,
    int width,
    int height) {
    gl().glScissor(
      x,
      y,
      width,
      height);
  }

  public static void glStencilFunc(
    int func,
    int ref,
    int mask) {
    gl().glStencilFunc(
      func,
      ref,
      mask);
  }

  public static void glStencilMask(
    int mask) {
    gl().glStencilMask(
      mask);
  }

  public static void glStencilOp(
    int fail,
    int zfail,
    int zpass) {
    gl().glStencilOp(
      fail,
      zfail,
      zpass);
  }

  public static void glTexImage2D(
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int border,
    int format,
    int type,
    Buffer pixels) {
    gl().glTexImage2D(
      target,
      level,
      internalformat,
      width,
      height,
      border,
      format,
      type,
      pixels);
  }

  public static void glTexImage2D(
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int border,
    int format,
    int type,
    long pixels_buffer_offset) {
    gl().glTexImage2D(
      target,
      level,
      internalformat,
      width,
      height,
      border,
      format,
      type,
      pixels_buffer_offset);
  }

  public static void glTexParameterf(
    int target,
    int pname,
    float param) {
    gl().glTexParameterf(
      target,
      pname,
      param);
  }

  public static void glTexParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glTexParameterfv(
      target,
      pname,
      params);
  }

  public static void glTexParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glTexParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexParameteri(
    int target,
    int pname,
    int param) {
    gl().glTexParameteri(
      target,
      pname,
      param);
  }

  public static void glTexParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glTexParameteriv(
      target,
      pname,
      params);
  }

  public static void glTexParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTexParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexStorage1D(
    int target,
    int levels,
    int internalformat,
    int width) {
    gl().glTexStorage1D(
      target,
      levels,
      internalformat,
      width);
  }

  public static void glTexStorage2D(
    int target,
    int levels,
    int internalformat,
    int width,
    int height) {
    gl().glTexStorage2D(
      target,
      levels,
      internalformat,
      width,
      height);
  }

  public static void glTexStorage3D(
    int target,
    int levels,
    int internalformat,
    int width,
    int height,
    int depth) {
    gl().glTexStorage3D(
      target,
      levels,
      internalformat,
      width,
      height,
      depth);
  }

  public static void glTexSubImage2D(
    int target,
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

  public static void glTexSubImage2D(
    int target,
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

  public static void glTextureStorage1DEXT(
    int texture,
    int target,
    int levels,
    int internalformat,
    int width) {
    gl().glTextureStorage1DEXT(
      texture,
      target,
      levels,
      internalformat,
      width);
  }

  public static void glTextureStorage2DEXT(
    int texture,
    int target,
    int levels,
    int internalformat,
    int width,
    int height) {
    gl().glTextureStorage2DEXT(
      texture,
      target,
      levels,
      internalformat,
      width,
      height);
  }

  public static void glTextureStorage3DEXT(
    int texture,
    int target,
    int levels,
    int internalformat,
    int width,
    int height,
    int depth) {
    gl().glTextureStorage3DEXT(
      texture,
      target,
      levels,
      internalformat,
      width,
      height,
      depth);
  }

  public static boolean glUnmapBuffer(
    int target) {
    return gl().glUnmapBuffer(
      target);
  }

  public static void glViewport(
    int x,
    int y,
    int width,
    int height) {
    gl().glViewport(
      x,
      y,
      width,
      height);
  }


  // Generated from GLBase.html

  public static void glClearDepth(
    double depth) {
    gl().glClearDepth(
      depth);
  }

  public static void glDepthRange(
    double zNear,
    double zFar) {
    gl().glDepthRange(
      zNear,
      zFar);
  }

  public static int glGetBoundBuffer(
    int target) {
    return gl().glGetBoundBuffer(
      target);
  }

  public static long glGetBufferSize(
    int buffer) {
    return gl().glGetBufferSize(
      buffer);
  }

  public static boolean glIsVBOArrayEnabled(
    ) {
    return gl().glIsVBOArrayEnabled();
  }

  public static boolean glIsVBOElementArrayEnabled(
    ) {
    return gl().glIsVBOElementArrayEnabled();
  }


  // Generated from GLLightingFunc.html

  public static final int GL_LIGHT0
    = GL2.GL_LIGHT0;

  public static final int GL_LIGHT1
    = GL2.GL_LIGHT1;

  public static final int GL_LIGHT2
    = GL2.GL_LIGHT2;

  public static final int GL_LIGHT3
    = GL2.GL_LIGHT3;

  public static final int GL_LIGHT4
    = GL2.GL_LIGHT4;

  public static final int GL_LIGHT5
    = GL2.GL_LIGHT5;

  public static final int GL_LIGHT6
    = GL2.GL_LIGHT6;

  public static final int GL_LIGHT7
    = GL2.GL_LIGHT7;

  public static final int GL_LIGHTING
    = GL2.GL_LIGHTING;

  public static final int GL_AMBIENT
    = GL2.GL_AMBIENT;

  public static final int GL_DIFFUSE
    = GL2.GL_DIFFUSE;

  public static final int GL_SPECULAR
    = GL2.GL_SPECULAR;

  public static final int GL_POSITION
    = GL2.GL_POSITION;

  public static final int GL_SPOT_DIRECTION
    = GL2.GL_SPOT_DIRECTION;

  public static final int GL_SPOT_EXPONENT
    = GL2.GL_SPOT_EXPONENT;

  public static final int GL_SPOT_CUTOFF
    = GL2.GL_SPOT_CUTOFF;

  public static final int GL_CONSTANT_ATTENUATION
    = GL2.GL_CONSTANT_ATTENUATION;

  public static final int GL_LINEAR_ATTENUATION
    = GL2.GL_LINEAR_ATTENUATION;

  public static final int GL_QUADRATIC_ATTENUATION
    = GL2.GL_QUADRATIC_ATTENUATION;

  public static final int GL_EMISSION
    = GL2.GL_EMISSION;

  public static final int GL_SHININESS
    = GL2.GL_SHININESS;

  public static final int GL_AMBIENT_AND_DIFFUSE
    = GL2.GL_AMBIENT_AND_DIFFUSE;

  public static final int GL_COLOR_MATERIAL
    = GL2.GL_COLOR_MATERIAL;

  public static final int GL_NORMALIZE
    = GL2.GL_NORMALIZE;

  public static final int GL_FLAT
    = GL2.GL_FLAT;

  public static final int GL_SMOOTH
    = GL2.GL_SMOOTH;

  public static void glLightfv(
    int light,
    int pname,
    FloatBuffer params) {
    gl().glLightfv(
      light,
      pname,
      params);
  }

  public static void glLightfv(
    int light,
    int pname,
    float[] params,
    int params_offset) {
    gl().glLightfv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glMaterialf(
    int face,
    int pname,
    float param) {
    gl().glMaterialf(
      face,
      pname,
      param);
  }

  public static void glMaterialfv(
    int face,
    int pname,
    FloatBuffer params) {
    gl().glMaterialfv(
      face,
      pname,
      params);
  }

  public static void glMaterialfv(
    int face,
    int pname,
    float[] params,
    int params_offset) {
    gl().glMaterialfv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glColor4f(
    float red,
    float green,
    float blue,
    float alpha) {
    gl().glColor4f(
      red,
      green,
      blue,
      alpha);
  }

  public static void glShadeModel(
    int mode) {
    gl().glShadeModel(
      mode);
  }


  // Generated from GLMatrixFunc.html

  public static final int GL_MATRIX_MODE
    = GL2.GL_MATRIX_MODE;

  public static final int GL_MODELVIEW
    = GL2.GL_MODELVIEW;

  public static final int GL_PROJECTION
    = GL2.GL_PROJECTION;

  public static final int GL_MODELVIEW_MATRIX
    = GL2.GL_MODELVIEW_MATRIX;

  public static final int GL_PROJECTION_MATRIX
    = GL2.GL_PROJECTION_MATRIX;

  public static final int GL_TEXTURE_MATRIX
    = GL2.GL_TEXTURE_MATRIX;

  public static void glMatrixMode(
    int mode) {
    gl().glMatrixMode(
      mode);
  }

  public static void glPushMatrix(
    ) {
    gl().glPushMatrix();
  }

  public static void glPopMatrix(
    ) {
    gl().glPopMatrix();
  }

  public static void glLoadIdentity(
    ) {
    gl().glLoadIdentity();
  }

  public static void glLoadMatrixf(
    FloatBuffer m) {
    gl().glLoadMatrixf(
      m);
  }

  public static void glLoadMatrixf(
    float[] m,
    int m_offset) {
    gl().glLoadMatrixf(
      m,
      m_offset);
  }

  public static void glMultMatrixf(
    FloatBuffer m) {
    gl().glMultMatrixf(
      m);
  }

  public static void glMultMatrixf(
    float[] m,
    int m_offset) {
    gl().glMultMatrixf(
      m,
      m_offset);
  }

  public static void glTranslatef(
    float x,
    float y,
    float z) {
    gl().glTranslatef(
      x,
      y,
      z);
  }

  public static void glRotatef(
    float angle,
    float x,
    float y,
    float z) {
    gl().glRotatef(
      angle,
      x,
      y,
      z);
  }

  public static void glScalef(
    float x,
    float y,
    float z) {
    gl().glScalef(
      x,
      y,
      z);
  }

  public static void glOrthof(
    float left,
    float right,
    float bottom,
    float top,
    float zNear,
    float zFar) {
    gl().glOrthof(
      left,
      right,
      bottom,
      top,
      zNear,
      zFar);
  }

  public static void glFrustumf(
    float left,
    float right,
    float bottom,
    float top,
    float zNear,
    float zFar) {
    gl().glFrustumf(
      left,
      right,
      bottom,
      top,
      zNear,
      zFar);
  }


  // Generated from GLPointerFunc.html

  public static final int GL_VERTEX_ARRAY
    = GL2.GL_VERTEX_ARRAY;

  public static final int GL_NORMAL_ARRAY
    = GL2.GL_NORMAL_ARRAY;

  public static final int GL_COLOR_ARRAY
    = GL2.GL_COLOR_ARRAY;

  public static final int GL_TEXTURE_COORD_ARRAY
    = GL2.GL_TEXTURE_COORD_ARRAY;

  public static void glEnableClientState(
    int arrayName) {
    gl().glEnableClientState(
      arrayName);
  }

  public static void glDisableClientState(
    int arrayName) {
    gl().glDisableClientState(
      arrayName);
  }

  public static void glVertexPointer(
    GLArrayData array) {
    gl().glVertexPointer(
      array);
  }

  public static void glVertexPointer(
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glVertexPointer(
      size,
      type,
      stride,
      pointer);
  }

  public static void glVertexPointer(
    int size,
    int type,
    int stride,
    long pointer_buffer_offset) {
    gl().glVertexPointer(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glColorPointer(
    GLArrayData array) {
    gl().glColorPointer(
      array);
  }

  public static void glColorPointer(
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glColorPointer(
      size,
      type,
      stride,
      pointer);
  }

  public static void glColorPointer(
    int size,
    int type,
    int stride,
    long pointer_buffer_offset) {
    gl().glColorPointer(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glNormalPointer(
    GLArrayData array) {
    gl().glNormalPointer(
      array);
  }

  public static void glNormalPointer(
    int type,
    int stride,
    Buffer pointer) {
    gl().glNormalPointer(
      type,
      stride,
      pointer);
  }

  public static void glNormalPointer(
    int type,
    int stride,
    long pointer_buffer_offset) {
    gl().glNormalPointer(
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glTexCoordPointer(
    GLArrayData array) {
    gl().glTexCoordPointer(
      array);
  }

  public static void glTexCoordPointer(
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glTexCoordPointer(
      size,
      type,
      stride,
      pointer);
  }

  public static void glTexCoordPointer(
    int size,
    int type,
    int stride,
    long pointer_buffer_offset) {
    gl().glTexCoordPointer(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }


  // Generated from GL2.html

  public static final int GL_2_BYTES
    = GL2.GL_2_BYTES;

  public static final int GL_3_BYTES
    = GL2.GL_3_BYTES;

  public static final int GL_4_BYTES
    = GL2.GL_4_BYTES;

  public static final int GL_QUADS
    = GL2.GL_QUADS;

  public static final int GL_QUAD_STRIP
    = GL2.GL_QUAD_STRIP;

  public static final int GL_POLYGON
    = GL2.GL_POLYGON;

  public static final int GL_LINE_STIPPLE
    = GL2.GL_LINE_STIPPLE;

  public static final int GL_LINE_STIPPLE_PATTERN
    = GL2.GL_LINE_STIPPLE_PATTERN;

  public static final int GL_LINE_STIPPLE_REPEAT
    = GL2.GL_LINE_STIPPLE_REPEAT;

  public static final int GL_POLYGON_MODE
    = GL2.GL_POLYGON_MODE;

  public static final int GL_POLYGON_STIPPLE
    = GL2.GL_POLYGON_STIPPLE;

  public static final int GL_EDGE_FLAG
    = GL2.GL_EDGE_FLAG;

  public static final int GL_COMPILE
    = GL2.GL_COMPILE;

  public static final int GL_COMPILE_AND_EXECUTE
    = GL2.GL_COMPILE_AND_EXECUTE;

  public static final int GL_LIST_BASE
    = GL2.GL_LIST_BASE;

  public static final int GL_LIST_INDEX
    = GL2.GL_LIST_INDEX;

  public static final int GL_LIST_MODE
    = GL2.GL_LIST_MODE;

  public static final int GL_COLOR_INDEXES
    = GL2.GL_COLOR_INDEXES;

  public static final int GL_LIGHT_MODEL_LOCAL_VIEWER
    = GL2.GL_LIGHT_MODEL_LOCAL_VIEWER;

  public static final int GL_COLOR_MATERIAL_FACE
    = GL2.GL_COLOR_MATERIAL_FACE;

  public static final int GL_COLOR_MATERIAL_PARAMETER
    = GL2.GL_COLOR_MATERIAL_PARAMETER;

  public static final int GL_ACCUM_RED_BITS
    = GL2.GL_ACCUM_RED_BITS;

  public static final int GL_ACCUM_GREEN_BITS
    = GL2.GL_ACCUM_GREEN_BITS;

  public static final int GL_ACCUM_BLUE_BITS
    = GL2.GL_ACCUM_BLUE_BITS;

  public static final int GL_ACCUM_ALPHA_BITS
    = GL2.GL_ACCUM_ALPHA_BITS;

  public static final int GL_ACCUM_CLEAR_VALUE
    = GL2.GL_ACCUM_CLEAR_VALUE;

  public static final int GL_ACCUM
    = GL2.GL_ACCUM;

  public static final int GL_LOAD
    = GL2.GL_LOAD;

  public static final int GL_MULT
    = GL2.GL_MULT;

  public static final int GL_RETURN
    = GL2.GL_RETURN;

  public static final int GL_FEEDBACK
    = GL2.GL_FEEDBACK;

  public static final int GL_RENDER
    = GL2.GL_RENDER;

  public static final int GL_SELECT
    = GL2.GL_SELECT;

  public static final int GL_2D
    = GL2.GL_2D;

  public static final int GL_3D
    = GL2.GL_3D;

  public static final int GL_3D_COLOR
    = GL2.GL_3D_COLOR;

  public static final int GL_3D_COLOR_TEXTURE
    = GL2.GL_3D_COLOR_TEXTURE;

  public static final int GL_4D_COLOR_TEXTURE
    = GL2.GL_4D_COLOR_TEXTURE;

  public static final int GL_POINT_TOKEN
    = GL2.GL_POINT_TOKEN;

  public static final int GL_LINE_TOKEN
    = GL2.GL_LINE_TOKEN;

  public static final int GL_LINE_RESET_TOKEN
    = GL2.GL_LINE_RESET_TOKEN;

  public static final int GL_POLYGON_TOKEN
    = GL2.GL_POLYGON_TOKEN;

  public static final int GL_BITMAP_TOKEN
    = GL2.GL_BITMAP_TOKEN;

  public static final int GL_DRAW_PIXEL_TOKEN
    = GL2.GL_DRAW_PIXEL_TOKEN;

  public static final int GL_COPY_PIXEL_TOKEN
    = GL2.GL_COPY_PIXEL_TOKEN;

  public static final int GL_PASS_THROUGH_TOKEN
    = GL2.GL_PASS_THROUGH_TOKEN;

  public static final int GL_FEEDBACK_BUFFER_POINTER
    = GL2.GL_FEEDBACK_BUFFER_POINTER;

  public static final int GL_FEEDBACK_BUFFER_SIZE
    = GL2.GL_FEEDBACK_BUFFER_SIZE;

  public static final int GL_FEEDBACK_BUFFER_TYPE
    = GL2.GL_FEEDBACK_BUFFER_TYPE;

  public static final int GL_SELECTION_BUFFER_POINTER
    = GL2.GL_SELECTION_BUFFER_POINTER;

  public static final int GL_SELECTION_BUFFER_SIZE
    = GL2.GL_SELECTION_BUFFER_SIZE;

  public static final int GL_FOG_INDEX
    = GL2.GL_FOG_INDEX;

  public static final int GL_LOGIC_OP
    = GL2.GL_LOGIC_OP;

  public static final int GL_INDEX_LOGIC_OP
    = GL2.GL_INDEX_LOGIC_OP;

  public static final int GL_AUX0
    = GL2.GL_AUX0;

  public static final int GL_AUX1
    = GL2.GL_AUX1;

  public static final int GL_AUX2
    = GL2.GL_AUX2;

  public static final int GL_AUX3
    = GL2.GL_AUX3;

  public static final int GL_COLOR_INDEX
    = GL2.GL_COLOR_INDEX;

  public static final int GL_INDEX_BITS
    = GL2.GL_INDEX_BITS;

  public static final int GL_AUX_BUFFERS
    = GL2.GL_AUX_BUFFERS;

  public static final int GL_BITMAP
    = GL2.GL_BITMAP;

  public static final int GL_MAX_LIST_NESTING
    = GL2.GL_MAX_LIST_NESTING;

  public static final int GL_MAX_ATTRIB_STACK_DEPTH
    = GL2.GL_MAX_ATTRIB_STACK_DEPTH;

  public static final int GL_MAX_NAME_STACK_DEPTH
    = GL2.GL_MAX_NAME_STACK_DEPTH;

  public static final int GL_MAX_EVAL_ORDER
    = GL2.GL_MAX_EVAL_ORDER;

  public static final int GL_MAX_PIXEL_MAP_TABLE
    = GL2.GL_MAX_PIXEL_MAP_TABLE;

  public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH
    = GL2.GL_MAX_CLIENT_ATTRIB_STACK_DEPTH;

  public static final int GL_ATTRIB_STACK_DEPTH
    = GL2.GL_ATTRIB_STACK_DEPTH;

  public static final int GL_CLIENT_ATTRIB_STACK_DEPTH
    = GL2.GL_CLIENT_ATTRIB_STACK_DEPTH;

  public static final int GL_CURRENT_INDEX
    = GL2.GL_CURRENT_INDEX;

  public static final int GL_CURRENT_RASTER_COLOR
    = GL2.GL_CURRENT_RASTER_COLOR;

  public static final int GL_CURRENT_RASTER_DISTANCE
    = GL2.GL_CURRENT_RASTER_DISTANCE;

  public static final int GL_CURRENT_RASTER_INDEX
    = GL2.GL_CURRENT_RASTER_INDEX;

  public static final int GL_CURRENT_RASTER_POSITION
    = GL2.GL_CURRENT_RASTER_POSITION;

  public static final int GL_CURRENT_RASTER_TEXTURE_COORDS
    = GL2.GL_CURRENT_RASTER_TEXTURE_COORDS;

  public static final int GL_CURRENT_RASTER_POSITION_VALID
    = GL2.GL_CURRENT_RASTER_POSITION_VALID;

  public static final int GL_INDEX_CLEAR_VALUE
    = GL2.GL_INDEX_CLEAR_VALUE;

  public static final int GL_INDEX_MODE
    = GL2.GL_INDEX_MODE;

  public static final int GL_INDEX_WRITEMASK
    = GL2.GL_INDEX_WRITEMASK;

  public static final int GL_NAME_STACK_DEPTH
    = GL2.GL_NAME_STACK_DEPTH;

  public static final int GL_RENDER_MODE
    = GL2.GL_RENDER_MODE;

  public static final int GL_RGBA_MODE
    = GL2.GL_RGBA_MODE;

  public static final int GL_AUTO_NORMAL
    = GL2.GL_AUTO_NORMAL;

  public static final int GL_MAP1_COLOR_4
    = GL2.GL_MAP1_COLOR_4;

  public static final int GL_MAP1_INDEX
    = GL2.GL_MAP1_INDEX;

  public static final int GL_MAP1_NORMAL
    = GL2.GL_MAP1_NORMAL;

  public static final int GL_MAP1_TEXTURE_COORD_1
    = GL2.GL_MAP1_TEXTURE_COORD_1;

  public static final int GL_MAP1_TEXTURE_COORD_2
    = GL2.GL_MAP1_TEXTURE_COORD_2;

  public static final int GL_MAP1_TEXTURE_COORD_3
    = GL2.GL_MAP1_TEXTURE_COORD_3;

  public static final int GL_MAP1_TEXTURE_COORD_4
    = GL2.GL_MAP1_TEXTURE_COORD_4;

  public static final int GL_MAP1_VERTEX_3
    = GL2.GL_MAP1_VERTEX_3;

  public static final int GL_MAP1_VERTEX_4
    = GL2.GL_MAP1_VERTEX_4;

  public static final int GL_MAP2_COLOR_4
    = GL2.GL_MAP2_COLOR_4;

  public static final int GL_MAP2_INDEX
    = GL2.GL_MAP2_INDEX;

  public static final int GL_MAP2_NORMAL
    = GL2.GL_MAP2_NORMAL;

  public static final int GL_MAP2_TEXTURE_COORD_1
    = GL2.GL_MAP2_TEXTURE_COORD_1;

  public static final int GL_MAP2_TEXTURE_COORD_2
    = GL2.GL_MAP2_TEXTURE_COORD_2;

  public static final int GL_MAP2_TEXTURE_COORD_3
    = GL2.GL_MAP2_TEXTURE_COORD_3;

  public static final int GL_MAP2_TEXTURE_COORD_4
    = GL2.GL_MAP2_TEXTURE_COORD_4;

  public static final int GL_MAP2_VERTEX_3
    = GL2.GL_MAP2_VERTEX_3;

  public static final int GL_MAP2_VERTEX_4
    = GL2.GL_MAP2_VERTEX_4;

  public static final int GL_MAP1_GRID_DOMAIN
    = GL2.GL_MAP1_GRID_DOMAIN;

  public static final int GL_MAP1_GRID_SEGMENTS
    = GL2.GL_MAP1_GRID_SEGMENTS;

  public static final int GL_MAP2_GRID_DOMAIN
    = GL2.GL_MAP2_GRID_DOMAIN;

  public static final int GL_MAP2_GRID_SEGMENTS
    = GL2.GL_MAP2_GRID_SEGMENTS;

  public static final int GL_COEFF
    = GL2.GL_COEFF;

  public static final int GL_DOMAIN
    = GL2.GL_DOMAIN;

  public static final int GL_ORDER
    = GL2.GL_ORDER;

  public static final int GL_MAP_COLOR
    = GL2.GL_MAP_COLOR;

  public static final int GL_MAP_STENCIL
    = GL2.GL_MAP_STENCIL;

  public static final int GL_INDEX_SHIFT
    = GL2.GL_INDEX_SHIFT;

  public static final int GL_INDEX_OFFSET
    = GL2.GL_INDEX_OFFSET;

  public static final int GL_RED_SCALE
    = GL2.GL_RED_SCALE;

  public static final int GL_RED_BIAS
    = GL2.GL_RED_BIAS;

  public static final int GL_GREEN_SCALE
    = GL2.GL_GREEN_SCALE;

  public static final int GL_GREEN_BIAS
    = GL2.GL_GREEN_BIAS;

  public static final int GL_BLUE_SCALE
    = GL2.GL_BLUE_SCALE;

  public static final int GL_BLUE_BIAS
    = GL2.GL_BLUE_BIAS;

  public static final int GL_ALPHA_BIAS
    = GL2.GL_ALPHA_BIAS;

  public static final int GL_DEPTH_SCALE
    = GL2.GL_DEPTH_SCALE;

  public static final int GL_DEPTH_BIAS
    = GL2.GL_DEPTH_BIAS;

  public static final int GL_PIXEL_MAP_S_TO_S_SIZE
    = GL2.GL_PIXEL_MAP_S_TO_S_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_I_SIZE
    = GL2.GL_PIXEL_MAP_I_TO_I_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_R_SIZE
    = GL2.GL_PIXEL_MAP_I_TO_R_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_G_SIZE
    = GL2.GL_PIXEL_MAP_I_TO_G_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_B_SIZE
    = GL2.GL_PIXEL_MAP_I_TO_B_SIZE;

  public static final int GL_PIXEL_MAP_I_TO_A_SIZE
    = GL2.GL_PIXEL_MAP_I_TO_A_SIZE;

  public static final int GL_PIXEL_MAP_R_TO_R_SIZE
    = GL2.GL_PIXEL_MAP_R_TO_R_SIZE;

  public static final int GL_PIXEL_MAP_G_TO_G_SIZE
    = GL2.GL_PIXEL_MAP_G_TO_G_SIZE;

  public static final int GL_PIXEL_MAP_B_TO_B_SIZE
    = GL2.GL_PIXEL_MAP_B_TO_B_SIZE;

  public static final int GL_PIXEL_MAP_A_TO_A_SIZE
    = GL2.GL_PIXEL_MAP_A_TO_A_SIZE;

  public static final int GL_PIXEL_MAP_S_TO_S
    = GL2.GL_PIXEL_MAP_S_TO_S;

  public static final int GL_PIXEL_MAP_I_TO_I
    = GL2.GL_PIXEL_MAP_I_TO_I;

  public static final int GL_PIXEL_MAP_I_TO_R
    = GL2.GL_PIXEL_MAP_I_TO_R;

  public static final int GL_PIXEL_MAP_I_TO_G
    = GL2.GL_PIXEL_MAP_I_TO_G;

  public static final int GL_PIXEL_MAP_I_TO_B
    = GL2.GL_PIXEL_MAP_I_TO_B;

  public static final int GL_PIXEL_MAP_I_TO_A
    = GL2.GL_PIXEL_MAP_I_TO_A;

  public static final int GL_PIXEL_MAP_R_TO_R
    = GL2.GL_PIXEL_MAP_R_TO_R;

  public static final int GL_PIXEL_MAP_G_TO_G
    = GL2.GL_PIXEL_MAP_G_TO_G;

  public static final int GL_PIXEL_MAP_B_TO_B
    = GL2.GL_PIXEL_MAP_B_TO_B;

  public static final int GL_PIXEL_MAP_A_TO_A
    = GL2.GL_PIXEL_MAP_A_TO_A;

  public static final int GL_ZOOM_X
    = GL2.GL_ZOOM_X;

  public static final int GL_ZOOM_Y
    = GL2.GL_ZOOM_Y;

  public static final int GL_TEXTURE_GEN_S
    = GL2.GL_TEXTURE_GEN_S;

  public static final int GL_TEXTURE_GEN_T
    = GL2.GL_TEXTURE_GEN_T;

  public static final int GL_TEXTURE_BORDER
    = GL2.GL_TEXTURE_BORDER;

  public static final int GL_TEXTURE_COMPONENTS
    = GL2.GL_TEXTURE_COMPONENTS;

  public static final int GL_TEXTURE_LUMINANCE_SIZE
    = GL2.GL_TEXTURE_LUMINANCE_SIZE;

  public static final int GL_TEXTURE_INTENSITY_SIZE
    = GL2.GL_TEXTURE_INTENSITY_SIZE;

  public static final int GL_OBJECT_LINEAR
    = GL2.GL_OBJECT_LINEAR;

  public static final int GL_OBJECT_PLANE
    = GL2.GL_OBJECT_PLANE;

  public static final int GL_EYE_LINEAR
    = GL2.GL_EYE_LINEAR;

  public static final int GL_EYE_PLANE
    = GL2.GL_EYE_PLANE;

  public static final int GL_SPHERE_MAP
    = GL2.GL_SPHERE_MAP;

  public static final int GL_CLAMP
    = GL2.GL_CLAMP;

  public static final int GL_S
    = GL2.GL_S;

  public static final int GL_T
    = GL2.GL_T;

  public static final int GL_R
    = GL2.GL_R;

  public static final int GL_Q
    = GL2.GL_Q;

  public static final int GL_TEXTURE_GEN_R
    = GL2.GL_TEXTURE_GEN_R;

  public static final int GL_TEXTURE_GEN_Q
    = GL2.GL_TEXTURE_GEN_Q;

  public static final int GL_CURRENT_BIT
    = GL2.GL_CURRENT_BIT;

  public static final int GL_POINT_BIT
    = GL2.GL_POINT_BIT;

  public static final int GL_LINE_BIT
    = GL2.GL_LINE_BIT;

  public static final int GL_POLYGON_BIT
    = GL2.GL_POLYGON_BIT;

  public static final int GL_POLYGON_STIPPLE_BIT
    = GL2.GL_POLYGON_STIPPLE_BIT;

  public static final int GL_PIXEL_MODE_BIT
    = GL2.GL_PIXEL_MODE_BIT;

  public static final int GL_LIGHTING_BIT
    = GL2.GL_LIGHTING_BIT;

  public static final int GL_FOG_BIT
    = GL2.GL_FOG_BIT;

  public static final int GL_ACCUM_BUFFER_BIT
    = GL2.GL_ACCUM_BUFFER_BIT;

  public static final int GL_VIEWPORT_BIT
    = GL2.GL_VIEWPORT_BIT;

  public static final int GL_TRANSFORM_BIT
    = GL2.GL_TRANSFORM_BIT;

  public static final int GL_ENABLE_BIT
    = GL2.GL_ENABLE_BIT;

  public static final int GL_HINT_BIT
    = GL2.GL_HINT_BIT;

  public static final int GL_EVAL_BIT
    = GL2.GL_EVAL_BIT;

  public static final int GL_LIST_BIT
    = GL2.GL_LIST_BIT;

  public static final int GL_TEXTURE_BIT
    = GL2.GL_TEXTURE_BIT;

  public static final int GL_SCISSOR_BIT
    = GL2.GL_SCISSOR_BIT;

  public static final int GL_ALL_ATTRIB_BITS
    = GL2.GL_ALL_ATTRIB_BITS;

  public static final int GL_TEXTURE_PRIORITY
    = GL2.GL_TEXTURE_PRIORITY;

  public static final int GL_TEXTURE_RESIDENT
    = GL2.GL_TEXTURE_RESIDENT;

  public static final int GL_ALPHA4
    = GL2.GL_ALPHA4;

  public static final int GL_ALPHA8
    = GL2.GL_ALPHA8;

  public static final int GL_ALPHA12
    = GL2.GL_ALPHA12;

  public static final int GL_ALPHA16
    = GL2.GL_ALPHA16;

  public static final int GL_LUMINANCE4
    = GL2.GL_LUMINANCE4;

  public static final int GL_LUMINANCE8
    = GL2.GL_LUMINANCE8;

  public static final int GL_LUMINANCE12
    = GL2.GL_LUMINANCE12;

  public static final int GL_LUMINANCE16
    = GL2.GL_LUMINANCE16;

  public static final int GL_LUMINANCE4_ALPHA4
    = GL2.GL_LUMINANCE4_ALPHA4;

  public static final int GL_LUMINANCE6_ALPHA2
    = GL2.GL_LUMINANCE6_ALPHA2;

  public static final int GL_LUMINANCE8_ALPHA8
    = GL2.GL_LUMINANCE8_ALPHA8;

  public static final int GL_LUMINANCE12_ALPHA4
    = GL2.GL_LUMINANCE12_ALPHA4;

  public static final int GL_LUMINANCE12_ALPHA12
    = GL2.GL_LUMINANCE12_ALPHA12;

  public static final int GL_LUMINANCE16_ALPHA16
    = GL2.GL_LUMINANCE16_ALPHA16;

  public static final int GL_INTENSITY
    = GL2.GL_INTENSITY;

  public static final int GL_INTENSITY4
    = GL2.GL_INTENSITY4;

  public static final int GL_INTENSITY8
    = GL2.GL_INTENSITY8;

  public static final int GL_INTENSITY12
    = GL2.GL_INTENSITY12;

  public static final int GL_INTENSITY16
    = GL2.GL_INTENSITY16;

  public static final int GL_CLIENT_PIXEL_STORE_BIT
    = GL2.GL_CLIENT_PIXEL_STORE_BIT;

  public static final int GL_CLIENT_VERTEX_ARRAY_BIT
    = GL2.GL_CLIENT_VERTEX_ARRAY_BIT;

  public static final int GL_INDEX_ARRAY
    = GL2.GL_INDEX_ARRAY;

  public static final int GL_EDGE_FLAG_ARRAY
    = GL2.GL_EDGE_FLAG_ARRAY;

  public static final int GL_INDEX_ARRAY_TYPE
    = GL2.GL_INDEX_ARRAY_TYPE;

  public static final int GL_INDEX_ARRAY_STRIDE
    = GL2.GL_INDEX_ARRAY_STRIDE;

  public static final int GL_EDGE_FLAG_ARRAY_STRIDE
    = GL2.GL_EDGE_FLAG_ARRAY_STRIDE;

  public static final int GL_INDEX_ARRAY_POINTER
    = GL2.GL_INDEX_ARRAY_POINTER;

  public static final int GL_EDGE_FLAG_ARRAY_POINTER
    = GL2.GL_EDGE_FLAG_ARRAY_POINTER;

  public static final int GL_V2F
    = GL2.GL_V2F;

  public static final int GL_V3F
    = GL2.GL_V3F;

  public static final int GL_C4UB_V2F
    = GL2.GL_C4UB_V2F;

  public static final int GL_C4UB_V3F
    = GL2.GL_C4UB_V3F;

  public static final int GL_C3F_V3F
    = GL2.GL_C3F_V3F;

  public static final int GL_N3F_V3F
    = GL2.GL_N3F_V3F;

  public static final int GL_C4F_N3F_V3F
    = GL2.GL_C4F_N3F_V3F;

  public static final int GL_T2F_V3F
    = GL2.GL_T2F_V3F;

  public static final int GL_T4F_V4F
    = GL2.GL_T4F_V4F;

  public static final int GL_T2F_C4UB_V3F
    = GL2.GL_T2F_C4UB_V3F;

  public static final int GL_T2F_C3F_V3F
    = GL2.GL_T2F_C3F_V3F;

  public static final int GL_T2F_N3F_V3F
    = GL2.GL_T2F_N3F_V3F;

  public static final int GL_T2F_C4F_N3F_V3F
    = GL2.GL_T2F_C4F_N3F_V3F;

  public static final int GL_T4F_C4F_N3F_V4F
    = GL2.GL_T4F_C4F_N3F_V4F;

  public static final int GL_LIGHT_MODEL_COLOR_CONTROL
    = GL2.GL_LIGHT_MODEL_COLOR_CONTROL;

  public static final int GL_SINGLE_COLOR
    = GL2.GL_SINGLE_COLOR;

  public static final int GL_SEPARATE_SPECULAR_COLOR
    = GL2.GL_SEPARATE_SPECULAR_COLOR;

  public static final int GL_CONVOLUTION_1D
    = GL2.GL_CONVOLUTION_1D;

  public static final int GL_CONVOLUTION_2D
    = GL2.GL_CONVOLUTION_2D;

  public static final int GL_SEPARABLE_2D
    = GL2.GL_SEPARABLE_2D;

  public static final int GL_CONVOLUTION_BORDER_MODE
    = GL2.GL_CONVOLUTION_BORDER_MODE;

  public static final int GL_CONVOLUTION_FILTER_SCALE
    = GL2.GL_CONVOLUTION_FILTER_SCALE;

  public static final int GL_CONVOLUTION_FILTER_BIAS
    = GL2.GL_CONVOLUTION_FILTER_BIAS;

  public static final int GL_REDUCE
    = GL2.GL_REDUCE;

  public static final int GL_CONVOLUTION_FORMAT
    = GL2.GL_CONVOLUTION_FORMAT;

  public static final int GL_CONVOLUTION_WIDTH
    = GL2.GL_CONVOLUTION_WIDTH;

  public static final int GL_CONVOLUTION_HEIGHT
    = GL2.GL_CONVOLUTION_HEIGHT;

  public static final int GL_MAX_CONVOLUTION_WIDTH
    = GL2.GL_MAX_CONVOLUTION_WIDTH;

  public static final int GL_MAX_CONVOLUTION_HEIGHT
    = GL2.GL_MAX_CONVOLUTION_HEIGHT;

  public static final int GL_POST_CONVOLUTION_RED_SCALE
    = GL2.GL_POST_CONVOLUTION_RED_SCALE;

  public static final int GL_POST_CONVOLUTION_GREEN_SCALE
    = GL2.GL_POST_CONVOLUTION_GREEN_SCALE;

  public static final int GL_POST_CONVOLUTION_BLUE_SCALE
    = GL2.GL_POST_CONVOLUTION_BLUE_SCALE;

  public static final int GL_POST_CONVOLUTION_ALPHA_SCALE
    = GL2.GL_POST_CONVOLUTION_ALPHA_SCALE;

  public static final int GL_POST_CONVOLUTION_RED_BIAS
    = GL2.GL_POST_CONVOLUTION_RED_BIAS;

  public static final int GL_POST_CONVOLUTION_GREEN_BIAS
    = GL2.GL_POST_CONVOLUTION_GREEN_BIAS;

  public static final int GL_POST_CONVOLUTION_BLUE_BIAS
    = GL2.GL_POST_CONVOLUTION_BLUE_BIAS;

  public static final int GL_POST_CONVOLUTION_ALPHA_BIAS
    = GL2.GL_POST_CONVOLUTION_ALPHA_BIAS;

  public static final int GL_HISTOGRAM
    = GL2.GL_HISTOGRAM;

  public static final int GL_PROXY_HISTOGRAM
    = GL2.GL_PROXY_HISTOGRAM;

  public static final int GL_HISTOGRAM_WIDTH
    = GL2.GL_HISTOGRAM_WIDTH;

  public static final int GL_HISTOGRAM_FORMAT
    = GL2.GL_HISTOGRAM_FORMAT;

  public static final int GL_HISTOGRAM_RED_SIZE
    = GL2.GL_HISTOGRAM_RED_SIZE;

  public static final int GL_HISTOGRAM_GREEN_SIZE
    = GL2.GL_HISTOGRAM_GREEN_SIZE;

  public static final int GL_HISTOGRAM_BLUE_SIZE
    = GL2.GL_HISTOGRAM_BLUE_SIZE;

  public static final int GL_HISTOGRAM_ALPHA_SIZE
    = GL2.GL_HISTOGRAM_ALPHA_SIZE;

  public static final int GL_HISTOGRAM_LUMINANCE_SIZE
    = GL2.GL_HISTOGRAM_LUMINANCE_SIZE;

  public static final int GL_HISTOGRAM_SINK
    = GL2.GL_HISTOGRAM_SINK;

  public static final int GL_MINMAX
    = GL2.GL_MINMAX;

  public static final int GL_MINMAX_FORMAT
    = GL2.GL_MINMAX_FORMAT;

  public static final int GL_MINMAX_SINK
    = GL2.GL_MINMAX_SINK;

  public static final int GL_TABLE_TOO_LARGE
    = GL2.GL_TABLE_TOO_LARGE;

  public static final int GL_COLOR_MATRIX
    = GL2.GL_COLOR_MATRIX;

  public static final int GL_COLOR_MATRIX_STACK_DEPTH
    = GL2.GL_COLOR_MATRIX_STACK_DEPTH;

  public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH
    = GL2.GL_MAX_COLOR_MATRIX_STACK_DEPTH;

  public static final int GL_POST_COLOR_MATRIX_RED_SCALE
    = GL2.GL_POST_COLOR_MATRIX_RED_SCALE;

  public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE
    = GL2.GL_POST_COLOR_MATRIX_GREEN_SCALE;

  public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE
    = GL2.GL_POST_COLOR_MATRIX_BLUE_SCALE;

  public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE
    = GL2.GL_POST_COLOR_MATRIX_ALPHA_SCALE;

  public static final int GL_POST_COLOR_MATRIX_RED_BIAS
    = GL2.GL_POST_COLOR_MATRIX_RED_BIAS;

  public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS
    = GL2.GL_POST_COLOR_MATRIX_GREEN_BIAS;

  public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS
    = GL2.GL_POST_COLOR_MATRIX_BLUE_BIAS;

  public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS
    = GL2.GL_POST_COLOR_MATRIX_ALPHA_BIAS;

  public static final int GL_COLOR_TABLE
    = GL2.GL_COLOR_TABLE;

  public static final int GL_POST_CONVOLUTION_COLOR_TABLE
    = GL2.GL_POST_CONVOLUTION_COLOR_TABLE;

  public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE
    = GL2.GL_POST_COLOR_MATRIX_COLOR_TABLE;

  public static final int GL_PROXY_COLOR_TABLE
    = GL2.GL_PROXY_COLOR_TABLE;

  public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE
    = GL2.GL_PROXY_POST_CONVOLUTION_COLOR_TABLE;

  public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE
    = GL2.GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE;

  public static final int GL_COLOR_TABLE_SCALE
    = GL2.GL_COLOR_TABLE_SCALE;

  public static final int GL_COLOR_TABLE_BIAS
    = GL2.GL_COLOR_TABLE_BIAS;

  public static final int GL_COLOR_TABLE_FORMAT
    = GL2.GL_COLOR_TABLE_FORMAT;

  public static final int GL_COLOR_TABLE_WIDTH
    = GL2.GL_COLOR_TABLE_WIDTH;

  public static final int GL_COLOR_TABLE_RED_SIZE
    = GL2.GL_COLOR_TABLE_RED_SIZE;

  public static final int GL_COLOR_TABLE_GREEN_SIZE
    = GL2.GL_COLOR_TABLE_GREEN_SIZE;

  public static final int GL_COLOR_TABLE_BLUE_SIZE
    = GL2.GL_COLOR_TABLE_BLUE_SIZE;

  public static final int GL_COLOR_TABLE_ALPHA_SIZE
    = GL2.GL_COLOR_TABLE_ALPHA_SIZE;

  public static final int GL_COLOR_TABLE_LUMINANCE_SIZE
    = GL2.GL_COLOR_TABLE_LUMINANCE_SIZE;

  public static final int GL_COLOR_TABLE_INTENSITY_SIZE
    = GL2.GL_COLOR_TABLE_INTENSITY_SIZE;

  public static final int GL_CONSTANT_BORDER
    = GL2.GL_CONSTANT_BORDER;

  public static final int GL_REPLICATE_BORDER
    = GL2.GL_REPLICATE_BORDER;

  public static final int GL_CONVOLUTION_BORDER_COLOR
    = GL2.GL_CONVOLUTION_BORDER_COLOR;

  public static final int GL_TRANSPOSE_MODELVIEW_MATRIX
    = GL2.GL_TRANSPOSE_MODELVIEW_MATRIX;

  public static final int GL_TRANSPOSE_PROJECTION_MATRIX
    = GL2.GL_TRANSPOSE_PROJECTION_MATRIX;

  public static final int GL_TRANSPOSE_TEXTURE_MATRIX
    = GL2.GL_TRANSPOSE_TEXTURE_MATRIX;

  public static final int GL_TRANSPOSE_COLOR_MATRIX
    = GL2.GL_TRANSPOSE_COLOR_MATRIX;

  public static final int GL_MULTISAMPLE_BIT
    = GL2.GL_MULTISAMPLE_BIT;

  public static final int GL_COMPRESSED_ALPHA
    = GL2.GL_COMPRESSED_ALPHA;

  public static final int GL_COMPRESSED_LUMINANCE
    = GL2.GL_COMPRESSED_LUMINANCE;

  public static final int GL_COMPRESSED_LUMINANCE_ALPHA
    = GL2.GL_COMPRESSED_LUMINANCE_ALPHA;

  public static final int GL_COMPRESSED_INTENSITY
    = GL2.GL_COMPRESSED_INTENSITY;

  public static final int GL_SOURCE0_RGB
    = GL2.GL_SOURCE0_RGB;

  public static final int GL_SOURCE1_RGB
    = GL2.GL_SOURCE1_RGB;

  public static final int GL_SOURCE2_RGB
    = GL2.GL_SOURCE2_RGB;

  public static final int GL_SOURCE0_ALPHA
    = GL2.GL_SOURCE0_ALPHA;

  public static final int GL_SOURCE1_ALPHA
    = GL2.GL_SOURCE1_ALPHA;

  public static final int GL_SOURCE2_ALPHA
    = GL2.GL_SOURCE2_ALPHA;

  public static final int GL_FOG_COORDINATE_SOURCE
    = GL2.GL_FOG_COORDINATE_SOURCE;

  public static final int GL_FOG_COORDINATE
    = GL2.GL_FOG_COORDINATE;

  public static final int GL_FRAGMENT_DEPTH
    = GL2.GL_FRAGMENT_DEPTH;

  public static final int GL_CURRENT_FOG_COORDINATE
    = GL2.GL_CURRENT_FOG_COORDINATE;

  public static final int GL_FOG_COORDINATE_ARRAY_TYPE
    = GL2.GL_FOG_COORDINATE_ARRAY_TYPE;

  public static final int GL_FOG_COORDINATE_ARRAY_STRIDE
    = GL2.GL_FOG_COORDINATE_ARRAY_STRIDE;

  public static final int GL_FOG_COORDINATE_ARRAY_POINTER
    = GL2.GL_FOG_COORDINATE_ARRAY_POINTER;

  public static final int GL_FOG_COORDINATE_ARRAY
    = GL2.GL_FOG_COORDINATE_ARRAY;

  public static final int GL_COLOR_SUM
    = GL2.GL_COLOR_SUM;

  public static final int GL_CURRENT_SECONDARY_COLOR
    = GL2.GL_CURRENT_SECONDARY_COLOR;

  public static final int GL_SECONDARY_COLOR_ARRAY_SIZE
    = GL2.GL_SECONDARY_COLOR_ARRAY_SIZE;

  public static final int GL_SECONDARY_COLOR_ARRAY_TYPE
    = GL2.GL_SECONDARY_COLOR_ARRAY_TYPE;

  public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE
    = GL2.GL_SECONDARY_COLOR_ARRAY_STRIDE;

  public static final int GL_SECONDARY_COLOR_ARRAY_POINTER
    = GL2.GL_SECONDARY_COLOR_ARRAY_POINTER;

  public static final int GL_SECONDARY_COLOR_ARRAY
    = GL2.GL_SECONDARY_COLOR_ARRAY;

  public static final int GL_TEXTURE_FILTER_CONTROL
    = GL2.GL_TEXTURE_FILTER_CONTROL;

  public static final int GL_DEPTH_TEXTURE_MODE
    = GL2.GL_DEPTH_TEXTURE_MODE;

  public static final int GL_COMPARE_R_TO_TEXTURE
    = GL2.GL_COMPARE_R_TO_TEXTURE;

  public static final int GL_INDEX_ARRAY_BUFFER_BINDING
    = GL2.GL_INDEX_ARRAY_BUFFER_BINDING;

  public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING
    = GL2.GL_EDGE_FLAG_ARRAY_BUFFER_BINDING;

  public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING
    = GL2.GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING;

  public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING
    = GL2.GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING;

  public static final int GL_FOG_COORD_SRC
    = GL2.GL_FOG_COORD_SRC;

  public static final int GL_FOG_COORD
    = GL2.GL_FOG_COORD;

  public static final int GL_CURRENT_FOG_COORD
    = GL2.GL_CURRENT_FOG_COORD;

  public static final int GL_FOG_COORD_ARRAY_TYPE
    = GL2.GL_FOG_COORD_ARRAY_TYPE;

  public static final int GL_FOG_COORD_ARRAY_STRIDE
    = GL2.GL_FOG_COORD_ARRAY_STRIDE;

  public static final int GL_FOG_COORD_ARRAY_POINTER
    = GL2.GL_FOG_COORD_ARRAY_POINTER;

  public static final int GL_FOG_COORD_ARRAY
    = GL2.GL_FOG_COORD_ARRAY;

  public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING
    = GL2.GL_FOG_COORD_ARRAY_BUFFER_BINDING;

  public static final int GL_VERTEX_PROGRAM_TWO_SIDE
    = GL2.GL_VERTEX_PROGRAM_TWO_SIDE;

  public static final int GL_MAX_TEXTURE_COORDS
    = GL2.GL_MAX_TEXTURE_COORDS;

  public static final int GL_CURRENT_RASTER_SECONDARY_COLOR
    = GL2.GL_CURRENT_RASTER_SECONDARY_COLOR;

  public static final int GL_SLUMINANCE_ALPHA
    = GL2.GL_SLUMINANCE_ALPHA;

  public static final int GL_SLUMINANCE8_ALPHA8
    = GL2.GL_SLUMINANCE8_ALPHA8;

  public static final int GL_SLUMINANCE
    = GL2.GL_SLUMINANCE;

  public static final int GL_SLUMINANCE8
    = GL2.GL_SLUMINANCE8;

  public static final int GL_COMPRESSED_SLUMINANCE
    = GL2.GL_COMPRESSED_SLUMINANCE;

  public static final int GL_COMPRESSED_SLUMINANCE_ALPHA
    = GL2.GL_COMPRESSED_SLUMINANCE_ALPHA;

  public static final int GL_CLAMP_VERTEX_COLOR
    = GL2.GL_CLAMP_VERTEX_COLOR;

  public static final int GL_CLAMP_FRAGMENT_COLOR
    = GL2.GL_CLAMP_FRAGMENT_COLOR;

  public static final int GL_ALPHA_INTEGER
    = GL2.GL_ALPHA_INTEGER;

  public static final int GL_ACTIVE_VERTEX_UNITS_ARB
    = GL2.GL_ACTIVE_VERTEX_UNITS_ARB;

  public static final int GL_WEIGHT_SUM_UNITY_ARB
    = GL2.GL_WEIGHT_SUM_UNITY_ARB;

  public static final int GL_VERTEX_BLEND_ARB
    = GL2.GL_VERTEX_BLEND_ARB;

  public static final int GL_CURRENT_WEIGHT_ARB
    = GL2.GL_CURRENT_WEIGHT_ARB;

  public static final int GL_MODELVIEW0_ARB
    = GL2.GL_MODELVIEW0_ARB;

  public static final int GL_MODELVIEW1_ARB
    = GL2.GL_MODELVIEW1_ARB;

  public static final int GL_MODELVIEW2_ARB
    = GL2.GL_MODELVIEW2_ARB;

  public static final int GL_MODELVIEW3_ARB
    = GL2.GL_MODELVIEW3_ARB;

  public static final int GL_MODELVIEW4_ARB
    = GL2.GL_MODELVIEW4_ARB;

  public static final int GL_MODELVIEW5_ARB
    = GL2.GL_MODELVIEW5_ARB;

  public static final int GL_MODELVIEW6_ARB
    = GL2.GL_MODELVIEW6_ARB;

  public static final int GL_MODELVIEW7_ARB
    = GL2.GL_MODELVIEW7_ARB;

  public static final int GL_MODELVIEW8_ARB
    = GL2.GL_MODELVIEW8_ARB;

  public static final int GL_MODELVIEW9_ARB
    = GL2.GL_MODELVIEW9_ARB;

  public static final int GL_MODELVIEW10_ARB
    = GL2.GL_MODELVIEW10_ARB;

  public static final int GL_MODELVIEW11_ARB
    = GL2.GL_MODELVIEW11_ARB;

  public static final int GL_MODELVIEW12_ARB
    = GL2.GL_MODELVIEW12_ARB;

  public static final int GL_MODELVIEW13_ARB
    = GL2.GL_MODELVIEW13_ARB;

  public static final int GL_MODELVIEW14_ARB
    = GL2.GL_MODELVIEW14_ARB;

  public static final int GL_MODELVIEW15_ARB
    = GL2.GL_MODELVIEW15_ARB;

  public static final int GL_MODELVIEW16_ARB
    = GL2.GL_MODELVIEW16_ARB;

  public static final int GL_MODELVIEW17_ARB
    = GL2.GL_MODELVIEW17_ARB;

  public static final int GL_MODELVIEW18_ARB
    = GL2.GL_MODELVIEW18_ARB;

  public static final int GL_MODELVIEW19_ARB
    = GL2.GL_MODELVIEW19_ARB;

  public static final int GL_MODELVIEW20_ARB
    = GL2.GL_MODELVIEW20_ARB;

  public static final int GL_MODELVIEW21_ARB
    = GL2.GL_MODELVIEW21_ARB;

  public static final int GL_MODELVIEW22_ARB
    = GL2.GL_MODELVIEW22_ARB;

  public static final int GL_MODELVIEW23_ARB
    = GL2.GL_MODELVIEW23_ARB;

  public static final int GL_MODELVIEW24_ARB
    = GL2.GL_MODELVIEW24_ARB;

  public static final int GL_MODELVIEW25_ARB
    = GL2.GL_MODELVIEW25_ARB;

  public static final int GL_MODELVIEW26_ARB
    = GL2.GL_MODELVIEW26_ARB;

  public static final int GL_MODELVIEW27_ARB
    = GL2.GL_MODELVIEW27_ARB;

  public static final int GL_MODELVIEW28_ARB
    = GL2.GL_MODELVIEW28_ARB;

  public static final int GL_MODELVIEW29_ARB
    = GL2.GL_MODELVIEW29_ARB;

  public static final int GL_MODELVIEW30_ARB
    = GL2.GL_MODELVIEW30_ARB;

  public static final int GL_MODELVIEW31_ARB
    = GL2.GL_MODELVIEW31_ARB;

  public static final int GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB
    = GL2.GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB;

  public static final int GL_CURRENT_MATRIX_INDEX_ARB
    = GL2.GL_CURRENT_MATRIX_INDEX_ARB;

  public static final int GL_TEXTURE_COMPARE_FAIL_VALUE_ARB
    = GL2.GL_TEXTURE_COMPARE_FAIL_VALUE_ARB;

  public static final int GL_COLOR_SUM_ARB
    = GL2.GL_COLOR_SUM_ARB;

  public static final int GL_VERTEX_PROGRAM_ARB
    = GL2.GL_VERTEX_PROGRAM_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB
    = GL2.GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB
    = GL2.GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB
    = GL2.GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB
    = GL2.GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB;

  public static final int GL_CURRENT_VERTEX_ATTRIB_ARB
    = GL2.GL_CURRENT_VERTEX_ATTRIB_ARB;

  public static final int GL_PROGRAM_LENGTH_ARB
    = GL2.GL_PROGRAM_LENGTH_ARB;

  public static final int GL_PROGRAM_STRING_ARB
    = GL2.GL_PROGRAM_STRING_ARB;

  public static final int GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB
    = GL2.GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB;

  public static final int GL_MAX_PROGRAM_MATRICES_ARB
    = GL2.GL_MAX_PROGRAM_MATRICES_ARB;

  public static final int GL_CURRENT_MATRIX_STACK_DEPTH_ARB
    = GL2.GL_CURRENT_MATRIX_STACK_DEPTH_ARB;

  public static final int GL_CURRENT_MATRIX_ARB
    = GL2.GL_CURRENT_MATRIX_ARB;

  public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB
    = GL2.GL_VERTEX_PROGRAM_POINT_SIZE_ARB;

  public static final int GL_VERTEX_PROGRAM_TWO_SIDE_ARB
    = GL2.GL_VERTEX_PROGRAM_TWO_SIDE_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB
    = GL2.GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB;

  public static final int GL_PROGRAM_ERROR_POSITION_ARB
    = GL2.GL_PROGRAM_ERROR_POSITION_ARB;

  public static final int GL_PROGRAM_BINDING_ARB
    = GL2.GL_PROGRAM_BINDING_ARB;

  public static final int GL_MAX_VERTEX_ATTRIBS_ARB
    = GL2.GL_MAX_VERTEX_ATTRIBS_ARB;

  public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB
    = GL2.GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB;

  public static final int GL_PROGRAM_ERROR_STRING_ARB
    = GL2.GL_PROGRAM_ERROR_STRING_ARB;

  public static final int GL_PROGRAM_FORMAT_ASCII_ARB
    = GL2.GL_PROGRAM_FORMAT_ASCII_ARB;

  public static final int GL_PROGRAM_FORMAT_ARB
    = GL2.GL_PROGRAM_FORMAT_ARB;

  public static final int GL_PROGRAM_INSTRUCTIONS_ARB
    = GL2.GL_PROGRAM_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_INSTRUCTIONS_ARB
    = GL2.GL_MAX_PROGRAM_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB
    = GL2.GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_TEMPORARIES_ARB
    = GL2.GL_PROGRAM_TEMPORARIES_ARB;

  public static final int GL_MAX_PROGRAM_TEMPORARIES_ARB
    = GL2.GL_MAX_PROGRAM_TEMPORARIES_ARB;

  public static final int GL_PROGRAM_NATIVE_TEMPORARIES_ARB
    = GL2.GL_PROGRAM_NATIVE_TEMPORARIES_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB;

  public static final int GL_PROGRAM_PARAMETERS_ARB
    = GL2.GL_PROGRAM_PARAMETERS_ARB;

  public static final int GL_MAX_PROGRAM_PARAMETERS_ARB
    = GL2.GL_MAX_PROGRAM_PARAMETERS_ARB;

  public static final int GL_PROGRAM_NATIVE_PARAMETERS_ARB
    = GL2.GL_PROGRAM_NATIVE_PARAMETERS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB;

  public static final int GL_PROGRAM_ATTRIBS_ARB
    = GL2.GL_PROGRAM_ATTRIBS_ARB;

  public static final int GL_MAX_PROGRAM_ATTRIBS_ARB
    = GL2.GL_MAX_PROGRAM_ATTRIBS_ARB;

  public static final int GL_PROGRAM_NATIVE_ATTRIBS_ARB
    = GL2.GL_PROGRAM_NATIVE_ATTRIBS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB;

  public static final int GL_PROGRAM_ADDRESS_REGISTERS_ARB
    = GL2.GL_PROGRAM_ADDRESS_REGISTERS_ARB;

  public static final int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB
    = GL2.GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB;

  public static final int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB
    = GL2.GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB;

  public static final int GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB
    = GL2.GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB;

  public static final int GL_MAX_PROGRAM_ENV_PARAMETERS_ARB
    = GL2.GL_MAX_PROGRAM_ENV_PARAMETERS_ARB;

  public static final int GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB
    = GL2.GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB;

  public static final int GL_TRANSPOSE_CURRENT_MATRIX_ARB
    = GL2.GL_TRANSPOSE_CURRENT_MATRIX_ARB;

  public static final int GL_MATRIX0_ARB
    = GL2.GL_MATRIX0_ARB;

  public static final int GL_MATRIX1_ARB
    = GL2.GL_MATRIX1_ARB;

  public static final int GL_MATRIX2_ARB
    = GL2.GL_MATRIX2_ARB;

  public static final int GL_MATRIX3_ARB
    = GL2.GL_MATRIX3_ARB;

  public static final int GL_MATRIX4_ARB
    = GL2.GL_MATRIX4_ARB;

  public static final int GL_MATRIX5_ARB
    = GL2.GL_MATRIX5_ARB;

  public static final int GL_MATRIX6_ARB
    = GL2.GL_MATRIX6_ARB;

  public static final int GL_MATRIX7_ARB
    = GL2.GL_MATRIX7_ARB;

  public static final int GL_MATRIX8_ARB
    = GL2.GL_MATRIX8_ARB;

  public static final int GL_MATRIX9_ARB
    = GL2.GL_MATRIX9_ARB;

  public static final int GL_MATRIX10_ARB
    = GL2.GL_MATRIX10_ARB;

  public static final int GL_MATRIX11_ARB
    = GL2.GL_MATRIX11_ARB;

  public static final int GL_MATRIX12_ARB
    = GL2.GL_MATRIX12_ARB;

  public static final int GL_MATRIX13_ARB
    = GL2.GL_MATRIX13_ARB;

  public static final int GL_MATRIX14_ARB
    = GL2.GL_MATRIX14_ARB;

  public static final int GL_MATRIX15_ARB
    = GL2.GL_MATRIX15_ARB;

  public static final int GL_MATRIX16_ARB
    = GL2.GL_MATRIX16_ARB;

  public static final int GL_MATRIX17_ARB
    = GL2.GL_MATRIX17_ARB;

  public static final int GL_MATRIX18_ARB
    = GL2.GL_MATRIX18_ARB;

  public static final int GL_MATRIX19_ARB
    = GL2.GL_MATRIX19_ARB;

  public static final int GL_MATRIX20_ARB
    = GL2.GL_MATRIX20_ARB;

  public static final int GL_MATRIX21_ARB
    = GL2.GL_MATRIX21_ARB;

  public static final int GL_MATRIX22_ARB
    = GL2.GL_MATRIX22_ARB;

  public static final int GL_MATRIX23_ARB
    = GL2.GL_MATRIX23_ARB;

  public static final int GL_MATRIX24_ARB
    = GL2.GL_MATRIX24_ARB;

  public static final int GL_MATRIX25_ARB
    = GL2.GL_MATRIX25_ARB;

  public static final int GL_MATRIX26_ARB
    = GL2.GL_MATRIX26_ARB;

  public static final int GL_MATRIX27_ARB
    = GL2.GL_MATRIX27_ARB;

  public static final int GL_MATRIX28_ARB
    = GL2.GL_MATRIX28_ARB;

  public static final int GL_MATRIX29_ARB
    = GL2.GL_MATRIX29_ARB;

  public static final int GL_MATRIX30_ARB
    = GL2.GL_MATRIX30_ARB;

  public static final int GL_MATRIX31_ARB
    = GL2.GL_MATRIX31_ARB;

  public static final int GL_FRAGMENT_PROGRAM_ARB
    = GL2.GL_FRAGMENT_PROGRAM_ARB;

  public static final int GL_PROGRAM_ALU_INSTRUCTIONS_ARB
    = GL2.GL_PROGRAM_ALU_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_TEX_INSTRUCTIONS_ARB
    = GL2.GL_PROGRAM_TEX_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_TEX_INDIRECTIONS_ARB
    = GL2.GL_PROGRAM_TEX_INDIRECTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB
    = GL2.GL_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB
    = GL2.GL_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB;

  public static final int GL_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB
    = GL2.GL_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB;

  public static final int GL_MAX_PROGRAM_ALU_INSTRUCTIONS_ARB
    = GL2.GL_MAX_PROGRAM_ALU_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_TEX_INSTRUCTIONS_ARB
    = GL2.GL_MAX_PROGRAM_TEX_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_TEX_INDIRECTIONS_ARB
    = GL2.GL_MAX_PROGRAM_TEX_INDIRECTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB;

  public static final int GL_MAX_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB
    = GL2.GL_MAX_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB;

  public static final int GL_MAX_TEXTURE_COORDS_ARB
    = GL2.GL_MAX_TEXTURE_COORDS_ARB;

  public static final int GL_MAX_TEXTURE_IMAGE_UNITS_ARB
    = GL2.GL_MAX_TEXTURE_IMAGE_UNITS_ARB;

  public static final int GL_PROGRAM_OBJECT_ARB
    = GL2.GL_PROGRAM_OBJECT_ARB;

  public static final int GL_SHADER_OBJECT_ARB
    = GL2.GL_SHADER_OBJECT_ARB;

  public static final int GL_OBJECT_TYPE_ARB
    = GL2.GL_OBJECT_TYPE_ARB;

  public static final int GL_OBJECT_SUBTYPE_ARB
    = GL2.GL_OBJECT_SUBTYPE_ARB;

  public static final int GL_FLOAT_VEC2_ARB
    = GL2.GL_FLOAT_VEC2_ARB;

  public static final int GL_FLOAT_VEC3_ARB
    = GL2.GL_FLOAT_VEC3_ARB;

  public static final int GL_FLOAT_VEC4_ARB
    = GL2.GL_FLOAT_VEC4_ARB;

  public static final int GL_INT_VEC2_ARB
    = GL2.GL_INT_VEC2_ARB;

  public static final int GL_INT_VEC3_ARB
    = GL2.GL_INT_VEC3_ARB;

  public static final int GL_INT_VEC4_ARB
    = GL2.GL_INT_VEC4_ARB;

  public static final int GL_BOOL_ARB
    = GL2.GL_BOOL_ARB;

  public static final int GL_BOOL_VEC2_ARB
    = GL2.GL_BOOL_VEC2_ARB;

  public static final int GL_BOOL_VEC3_ARB
    = GL2.GL_BOOL_VEC3_ARB;

  public static final int GL_BOOL_VEC4_ARB
    = GL2.GL_BOOL_VEC4_ARB;

  public static final int GL_FLOAT_MAT2_ARB
    = GL2.GL_FLOAT_MAT2_ARB;

  public static final int GL_FLOAT_MAT3_ARB
    = GL2.GL_FLOAT_MAT3_ARB;

  public static final int GL_FLOAT_MAT4_ARB
    = GL2.GL_FLOAT_MAT4_ARB;

  public static final int GL_SAMPLER_1D_ARB
    = GL2.GL_SAMPLER_1D_ARB;

  public static final int GL_SAMPLER_2D_ARB
    = GL2.GL_SAMPLER_2D_ARB;

  public static final int GL_SAMPLER_3D_ARB
    = GL2.GL_SAMPLER_3D_ARB;

  public static final int GL_SAMPLER_CUBE_ARB
    = GL2.GL_SAMPLER_CUBE_ARB;

  public static final int GL_SAMPLER_1D_SHADOW_ARB
    = GL2.GL_SAMPLER_1D_SHADOW_ARB;

  public static final int GL_SAMPLER_2D_SHADOW_ARB
    = GL2.GL_SAMPLER_2D_SHADOW_ARB;

  public static final int GL_OBJECT_DELETE_STATUS_ARB
    = GL2.GL_OBJECT_DELETE_STATUS_ARB;

  public static final int GL_OBJECT_COMPILE_STATUS_ARB
    = GL2.GL_OBJECT_COMPILE_STATUS_ARB;

  public static final int GL_OBJECT_LINK_STATUS_ARB
    = GL2.GL_OBJECT_LINK_STATUS_ARB;

  public static final int GL_OBJECT_VALIDATE_STATUS_ARB
    = GL2.GL_OBJECT_VALIDATE_STATUS_ARB;

  public static final int GL_OBJECT_INFO_LOG_LENGTH_ARB
    = GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB;

  public static final int GL_OBJECT_ATTACHED_OBJECTS_ARB
    = GL2.GL_OBJECT_ATTACHED_OBJECTS_ARB;

  public static final int GL_OBJECT_ACTIVE_UNIFORMS_ARB
    = GL2.GL_OBJECT_ACTIVE_UNIFORMS_ARB;

  public static final int GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB
    = GL2.GL_OBJECT_ACTIVE_UNIFORM_MAX_LENGTH_ARB;

  public static final int GL_OBJECT_SHADER_SOURCE_LENGTH_ARB
    = GL2.GL_OBJECT_SHADER_SOURCE_LENGTH_ARB;

  public static final int GL_SHADING_LANGUAGE_VERSION_ARB
    = GL2.GL_SHADING_LANGUAGE_VERSION_ARB;

  public static final int GL_RGBA_FLOAT_MODE
    = GL2.GL_RGBA_FLOAT_MODE;

  public static final int GL_TEXTURE_LUMINANCE_TYPE
    = GL2.GL_TEXTURE_LUMINANCE_TYPE;

  public static final int GL_TEXTURE_INTENSITY_TYPE
    = GL2.GL_TEXTURE_INTENSITY_TYPE;

  public static final int GL_ALPHA32F
    = GL2.GL_ALPHA32F;

  public static final int GL_INTENSITY32F
    = GL2.GL_INTENSITY32F;

  public static final int GL_LUMINANCE32F
    = GL2.GL_LUMINANCE32F;

  public static final int GL_LUMINANCE_ALPHA32F
    = GL2.GL_LUMINANCE_ALPHA32F;

  public static final int GL_ALPHA16F
    = GL2.GL_ALPHA16F;

  public static final int GL_INTENSITY16F
    = GL2.GL_INTENSITY16F;

  public static final int GL_LUMINANCE16F
    = GL2.GL_LUMINANCE16F;

  public static final int GL_LUMINANCE_ALPHA16F
    = GL2.GL_LUMINANCE_ALPHA16F;

  public static final int GL_INDEX
    = GL2.GL_INDEX;

  public static final int GL_ABGR_EXT
    = GL2.GL_ABGR_EXT;

  public static final int GL_CMYK_EXT
    = GL2.GL_CMYK_EXT;

  public static final int GL_CMYKA_EXT
    = GL2.GL_CMYKA_EXT;

  public static final int GL_PACK_CMYK_HINT_EXT
    = GL2.GL_PACK_CMYK_HINT_EXT;

  public static final int GL_UNPACK_CMYK_HINT_EXT
    = GL2.GL_UNPACK_CMYK_HINT_EXT;

  public static final int GL_VERTEX_DATA_HINT_PGI
    = GL2.GL_VERTEX_DATA_HINT_PGI;

  public static final int GL_VERTEX_CONSISTENT_HINT_PGI
    = GL2.GL_VERTEX_CONSISTENT_HINT_PGI;

  public static final int GL_MATERIAL_SIDE_HINT_PGI
    = GL2.GL_MATERIAL_SIDE_HINT_PGI;

  public static final int GL_MAX_VERTEX_HINT_PGI
    = GL2.GL_MAX_VERTEX_HINT_PGI;

  public static final int GL_COLOR3_BIT_PGI
    = GL2.GL_COLOR3_BIT_PGI;

  public static final int GL_COLOR4_BIT_PGI
    = GL2.GL_COLOR4_BIT_PGI;

  public static final int GL_EDGEFLAG_BIT_PGI
    = GL2.GL_EDGEFLAG_BIT_PGI;

  public static final int GL_INDEX_BIT_PGI
    = GL2.GL_INDEX_BIT_PGI;

  public static final int GL_MAT_AMBIENT_BIT_PGI
    = GL2.GL_MAT_AMBIENT_BIT_PGI;

  public static final int GL_MAT_AMBIENT_AND_DIFFUSE_BIT_PGI
    = GL2.GL_MAT_AMBIENT_AND_DIFFUSE_BIT_PGI;

  public static final int GL_MAT_DIFFUSE_BIT_PGI
    = GL2.GL_MAT_DIFFUSE_BIT_PGI;

  public static final int GL_MAT_EMISSION_BIT_PGI
    = GL2.GL_MAT_EMISSION_BIT_PGI;

  public static final int GL_MAT_COLOR_INDEXES_BIT_PGI
    = GL2.GL_MAT_COLOR_INDEXES_BIT_PGI;

  public static final int GL_MAT_SHININESS_BIT_PGI
    = GL2.GL_MAT_SHININESS_BIT_PGI;

  public static final int GL_MAT_SPECULAR_BIT_PGI
    = GL2.GL_MAT_SPECULAR_BIT_PGI;

  public static final int GL_NORMAL_BIT_PGI
    = GL2.GL_NORMAL_BIT_PGI;

  public static final int GL_TEXCOORD1_BIT_PGI
    = GL2.GL_TEXCOORD1_BIT_PGI;

  public static final int GL_TEXCOORD2_BIT_PGI
    = GL2.GL_TEXCOORD2_BIT_PGI;

  public static final int GL_TEXCOORD3_BIT_PGI
    = GL2.GL_TEXCOORD3_BIT_PGI;

  public static final int GL_VERTEX23_BIT_PGI
    = GL2.GL_VERTEX23_BIT_PGI;

  public static final int GL_VERTEX4_BIT_PGI
    = GL2.GL_VERTEX4_BIT_PGI;

  public static final int GL_PREFER_DOUBLEBUFFER_HINT_PGI
    = GL2.GL_PREFER_DOUBLEBUFFER_HINT_PGI;

  public static final int GL_CONSERVE_MEMORY_HINT_PGI
    = GL2.GL_CONSERVE_MEMORY_HINT_PGI;

  public static final int GL_RECLAIM_MEMORY_HINT_PGI
    = GL2.GL_RECLAIM_MEMORY_HINT_PGI;

  public static final int GL_NATIVE_GRAPHICS_HANDLE_PGI
    = GL2.GL_NATIVE_GRAPHICS_HANDLE_PGI;

  public static final int GL_NATIVE_GRAPHICS_BEGIN_HINT_PGI
    = GL2.GL_NATIVE_GRAPHICS_BEGIN_HINT_PGI;

  public static final int GL_NATIVE_GRAPHICS_END_HINT_PGI
    = GL2.GL_NATIVE_GRAPHICS_END_HINT_PGI;

  public static final int GL_ALWAYS_FAST_HINT_PGI
    = GL2.GL_ALWAYS_FAST_HINT_PGI;

  public static final int GL_ALWAYS_SOFT_HINT_PGI
    = GL2.GL_ALWAYS_SOFT_HINT_PGI;

  public static final int GL_ALLOW_DRAW_OBJ_HINT_PGI
    = GL2.GL_ALLOW_DRAW_OBJ_HINT_PGI;

  public static final int GL_ALLOW_DRAW_WIN_HINT_PGI
    = GL2.GL_ALLOW_DRAW_WIN_HINT_PGI;

  public static final int GL_ALLOW_DRAW_FRG_HINT_PGI
    = GL2.GL_ALLOW_DRAW_FRG_HINT_PGI;

  public static final int GL_ALLOW_DRAW_MEM_HINT_PGI
    = GL2.GL_ALLOW_DRAW_MEM_HINT_PGI;

  public static final int GL_STRICT_DEPTHFUNC_HINT_PGI
    = GL2.GL_STRICT_DEPTHFUNC_HINT_PGI;

  public static final int GL_STRICT_LIGHTING_HINT_PGI
    = GL2.GL_STRICT_LIGHTING_HINT_PGI;

  public static final int GL_STRICT_SCISSOR_HINT_PGI
    = GL2.GL_STRICT_SCISSOR_HINT_PGI;

  public static final int GL_FULL_STIPPLE_HINT_PGI
    = GL2.GL_FULL_STIPPLE_HINT_PGI;

  public static final int GL_CLIP_NEAR_HINT_PGI
    = GL2.GL_CLIP_NEAR_HINT_PGI;

  public static final int GL_CLIP_FAR_HINT_PGI
    = GL2.GL_CLIP_FAR_HINT_PGI;

  public static final int GL_WIDE_LINE_HINT_PGI
    = GL2.GL_WIDE_LINE_HINT_PGI;

  public static final int GL_BACK_NORMALS_HINT_PGI
    = GL2.GL_BACK_NORMALS_HINT_PGI;

  public static final int GL_CLIP_VOLUME_CLIPPING_HINT_EXT
    = GL2.GL_CLIP_VOLUME_CLIPPING_HINT_EXT;

  public static final int GL_INDEX_MATERIAL_EXT
    = GL2.GL_INDEX_MATERIAL_EXT;

  public static final int GL_INDEX_MATERIAL_PARAMETER_EXT
    = GL2.GL_INDEX_MATERIAL_PARAMETER_EXT;

  public static final int GL_INDEX_MATERIAL_FACE_EXT
    = GL2.GL_INDEX_MATERIAL_FACE_EXT;

  public static final int GL_INDEX_TEST_EXT
    = GL2.GL_INDEX_TEST_EXT;

  public static final int GL_INDEX_TEST_FUNC_EXT
    = GL2.GL_INDEX_TEST_FUNC_EXT;

  public static final int GL_INDEX_TEST_REF_EXT
    = GL2.GL_INDEX_TEST_REF_EXT;

  public static final int GL_IUI_V2F_EXT
    = GL2.GL_IUI_V2F_EXT;

  public static final int GL_IUI_V3F_EXT
    = GL2.GL_IUI_V3F_EXT;

  public static final int GL_IUI_N3F_V2F_EXT
    = GL2.GL_IUI_N3F_V2F_EXT;

  public static final int GL_IUI_N3F_V3F_EXT
    = GL2.GL_IUI_N3F_V3F_EXT;

  public static final int GL_T2F_IUI_V2F_EXT
    = GL2.GL_T2F_IUI_V2F_EXT;

  public static final int GL_T2F_IUI_V3F_EXT
    = GL2.GL_T2F_IUI_V3F_EXT;

  public static final int GL_T2F_IUI_N3F_V2F_EXT
    = GL2.GL_T2F_IUI_N3F_V2F_EXT;

  public static final int GL_T2F_IUI_N3F_V3F_EXT
    = GL2.GL_T2F_IUI_N3F_V3F_EXT;

  public static final int GL_ARRAY_ELEMENT_LOCK_FIRST_EXT
    = GL2.GL_ARRAY_ELEMENT_LOCK_FIRST_EXT;

  public static final int GL_ARRAY_ELEMENT_LOCK_COUNT_EXT
    = GL2.GL_ARRAY_ELEMENT_LOCK_COUNT_EXT;

  public static final int GL_CULL_VERTEX_EXT
    = GL2.GL_CULL_VERTEX_EXT;

  public static final int GL_CULL_VERTEX_EYE_POSITION_EXT
    = GL2.GL_CULL_VERTEX_EYE_POSITION_EXT;

  public static final int GL_CULL_VERTEX_OBJECT_POSITION_EXT
    = GL2.GL_CULL_VERTEX_OBJECT_POSITION_EXT;

  public static final int GL_FRAGMENT_MATERIAL_EXT
    = GL2.GL_FRAGMENT_MATERIAL_EXT;

  public static final int GL_FRAGMENT_NORMAL_EXT
    = GL2.GL_FRAGMENT_NORMAL_EXT;

  public static final int GL_FRAGMENT_COLOR_EXT
    = GL2.GL_FRAGMENT_COLOR_EXT;

  public static final int GL_ATTENUATION_EXT
    = GL2.GL_ATTENUATION_EXT;

  public static final int GL_SHADOW_ATTENUATION_EXT
    = GL2.GL_SHADOW_ATTENUATION_EXT;

  public static final int GL_TEXTURE_APPLICATION_MODE_EXT
    = GL2.GL_TEXTURE_APPLICATION_MODE_EXT;

  public static final int GL_TEXTURE_LIGHT_EXT
    = GL2.GL_TEXTURE_LIGHT_EXT;

  public static final int GL_TEXTURE_MATERIAL_FACE_EXT
    = GL2.GL_TEXTURE_MATERIAL_FACE_EXT;

  public static final int GL_TEXTURE_MATERIAL_PARAMETER_EXT
    = GL2.GL_TEXTURE_MATERIAL_PARAMETER_EXT;

  public static final int GL_PIXEL_TRANSFORM_2D_EXT
    = GL2.GL_PIXEL_TRANSFORM_2D_EXT;

  public static final int GL_PIXEL_MAG_FILTER_EXT
    = GL2.GL_PIXEL_MAG_FILTER_EXT;

  public static final int GL_PIXEL_MIN_FILTER_EXT
    = GL2.GL_PIXEL_MIN_FILTER_EXT;

  public static final int GL_PIXEL_CUBIC_WEIGHT_EXT
    = GL2.GL_PIXEL_CUBIC_WEIGHT_EXT;

  public static final int GL_CUBIC_EXT
    = GL2.GL_CUBIC_EXT;

  public static final int GL_AVERAGE_EXT
    = GL2.GL_AVERAGE_EXT;

  public static final int GL_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT
    = GL2.GL_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT;

  public static final int GL_MAX_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT
    = GL2.GL_MAX_PIXEL_TRANSFORM_2D_STACK_DEPTH_EXT;

  public static final int GL_PIXEL_TRANSFORM_2D_MATRIX_EXT
    = GL2.GL_PIXEL_TRANSFORM_2D_MATRIX_EXT;

  public static final int GL_SHARED_TEXTURE_PALETTE_EXT
    = GL2.GL_SHARED_TEXTURE_PALETTE_EXT;

  public static final int GL_PERTURB_EXT
    = GL2.GL_PERTURB_EXT;

  public static final int GL_TEXTURE_NORMAL_EXT
    = GL2.GL_TEXTURE_NORMAL_EXT;

  public static final int GL_SCREEN_COORDINATES_REND
    = GL2.GL_SCREEN_COORDINATES_REND;

  public static final int GL_INVERTED_SCREEN_W_REND
    = GL2.GL_INVERTED_SCREEN_W_REND;

  public static final int GL_LIGHT_MODEL_SPECULAR_VECTOR_APPLE
    = GL2.GL_LIGHT_MODEL_SPECULAR_VECTOR_APPLE;

  public static final int GL_TRANSFORM_HINT_APPLE
    = GL2.GL_TRANSFORM_HINT_APPLE;

  public static final int GL_UNPACK_CONSTANT_DATA_SUNX
    = GL2.GL_UNPACK_CONSTANT_DATA_SUNX;

  public static final int GL_TEXTURE_CONSTANT_DATA_SUNX
    = GL2.GL_TEXTURE_CONSTANT_DATA_SUNX;

  public static final int GL_RED_MIN_CLAMP_INGR
    = GL2.GL_RED_MIN_CLAMP_INGR;

  public static final int GL_GREEN_MIN_CLAMP_INGR
    = GL2.GL_GREEN_MIN_CLAMP_INGR;

  public static final int GL_BLUE_MIN_CLAMP_INGR
    = GL2.GL_BLUE_MIN_CLAMP_INGR;

  public static final int GL_ALPHA_MIN_CLAMP_INGR
    = GL2.GL_ALPHA_MIN_CLAMP_INGR;

  public static final int GL_RED_MAX_CLAMP_INGR
    = GL2.GL_RED_MAX_CLAMP_INGR;

  public static final int GL_GREEN_MAX_CLAMP_INGR
    = GL2.GL_GREEN_MAX_CLAMP_INGR;

  public static final int GL_BLUE_MAX_CLAMP_INGR
    = GL2.GL_BLUE_MAX_CLAMP_INGR;

  public static final int GL_ALPHA_MAX_CLAMP_INGR
    = GL2.GL_ALPHA_MAX_CLAMP_INGR;

  public static final int GL_INTERLACE_READ_INGR
    = GL2.GL_INTERLACE_READ_INGR;

  public static final int GL_422_EXT
    = GL2.GL_422_EXT;

  public static final int GL_422_REV_EXT
    = GL2.GL_422_REV_EXT;

  public static final int GL_422_AVERAGE_EXT
    = GL2.GL_422_AVERAGE_EXT;

  public static final int GL_422_REV_AVERAGE_EXT
    = GL2.GL_422_REV_AVERAGE_EXT;

  public static final int GL_MODELVIEW0_STACK_DEPTH_EXT
    = GL2.GL_MODELVIEW0_STACK_DEPTH_EXT;

  public static final int GL_MODELVIEW1_STACK_DEPTH_EXT
    = GL2.GL_MODELVIEW1_STACK_DEPTH_EXT;

  public static final int GL_MODELVIEW0_MATRIX_EXT
    = GL2.GL_MODELVIEW0_MATRIX_EXT;

  public static final int GL_MODELVIEW1_MATRIX_EXT
    = GL2.GL_MODELVIEW1_MATRIX_EXT;

  public static final int GL_VERTEX_WEIGHTING_EXT
    = GL2.GL_VERTEX_WEIGHTING_EXT;

  public static final int GL_MODELVIEW0_EXT
    = GL2.GL_MODELVIEW0_EXT;

  public static final int GL_MODELVIEW1_EXT
    = GL2.GL_MODELVIEW1_EXT;

  public static final int GL_CURRENT_VERTEX_WEIGHT_EXT
    = GL2.GL_CURRENT_VERTEX_WEIGHT_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_EXT
    = GL2.GL_VERTEX_WEIGHT_ARRAY_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT
    = GL2.GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT
    = GL2.GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT
    = GL2.GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT;

  public static final int GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT
    = GL2.GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT;

  public static final int GL_MAX_SHININESS_NV
    = GL2.GL_MAX_SHININESS_NV;

  public static final int GL_MAX_SPOT_EXPONENT_NV
    = GL2.GL_MAX_SPOT_EXPONENT_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_NV
    = GL2.GL_VERTEX_ARRAY_RANGE_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_NV
    = GL2.GL_VERTEX_ARRAY_RANGE_LENGTH_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_VALID_NV
    = GL2.GL_VERTEX_ARRAY_RANGE_VALID_NV;

  public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV
    = GL2.GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_POINTER_NV
    = GL2.GL_VERTEX_ARRAY_RANGE_POINTER_NV;

  public static final int GL_FOG_DISTANCE_MODE_NV
    = GL2.GL_FOG_DISTANCE_MODE_NV;

  public static final int GL_EYE_RADIAL_NV
    = GL2.GL_EYE_RADIAL_NV;

  public static final int GL_EYE_PLANE_ABSOLUTE_NV
    = GL2.GL_EYE_PLANE_ABSOLUTE_NV;

  public static final int GL_EMBOSS_LIGHT_NV
    = GL2.GL_EMBOSS_LIGHT_NV;

  public static final int GL_EMBOSS_CONSTANT_NV
    = GL2.GL_EMBOSS_CONSTANT_NV;

  public static final int GL_EMBOSS_MAP_NV
    = GL2.GL_EMBOSS_MAP_NV;

  public static final int GL_ALL_COMPLETED_NV
    = GL2.GL_ALL_COMPLETED_NV;

  public static final int GL_FENCE_STATUS_NV
    = GL2.GL_FENCE_STATUS_NV;

  public static final int GL_FENCE_CONDITION_NV
    = GL2.GL_FENCE_CONDITION_NV;

  public static final int GL_EVAL_2D_NV
    = GL2.GL_EVAL_2D_NV;

  public static final int GL_EVAL_TRIANGULAR_2D_NV
    = GL2.GL_EVAL_TRIANGULAR_2D_NV;

  public static final int GL_MAP_TESSELLATION_NV
    = GL2.GL_MAP_TESSELLATION_NV;

  public static final int GL_MAP_ATTRIB_U_ORDER_NV
    = GL2.GL_MAP_ATTRIB_U_ORDER_NV;

  public static final int GL_MAP_ATTRIB_V_ORDER_NV
    = GL2.GL_MAP_ATTRIB_V_ORDER_NV;

  public static final int GL_EVAL_FRACTIONAL_TESSELLATION_NV
    = GL2.GL_EVAL_FRACTIONAL_TESSELLATION_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB0_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB0_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB1_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB1_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB2_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB2_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB3_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB3_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB4_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB4_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB5_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB5_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB6_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB6_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB7_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB7_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB8_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB8_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB9_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB9_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB10_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB10_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB11_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB11_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB12_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB12_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB13_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB13_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB14_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB14_NV;

  public static final int GL_EVAL_VERTEX_ATTRIB15_NV
    = GL2.GL_EVAL_VERTEX_ATTRIB15_NV;

  public static final int GL_MAX_MAP_TESSELLATION_NV
    = GL2.GL_MAX_MAP_TESSELLATION_NV;

  public static final int GL_MAX_RATIONAL_EVAL_ORDER_NV
    = GL2.GL_MAX_RATIONAL_EVAL_ORDER_NV;

  public static final int GL_OFFSET_TEXTURE_RECTANGLE_NV
    = GL2.GL_OFFSET_TEXTURE_RECTANGLE_NV;

  public static final int GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV
    = GL2.GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV
    = GL2.GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV;

  public static final int GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV
    = GL2.GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV;

  public static final int GL_UNSIGNED_INT_S8_S8_8_8_NV
    = GL2.GL_UNSIGNED_INT_S8_S8_8_8_NV;

  public static final int GL_UNSIGNED_INT_8_8_S8_S8_REV_NV
    = GL2.GL_UNSIGNED_INT_8_8_S8_S8_REV_NV;

  public static final int GL_DSDT_MAG_INTENSITY_NV
    = GL2.GL_DSDT_MAG_INTENSITY_NV;

  public static final int GL_SHADER_CONSISTENT_NV
    = GL2.GL_SHADER_CONSISTENT_NV;

  public static final int GL_TEXTURE_SHADER_NV
    = GL2.GL_TEXTURE_SHADER_NV;

  public static final int GL_SHADER_OPERATION_NV
    = GL2.GL_SHADER_OPERATION_NV;

  public static final int GL_CULL_MODES_NV
    = GL2.GL_CULL_MODES_NV;

  public static final int GL_OFFSET_TEXTURE_MATRIX_NV
    = GL2.GL_OFFSET_TEXTURE_MATRIX_NV;

  public static final int GL_OFFSET_TEXTURE_SCALE_NV
    = GL2.GL_OFFSET_TEXTURE_SCALE_NV;

  public static final int GL_OFFSET_TEXTURE_BIAS_NV
    = GL2.GL_OFFSET_TEXTURE_BIAS_NV;

  public static final int GL_OFFSET_TEXTURE_2D_MATRIX_NV
    = GL2.GL_OFFSET_TEXTURE_2D_MATRIX_NV;

  public static final int GL_OFFSET_TEXTURE_2D_SCALE_NV
    = GL2.GL_OFFSET_TEXTURE_2D_SCALE_NV;

  public static final int GL_OFFSET_TEXTURE_2D_BIAS_NV
    = GL2.GL_OFFSET_TEXTURE_2D_BIAS_NV;

  public static final int GL_PREVIOUS_TEXTURE_INPUT_NV
    = GL2.GL_PREVIOUS_TEXTURE_INPUT_NV;

  public static final int GL_CONST_EYE_NV
    = GL2.GL_CONST_EYE_NV;

  public static final int GL_PASS_THROUGH_NV
    = GL2.GL_PASS_THROUGH_NV;

  public static final int GL_CULL_FRAGMENT_NV
    = GL2.GL_CULL_FRAGMENT_NV;

  public static final int GL_OFFSET_TEXTURE_2D_NV
    = GL2.GL_OFFSET_TEXTURE_2D_NV;

  public static final int GL_DEPENDENT_AR_TEXTURE_2D_NV
    = GL2.GL_DEPENDENT_AR_TEXTURE_2D_NV;

  public static final int GL_DEPENDENT_GB_TEXTURE_2D_NV
    = GL2.GL_DEPENDENT_GB_TEXTURE_2D_NV;

  public static final int GL_DOT_PRODUCT_NV
    = GL2.GL_DOT_PRODUCT_NV;

  public static final int GL_DOT_PRODUCT_DEPTH_REPLACE_NV
    = GL2.GL_DOT_PRODUCT_DEPTH_REPLACE_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_2D_NV
    = GL2.GL_DOT_PRODUCT_TEXTURE_2D_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV
    = GL2.GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV
    = GL2.GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV
    = GL2.GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV
    = GL2.GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV;

  public static final int GL_HILO_NV
    = GL2.GL_HILO_NV;

  public static final int GL_DSDT_NV
    = GL2.GL_DSDT_NV;

  public static final int GL_DSDT_MAG_NV
    = GL2.GL_DSDT_MAG_NV;

  public static final int GL_DSDT_MAG_VIB_NV
    = GL2.GL_DSDT_MAG_VIB_NV;

  public static final int GL_HILO16_NV
    = GL2.GL_HILO16_NV;

  public static final int GL_SIGNED_HILO_NV
    = GL2.GL_SIGNED_HILO_NV;

  public static final int GL_SIGNED_HILO16_NV
    = GL2.GL_SIGNED_HILO16_NV;

  public static final int GL_SIGNED_RGBA_NV
    = GL2.GL_SIGNED_RGBA_NV;

  public static final int GL_SIGNED_RGBA8_NV
    = GL2.GL_SIGNED_RGBA8_NV;

  public static final int GL_SIGNED_RGB_NV
    = GL2.GL_SIGNED_RGB_NV;

  public static final int GL_SIGNED_RGB8_NV
    = GL2.GL_SIGNED_RGB8_NV;

  public static final int GL_SIGNED_LUMINANCE_NV
    = GL2.GL_SIGNED_LUMINANCE_NV;

  public static final int GL_SIGNED_LUMINANCE8_NV
    = GL2.GL_SIGNED_LUMINANCE8_NV;

  public static final int GL_SIGNED_LUMINANCE_ALPHA_NV
    = GL2.GL_SIGNED_LUMINANCE_ALPHA_NV;

  public static final int GL_SIGNED_LUMINANCE8_ALPHA8_NV
    = GL2.GL_SIGNED_LUMINANCE8_ALPHA8_NV;

  public static final int GL_SIGNED_ALPHA_NV
    = GL2.GL_SIGNED_ALPHA_NV;

  public static final int GL_SIGNED_ALPHA8_NV
    = GL2.GL_SIGNED_ALPHA8_NV;

  public static final int GL_SIGNED_INTENSITY_NV
    = GL2.GL_SIGNED_INTENSITY_NV;

  public static final int GL_SIGNED_INTENSITY8_NV
    = GL2.GL_SIGNED_INTENSITY8_NV;

  public static final int GL_DSDT8_NV
    = GL2.GL_DSDT8_NV;

  public static final int GL_DSDT8_MAG8_NV
    = GL2.GL_DSDT8_MAG8_NV;

  public static final int GL_DSDT8_MAG8_INTENSITY8_NV
    = GL2.GL_DSDT8_MAG8_INTENSITY8_NV;

  public static final int GL_SIGNED_RGB_UNSIGNED_ALPHA_NV
    = GL2.GL_SIGNED_RGB_UNSIGNED_ALPHA_NV;

  public static final int GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV
    = GL2.GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV;

  public static final int GL_HI_SCALE_NV
    = GL2.GL_HI_SCALE_NV;

  public static final int GL_LO_SCALE_NV
    = GL2.GL_LO_SCALE_NV;

  public static final int GL_DS_SCALE_NV
    = GL2.GL_DS_SCALE_NV;

  public static final int GL_DT_SCALE_NV
    = GL2.GL_DT_SCALE_NV;

  public static final int GL_MAGNITUDE_SCALE_NV
    = GL2.GL_MAGNITUDE_SCALE_NV;

  public static final int GL_VIBRANCE_SCALE_NV
    = GL2.GL_VIBRANCE_SCALE_NV;

  public static final int GL_HI_BIAS_NV
    = GL2.GL_HI_BIAS_NV;

  public static final int GL_LO_BIAS_NV
    = GL2.GL_LO_BIAS_NV;

  public static final int GL_DS_BIAS_NV
    = GL2.GL_DS_BIAS_NV;

  public static final int GL_DT_BIAS_NV
    = GL2.GL_DT_BIAS_NV;

  public static final int GL_MAGNITUDE_BIAS_NV
    = GL2.GL_MAGNITUDE_BIAS_NV;

  public static final int GL_VIBRANCE_BIAS_NV
    = GL2.GL_VIBRANCE_BIAS_NV;

  public static final int GL_TEXTURE_BORDER_VALUES_NV
    = GL2.GL_TEXTURE_BORDER_VALUES_NV;

  public static final int GL_TEXTURE_HI_SIZE_NV
    = GL2.GL_TEXTURE_HI_SIZE_NV;

  public static final int GL_TEXTURE_LO_SIZE_NV
    = GL2.GL_TEXTURE_LO_SIZE_NV;

  public static final int GL_TEXTURE_DS_SIZE_NV
    = GL2.GL_TEXTURE_DS_SIZE_NV;

  public static final int GL_TEXTURE_DT_SIZE_NV
    = GL2.GL_TEXTURE_DT_SIZE_NV;

  public static final int GL_TEXTURE_MAG_SIZE_NV
    = GL2.GL_TEXTURE_MAG_SIZE_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_3D_NV
    = GL2.GL_DOT_PRODUCT_TEXTURE_3D_NV;

  public static final int GL_VERTEX_ARRAY_RANGE_WITHOUT_FLUSH_NV
    = GL2.GL_VERTEX_ARRAY_RANGE_WITHOUT_FLUSH_NV;

  public static final int GL_INTERLACE_OML
    = GL2.GL_INTERLACE_OML;

  public static final int GL_INTERLACE_READ_OML
    = GL2.GL_INTERLACE_READ_OML;

  public static final int GL_FORMAT_SUBSAMPLE_24_24_OML
    = GL2.GL_FORMAT_SUBSAMPLE_24_24_OML;

  public static final int GL_FORMAT_SUBSAMPLE_244_244_OML
    = GL2.GL_FORMAT_SUBSAMPLE_244_244_OML;

  public static final int GL_PACK_RESAMPLE_OML
    = GL2.GL_PACK_RESAMPLE_OML;

  public static final int GL_UNPACK_RESAMPLE_OML
    = GL2.GL_UNPACK_RESAMPLE_OML;

  public static final int GL_RESAMPLE_REPLICATE_OML
    = GL2.GL_RESAMPLE_REPLICATE_OML;

  public static final int GL_RESAMPLE_ZERO_FILL_OML
    = GL2.GL_RESAMPLE_ZERO_FILL_OML;

  public static final int GL_RESAMPLE_AVERAGE_OML
    = GL2.GL_RESAMPLE_AVERAGE_OML;

  public static final int GL_RESAMPLE_DECIMATE_OML
    = GL2.GL_RESAMPLE_DECIMATE_OML;

  public static final int GL_DEPTH_STENCIL_TO_RGBA_NV
    = GL2.GL_DEPTH_STENCIL_TO_RGBA_NV;

  public static final int GL_DEPTH_STENCIL_TO_BGRA_NV
    = GL2.GL_DEPTH_STENCIL_TO_BGRA_NV;

  public static final int GL_PN_TRIANGLES_ATI
    = GL2.GL_PN_TRIANGLES_ATI;

  public static final int GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI
    = GL2.GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI;

  public static final int GL_PN_TRIANGLES_POINT_MODE_ATI
    = GL2.GL_PN_TRIANGLES_POINT_MODE_ATI;

  public static final int GL_PN_TRIANGLES_NORMAL_MODE_ATI
    = GL2.GL_PN_TRIANGLES_NORMAL_MODE_ATI;

  public static final int GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI
    = GL2.GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI;

  public static final int GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI
    = GL2.GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI;

  public static final int GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI
    = GL2.GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI;

  public static final int GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI
    = GL2.GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI;

  public static final int GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI
    = GL2.GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI;

  public static final int GL_VERTEX_SHADER_EXT
    = GL2.GL_VERTEX_SHADER_EXT;

  public static final int GL_VERTEX_SHADER_BINDING_EXT
    = GL2.GL_VERTEX_SHADER_BINDING_EXT;

  public static final int GL_OP_INDEX_EXT
    = GL2.GL_OP_INDEX_EXT;

  public static final int GL_OP_NEGATE_EXT
    = GL2.GL_OP_NEGATE_EXT;

  public static final int GL_OP_DOT3_EXT
    = GL2.GL_OP_DOT3_EXT;

  public static final int GL_OP_DOT4_EXT
    = GL2.GL_OP_DOT4_EXT;

  public static final int GL_OP_MUL_EXT
    = GL2.GL_OP_MUL_EXT;

  public static final int GL_OP_ADD_EXT
    = GL2.GL_OP_ADD_EXT;

  public static final int GL_OP_MADD_EXT
    = GL2.GL_OP_MADD_EXT;

  public static final int GL_OP_FRAC_EXT
    = GL2.GL_OP_FRAC_EXT;

  public static final int GL_OP_MAX_EXT
    = GL2.GL_OP_MAX_EXT;

  public static final int GL_OP_MIN_EXT
    = GL2.GL_OP_MIN_EXT;

  public static final int GL_OP_SET_GE_EXT
    = GL2.GL_OP_SET_GE_EXT;

  public static final int GL_OP_SET_LT_EXT
    = GL2.GL_OP_SET_LT_EXT;

  public static final int GL_OP_CLAMP_EXT
    = GL2.GL_OP_CLAMP_EXT;

  public static final int GL_OP_FLOOR_EXT
    = GL2.GL_OP_FLOOR_EXT;

  public static final int GL_OP_ROUND_EXT
    = GL2.GL_OP_ROUND_EXT;

  public static final int GL_OP_EXP_BASE_2_EXT
    = GL2.GL_OP_EXP_BASE_2_EXT;

  public static final int GL_OP_LOG_BASE_2_EXT
    = GL2.GL_OP_LOG_BASE_2_EXT;

  public static final int GL_OP_POWER_EXT
    = GL2.GL_OP_POWER_EXT;

  public static final int GL_OP_RECIP_EXT
    = GL2.GL_OP_RECIP_EXT;

  public static final int GL_OP_RECIP_SQRT_EXT
    = GL2.GL_OP_RECIP_SQRT_EXT;

  public static final int GL_OP_SUB_EXT
    = GL2.GL_OP_SUB_EXT;

  public static final int GL_OP_CROSS_PRODUCT_EXT
    = GL2.GL_OP_CROSS_PRODUCT_EXT;

  public static final int GL_OP_MULTIPLY_MATRIX_EXT
    = GL2.GL_OP_MULTIPLY_MATRIX_EXT;

  public static final int GL_OP_MOV_EXT
    = GL2.GL_OP_MOV_EXT;

  public static final int GL_OUTPUT_VERTEX_EXT
    = GL2.GL_OUTPUT_VERTEX_EXT;

  public static final int GL_OUTPUT_COLOR0_EXT
    = GL2.GL_OUTPUT_COLOR0_EXT;

  public static final int GL_OUTPUT_COLOR1_EXT
    = GL2.GL_OUTPUT_COLOR1_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD0_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD0_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD1_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD1_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD2_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD2_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD3_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD3_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD4_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD4_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD5_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD5_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD6_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD6_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD7_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD7_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD8_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD8_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD9_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD9_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD10_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD10_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD11_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD11_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD12_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD12_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD13_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD13_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD14_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD14_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD15_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD15_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD16_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD16_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD17_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD17_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD18_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD18_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD19_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD19_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD20_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD20_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD21_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD21_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD22_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD22_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD23_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD23_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD24_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD24_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD25_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD25_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD26_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD26_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD27_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD27_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD28_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD28_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD29_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD29_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD30_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD30_EXT;

  public static final int GL_OUTPUT_TEXTURE_COORD31_EXT
    = GL2.GL_OUTPUT_TEXTURE_COORD31_EXT;

  public static final int GL_OUTPUT_FOG_EXT
    = GL2.GL_OUTPUT_FOG_EXT;

  public static final int GL_SCALAR_EXT
    = GL2.GL_SCALAR_EXT;

  public static final int GL_VECTOR_EXT
    = GL2.GL_VECTOR_EXT;

  public static final int GL_MATRIX_EXT
    = GL2.GL_MATRIX_EXT;

  public static final int GL_VARIANT_EXT
    = GL2.GL_VARIANT_EXT;

  public static final int GL_INVARIANT_EXT
    = GL2.GL_INVARIANT_EXT;

  public static final int GL_LOCAL_CONSTANT_EXT
    = GL2.GL_LOCAL_CONSTANT_EXT;

  public static final int GL_LOCAL_EXT
    = GL2.GL_LOCAL_EXT;

  public static final int GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT
    = GL2.GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_VARIANTS_EXT
    = GL2.GL_MAX_VERTEX_SHADER_VARIANTS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_INVARIANTS_EXT
    = GL2.GL_MAX_VERTEX_SHADER_INVARIANTS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT
    = GL2.GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT;

  public static final int GL_MAX_VERTEX_SHADER_LOCALS_EXT
    = GL2.GL_MAX_VERTEX_SHADER_LOCALS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT
    = GL2.GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT
    = GL2.GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT
    = GL2.GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT
    = GL2.GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT;

  public static final int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT
    = GL2.GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT;

  public static final int GL_VERTEX_SHADER_INSTRUCTIONS_EXT
    = GL2.GL_VERTEX_SHADER_INSTRUCTIONS_EXT;

  public static final int GL_VERTEX_SHADER_VARIANTS_EXT
    = GL2.GL_VERTEX_SHADER_VARIANTS_EXT;

  public static final int GL_VERTEX_SHADER_INVARIANTS_EXT
    = GL2.GL_VERTEX_SHADER_INVARIANTS_EXT;

  public static final int GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT
    = GL2.GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT;

  public static final int GL_VERTEX_SHADER_LOCALS_EXT
    = GL2.GL_VERTEX_SHADER_LOCALS_EXT;

  public static final int GL_VERTEX_SHADER_OPTIMIZED_EXT
    = GL2.GL_VERTEX_SHADER_OPTIMIZED_EXT;

  public static final int GL_X_EXT
    = GL2.GL_X_EXT;

  public static final int GL_Y_EXT
    = GL2.GL_Y_EXT;

  public static final int GL_Z_EXT
    = GL2.GL_Z_EXT;

  public static final int GL_W_EXT
    = GL2.GL_W_EXT;

  public static final int GL_NEGATIVE_X_EXT
    = GL2.GL_NEGATIVE_X_EXT;

  public static final int GL_NEGATIVE_Y_EXT
    = GL2.GL_NEGATIVE_Y_EXT;

  public static final int GL_NEGATIVE_Z_EXT
    = GL2.GL_NEGATIVE_Z_EXT;

  public static final int GL_NEGATIVE_W_EXT
    = GL2.GL_NEGATIVE_W_EXT;

  public static final int GL_ZERO_EXT
    = GL2.GL_ZERO_EXT;

  public static final int GL_ONE_EXT
    = GL2.GL_ONE_EXT;

  public static final int GL_NEGATIVE_ONE_EXT
    = GL2.GL_NEGATIVE_ONE_EXT;

  public static final int GL_NORMALIZED_RANGE_EXT
    = GL2.GL_NORMALIZED_RANGE_EXT;

  public static final int GL_FULL_RANGE_EXT
    = GL2.GL_FULL_RANGE_EXT;

  public static final int GL_CURRENT_VERTEX_EXT
    = GL2.GL_CURRENT_VERTEX_EXT;

  public static final int GL_MVP_MATRIX_EXT
    = GL2.GL_MVP_MATRIX_EXT;

  public static final int GL_VARIANT_VALUE_EXT
    = GL2.GL_VARIANT_VALUE_EXT;

  public static final int GL_VARIANT_DATATYPE_EXT
    = GL2.GL_VARIANT_DATATYPE_EXT;

  public static final int GL_VARIANT_ARRAY_STRIDE_EXT
    = GL2.GL_VARIANT_ARRAY_STRIDE_EXT;

  public static final int GL_VARIANT_ARRAY_TYPE_EXT
    = GL2.GL_VARIANT_ARRAY_TYPE_EXT;

  public static final int GL_VARIANT_ARRAY_EXT
    = GL2.GL_VARIANT_ARRAY_EXT;

  public static final int GL_VARIANT_ARRAY_POINTER_EXT
    = GL2.GL_VARIANT_ARRAY_POINTER_EXT;

  public static final int GL_INVARIANT_VALUE_EXT
    = GL2.GL_INVARIANT_VALUE_EXT;

  public static final int GL_INVARIANT_DATATYPE_EXT
    = GL2.GL_INVARIANT_DATATYPE_EXT;

  public static final int GL_LOCAL_CONSTANT_VALUE_EXT
    = GL2.GL_LOCAL_CONSTANT_VALUE_EXT;

  public static final int GL_LOCAL_CONSTANT_DATATYPE_EXT
    = GL2.GL_LOCAL_CONSTANT_DATATYPE_EXT;

  public static final int GL_MULTISAMPLE_FILTER_HINT_NV
    = GL2.GL_MULTISAMPLE_FILTER_HINT_NV;

  public static final int GL_DEPTH_CLAMP_NV
    = GL2.GL_DEPTH_CLAMP_NV;

  public static final int GL_PIXEL_COUNTER_BITS_NV
    = GL2.GL_PIXEL_COUNTER_BITS_NV;

  public static final int GL_CURRENT_OCCLUSION_QUERY_ID_NV
    = GL2.GL_CURRENT_OCCLUSION_QUERY_ID_NV;

  public static final int GL_PIXEL_COUNT_NV
    = GL2.GL_PIXEL_COUNT_NV;

  public static final int GL_PIXEL_COUNT_AVAILABLE_NV
    = GL2.GL_PIXEL_COUNT_AVAILABLE_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_2D_NV
    = GL2.GL_OFFSET_PROJECTIVE_TEXTURE_2D_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_2D_SCALE_NV
    = GL2.GL_OFFSET_PROJECTIVE_TEXTURE_2D_SCALE_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_NV
    = GL2.GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_NV;

  public static final int GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_SCALE_NV
    = GL2.GL_OFFSET_PROJECTIVE_TEXTURE_RECTANGLE_SCALE_NV;

  public static final int GL_OFFSET_HILO_TEXTURE_2D_NV
    = GL2.GL_OFFSET_HILO_TEXTURE_2D_NV;

  public static final int GL_OFFSET_HILO_TEXTURE_RECTANGLE_NV
    = GL2.GL_OFFSET_HILO_TEXTURE_RECTANGLE_NV;

  public static final int GL_OFFSET_HILO_PROJECTIVE_TEXTURE_2D_NV
    = GL2.GL_OFFSET_HILO_PROJECTIVE_TEXTURE_2D_NV;

  public static final int GL_OFFSET_HILO_PROJECTIVE_TEXTURE_RECTANGLE_NV
    = GL2.GL_OFFSET_HILO_PROJECTIVE_TEXTURE_RECTANGLE_NV;

  public static final int GL_DEPENDENT_HILO_TEXTURE_2D_NV
    = GL2.GL_DEPENDENT_HILO_TEXTURE_2D_NV;

  public static final int GL_DEPENDENT_RGB_TEXTURE_3D_NV
    = GL2.GL_DEPENDENT_RGB_TEXTURE_3D_NV;

  public static final int GL_DEPENDENT_RGB_TEXTURE_CUBE_MAP_NV
    = GL2.GL_DEPENDENT_RGB_TEXTURE_CUBE_MAP_NV;

  public static final int GL_DOT_PRODUCT_PASS_THROUGH_NV
    = GL2.GL_DOT_PRODUCT_PASS_THROUGH_NV;

  public static final int GL_DOT_PRODUCT_TEXTURE_1D_NV
    = GL2.GL_DOT_PRODUCT_TEXTURE_1D_NV;

  public static final int GL_DOT_PRODUCT_AFFINE_DEPTH_REPLACE_NV
    = GL2.GL_DOT_PRODUCT_AFFINE_DEPTH_REPLACE_NV;

  public static final int GL_HILO8_NV
    = GL2.GL_HILO8_NV;

  public static final int GL_SIGNED_HILO8_NV
    = GL2.GL_SIGNED_HILO8_NV;

  public static final int GL_FORCE_BLUE_TO_ONE_NV
    = GL2.GL_FORCE_BLUE_TO_ONE_NV;

  public static final int GL_STENCIL_TEST_TWO_SIDE_EXT
    = GL2.GL_STENCIL_TEST_TWO_SIDE_EXT;

  public static final int GL_ACTIVE_STENCIL_FACE_EXT
    = GL2.GL_ACTIVE_STENCIL_FACE_EXT;

  public static final int GL_UNPACK_CLIENT_STORAGE_APPLE
    = GL2.GL_UNPACK_CLIENT_STORAGE_APPLE;

  public static final int GL_DRAW_PIXELS_APPLE
    = GL2.GL_DRAW_PIXELS_APPLE;

  public static final int GL_FENCE_APPLE
    = GL2.GL_FENCE_APPLE;

  public static final int GL_VERTEX_ARRAY_RANGE_APPLE
    = GL2.GL_VERTEX_ARRAY_RANGE_APPLE;

  public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_APPLE
    = GL2.GL_VERTEX_ARRAY_RANGE_LENGTH_APPLE;

  public static final int GL_VERTEX_ARRAY_STORAGE_HINT_APPLE
    = GL2.GL_VERTEX_ARRAY_STORAGE_HINT_APPLE;

  public static final int GL_VERTEX_ARRAY_RANGE_POINTER_APPLE
    = GL2.GL_VERTEX_ARRAY_RANGE_POINTER_APPLE;

  public static final int GL_STORAGE_CLIENT_APPLE
    = GL2.GL_STORAGE_CLIENT_APPLE;

  public static final int GL_STORAGE_CACHED_APPLE
    = GL2.GL_STORAGE_CACHED_APPLE;

  public static final int GL_STORAGE_SHARED_APPLE
    = GL2.GL_STORAGE_SHARED_APPLE;

  public static final int GL_YCBCR_422_APPLE
    = GL2.GL_YCBCR_422_APPLE;

  public static final int GL_UNSIGNED_SHORT_8_8_APPLE
    = GL2.GL_UNSIGNED_SHORT_8_8_APPLE;

  public static final int GL_UNSIGNED_SHORT_8_8_REV_APPLE
    = GL2.GL_UNSIGNED_SHORT_8_8_REV_APPLE;

  public static final int GL_RGB_S3TC
    = GL2.GL_RGB_S3TC;

  public static final int GL_RGB4_S3TC
    = GL2.GL_RGB4_S3TC;

  public static final int GL_RGBA_S3TC
    = GL2.GL_RGBA_S3TC;

  public static final int GL_RGBA4_S3TC
    = GL2.GL_RGBA4_S3TC;

  public static final int GL_MAX_DRAW_BUFFERS_ATI
    = GL2.GL_MAX_DRAW_BUFFERS_ATI;

  public static final int GL_DRAW_BUFFER0_ATI
    = GL2.GL_DRAW_BUFFER0_ATI;

  public static final int GL_DRAW_BUFFER1_ATI
    = GL2.GL_DRAW_BUFFER1_ATI;

  public static final int GL_DRAW_BUFFER2_ATI
    = GL2.GL_DRAW_BUFFER2_ATI;

  public static final int GL_DRAW_BUFFER3_ATI
    = GL2.GL_DRAW_BUFFER3_ATI;

  public static final int GL_DRAW_BUFFER4_ATI
    = GL2.GL_DRAW_BUFFER4_ATI;

  public static final int GL_DRAW_BUFFER5_ATI
    = GL2.GL_DRAW_BUFFER5_ATI;

  public static final int GL_DRAW_BUFFER6_ATI
    = GL2.GL_DRAW_BUFFER6_ATI;

  public static final int GL_DRAW_BUFFER7_ATI
    = GL2.GL_DRAW_BUFFER7_ATI;

  public static final int GL_DRAW_BUFFER8_ATI
    = GL2.GL_DRAW_BUFFER8_ATI;

  public static final int GL_DRAW_BUFFER9_ATI
    = GL2.GL_DRAW_BUFFER9_ATI;

  public static final int GL_DRAW_BUFFER10_ATI
    = GL2.GL_DRAW_BUFFER10_ATI;

  public static final int GL_DRAW_BUFFER11_ATI
    = GL2.GL_DRAW_BUFFER11_ATI;

  public static final int GL_DRAW_BUFFER12_ATI
    = GL2.GL_DRAW_BUFFER12_ATI;

  public static final int GL_DRAW_BUFFER13_ATI
    = GL2.GL_DRAW_BUFFER13_ATI;

  public static final int GL_DRAW_BUFFER14_ATI
    = GL2.GL_DRAW_BUFFER14_ATI;

  public static final int GL_DRAW_BUFFER15_ATI
    = GL2.GL_DRAW_BUFFER15_ATI;

  public static final int GL_TYPE_RGBA_FLOAT_ATI
    = GL2.GL_TYPE_RGBA_FLOAT_ATI;

  public static final int GL_COLOR_CLEAR_UNCLAMPED_VALUE_ATI
    = GL2.GL_COLOR_CLEAR_UNCLAMPED_VALUE_ATI;

  public static final int GL_RGBA_FLOAT32_ATI
    = GL2.GL_RGBA_FLOAT32_ATI;

  public static final int GL_RGB_FLOAT32_ATI
    = GL2.GL_RGB_FLOAT32_ATI;

  public static final int GL_ALPHA_FLOAT32_ATI
    = GL2.GL_ALPHA_FLOAT32_ATI;

  public static final int GL_INTENSITY_FLOAT32_ATI
    = GL2.GL_INTENSITY_FLOAT32_ATI;

  public static final int GL_LUMINANCE_FLOAT32_ATI
    = GL2.GL_LUMINANCE_FLOAT32_ATI;

  public static final int GL_LUMINANCE_ALPHA_FLOAT32_ATI
    = GL2.GL_LUMINANCE_ALPHA_FLOAT32_ATI;

  public static final int GL_RGBA_FLOAT16_ATI
    = GL2.GL_RGBA_FLOAT16_ATI;

  public static final int GL_RGB_FLOAT16_ATI
    = GL2.GL_RGB_FLOAT16_ATI;

  public static final int GL_ALPHA_FLOAT16_ATI
    = GL2.GL_ALPHA_FLOAT16_ATI;

  public static final int GL_INTENSITY_FLOAT16_ATI
    = GL2.GL_INTENSITY_FLOAT16_ATI;

  public static final int GL_LUMINANCE_FLOAT16_ATI
    = GL2.GL_LUMINANCE_FLOAT16_ATI;

  public static final int GL_LUMINANCE_ALPHA_FLOAT16_ATI
    = GL2.GL_LUMINANCE_ALPHA_FLOAT16_ATI;

  public static final int GL_FLOAT_R_NV
    = GL2.GL_FLOAT_R_NV;

  public static final int GL_FLOAT_RG_NV
    = GL2.GL_FLOAT_RG_NV;

  public static final int GL_FLOAT_RGB_NV
    = GL2.GL_FLOAT_RGB_NV;

  public static final int GL_FLOAT_RGBA_NV
    = GL2.GL_FLOAT_RGBA_NV;

  public static final int GL_FLOAT_R16_NV
    = GL2.GL_FLOAT_R16_NV;

  public static final int GL_FLOAT_R32_NV
    = GL2.GL_FLOAT_R32_NV;

  public static final int GL_FLOAT_RG16_NV
    = GL2.GL_FLOAT_RG16_NV;

  public static final int GL_FLOAT_RG32_NV
    = GL2.GL_FLOAT_RG32_NV;

  public static final int GL_FLOAT_RGB16_NV
    = GL2.GL_FLOAT_RGB16_NV;

  public static final int GL_FLOAT_RGB32_NV
    = GL2.GL_FLOAT_RGB32_NV;

  public static final int GL_FLOAT_RGBA16_NV
    = GL2.GL_FLOAT_RGBA16_NV;

  public static final int GL_FLOAT_RGBA32_NV
    = GL2.GL_FLOAT_RGBA32_NV;

  public static final int GL_TEXTURE_FLOAT_COMPONENTS_NV
    = GL2.GL_TEXTURE_FLOAT_COMPONENTS_NV;

  public static final int GL_FLOAT_CLEAR_COLOR_VALUE_NV
    = GL2.GL_FLOAT_CLEAR_COLOR_VALUE_NV;

  public static final int GL_FLOAT_RGBA_MODE_NV
    = GL2.GL_FLOAT_RGBA_MODE_NV;

  public static final int GL_WRITE_PIXEL_DATA_RANGE_NV
    = GL2.GL_WRITE_PIXEL_DATA_RANGE_NV;

  public static final int GL_READ_PIXEL_DATA_RANGE_NV
    = GL2.GL_READ_PIXEL_DATA_RANGE_NV;

  public static final int GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV
    = GL2.GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV;

  public static final int GL_READ_PIXEL_DATA_RANGE_LENGTH_NV
    = GL2.GL_READ_PIXEL_DATA_RANGE_LENGTH_NV;

  public static final int GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV
    = GL2.GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV;

  public static final int GL_READ_PIXEL_DATA_RANGE_POINTER_NV
    = GL2.GL_READ_PIXEL_DATA_RANGE_POINTER_NV;

  public static final int GL_PRIMITIVE_RESTART_NV
    = GL2.GL_PRIMITIVE_RESTART_NV;

  public static final int GL_PRIMITIVE_RESTART_INDEX_NV
    = GL2.GL_PRIMITIVE_RESTART_INDEX_NV;

  public static final int GL_TEXTURE_UNSIGNED_REMAP_MODE_NV
    = GL2.GL_TEXTURE_UNSIGNED_REMAP_MODE_NV;

  public static final int GL_DEPTH_BOUNDS_TEST_EXT
    = GL2.GL_DEPTH_BOUNDS_TEST_EXT;

  public static final int GL_DEPTH_BOUNDS_EXT
    = GL2.GL_DEPTH_BOUNDS_EXT;

  public static final int GL_MIRROR_CLAMP_EXT
    = GL2.GL_MIRROR_CLAMP_EXT;

  public static final int GL_MIRROR_CLAMP_TO_EDGE_EXT
    = GL2.GL_MIRROR_CLAMP_TO_EDGE_EXT;

  public static final int GL_MIRROR_CLAMP_TO_BORDER_EXT
    = GL2.GL_MIRROR_CLAMP_TO_BORDER_EXT;

  public static final int GL_PIXEL_PACK_BUFFER_EXT
    = GL2.GL_PIXEL_PACK_BUFFER_EXT;

  public static final int GL_PIXEL_UNPACK_BUFFER_EXT
    = GL2.GL_PIXEL_UNPACK_BUFFER_EXT;

  public static final int GL_PIXEL_PACK_BUFFER_BINDING_EXT
    = GL2.GL_PIXEL_PACK_BUFFER_BINDING_EXT;

  public static final int GL_PIXEL_UNPACK_BUFFER_BINDING_EXT
    = GL2.GL_PIXEL_UNPACK_BUFFER_BINDING_EXT;

  public static final int GL_STENCIL_TAG_BITS_EXT
    = GL2.GL_STENCIL_TAG_BITS_EXT;

  public static final int GL_STENCIL_CLEAR_TAG_VALUE_EXT
    = GL2.GL_STENCIL_CLEAR_TAG_VALUE_EXT;

  public static final int GL_TIME_ELAPSED_EXT
    = GL2.GL_TIME_ELAPSED_EXT;

  public static final int GL_BUFFER_SERIALIZED_MODIFY
    = GL2.GL_BUFFER_SERIALIZED_MODIFY;

  public static final int GL_BUFFER_FLUSHING_UNMAP
    = GL2.GL_BUFFER_FLUSHING_UNMAP;

  public static final int GL_MIN_PROGRAM_TEXEL_OFFSET_NV
    = GL2.GL_MIN_PROGRAM_TEXEL_OFFSET_NV;

  public static final int GL_MAX_PROGRAM_TEXEL_OFFSET_NV
    = GL2.GL_MAX_PROGRAM_TEXEL_OFFSET_NV;

  public static final int GL_PROGRAM_ATTRIB_COMPONENTS_NV
    = GL2.GL_PROGRAM_ATTRIB_COMPONENTS_NV;

  public static final int GL_PROGRAM_RESULT_COMPONENTS_NV
    = GL2.GL_PROGRAM_RESULT_COMPONENTS_NV;

  public static final int GL_MAX_PROGRAM_ATTRIB_COMPONENTS_NV
    = GL2.GL_MAX_PROGRAM_ATTRIB_COMPONENTS_NV;

  public static final int GL_MAX_PROGRAM_RESULT_COMPONENTS_NV
    = GL2.GL_MAX_PROGRAM_RESULT_COMPONENTS_NV;

  public static final int GL_MAX_PROGRAM_GENERIC_ATTRIBS_NV
    = GL2.GL_MAX_PROGRAM_GENERIC_ATTRIBS_NV;

  public static final int GL_MAX_PROGRAM_GENERIC_RESULTS_NV
    = GL2.GL_MAX_PROGRAM_GENERIC_RESULTS_NV;

  public static final int GL_LINES_ADJACENCY_EXT
    = GL2.GL_LINES_ADJACENCY_EXT;

  public static final int GL_LINE_STRIP_ADJACENCY_EXT
    = GL2.GL_LINE_STRIP_ADJACENCY_EXT;

  public static final int GL_TRIANGLES_ADJACENCY_EXT
    = GL2.GL_TRIANGLES_ADJACENCY_EXT;

  public static final int GL_TRIANGLE_STRIP_ADJACENCY_EXT
    = GL2.GL_TRIANGLE_STRIP_ADJACENCY_EXT;

  public static final int GL_GEOMETRY_PROGRAM_NV
    = GL2.GL_GEOMETRY_PROGRAM_NV;

  public static final int GL_MAX_PROGRAM_OUTPUT_VERTICES_NV
    = GL2.GL_MAX_PROGRAM_OUTPUT_VERTICES_NV;

  public static final int GL_MAX_PROGRAM_TOTAL_OUTPUT_COMPONENTS_NV
    = GL2.GL_MAX_PROGRAM_TOTAL_OUTPUT_COMPONENTS_NV;

  public static final int GL_GEOMETRY_VERTICES_OUT_EXT
    = GL2.GL_GEOMETRY_VERTICES_OUT_EXT;

  public static final int GL_GEOMETRY_INPUT_TYPE_EXT
    = GL2.GL_GEOMETRY_INPUT_TYPE_EXT;

  public static final int GL_GEOMETRY_OUTPUT_TYPE_EXT
    = GL2.GL_GEOMETRY_OUTPUT_TYPE_EXT;

  public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_EXT
    = GL2.GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_EXT;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_EXT
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_LAYERED_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_EXT
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_EXT;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_EXT
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_EXT;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT;

  public static final int GL_PROGRAM_POINT_SIZE_EXT
    = GL2.GL_PROGRAM_POINT_SIZE_EXT;

  public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER_NV
    = GL2.GL_VERTEX_ATTRIB_ARRAY_INTEGER_NV;

  public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE
    = GL2.GL_COMPARE_REF_DEPTH_TO_TEXTURE;

  public static final int GL_COMPRESSED_LUMINANCE_LATC1_EXT
    = GL2.GL_COMPRESSED_LUMINANCE_LATC1_EXT;

  public static final int GL_COMPRESSED_SIGNED_LUMINANCE_LATC1_EXT
    = GL2.GL_COMPRESSED_SIGNED_LUMINANCE_LATC1_EXT;

  public static final int GL_COMPRESSED_LUMINANCE_ALPHA_LATC2_EXT
    = GL2.GL_COMPRESSED_LUMINANCE_ALPHA_LATC2_EXT;

  public static final int GL_COMPRESSED_SIGNED_LUMINANCE_ALPHA_LATC2_EXT
    = GL2.GL_COMPRESSED_SIGNED_LUMINANCE_ALPHA_LATC2_EXT;

  public static final int GL_RENDERBUFFER_COVERAGE_SAMPLES_NV
    = GL2.GL_RENDERBUFFER_COVERAGE_SAMPLES_NV;

  public static final int GL_RENDERBUFFER_COLOR_SAMPLES_NV
    = GL2.GL_RENDERBUFFER_COLOR_SAMPLES_NV;

  public static final int GL_MAX_MULTISAMPLE_COVERAGE_MODES_NV
    = GL2.GL_MAX_MULTISAMPLE_COVERAGE_MODES_NV;

  public static final int GL_MULTISAMPLE_COVERAGE_MODES_NV
    = GL2.GL_MULTISAMPLE_COVERAGE_MODES_NV;

  public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV
    = GL2.GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV;

  public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV
    = GL2.GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV;

  public static final int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV
    = GL2.GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV;

  public static final int GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV
    = GL2.GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV;

  public static final int GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV
    = GL2.GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV;

  public static final int GL_MAX_VERTEX_BINDABLE_UNIFORMS_EXT
    = GL2.GL_MAX_VERTEX_BINDABLE_UNIFORMS_EXT;

  public static final int GL_MAX_FRAGMENT_BINDABLE_UNIFORMS_EXT
    = GL2.GL_MAX_FRAGMENT_BINDABLE_UNIFORMS_EXT;

  public static final int GL_MAX_GEOMETRY_BINDABLE_UNIFORMS_EXT
    = GL2.GL_MAX_GEOMETRY_BINDABLE_UNIFORMS_EXT;

  public static final int GL_MAX_BINDABLE_UNIFORM_SIZE_EXT
    = GL2.GL_MAX_BINDABLE_UNIFORM_SIZE_EXT;

  public static final int GL_UNIFORM_BUFFER_EXT
    = GL2.GL_UNIFORM_BUFFER_EXT;

  public static final int GL_UNIFORM_BUFFER_BINDING_EXT
    = GL2.GL_UNIFORM_BUFFER_BINDING_EXT;

  public static final int GL_ALPHA32UI
    = GL2.GL_ALPHA32UI;

  public static final int GL_INTENSITY32UI
    = GL2.GL_INTENSITY32UI;

  public static final int GL_LUMINANCE32UI
    = GL2.GL_LUMINANCE32UI;

  public static final int GL_LUMINANCE_ALPHA32UI
    = GL2.GL_LUMINANCE_ALPHA32UI;

  public static final int GL_ALPHA16UI
    = GL2.GL_ALPHA16UI;

  public static final int GL_INTENSITY16UI
    = GL2.GL_INTENSITY16UI;

  public static final int GL_LUMINANCE16UI
    = GL2.GL_LUMINANCE16UI;

  public static final int GL_LUMINANCE_ALPHA16UI
    = GL2.GL_LUMINANCE_ALPHA16UI;

  public static final int GL_ALPHA8UI
    = GL2.GL_ALPHA8UI;

  public static final int GL_INTENSITY8UI
    = GL2.GL_INTENSITY8UI;

  public static final int GL_LUMINANCE8UI
    = GL2.GL_LUMINANCE8UI;

  public static final int GL_LUMINANCE_ALPHA8UI
    = GL2.GL_LUMINANCE_ALPHA8UI;

  public static final int GL_ALPHA32I
    = GL2.GL_ALPHA32I;

  public static final int GL_INTENSITY32I
    = GL2.GL_INTENSITY32I;

  public static final int GL_LUMINANCE32I
    = GL2.GL_LUMINANCE32I;

  public static final int GL_LUMINANCE_ALPHA32I
    = GL2.GL_LUMINANCE_ALPHA32I;

  public static final int GL_ALPHA16I
    = GL2.GL_ALPHA16I;

  public static final int GL_INTENSITY16I
    = GL2.GL_INTENSITY16I;

  public static final int GL_LUMINANCE16I
    = GL2.GL_LUMINANCE16I;

  public static final int GL_LUMINANCE_ALPHA16I
    = GL2.GL_LUMINANCE_ALPHA16I;

  public static final int GL_ALPHA8I
    = GL2.GL_ALPHA8I;

  public static final int GL_INTENSITY8I
    = GL2.GL_INTENSITY8I;

  public static final int GL_LUMINANCE8I
    = GL2.GL_LUMINANCE8I;

  public static final int GL_LUMINANCE_ALPHA8I
    = GL2.GL_LUMINANCE_ALPHA8I;

  public static final int GL_LUMINANCE_INTEGER
    = GL2.GL_LUMINANCE_INTEGER;

  public static final int GL_LUMINANCE_ALPHA_INTEGER
    = GL2.GL_LUMINANCE_ALPHA_INTEGER;

  public static final int GL_RGBA_INTEGER_MODE
    = GL2.GL_RGBA_INTEGER_MODE;

  public static final int GL_PROGRAM_MATRIX_EXT
    = GL2.GL_PROGRAM_MATRIX_EXT;

  public static final int GL_TRANSPOSE_PROGRAM_MATRIX_EXT
    = GL2.GL_TRANSPOSE_PROGRAM_MATRIX_EXT;

  public static final int GL_PROGRAM_MATRIX_STACK_DEPTH_EXT
    = GL2.GL_PROGRAM_MATRIX_STACK_DEPTH_EXT;

  public static final int GL_TEXTURE_SWIZZLE_R_EXT
    = GL2.GL_TEXTURE_SWIZZLE_R_EXT;

  public static final int GL_TEXTURE_SWIZZLE_G_EXT
    = GL2.GL_TEXTURE_SWIZZLE_G_EXT;

  public static final int GL_TEXTURE_SWIZZLE_B_EXT
    = GL2.GL_TEXTURE_SWIZZLE_B_EXT;

  public static final int GL_TEXTURE_SWIZZLE_A_EXT
    = GL2.GL_TEXTURE_SWIZZLE_A_EXT;

  public static final int GL_TEXTURE_SWIZZLE_RGBA_EXT
    = GL2.GL_TEXTURE_SWIZZLE_RGBA_EXT;

  public static final int GL_SAMPLE_POSITION_NV
    = GL2.GL_SAMPLE_POSITION_NV;

  public static final int GL_SAMPLE_MASK_NV
    = GL2.GL_SAMPLE_MASK_NV;

  public static final int GL_SAMPLE_MASK_VALUE_NV
    = GL2.GL_SAMPLE_MASK_VALUE_NV;

  public static final int GL_TEXTURE_BINDING_RENDERBUFFER_NV
    = GL2.GL_TEXTURE_BINDING_RENDERBUFFER_NV;

  public static final int GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV
    = GL2.GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV;

  public static final int GL_TEXTURE_RENDERBUFFER_NV
    = GL2.GL_TEXTURE_RENDERBUFFER_NV;

  public static final int GL_SAMPLER_RENDERBUFFER_NV
    = GL2.GL_SAMPLER_RENDERBUFFER_NV;

  public static final int GL_INT_SAMPLER_RENDERBUFFER_NV
    = GL2.GL_INT_SAMPLER_RENDERBUFFER_NV;

  public static final int GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV
    = GL2.GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV;

  public static final int GL_MAX_SAMPLE_MASK_WORDS_NV
    = GL2.GL_MAX_SAMPLE_MASK_WORDS_NV;

  public static final int GL_TRANSFORM_FEEDBACK_NV
    = GL2.GL_TRANSFORM_FEEDBACK_NV;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV;

  public static final int GL_TRANSFORM_FEEDBACK_BINDING_NV
    = GL2.GL_TRANSFORM_FEEDBACK_BINDING_NV;

  public static final int GL_VBO_FREE_MEMORY_ATI
    = GL2.GL_VBO_FREE_MEMORY_ATI;

  public static final int GL_TEXTURE_FREE_MEMORY_ATI
    = GL2.GL_TEXTURE_FREE_MEMORY_ATI;

  public static final int GL_RENDERBUFFER_FREE_MEMORY_ATI
    = GL2.GL_RENDERBUFFER_FREE_MEMORY_ATI;

  public static final int GL_COUNTER_TYPE_AMD
    = GL2.GL_COUNTER_TYPE_AMD;

  public static final int GL_COUNTER_RANGE_AMD
    = GL2.GL_COUNTER_RANGE_AMD;

  public static final int GL_UNSIGNED_INT64_AMD
    = GL2.GL_UNSIGNED_INT64_AMD;

  public static final int GL_PERCENTAGE_AMD
    = GL2.GL_PERCENTAGE_AMD;

  public static final int GL_PERFMON_RESULT_AVAILABLE_AMD
    = GL2.GL_PERFMON_RESULT_AVAILABLE_AMD;

  public static final int GL_PERFMON_RESULT_SIZE_AMD
    = GL2.GL_PERFMON_RESULT_SIZE_AMD;

  public static final int GL_PERFMON_RESULT_AMD
    = GL2.GL_PERFMON_RESULT_AMD;

  public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION_EXT
    = GL2.GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION_EXT;

  public static final int GL_FIRST_VERTEX_CONVENTION_EXT
    = GL2.GL_FIRST_VERTEX_CONVENTION_EXT;

  public static final int GL_LAST_VERTEX_CONVENTION_EXT
    = GL2.GL_LAST_VERTEX_CONVENTION_EXT;

  public static final int GL_PROVOKING_VERTEX_EXT
    = GL2.GL_PROVOKING_VERTEX_EXT;

  public static final int GL_ALPHA_SNORM
    = GL2.GL_ALPHA_SNORM;

  public static final int GL_LUMINANCE_SNORM
    = GL2.GL_LUMINANCE_SNORM;

  public static final int GL_LUMINANCE_ALPHA_SNORM
    = GL2.GL_LUMINANCE_ALPHA_SNORM;

  public static final int GL_INTENSITY_SNORM
    = GL2.GL_INTENSITY_SNORM;

  public static final int GL_ALPHA8_SNORM
    = GL2.GL_ALPHA8_SNORM;

  public static final int GL_LUMINANCE8_SNORM
    = GL2.GL_LUMINANCE8_SNORM;

  public static final int GL_LUMINANCE8_ALPHA8_SNORM
    = GL2.GL_LUMINANCE8_ALPHA8_SNORM;

  public static final int GL_INTENSITY8_SNORM
    = GL2.GL_INTENSITY8_SNORM;

  public static final int GL_ALPHA16_SNORM
    = GL2.GL_ALPHA16_SNORM;

  public static final int GL_LUMINANCE16_SNORM
    = GL2.GL_LUMINANCE16_SNORM;

  public static final int GL_LUMINANCE16_ALPHA16_SNORM
    = GL2.GL_LUMINANCE16_ALPHA16_SNORM;

  public static final int GL_INTENSITY16_SNORM
    = GL2.GL_INTENSITY16_SNORM;

  public static final int GL_TEXTURE_RANGE_LENGTH_APPLE
    = GL2.GL_TEXTURE_RANGE_LENGTH_APPLE;

  public static final int GL_TEXTURE_RANGE_POINTER_APPLE
    = GL2.GL_TEXTURE_RANGE_POINTER_APPLE;

  public static final int GL_TEXTURE_STORAGE_HINT_APPLE
    = GL2.GL_TEXTURE_STORAGE_HINT_APPLE;

  public static final int GL_STORAGE_PRIVATE_APPLE
    = GL2.GL_STORAGE_PRIVATE_APPLE;

  public static final int GL_HALF_APPLE
    = GL2.GL_HALF_APPLE;

  public static final int GL_RGBA_FLOAT32_APPLE
    = GL2.GL_RGBA_FLOAT32_APPLE;

  public static final int GL_RGB_FLOAT32_APPLE
    = GL2.GL_RGB_FLOAT32_APPLE;

  public static final int GL_ALPHA_FLOAT32_APPLE
    = GL2.GL_ALPHA_FLOAT32_APPLE;

  public static final int GL_INTENSITY_FLOAT32_APPLE
    = GL2.GL_INTENSITY_FLOAT32_APPLE;

  public static final int GL_LUMINANCE_FLOAT32_APPLE
    = GL2.GL_LUMINANCE_FLOAT32_APPLE;

  public static final int GL_LUMINANCE_ALPHA_FLOAT32_APPLE
    = GL2.GL_LUMINANCE_ALPHA_FLOAT32_APPLE;

  public static final int GL_RGBA_FLOAT16_APPLE
    = GL2.GL_RGBA_FLOAT16_APPLE;

  public static final int GL_RGB_FLOAT16_APPLE
    = GL2.GL_RGB_FLOAT16_APPLE;

  public static final int GL_ALPHA_FLOAT16_APPLE
    = GL2.GL_ALPHA_FLOAT16_APPLE;

  public static final int GL_INTENSITY_FLOAT16_APPLE
    = GL2.GL_INTENSITY_FLOAT16_APPLE;

  public static final int GL_LUMINANCE_FLOAT16_APPLE
    = GL2.GL_LUMINANCE_FLOAT16_APPLE;

  public static final int GL_LUMINANCE_ALPHA_FLOAT16_APPLE
    = GL2.GL_LUMINANCE_ALPHA_FLOAT16_APPLE;

  public static final int GL_COLOR_FLOAT_APPLE
    = GL2.GL_COLOR_FLOAT_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP1_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP2_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_SIZE_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP1_SIZE_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_COEFF_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP1_COEFF_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_ORDER_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP1_ORDER_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP1_DOMAIN_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP1_DOMAIN_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_SIZE_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP2_SIZE_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_COEFF_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP2_COEFF_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_ORDER_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP2_ORDER_APPLE;

  public static final int GL_VERTEX_ATTRIB_MAP2_DOMAIN_APPLE
    = GL2.GL_VERTEX_ATTRIB_MAP2_DOMAIN_APPLE;

  public static final int GL_AUX_DEPTH_STENCIL_APPLE
    = GL2.GL_AUX_DEPTH_STENCIL_APPLE;

  public static final int GL_BUFFER_OBJECT_APPLE
    = GL2.GL_BUFFER_OBJECT_APPLE;

  public static final int GL_RELEASED_APPLE
    = GL2.GL_RELEASED_APPLE;

  public static final int GL_VOLATILE_APPLE
    = GL2.GL_VOLATILE_APPLE;

  public static final int GL_RETAINED_APPLE
    = GL2.GL_RETAINED_APPLE;

  public static final int GL_UNDEFINED_APPLE
    = GL2.GL_UNDEFINED_APPLE;

  public static final int GL_PURGEABLE_APPLE
    = GL2.GL_PURGEABLE_APPLE;

  public static final int GL_PACK_ROW_BYTES_APPLE
    = GL2.GL_PACK_ROW_BYTES_APPLE;

  public static final int GL_UNPACK_ROW_BYTES_APPLE
    = GL2.GL_UNPACK_ROW_BYTES_APPLE;

  public static final int GL_RGB_422_APPLE
    = GL2.GL_RGB_422_APPLE;

  public static final int GL_VIDEO_BUFFER_NV
    = GL2.GL_VIDEO_BUFFER_NV;

  public static final int GL_VIDEO_BUFFER_BINDING_NV
    = GL2.GL_VIDEO_BUFFER_BINDING_NV;

  public static final int GL_FIELD_UPPER_NV
    = GL2.GL_FIELD_UPPER_NV;

  public static final int GL_FIELD_LOWER_NV
    = GL2.GL_FIELD_LOWER_NV;

  public static final int GL_NUM_VIDEO_CAPTURE_STREAMS_NV
    = GL2.GL_NUM_VIDEO_CAPTURE_STREAMS_NV;

  public static final int GL_NEXT_VIDEO_CAPTURE_BUFFER_STATUS_NV
    = GL2.GL_NEXT_VIDEO_CAPTURE_BUFFER_STATUS_NV;

  public static final int GL_VIDEO_CAPTURE_TO_422_SUPPORTED_NV
    = GL2.GL_VIDEO_CAPTURE_TO_422_SUPPORTED_NV;

  public static final int GL_LAST_VIDEO_CAPTURE_STATUS_NV
    = GL2.GL_LAST_VIDEO_CAPTURE_STATUS_NV;

  public static final int GL_VIDEO_BUFFER_PITCH_NV
    = GL2.GL_VIDEO_BUFFER_PITCH_NV;

  public static final int GL_VIDEO_COLOR_CONVERSION_MATRIX_NV
    = GL2.GL_VIDEO_COLOR_CONVERSION_MATRIX_NV;

  public static final int GL_VIDEO_COLOR_CONVERSION_MAX_NV
    = GL2.GL_VIDEO_COLOR_CONVERSION_MAX_NV;

  public static final int GL_VIDEO_COLOR_CONVERSION_MIN_NV
    = GL2.GL_VIDEO_COLOR_CONVERSION_MIN_NV;

  public static final int GL_VIDEO_COLOR_CONVERSION_OFFSET_NV
    = GL2.GL_VIDEO_COLOR_CONVERSION_OFFSET_NV;

  public static final int GL_VIDEO_BUFFER_INTERNAL_FORMAT_NV
    = GL2.GL_VIDEO_BUFFER_INTERNAL_FORMAT_NV;

  public static final int GL_PARTIAL_SUCCESS_NV
    = GL2.GL_PARTIAL_SUCCESS_NV;

  public static final int GL_SUCCESS_NV
    = GL2.GL_SUCCESS_NV;

  public static final int GL_FAILURE_NV
    = GL2.GL_FAILURE_NV;

  public static final int GL_YCBYCR8_422_NV
    = GL2.GL_YCBYCR8_422_NV;

  public static final int GL_YCBAYCR8A_4224_NV
    = GL2.GL_YCBAYCR8A_4224_NV;

  public static final int GL_Z6Y10Z6CB10Z6Y10Z6CR10_422_NV
    = GL2.GL_Z6Y10Z6CB10Z6Y10Z6CR10_422_NV;

  public static final int GL_Z6Y10Z6CB10Z6A10Z6Y10Z6CR10Z6A10_4224_NV
    = GL2.GL_Z6Y10Z6CB10Z6A10Z6Y10Z6CR10Z6A10_4224_NV;

  public static final int GL_Z4Y12Z4CB12Z4Y12Z4CR12_422_NV
    = GL2.GL_Z4Y12Z4CB12Z4Y12Z4CR12_422_NV;

  public static final int GL_Z4Y12Z4CB12Z4A12Z4Y12Z4CR12Z4A12_4224_NV
    = GL2.GL_Z4Y12Z4CB12Z4A12Z4Y12Z4CR12Z4A12_4224_NV;

  public static final int GL_Z4Y12Z4CB12Z4CR12_444_NV
    = GL2.GL_Z4Y12Z4CB12Z4CR12_444_NV;

  public static final int GL_VIDEO_CAPTURE_FRAME_WIDTH_NV
    = GL2.GL_VIDEO_CAPTURE_FRAME_WIDTH_NV;

  public static final int GL_VIDEO_CAPTURE_FRAME_HEIGHT_NV
    = GL2.GL_VIDEO_CAPTURE_FRAME_HEIGHT_NV;

  public static final int GL_VIDEO_CAPTURE_FIELD_UPPER_HEIGHT_NV
    = GL2.GL_VIDEO_CAPTURE_FIELD_UPPER_HEIGHT_NV;

  public static final int GL_VIDEO_CAPTURE_FIELD_LOWER_HEIGHT_NV
    = GL2.GL_VIDEO_CAPTURE_FIELD_LOWER_HEIGHT_NV;

  public static final int GL_VIDEO_CAPTURE_SURFACE_ORIGIN_NV
    = GL2.GL_VIDEO_CAPTURE_SURFACE_ORIGIN_NV;

  public static final int GL_MAX_GEOMETRY_PROGRAM_INVOCATIONS_NV
    = GL2.GL_MAX_GEOMETRY_PROGRAM_INVOCATIONS_NV;

  public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET_NV
    = GL2.GL_MIN_FRAGMENT_INTERPOLATION_OFFSET_NV;

  public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET_NV
    = GL2.GL_MAX_FRAGMENT_INTERPOLATION_OFFSET_NV;

  public static final int GL_FRAGMENT_PROGRAM_INTERPOLATION_OFFSET_BITS_NV
    = GL2.GL_FRAGMENT_PROGRAM_INTERPOLATION_OFFSET_BITS_NV;

  public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_NV
    = GL2.GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_NV;

  public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_NV
    = GL2.GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_NV;

  public static final int GL_MAX_PROGRAM_SUBROUTINE_PARAMETERS_NV
    = GL2.GL_MAX_PROGRAM_SUBROUTINE_PARAMETERS_NV;

  public static final int GL_MAX_PROGRAM_SUBROUTINE_NUM_NV
    = GL2.GL_MAX_PROGRAM_SUBROUTINE_NUM_NV;

  public static final int GL_SHADER_GLOBAL_ACCESS_BARRIER_BIT_NV
    = GL2.GL_SHADER_GLOBAL_ACCESS_BARRIER_BIT_NV;

  public static final int GL_MAX_PROGRAM_PATCH_ATTRIBS_NV
    = GL2.GL_MAX_PROGRAM_PATCH_ATTRIBS_NV;

  public static final int GL_TESS_CONTROL_PROGRAM_NV
    = GL2.GL_TESS_CONTROL_PROGRAM_NV;

  public static final int GL_TESS_EVALUATION_PROGRAM_NV
    = GL2.GL_TESS_EVALUATION_PROGRAM_NV;

  public static final int GL_TESS_CONTROL_PROGRAM_PARAMETER_BUFFER_NV
    = GL2.GL_TESS_CONTROL_PROGRAM_PARAMETER_BUFFER_NV;

  public static final int GL_TESS_EVALUATION_PROGRAM_PARAMETER_BUFFER_NV
    = GL2.GL_TESS_EVALUATION_PROGRAM_PARAMETER_BUFFER_NV;

  public static final int GL_COVERAGE_SAMPLES_NV
    = GL2.GL_COVERAGE_SAMPLES_NV;

  public static final int GL_COLOR_SAMPLES_NV
    = GL2.GL_COLOR_SAMPLES_NV;

  public static final int GL_DATA_BUFFER_AMD
    = GL2.GL_DATA_BUFFER_AMD;

  public static final int GL_PERFORMANCE_MONITOR_AMD
    = GL2.GL_PERFORMANCE_MONITOR_AMD;

  public static final int GL_QUERY_OBJECT_AMD
    = GL2.GL_QUERY_OBJECT_AMD;

  public static final int GL_VERTEX_ARRAY_OBJECT_AMD
    = GL2.GL_VERTEX_ARRAY_OBJECT_AMD;

  public static final int GL_SAMPLER_OBJECT_AMD
    = GL2.GL_SAMPLER_OBJECT_AMD;

  public static final int GL_SURFACE_STATE_NV
    = GL2.GL_SURFACE_STATE_NV;

  public static final int GL_SURFACE_REGISTERED_NV
    = GL2.GL_SURFACE_REGISTERED_NV;

  public static final int GL_SURFACE_MAPPED_NV
    = GL2.GL_SURFACE_MAPPED_NV;

  public static final int GL_WRITE_DISCARD_NV
    = GL2.GL_WRITE_DISCARD_NV;

  public static final int GL_CG_VERTEX_SHADER_EXT
    = GL2.GL_CG_VERTEX_SHADER_EXT;

  public static final int GL_CG_FRAGMENT_SHADER_EXT
    = GL2.GL_CG_FRAGMENT_SHADER_EXT;

  public static final int GL_MIN_PBUFFER_VIEWPORT_DIMS_APPLE
    = GL2.GL_MIN_PBUFFER_VIEWPORT_DIMS_APPLE;

  public static void glAccum(
    int op,
    float value) {
    gl().glAccum(
      op,
      value);
  }

  public static void glActiveStencilFaceEXT(
    int face) {
    gl().glActiveStencilFaceEXT(
      face);
  }

  public static void glApplyTextureEXT(
    int mode) {
    gl().glApplyTextureEXT(
      mode);
  }

  public static boolean glAreTexturesResident(
    int n,
    IntBuffer textures,
    ByteBuffer residences) {
    return gl().glAreTexturesResident(
      n,
      textures,
      residences);
  }

  public static boolean glAreTexturesResident(
    int n,
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

  public static void glArrayElement(
    int i) {
    gl().glArrayElement(
      i);
  }

  public static void glAttachObjectARB(
    int containerObj,
    int obj) {
    gl().glAttachObjectARB(
      containerObj,
      obj);
  }

  public static void glBegin(
    int mode) {
    gl().glBegin(
      mode);
  }

  public static void glBeginOcclusionQueryNV(
    int id) {
    gl().glBeginOcclusionQueryNV(
      id);
  }

  public static void glBeginPerfMonitorAMD(
    int monitor) {
    gl().glBeginPerfMonitorAMD(
      monitor);
  }

  public static void glBeginVertexShaderEXT(
    ) {
    gl().glBeginVertexShaderEXT();
  }

  public static void glBeginVideoCaptureNV(
    int video_capture_slot) {
    gl().glBeginVideoCaptureNV(
      video_capture_slot);
  }

  public static void glBindBufferOffset(
    int target,
    int index,
    int buffer,
    long offset) {
    gl().glBindBufferOffset(
      target,
      index,
      buffer,
      offset);
  }

  public static int glBindLightParameterEXT(
    int light,
    int value) {
    return gl().glBindLightParameterEXT(
      light,
      value);
  }

  public static int glBindMaterialParameterEXT(
    int face,
    int value) {
    return gl().glBindMaterialParameterEXT(
      face,
      value);
  }

  public static void glBindMultiTextureEXT(
    int texunit,
    int target,
    int texture) {
    gl().glBindMultiTextureEXT(
      texunit,
      target,
      texture);
  }

  public static int glBindParameterEXT(
    int value) {
    return gl().glBindParameterEXT(
      value);
  }

  public static void glBindProgramARB(
    int target,
    int program) {
    gl().glBindProgramARB(
      target,
      program);
  }

  public static int glBindTexGenParameterEXT(
    int unit,
    int coord,
    int value) {
    return gl().glBindTexGenParameterEXT(
      unit,
      coord,
      value);
  }

  public static int glBindTextureUnitParameterEXT(
    int unit,
    int value) {
    return gl().glBindTextureUnitParameterEXT(
      unit,
      value);
  }

  public static void glBindTransformFeedbackNV(
    int target,
    int id) {
    gl().glBindTransformFeedbackNV(
      target,
      id);
  }

  public static void glBindVertexShaderEXT(
    int id) {
    gl().glBindVertexShaderEXT(
      id);
  }

  public static void glBindVideoCaptureStreamBufferNV(
    int video_capture_slot,
    int stream,
    int frame_region,
    long offset) {
    gl().glBindVideoCaptureStreamBufferNV(
      video_capture_slot,
      stream,
      frame_region,
      offset);
  }

  public static void glBindVideoCaptureStreamTextureNV(
    int video_capture_slot,
    int stream,
    int frame_region,
    int target,
    int texture) {
    gl().glBindVideoCaptureStreamTextureNV(
      video_capture_slot,
      stream,
      frame_region,
      target,
      texture);
  }

  public static void glBitmap(
    int width,
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

  public static void glBitmap(
    int width,
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

  public static void glBitmap(
    int width,
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

  public static void glBlendEquationIndexedAMD(
    int buf,
    int mode) {
    gl().glBlendEquationIndexedAMD(
      buf,
      mode);
  }

  public static void glBlendEquationSeparateIndexedAMD(
    int buf,
    int modeRGB,
    int modeAlpha) {
    gl().glBlendEquationSeparateIndexedAMD(
      buf,
      modeRGB,
      modeAlpha);
  }

  public static void glBlendFuncIndexedAMD(
    int buf,
    int src,
    int dst) {
    gl().glBlendFuncIndexedAMD(
      buf,
      src,
      dst);
  }

  public static void glBlendFuncSeparateINGR(
    int sfactorRGB,
    int dfactorRGB,
    int sfactorAlpha,
    int dfactorAlpha) {
    gl().glBlendFuncSeparateINGR(
      sfactorRGB,
      dfactorRGB,
      sfactorAlpha,
      dfactorAlpha);
  }

  public static void glBlendFuncSeparateIndexedAMD(
    int buf,
    int srcRGB,
    int dstRGB,
    int srcAlpha,
    int dstAlpha) {
    gl().glBlendFuncSeparateIndexedAMD(
      buf,
      srcRGB,
      dstRGB,
      srcAlpha,
      dstAlpha);
  }

  public static void glBufferParameteri(
    int target,
    int pname,
    int param) {
    gl().glBufferParameteri(
      target,
      pname,
      param);
  }

  public static void glCallList(
    int list) {
    gl().glCallList(
      list);
  }

  public static void glCallLists(
    int n,
    int type,
    Buffer lists) {
    gl().glCallLists(
      n,
      type,
      lists);
  }

  public static int glCheckNamedFramebufferStatusEXT(
    int framebuffer,
    int target) {
    return gl().glCheckNamedFramebufferStatusEXT(
      framebuffer,
      target);
  }

  public static void glClearAccum(
    float red,
    float green,
    float blue,
    float alpha) {
    gl().glClearAccum(
      red,
      green,
      blue,
      alpha);
  }

  public static void glClearColorIi(
    int red,
    int green,
    int blue,
    int alpha) {
    gl().glClearColorIi(
      red,
      green,
      blue,
      alpha);
  }

  public static void glClearColorIui(
    int red,
    int green,
    int blue,
    int alpha) {
    gl().glClearColorIui(
      red,
      green,
      blue,
      alpha);
  }

  public static void glClearIndex(
    float c) {
    gl().glClearIndex(
      c);
  }

  public static void glClientAttribDefaultEXT(
    int mask) {
    gl().glClientAttribDefaultEXT(
      mask);
  }

  public static void glClipPlane(
    int plane,
    DoubleBuffer equation) {
    gl().glClipPlane(
      plane,
      equation);
  }

  public static void glClipPlane(
    int plane,
    double[] equation,
    int equation_offset) {
    gl().glClipPlane(
      plane,
      equation,
      equation_offset);
  }

  public static void glColor3b(
    byte red,
    byte green,
    byte blue) {
    gl().glColor3b(
      red,
      green,
      blue);
  }

  public static void glColor3bv(
    ByteBuffer v) {
    gl().glColor3bv(
      v);
  }

  public static void glColor3bv(
    byte[] v,
    int v_offset) {
    gl().glColor3bv(
      v,
      v_offset);
  }

  public static void glColor3d(
    double red,
    double green,
    double blue) {
    gl().glColor3d(
      red,
      green,
      blue);
  }

  public static void glColor3dv(
    DoubleBuffer v) {
    gl().glColor3dv(
      v);
  }

  public static void glColor3dv(
    double[] v,
    int v_offset) {
    gl().glColor3dv(
      v,
      v_offset);
  }

  public static void glColor3f(
    float red,
    float green,
    float blue) {
    gl().glColor3f(
      red,
      green,
      blue);
  }

  public static void glColor3fv(
    FloatBuffer v) {
    gl().glColor3fv(
      v);
  }

  public static void glColor3fv(
    float[] v,
    int v_offset) {
    gl().glColor3fv(
      v,
      v_offset);
  }

  public static void glColor3h(
    short red,
    short green,
    short blue) {
    gl().glColor3h(
      red,
      green,
      blue);
  }

  public static void glColor3hv(
    ShortBuffer v) {
    gl().glColor3hv(
      v);
  }

  public static void glColor3hv(
    short[] v,
    int v_offset) {
    gl().glColor3hv(
      v,
      v_offset);
  }

  public static void glColor3i(
    int red,
    int green,
    int blue) {
    gl().glColor3i(
      red,
      green,
      blue);
  }

  public static void glColor3iv(
    IntBuffer v) {
    gl().glColor3iv(
      v);
  }

  public static void glColor3iv(
    int[] v,
    int v_offset) {
    gl().glColor3iv(
      v,
      v_offset);
  }

  public static void glColor3s(
    short red,
    short green,
    short blue) {
    gl().glColor3s(
      red,
      green,
      blue);
  }

  public static void glColor3sv(
    ShortBuffer v) {
    gl().glColor3sv(
      v);
  }

  public static void glColor3sv(
    short[] v,
    int v_offset) {
    gl().glColor3sv(
      v,
      v_offset);
  }

  public static void glColor3ub(
    byte red,
    byte green,
    byte blue) {
    gl().glColor3ub(
      red,
      green,
      blue);
  }

  public static void glColor3ubv(
    ByteBuffer v) {
    gl().glColor3ubv(
      v);
  }

  public static void glColor3ubv(
    byte[] v,
    int v_offset) {
    gl().glColor3ubv(
      v,
      v_offset);
  }

  public static void glColor3ui(
    int red,
    int green,
    int blue) {
    gl().glColor3ui(
      red,
      green,
      blue);
  }

  public static void glColor3uiv(
    IntBuffer v) {
    gl().glColor3uiv(
      v);
  }

  public static void glColor3uiv(
    int[] v,
    int v_offset) {
    gl().glColor3uiv(
      v,
      v_offset);
  }

  public static void glColor3us(
    short red,
    short green,
    short blue) {
    gl().glColor3us(
      red,
      green,
      blue);
  }

  public static void glColor3usv(
    ShortBuffer v) {
    gl().glColor3usv(
      v);
  }

  public static void glColor3usv(
    short[] v,
    int v_offset) {
    gl().glColor3usv(
      v,
      v_offset);
  }

  public static void glColor4b(
    byte red,
    byte green,
    byte blue,
    byte alpha) {
    gl().glColor4b(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4bv(
    ByteBuffer v) {
    gl().glColor4bv(
      v);
  }

  public static void glColor4bv(
    byte[] v,
    int v_offset) {
    gl().glColor4bv(
      v,
      v_offset);
  }

  public static void glColor4d(
    double red,
    double green,
    double blue,
    double alpha) {
    gl().glColor4d(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4dv(
    DoubleBuffer v) {
    gl().glColor4dv(
      v);
  }

  public static void glColor4dv(
    double[] v,
    int v_offset) {
    gl().glColor4dv(
      v,
      v_offset);
  }

  public static void glColor4fv(
    FloatBuffer v) {
    gl().glColor4fv(
      v);
  }

  public static void glColor4fv(
    float[] v,
    int v_offset) {
    gl().glColor4fv(
      v,
      v_offset);
  }

  public static void glColor4h(
    short red,
    short green,
    short blue,
    short alpha) {
    gl().glColor4h(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4hv(
    ShortBuffer v) {
    gl().glColor4hv(
      v);
  }

  public static void glColor4hv(
    short[] v,
    int v_offset) {
    gl().glColor4hv(
      v,
      v_offset);
  }

  public static void glColor4i(
    int red,
    int green,
    int blue,
    int alpha) {
    gl().glColor4i(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4iv(
    IntBuffer v) {
    gl().glColor4iv(
      v);
  }

  public static void glColor4iv(
    int[] v,
    int v_offset) {
    gl().glColor4iv(
      v,
      v_offset);
  }

  public static void glColor4s(
    short red,
    short green,
    short blue,
    short alpha) {
    gl().glColor4s(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4sv(
    ShortBuffer v) {
    gl().glColor4sv(
      v);
  }

  public static void glColor4sv(
    short[] v,
    int v_offset) {
    gl().glColor4sv(
      v,
      v_offset);
  }

  public static void glColor4ubv(
    ByteBuffer v) {
    gl().glColor4ubv(
      v);
  }

  public static void glColor4ubv(
    byte[] v,
    int v_offset) {
    gl().glColor4ubv(
      v,
      v_offset);
  }

  public static void glColor4ui(
    int red,
    int green,
    int blue,
    int alpha) {
    gl().glColor4ui(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4uiv(
    IntBuffer v) {
    gl().glColor4uiv(
      v);
  }

  public static void glColor4uiv(
    int[] v,
    int v_offset) {
    gl().glColor4uiv(
      v,
      v_offset);
  }

  public static void glColor4us(
    short red,
    short green,
    short blue,
    short alpha) {
    gl().glColor4us(
      red,
      green,
      blue,
      alpha);
  }

  public static void glColor4usv(
    ShortBuffer v) {
    gl().glColor4usv(
      v);
  }

  public static void glColor4usv(
    short[] v,
    int v_offset) {
    gl().glColor4usv(
      v,
      v_offset);
  }

  public static void glColorMaskIndexed(
    int index,
    boolean r,
    boolean g,
    boolean b,
    boolean a) {
    gl().glColorMaskIndexed(
      index,
      r,
      g,
      b,
      a);
  }

  public static void glColorMaterial(
    int face,
    int mode) {
    gl().glColorMaterial(
      face,
      mode);
  }

  public static void glColorSubTable(
    int target,
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

  public static void glColorSubTable(
    int target,
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

  public static void glColorTable(
    int target,
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

  public static void glColorTable(
    int target,
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

  public static void glColorTableParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glColorTableParameterfv(
      target,
      pname,
      params);
  }

  public static void glColorTableParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glColorTableParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glColorTableParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glColorTableParameteriv(
      target,
      pname,
      params);
  }

  public static void glColorTableParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glColorTableParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glCompileShaderARB(
    int shaderObj) {
    gl().glCompileShaderARB(
      shaderObj);
  }

  public static void glCompressedMultiTexImage1DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int width,
    int border,
    int imageSize,
    Buffer bits) {
    gl().glCompressedMultiTexImage1DEXT(
      texunit,
      target,
      level,
      internalformat,
      width,
      border,
      imageSize,
      bits);
  }

  public static void glCompressedMultiTexImage2DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int border,
    int imageSize,
    Buffer bits) {
    gl().glCompressedMultiTexImage2DEXT(
      texunit,
      target,
      level,
      internalformat,
      width,
      height,
      border,
      imageSize,
      bits);
  }

  public static void glCompressedMultiTexImage3DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int depth,
    int border,
    int imageSize,
    Buffer bits) {
    gl().glCompressedMultiTexImage3DEXT(
      texunit,
      target,
      level,
      internalformat,
      width,
      height,
      depth,
      border,
      imageSize,
      bits);
  }

  public static void glCompressedMultiTexSubImage1DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int width,
    int format,
    int imageSize,
    Buffer bits) {
    gl().glCompressedMultiTexSubImage1DEXT(
      texunit,
      target,
      level,
      xoffset,
      width,
      format,
      imageSize,
      bits);
  }

  public static void glCompressedMultiTexSubImage2DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int width,
    int height,
    int format,
    int imageSize,
    Buffer bits) {
    gl().glCompressedMultiTexSubImage2DEXT(
      texunit,
      target,
      level,
      xoffset,
      yoffset,
      width,
      height,
      format,
      imageSize,
      bits);
  }

  public static void glCompressedMultiTexSubImage3DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int zoffset,
    int width,
    int height,
    int depth,
    int format,
    int imageSize,
    Buffer bits) {
    gl().glCompressedMultiTexSubImage3DEXT(
      texunit,
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
      bits);
  }

  public static void glCompressedTextureImage1DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int width,
    int border,
    int imageSize,
    Buffer bits) {
    gl().glCompressedTextureImage1DEXT(
      texture,
      target,
      level,
      internalformat,
      width,
      border,
      imageSize,
      bits);
  }

  public static void glCompressedTextureImage2DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int border,
    int imageSize,
    Buffer bits) {
    gl().glCompressedTextureImage2DEXT(
      texture,
      target,
      level,
      internalformat,
      width,
      height,
      border,
      imageSize,
      bits);
  }

  public static void glCompressedTextureImage3DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int depth,
    int border,
    int imageSize,
    Buffer bits) {
    gl().glCompressedTextureImage3DEXT(
      texture,
      target,
      level,
      internalformat,
      width,
      height,
      depth,
      border,
      imageSize,
      bits);
  }

  public static void glCompressedTextureSubImage1DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int width,
    int format,
    int imageSize,
    Buffer bits) {
    gl().glCompressedTextureSubImage1DEXT(
      texture,
      target,
      level,
      xoffset,
      width,
      format,
      imageSize,
      bits);
  }

  public static void glCompressedTextureSubImage2DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int width,
    int height,
    int format,
    int imageSize,
    Buffer bits) {
    gl().glCompressedTextureSubImage2DEXT(
      texture,
      target,
      level,
      xoffset,
      yoffset,
      width,
      height,
      format,
      imageSize,
      bits);
  }

  public static void glCompressedTextureSubImage3DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int zoffset,
    int width,
    int height,
    int depth,
    int format,
    int imageSize,
    Buffer bits) {
    gl().glCompressedTextureSubImage3DEXT(
      texture,
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
      bits);
  }

  public static void glConvolutionFilter1D(
    int target,
    int internalformat,
    int width,
    int format,
    int type,
    Buffer image) {
    gl().glConvolutionFilter1D(
      target,
      internalformat,
      width,
      format,
      type,
      image);
  }

  public static void glConvolutionFilter1D(
    int target,
    int internalformat,
    int width,
    int format,
    int type,
    long image_buffer_offset) {
    gl().glConvolutionFilter1D(
      target,
      internalformat,
      width,
      format,
      type,
      image_buffer_offset);
  }

  public static void glConvolutionFilter2D(
    int target,
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

  public static void glConvolutionFilter2D(
    int target,
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

  public static void glConvolutionParameterf(
    int target,
    int pname,
    float params) {
    gl().glConvolutionParameterf(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glConvolutionParameterfv(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glConvolutionParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glConvolutionParameteri(
    int target,
    int pname,
    int params) {
    gl().glConvolutionParameteri(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glConvolutionParameteriv(
      target,
      pname,
      params);
  }

  public static void glConvolutionParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glConvolutionParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glCopyColorSubTable(
    int target,
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

  public static void glCopyColorTable(
    int target,
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

  public static void glCopyConvolutionFilter1D(
    int target,
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

  public static void glCopyConvolutionFilter2D(
    int target,
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

  public static void glCopyImageSubDataNV(
    int srcName,
    int srcTarget,
    int srcLevel,
    int srcX,
    int srcY,
    int srcZ,
    int dstName,
    int dstTarget,
    int dstLevel,
    int dstX,
    int dstY,
    int dstZ,
    int width,
    int height,
    int depth) {
    gl().glCopyImageSubDataNV(
      srcName,
      srcTarget,
      srcLevel,
      srcX,
      srcY,
      srcZ,
      dstName,
      dstTarget,
      dstLevel,
      dstX,
      dstY,
      dstZ,
      width,
      height,
      depth);
  }

  public static void glCopyMultiTexImage1DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int x,
    int y,
    int width,
    int border) {
    gl().glCopyMultiTexImage1DEXT(
      texunit,
      target,
      level,
      internalformat,
      x,
      y,
      width,
      border);
  }

  public static void glCopyMultiTexImage2DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int x,
    int y,
    int width,
    int height,
    int border) {
    gl().glCopyMultiTexImage2DEXT(
      texunit,
      target,
      level,
      internalformat,
      x,
      y,
      width,
      height,
      border);
  }

  public static void glCopyMultiTexSubImage1DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int x,
    int y,
    int width) {
    gl().glCopyMultiTexSubImage1DEXT(
      texunit,
      target,
      level,
      xoffset,
      x,
      y,
      width);
  }

  public static void glCopyMultiTexSubImage2DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int x,
    int y,
    int width,
    int height) {
    gl().glCopyMultiTexSubImage2DEXT(
      texunit,
      target,
      level,
      xoffset,
      yoffset,
      x,
      y,
      width,
      height);
  }

  public static void glCopyMultiTexSubImage3DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int zoffset,
    int x,
    int y,
    int width,
    int height) {
    gl().glCopyMultiTexSubImage3DEXT(
      texunit,
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

  public static void glCopyPixels(
    int x,
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

  public static void glCopyTextureImage1DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int x,
    int y,
    int width,
    int border) {
    gl().glCopyTextureImage1DEXT(
      texture,
      target,
      level,
      internalformat,
      x,
      y,
      width,
      border);
  }

  public static void glCopyTextureImage2DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int x,
    int y,
    int width,
    int height,
    int border) {
    gl().glCopyTextureImage2DEXT(
      texture,
      target,
      level,
      internalformat,
      x,
      y,
      width,
      height,
      border);
  }

  public static void glCopyTextureSubImage1DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int x,
    int y,
    int width) {
    gl().glCopyTextureSubImage1DEXT(
      texture,
      target,
      level,
      xoffset,
      x,
      y,
      width);
  }

  public static void glCopyTextureSubImage2DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int x,
    int y,
    int width,
    int height) {
    gl().glCopyTextureSubImage2DEXT(
      texture,
      target,
      level,
      xoffset,
      yoffset,
      x,
      y,
      width,
      height);
  }

  public static void glCopyTextureSubImage3DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int zoffset,
    int x,
    int y,
    int width,
    int height) {
    gl().glCopyTextureSubImage3DEXT(
      texture,
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

  public static int glCreateProgramObjectARB(
    ) {
    return gl().glCreateProgramObjectARB();
  }

  public static int glCreateShaderObjectARB(
    int shaderType) {
    return gl().glCreateShaderObjectARB(
      shaderType);
  }

  public static void glCullParameterdvEXT(
    int pname,
    DoubleBuffer params) {
    gl().glCullParameterdvEXT(
      pname,
      params);
  }

  public static void glCullParameterdvEXT(
    int pname,
    double[] params,
    int params_offset) {
    gl().glCullParameterdvEXT(
      pname,
      params,
      params_offset);
  }

  public static void glCullParameterfvEXT(
    int pname,
    FloatBuffer params) {
    gl().glCullParameterfvEXT(
      pname,
      params);
  }

  public static void glCullParameterfvEXT(
    int pname,
    float[] params,
    int params_offset) {
    gl().glCullParameterfvEXT(
      pname,
      params,
      params_offset);
  }

  public static void glDeleteFencesAPPLE(
    int n,
    IntBuffer fences) {
    gl().glDeleteFencesAPPLE(
      n,
      fences);
  }

  public static void glDeleteFencesAPPLE(
    int n,
    int[] fences,
    int fences_offset) {
    gl().glDeleteFencesAPPLE(
      n,
      fences,
      fences_offset);
  }

  public static void glDeleteFencesNV(
    int n,
    IntBuffer fences) {
    gl().glDeleteFencesNV(
      n,
      fences);
  }

  public static void glDeleteFencesNV(
    int n,
    int[] fences,
    int fences_offset) {
    gl().glDeleteFencesNV(
      n,
      fences,
      fences_offset);
  }

  public static void glDeleteLists(
    int list,
    int range) {
    gl().glDeleteLists(
      list,
      range);
  }

  public static void glDeleteNamesAMD(
    int identifier,
    int num,
    IntBuffer names) {
    gl().glDeleteNamesAMD(
      identifier,
      num,
      names);
  }

  public static void glDeleteNamesAMD(
    int identifier,
    int num,
    int[] names,
    int names_offset) {
    gl().glDeleteNamesAMD(
      identifier,
      num,
      names,
      names_offset);
  }

  public static void glDeleteObjectARB(
    int obj) {
    gl().glDeleteObjectARB(
      obj);
  }

  public static void glDeleteOcclusionQueriesNV(
    int n,
    IntBuffer ids) {
    gl().glDeleteOcclusionQueriesNV(
      n,
      ids);
  }

  public static void glDeleteOcclusionQueriesNV(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glDeleteOcclusionQueriesNV(
      n,
      ids,
      ids_offset);
  }

  public static void glDeletePerfMonitorsAMD(
    int n,
    IntBuffer monitors) {
    gl().glDeletePerfMonitorsAMD(
      n,
      monitors);
  }

  public static void glDeletePerfMonitorsAMD(
    int n,
    int[] monitors,
    int monitors_offset) {
    gl().glDeletePerfMonitorsAMD(
      n,
      monitors,
      monitors_offset);
  }

  public static void glDeleteProgramsARB(
    int n,
    IntBuffer programs) {
    gl().glDeleteProgramsARB(
      n,
      programs);
  }

  public static void glDeleteProgramsARB(
    int n,
    int[] programs,
    int programs_offset) {
    gl().glDeleteProgramsARB(
      n,
      programs,
      programs_offset);
  }

  public static void glDeleteTransformFeedbacksNV(
    int n,
    IntBuffer ids) {
    gl().glDeleteTransformFeedbacksNV(
      n,
      ids);
  }

  public static void glDeleteTransformFeedbacksNV(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glDeleteTransformFeedbacksNV(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteVertexShaderEXT(
    int id) {
    gl().glDeleteVertexShaderEXT(
      id);
  }

  public static void glDepthBoundsEXT(
    double zmin,
    double zmax) {
    gl().glDepthBoundsEXT(
      zmin,
      zmax);
  }

  public static void glDetachObjectARB(
    int containerObj,
    int attachedObj) {
    gl().glDetachObjectARB(
      containerObj,
      attachedObj);
  }

  public static void glDisableClientStateIndexedEXT(
    int array,
    int index) {
    gl().glDisableClientStateIndexedEXT(
      array,
      index);
  }

  public static void glDisableIndexed(
    int target,
    int index) {
    gl().glDisableIndexed(
      target,
      index);
  }

  public static void glDisableVariantClientStateEXT(
    int id) {
    gl().glDisableVariantClientStateEXT(
      id);
  }

  public static void glDisableVertexAttribAPPLE(
    int index,
    int pname) {
    gl().glDisableVertexAttribAPPLE(
      index,
      pname);
  }

  public static void glDisableVertexAttribArrayARB(
    int index) {
    gl().glDisableVertexAttribArrayARB(
      index);
  }

  public static void glDrawBuffersATI(
    int n,
    IntBuffer bufs) {
    gl().glDrawBuffersATI(
      n,
      bufs);
  }

  public static void glDrawBuffersATI(
    int n,
    int[] bufs,
    int bufs_offset) {
    gl().glDrawBuffersATI(
      n,
      bufs,
      bufs_offset);
  }

  public static void glDrawPixels(
    int width,
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

  public static void glDrawPixels(
    int width,
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

  public static void glDrawTransformFeedbackNV(
    int mode,
    int id) {
    gl().glDrawTransformFeedbackNV(
      mode,
      id);
  }

  public static void glEdgeFlag(
    boolean flag) {
    gl().glEdgeFlag(
      flag);
  }

  public static void glEdgeFlagPointer(
    int stride,
    Buffer ptr) {
    gl().glEdgeFlagPointer(
      stride,
      ptr);
  }

  public static void glEdgeFlagPointer(
    int stride,
    long ptr_buffer_offset) {
    gl().glEdgeFlagPointer(
      stride,
      ptr_buffer_offset);
  }

  public static void glEdgeFlagv(
    ByteBuffer flag) {
    gl().glEdgeFlagv(
      flag);
  }

  public static void glEdgeFlagv(
    byte[] flag,
    int flag_offset) {
    gl().glEdgeFlagv(
      flag,
      flag_offset);
  }

  public static void glEnableClientStateIndexedEXT(
    int array,
    int index) {
    gl().glEnableClientStateIndexedEXT(
      array,
      index);
  }

  public static void glEnableIndexed(
    int target,
    int index) {
    gl().glEnableIndexed(
      target,
      index);
  }

  public static void glEnableVariantClientStateEXT(
    int id) {
    gl().glEnableVariantClientStateEXT(
      id);
  }

  public static void glEnableVertexAttribAPPLE(
    int index,
    int pname) {
    gl().glEnableVertexAttribAPPLE(
      index,
      pname);
  }

  public static void glEnableVertexAttribArrayARB(
    int index) {
    gl().glEnableVertexAttribArrayARB(
      index);
  }

  public static void glEnd(
    ) {
    gl().glEnd();
  }

  public static void glEndList(
    ) {
    gl().glEndList();
  }

  public static void glEndOcclusionQueryNV(
    ) {
    gl().glEndOcclusionQueryNV();
  }

  public static void glEndPerfMonitorAMD(
    int monitor) {
    gl().glEndPerfMonitorAMD(
      monitor);
  }

  public static void glEndVertexShaderEXT(
    ) {
    gl().glEndVertexShaderEXT();
  }

  public static void glEndVideoCaptureNV(
    int video_capture_slot) {
    gl().glEndVideoCaptureNV(
      video_capture_slot);
  }

  public static void glEvalCoord1d(
    double u) {
    gl().glEvalCoord1d(
      u);
  }

  public static void glEvalCoord1dv(
    DoubleBuffer u) {
    gl().glEvalCoord1dv(
      u);
  }

  public static void glEvalCoord1dv(
    double[] u,
    int u_offset) {
    gl().glEvalCoord1dv(
      u,
      u_offset);
  }

  public static void glEvalCoord1f(
    float u) {
    gl().glEvalCoord1f(
      u);
  }

  public static void glEvalCoord1fv(
    FloatBuffer u) {
    gl().glEvalCoord1fv(
      u);
  }

  public static void glEvalCoord1fv(
    float[] u,
    int u_offset) {
    gl().glEvalCoord1fv(
      u,
      u_offset);
  }

  public static void glEvalCoord2d(
    double u,
    double v) {
    gl().glEvalCoord2d(
      u,
      v);
  }

  public static void glEvalCoord2dv(
    DoubleBuffer u) {
    gl().glEvalCoord2dv(
      u);
  }

  public static void glEvalCoord2dv(
    double[] u,
    int u_offset) {
    gl().glEvalCoord2dv(
      u,
      u_offset);
  }

  public static void glEvalCoord2f(
    float u,
    float v) {
    gl().glEvalCoord2f(
      u,
      v);
  }

  public static void glEvalCoord2fv(
    FloatBuffer u) {
    gl().glEvalCoord2fv(
      u);
  }

  public static void glEvalCoord2fv(
    float[] u,
    int u_offset) {
    gl().glEvalCoord2fv(
      u,
      u_offset);
  }

  public static void glEvalMapsNV(
    int target,
    int mode) {
    gl().glEvalMapsNV(
      target,
      mode);
  }

  public static void glEvalMesh1(
    int mode,
    int i1,
    int i2) {
    gl().glEvalMesh1(
      mode,
      i1,
      i2);
  }

  public static void glEvalMesh2(
    int mode,
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

  public static void glEvalPoint1(
    int i) {
    gl().glEvalPoint1(
      i);
  }

  public static void glEvalPoint2(
    int i,
    int j) {
    gl().glEvalPoint2(
      i,
      j);
  }

  public static void glExtractComponentEXT(
    int res,
    int src,
    int num) {
    gl().glExtractComponentEXT(
      res,
      src,
      num);
  }

  public static void glFeedbackBuffer(
    int size,
    int type,
    FloatBuffer buffer) {
    gl().glFeedbackBuffer(
      size,
      type,
      buffer);
  }

/*
  public static void glFeedbackBuffer(
    int size,
    int type,
    float[] buffer,
    int buffer_offset) {
    gl().glFeedbackBuffer(
      size,
      type,
      buffer,
      buffer_offset);
  }
*/

  public static void glFinishFenceAPPLE(
    int fence) {
    gl().glFinishFenceAPPLE(
      fence);
  }

  public static void glFinishFenceNV(
    int fence) {
    gl().glFinishFenceNV(
      fence);
  }

  public static void glFinishObjectAPPLE(
    int object,
    int name) {
    gl().glFinishObjectAPPLE(
      object,
      name);
  }

  public static void glFinishRenderAPPLE(
    ) {
    gl().glFinishRenderAPPLE();
  }

  public static void glFinishTextureSUNX(
    ) {
    gl().glFinishTextureSUNX();
  }

  public static void glFlushMappedNamedBufferRangeEXT(
    int buffer,
    long offset,
    long length) {
    gl().glFlushMappedNamedBufferRangeEXT(
      buffer,
      offset,
      length);
  }

  public static void glFlushPixelDataRangeNV(
    int target) {
    gl().glFlushPixelDataRangeNV(
      target);
  }

  public static void glFlushRenderAPPLE(
    ) {
    gl().glFlushRenderAPPLE();
  }

  public static void glFlushVertexArrayRangeAPPLE(
    int length,
    Buffer pointer) {
    gl().glFlushVertexArrayRangeAPPLE(
      length,
      pointer);
  }

  public static void glFlushVertexArrayRangeNV(
    ) {
    gl().glFlushVertexArrayRangeNV();
  }

  public static void glFogCoordPointer(
    int type,
    int stride,
    Buffer pointer) {
    gl().glFogCoordPointer(
      type,
      stride,
      pointer);
  }

  public static void glFogCoordPointer(
    int type,
    int stride,
    long pointer_buffer_offset) {
    gl().glFogCoordPointer(
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glFogCoordd(
    double coord) {
    gl().glFogCoordd(
      coord);
  }

  public static void glFogCoorddv(
    DoubleBuffer coord) {
    gl().glFogCoorddv(
      coord);
  }

  public static void glFogCoorddv(
    double[] coord,
    int coord_offset) {
    gl().glFogCoorddv(
      coord,
      coord_offset);
  }

  public static void glFogCoordf(
    float coord) {
    gl().glFogCoordf(
      coord);
  }

  public static void glFogCoordfv(
    FloatBuffer coord) {
    gl().glFogCoordfv(
      coord);
  }

  public static void glFogCoordfv(
    float[] coord,
    int coord_offset) {
    gl().glFogCoordfv(
      coord,
      coord_offset);
  }

  public static void glFogCoordh(
    short fog) {
    gl().glFogCoordh(
      fog);
  }

  public static void glFogCoordhv(
    ShortBuffer fog) {
    gl().glFogCoordhv(
      fog);
  }

  public static void glFogCoordhv(
    short[] fog,
    int fog_offset) {
    gl().glFogCoordhv(
      fog,
      fog_offset);
  }

  public static void glFogi(
    int pname,
    int param) {
    gl().glFogi(
      pname,
      param);
  }

  public static void glFogiv(
    int pname,
    IntBuffer params) {
    gl().glFogiv(
      pname,
      params);
  }

  public static void glFogiv(
    int pname,
    int[] params,
    int params_offset) {
    gl().glFogiv(
      pname,
      params,
      params_offset);
  }

  public static void glFrameTerminatorGREMEDY(
    ) {
    gl().glFrameTerminatorGREMEDY();
  }

  public static void glFramebufferDrawBufferEXT(
    int framebuffer,
    int mode) {
    gl().glFramebufferDrawBufferEXT(
      framebuffer,
      mode);
  }

  public static void glFramebufferDrawBuffersEXT(
    int framebuffer,
    int n,
    IntBuffer bufs) {
    gl().glFramebufferDrawBuffersEXT(
      framebuffer,
      n,
      bufs);
  }

  public static void glFramebufferDrawBuffersEXT(
    int framebuffer,
    int n,
    int[] bufs,
    int bufs_offset) {
    gl().glFramebufferDrawBuffersEXT(
      framebuffer,
      n,
      bufs,
      bufs_offset);
  }

  public static void glFramebufferReadBufferEXT(
    int framebuffer,
    int mode) {
    gl().glFramebufferReadBufferEXT(
      framebuffer,
      mode);
  }

  public static void glFramebufferTextureEXT(
    int target,
    int attachment,
    int texture,
    int level) {
    gl().glFramebufferTextureEXT(
      target,
      attachment,
      texture,
      level);
  }

  public static void glFramebufferTextureFaceEXT(
    int target,
    int attachment,
    int texture,
    int level,
    int face) {
    gl().glFramebufferTextureFaceEXT(
      target,
      attachment,
      texture,
      level,
      face);
  }

  public static void glFramebufferTextureLayerEXT(
    int target,
    int attachment,
    int texture,
    int level,
    int layer) {
    gl().glFramebufferTextureLayerEXT(
      target,
      attachment,
      texture,
      level,
      layer);
  }

  public static void glGenFencesAPPLE(
    int n,
    IntBuffer fences) {
    gl().glGenFencesAPPLE(
      n,
      fences);
  }

  public static void glGenFencesAPPLE(
    int n,
    int[] fences,
    int fences_offset) {
    gl().glGenFencesAPPLE(
      n,
      fences,
      fences_offset);
  }

  public static void glGenFencesNV(
    int n,
    IntBuffer fences) {
    gl().glGenFencesNV(
      n,
      fences);
  }

  public static void glGenFencesNV(
    int n,
    int[] fences,
    int fences_offset) {
    gl().glGenFencesNV(
      n,
      fences,
      fences_offset);
  }

  public static int glGenLists(
    int range) {
    return gl().glGenLists(
      range);
  }

  public static void glGenNamesAMD(
    int identifier,
    int num,
    IntBuffer names) {
    gl().glGenNamesAMD(
      identifier,
      num,
      names);
  }

  public static void glGenNamesAMD(
    int identifier,
    int num,
    int[] names,
    int names_offset) {
    gl().glGenNamesAMD(
      identifier,
      num,
      names,
      names_offset);
  }

  public static void glGenOcclusionQueriesNV(
    int n,
    IntBuffer ids) {
    gl().glGenOcclusionQueriesNV(
      n,
      ids);
  }

  public static void glGenOcclusionQueriesNV(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glGenOcclusionQueriesNV(
      n,
      ids,
      ids_offset);
  }

  public static void glGenPerfMonitorsAMD(
    int n,
    IntBuffer monitors) {
    gl().glGenPerfMonitorsAMD(
      n,
      monitors);
  }

  public static void glGenPerfMonitorsAMD(
    int n,
    int[] monitors,
    int monitors_offset) {
    gl().glGenPerfMonitorsAMD(
      n,
      monitors,
      monitors_offset);
  }

  public static void glGenProgramsARB(
    int n,
    IntBuffer programs) {
    gl().glGenProgramsARB(
      n,
      programs);
  }

  public static void glGenProgramsARB(
    int n,
    int[] programs,
    int programs_offset) {
    gl().glGenProgramsARB(
      n,
      programs,
      programs_offset);
  }

  public static int glGenSymbolsEXT(
    int datatype,
    int storagetype,
    int range,
    int components) {
    return gl().glGenSymbolsEXT(
      datatype,
      storagetype,
      range,
      components);
  }

  public static void glGenTransformFeedbacksNV(
    int n,
    IntBuffer ids) {
    gl().glGenTransformFeedbacksNV(
      n,
      ids);
  }

  public static void glGenTransformFeedbacksNV(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glGenTransformFeedbacksNV(
      n,
      ids,
      ids_offset);
  }

  public static int glGenVertexShadersEXT(
    int range) {
    return gl().glGenVertexShadersEXT(
      range);
  }

  public static void glGenerateMultiTexMipmapEXT(
    int texunit,
    int target) {
    gl().glGenerateMultiTexMipmapEXT(
      texunit,
      target);
  }

  public static void glGenerateTextureMipmapEXT(
    int texture,
    int target) {
    gl().glGenerateTextureMipmapEXT(
      texture,
      target);
  }

  public static void glGetActiveUniformARB(
    int programObj,
    int index,
    int maxLength,
    IntBuffer length,
    IntBuffer size,
    IntBuffer type,
    ByteBuffer name) {
    gl().glGetActiveUniformARB(
      programObj,
      index,
      maxLength,
      length,
      size,
      type,
      name);
  }

  public static void glGetActiveUniformARB(
    int programObj,
    int index,
    int maxLength,
    int[] length,
    int length_offset,
    int[] size,
    int size_offset,
    int[] type,
    int type_offset,
    byte[] name,
    int name_offset) {
    gl().glGetActiveUniformARB(
      programObj,
      index,
      maxLength,
      length,
      length_offset,
      size,
      size_offset,
      type,
      type_offset,
      name,
      name_offset);
  }

  public static void glGetAttachedObjectsARB(
    int containerObj,
    int maxCount,
    IntBuffer count,
    IntBuffer obj) {
    gl().glGetAttachedObjectsARB(
      containerObj,
      maxCount,
      count,
      obj);
  }

  public static void glGetAttachedObjectsARB(
    int containerObj,
    int maxCount,
    int[] count,
    int count_offset,
    int[] obj,
    int obj_offset) {
    gl().glGetAttachedObjectsARB(
      containerObj,
      maxCount,
      count,
      count_offset,
      obj,
      obj_offset);
  }

  public static void glGetBooleanIndexedv(
    int target,
    int index,
    ByteBuffer data) {
    gl().glGetBooleanIndexedv(
      target,
      index,
      data);
  }

  public static void glGetBooleanIndexedv(
    int target,
    int index,
    byte[] data,
    int data_offset) {
    gl().glGetBooleanIndexedv(
      target,
      index,
      data,
      data_offset);
  }

  public static void glGetClipPlane(
    int plane,
    DoubleBuffer equation) {
    gl().glGetClipPlane(
      plane,
      equation);
  }

  public static void glGetClipPlane(
    int plane,
    double[] equation,
    int equation_offset) {
    gl().glGetClipPlane(
      plane,
      equation,
      equation_offset);
  }

  public static void glGetColorTable(
    int target,
    int format,
    int type,
    Buffer table) {
    gl().glGetColorTable(
      target,
      format,
      type,
      table);
  }

  public static void glGetColorTable(
    int target,
    int format,
    int type,
    long table_buffer_offset) {
    gl().glGetColorTable(
      target,
      format,
      type,
      table_buffer_offset);
  }

  public static void glGetColorTableParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetColorTableParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetColorTableParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetColorTableParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetColorTableParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetColorTableParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetColorTableParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetColorTableParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetCompressedMultiTexImageEXT(
    int texunit,
    int target,
    int lod,
    Buffer img) {
    gl().glGetCompressedMultiTexImageEXT(
      texunit,
      target,
      lod,
      img);
  }

  public static void glGetCompressedTextureImageEXT(
    int texture,
    int target,
    int lod,
    Buffer img) {
    gl().glGetCompressedTextureImageEXT(
      texture,
      target,
      lod,
      img);
  }

  public static void glGetConvolutionFilter(
    int target,
    int format,
    int type,
    Buffer image) {
    gl().glGetConvolutionFilter(
      target,
      format,
      type,
      image);
  }

  public static void glGetConvolutionFilter(
    int target,
    int format,
    int type,
    long image_buffer_offset) {
    gl().glGetConvolutionFilter(
      target,
      format,
      type,
      image_buffer_offset);
  }

  public static void glGetConvolutionParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetConvolutionParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetConvolutionParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetConvolutionParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetConvolutionParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetConvolutionParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetConvolutionParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetConvolutionParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetDoubleIndexedvEXT(
    int target,
    int index,
    DoubleBuffer data) {
    gl().glGetDoubleIndexedvEXT(
      target,
      index,
      data);
  }

  public static void glGetDoubleIndexedvEXT(
    int target,
    int index,
    double[] data,
    int data_offset) {
    gl().glGetDoubleIndexedvEXT(
      target,
      index,
      data,
      data_offset);
  }

  public static void glGetFenceivNV(
    int fence,
    int pname,
    IntBuffer params) {
    gl().glGetFenceivNV(
      fence,
      pname,
      params);
  }

  public static void glGetFenceivNV(
    int fence,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetFenceivNV(
      fence,
      pname,
      params,
      params_offset);
  }

  public static void glGetFloatIndexedvEXT(
    int target,
    int index,
    FloatBuffer data) {
    gl().glGetFloatIndexedvEXT(
      target,
      index,
      data);
  }

  public static void glGetFloatIndexedvEXT(
    int target,
    int index,
    float[] data,
    int data_offset) {
    gl().glGetFloatIndexedvEXT(
      target,
      index,
      data,
      data_offset);
  }

  public static void glGetFramebufferParameterivEXT(
    int framebuffer,
    int pname,
    IntBuffer params) {
    gl().glGetFramebufferParameterivEXT(
      framebuffer,
      pname,
      params);
  }

  public static void glGetFramebufferParameterivEXT(
    int framebuffer,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetFramebufferParameterivEXT(
      framebuffer,
      pname,
      params,
      params_offset);
  }

  public static int glGetHandleARB(
    int pname) {
    return gl().glGetHandleARB(
      pname);
  }

  public static void glGetHistogram(
    int target,
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

  public static void glGetHistogram(
    int target,
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

  public static void glGetHistogramParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetHistogramParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetHistogramParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetHistogramParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetHistogramParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetHistogramParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetHistogramParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetHistogramParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetInfoLogARB(
    int obj,
    int maxLength,
    IntBuffer length,
    ByteBuffer infoLog) {
    gl().glGetInfoLogARB(
      obj,
      maxLength,
      length,
      infoLog);
  }

  public static void glGetInfoLogARB(
    int obj,
    int maxLength,
    int[] length,
    int length_offset,
    byte[] infoLog,
    int infoLog_offset) {
    gl().glGetInfoLogARB(
      obj,
      maxLength,
      length,
      length_offset,
      infoLog,
      infoLog_offset);
  }

  public static void glGetIntegerIndexedv(
    int target,
    int index,
    IntBuffer data) {
    gl().glGetIntegerIndexedv(
      target,
      index,
      data);
  }

  public static void glGetIntegerIndexedv(
    int target,
    int index,
    int[] data,
    int data_offset) {
    gl().glGetIntegerIndexedv(
      target,
      index,
      data,
      data_offset);
  }

  public static void glGetInvariantBooleanvEXT(
    int id,
    int value,
    ByteBuffer data) {
    gl().glGetInvariantBooleanvEXT(
      id,
      value,
      data);
  }

  public static void glGetInvariantBooleanvEXT(
    int id,
    int value,
    byte[] data,
    int data_offset) {
    gl().glGetInvariantBooleanvEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetInvariantFloatvEXT(
    int id,
    int value,
    FloatBuffer data) {
    gl().glGetInvariantFloatvEXT(
      id,
      value,
      data);
  }

  public static void glGetInvariantFloatvEXT(
    int id,
    int value,
    float[] data,
    int data_offset) {
    gl().glGetInvariantFloatvEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetInvariantIntegervEXT(
    int id,
    int value,
    IntBuffer data) {
    gl().glGetInvariantIntegervEXT(
      id,
      value,
      data);
  }

  public static void glGetInvariantIntegervEXT(
    int id,
    int value,
    int[] data,
    int data_offset) {
    gl().glGetInvariantIntegervEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetLightiv(
    int light,
    int pname,
    IntBuffer params) {
    gl().glGetLightiv(
      light,
      pname,
      params);
  }

  public static void glGetLightiv(
    int light,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetLightiv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glGetLocalConstantBooleanvEXT(
    int id,
    int value,
    ByteBuffer data) {
    gl().glGetLocalConstantBooleanvEXT(
      id,
      value,
      data);
  }

  public static void glGetLocalConstantBooleanvEXT(
    int id,
    int value,
    byte[] data,
    int data_offset) {
    gl().glGetLocalConstantBooleanvEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetLocalConstantFloatvEXT(
    int id,
    int value,
    FloatBuffer data) {
    gl().glGetLocalConstantFloatvEXT(
      id,
      value,
      data);
  }

  public static void glGetLocalConstantFloatvEXT(
    int id,
    int value,
    float[] data,
    int data_offset) {
    gl().glGetLocalConstantFloatvEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetLocalConstantIntegervEXT(
    int id,
    int value,
    IntBuffer data) {
    gl().glGetLocalConstantIntegervEXT(
      id,
      value,
      data);
  }

  public static void glGetLocalConstantIntegervEXT(
    int id,
    int value,
    int[] data,
    int data_offset) {
    gl().glGetLocalConstantIntegervEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetMapAttribParameterfvNV(
    int target,
    int index,
    int pname,
    FloatBuffer params) {
    gl().glGetMapAttribParameterfvNV(
      target,
      index,
      pname,
      params);
  }

  public static void glGetMapAttribParameterfvNV(
    int target,
    int index,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMapAttribParameterfvNV(
      target,
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapAttribParameterivNV(
    int target,
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetMapAttribParameterivNV(
      target,
      index,
      pname,
      params);
  }

  public static void glGetMapAttribParameterivNV(
    int target,
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMapAttribParameterivNV(
      target,
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapControlPointsNV(
    int target,
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

  public static void glGetMapParameterfvNV(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetMapParameterfvNV(
      target,
      pname,
      params);
  }

  public static void glGetMapParameterfvNV(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMapParameterfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapParameterivNV(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetMapParameterivNV(
      target,
      pname,
      params);
  }

  public static void glGetMapParameterivNV(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMapParameterivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMapdv(
    int target,
    int query,
    DoubleBuffer v) {
    gl().glGetMapdv(
      target,
      query,
      v);
  }

  public static void glGetMapdv(
    int target,
    int query,
    double[] v,
    int v_offset) {
    gl().glGetMapdv(
      target,
      query,
      v,
      v_offset);
  }

  public static void glGetMapfv(
    int target,
    int query,
    FloatBuffer v) {
    gl().glGetMapfv(
      target,
      query,
      v);
  }

  public static void glGetMapfv(
    int target,
    int query,
    float[] v,
    int v_offset) {
    gl().glGetMapfv(
      target,
      query,
      v,
      v_offset);
  }

  public static void glGetMapiv(
    int target,
    int query,
    IntBuffer v) {
    gl().glGetMapiv(
      target,
      query,
      v);
  }

  public static void glGetMapiv(
    int target,
    int query,
    int[] v,
    int v_offset) {
    gl().glGetMapiv(
      target,
      query,
      v,
      v_offset);
  }

  public static void glGetMaterialiv(
    int face,
    int pname,
    IntBuffer params) {
    gl().glGetMaterialiv(
      face,
      pname,
      params);
  }

  public static void glGetMaterialiv(
    int face,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMaterialiv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glGetMinmax(
    int target,
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

  public static void glGetMinmax(
    int target,
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

  public static void glGetMinmaxParameterfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetMinmaxParameterfv(
      target,
      pname,
      params);
  }

  public static void glGetMinmaxParameterfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMinmaxParameterfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMinmaxParameteriv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetMinmaxParameteriv(
      target,
      pname,
      params);
  }

  public static void glGetMinmaxParameteriv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMinmaxParameteriv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexEnvfvEXT(
    int texunit,
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetMultiTexEnvfvEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glGetMultiTexEnvfvEXT(
    int texunit,
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMultiTexEnvfvEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexEnvivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetMultiTexEnvivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glGetMultiTexEnvivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMultiTexEnvivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexGendvEXT(
    int texunit,
    int coord,
    int pname,
    DoubleBuffer params) {
    gl().glGetMultiTexGendvEXT(
      texunit,
      coord,
      pname,
      params);
  }

  public static void glGetMultiTexGendvEXT(
    int texunit,
    int coord,
    int pname,
    double[] params,
    int params_offset) {
    gl().glGetMultiTexGendvEXT(
      texunit,
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexGenfvEXT(
    int texunit,
    int coord,
    int pname,
    FloatBuffer params) {
    gl().glGetMultiTexGenfvEXT(
      texunit,
      coord,
      pname,
      params);
  }

  public static void glGetMultiTexGenfvEXT(
    int texunit,
    int coord,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMultiTexGenfvEXT(
      texunit,
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexGenivEXT(
    int texunit,
    int coord,
    int pname,
    IntBuffer params) {
    gl().glGetMultiTexGenivEXT(
      texunit,
      coord,
      pname,
      params);
  }

  public static void glGetMultiTexGenivEXT(
    int texunit,
    int coord,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMultiTexGenivEXT(
      texunit,
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexImageEXT(
    int texunit,
    int target,
    int level,
    int format,
    int type,
    Buffer pixels) {
    gl().glGetMultiTexImageEXT(
      texunit,
      target,
      level,
      format,
      type,
      pixels);
  }

  public static void glGetMultiTexLevelParameterfvEXT(
    int texunit,
    int target,
    int level,
    int pname,
    FloatBuffer params) {
    gl().glGetMultiTexLevelParameterfvEXT(
      texunit,
      target,
      level,
      pname,
      params);
  }

  public static void glGetMultiTexLevelParameterfvEXT(
    int texunit,
    int target,
    int level,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMultiTexLevelParameterfvEXT(
      texunit,
      target,
      level,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexLevelParameterivEXT(
    int texunit,
    int target,
    int level,
    int pname,
    IntBuffer params) {
    gl().glGetMultiTexLevelParameterivEXT(
      texunit,
      target,
      level,
      pname,
      params);
  }

  public static void glGetMultiTexLevelParameterivEXT(
    int texunit,
    int target,
    int level,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMultiTexLevelParameterivEXT(
      texunit,
      target,
      level,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexParameterIivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetMultiTexParameterIivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glGetMultiTexParameterIivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMultiTexParameterIivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexParameterIuivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetMultiTexParameterIuivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glGetMultiTexParameterIuivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMultiTexParameterIuivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexParameterfvEXT(
    int texunit,
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetMultiTexParameterfvEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glGetMultiTexParameterfvEXT(
    int texunit,
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMultiTexParameterfvEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultiTexParameterivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetMultiTexParameterivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glGetMultiTexParameterivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetMultiTexParameterivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetMultisamplefvNV(
    int pname,
    int index,
    FloatBuffer val) {
    gl().glGetMultisamplefvNV(
      pname,
      index,
      val);
  }

  public static void glGetMultisamplefvNV(
    int pname,
    int index,
    float[] val,
    int val_offset) {
    gl().glGetMultisamplefvNV(
      pname,
      index,
      val,
      val_offset);
  }

  public static void glGetNamedBufferParameterivEXT(
    int buffer,
    int pname,
    IntBuffer params) {
    gl().glGetNamedBufferParameterivEXT(
      buffer,
      pname,
      params);
  }

  public static void glGetNamedBufferParameterivEXT(
    int buffer,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetNamedBufferParameterivEXT(
      buffer,
      pname,
      params,
      params_offset);
  }

  public static void glGetNamedBufferSubDataEXT(
    int buffer,
    long offset,
    long size,
    Buffer data) {
    gl().glGetNamedBufferSubDataEXT(
      buffer,
      offset,
      size,
      data);
  }

  public static void glGetNamedFramebufferAttachmentParameterivEXT(
    int framebuffer,
    int attachment,
    int pname,
    IntBuffer params) {
    gl().glGetNamedFramebufferAttachmentParameterivEXT(
      framebuffer,
      attachment,
      pname,
      params);
  }

  public static void glGetNamedFramebufferAttachmentParameterivEXT(
    int framebuffer,
    int attachment,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetNamedFramebufferAttachmentParameterivEXT(
      framebuffer,
      attachment,
      pname,
      params,
      params_offset);
  }

  public static void glGetNamedProgramLocalParameterIivEXT(
    int program,
    int target,
    int index,
    IntBuffer params) {
    gl().glGetNamedProgramLocalParameterIivEXT(
      program,
      target,
      index,
      params);
  }

  public static void glGetNamedProgramLocalParameterIivEXT(
    int program,
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glGetNamedProgramLocalParameterIivEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetNamedProgramLocalParameterIuivEXT(
    int program,
    int target,
    int index,
    IntBuffer params) {
    gl().glGetNamedProgramLocalParameterIuivEXT(
      program,
      target,
      index,
      params);
  }

  public static void glGetNamedProgramLocalParameterIuivEXT(
    int program,
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glGetNamedProgramLocalParameterIuivEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetNamedProgramLocalParameterdvEXT(
    int program,
    int target,
    int index,
    DoubleBuffer params) {
    gl().glGetNamedProgramLocalParameterdvEXT(
      program,
      target,
      index,
      params);
  }

  public static void glGetNamedProgramLocalParameterdvEXT(
    int program,
    int target,
    int index,
    double[] params,
    int params_offset) {
    gl().glGetNamedProgramLocalParameterdvEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetNamedProgramLocalParameterfvEXT(
    int program,
    int target,
    int index,
    FloatBuffer params) {
    gl().glGetNamedProgramLocalParameterfvEXT(
      program,
      target,
      index,
      params);
  }

  public static void glGetNamedProgramLocalParameterfvEXT(
    int program,
    int target,
    int index,
    float[] params,
    int params_offset) {
    gl().glGetNamedProgramLocalParameterfvEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetNamedProgramStringEXT(
    int program,
    int target,
    int pname,
    Buffer string) {
    gl().glGetNamedProgramStringEXT(
      program,
      target,
      pname,
      string);
  }

  public static void glGetNamedProgramivEXT(
    int program,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetNamedProgramivEXT(
      program,
      target,
      pname,
      params);
  }

  public static void glGetNamedProgramivEXT(
    int program,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetNamedProgramivEXT(
      program,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetNamedRenderbufferParameterivEXT(
    int renderbuffer,
    int pname,
    IntBuffer params) {
    gl().glGetNamedRenderbufferParameterivEXT(
      renderbuffer,
      pname,
      params);
  }

  public static void glGetNamedRenderbufferParameterivEXT(
    int renderbuffer,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetNamedRenderbufferParameterivEXT(
      renderbuffer,
      pname,
      params,
      params_offset);
  }

  public static void glGetObjectParameterfvARB(
    int obj,
    int pname,
    FloatBuffer params) {
    gl().glGetObjectParameterfvARB(
      obj,
      pname,
      params);
  }

  public static void glGetObjectParameterfvARB(
    int obj,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetObjectParameterfvARB(
      obj,
      pname,
      params,
      params_offset);
  }

  public static void glGetObjectParameterivAPPLE(
    int objectType,
    int name,
    int pname,
    IntBuffer params) {
    gl().glGetObjectParameterivAPPLE(
      objectType,
      name,
      pname,
      params);
  }

  public static void glGetObjectParameterivAPPLE(
    int objectType,
    int name,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetObjectParameterivAPPLE(
      objectType,
      name,
      pname,
      params,
      params_offset);
  }

  public static void glGetObjectParameterivARB(
    int obj,
    int pname,
    IntBuffer params) {
    gl().glGetObjectParameterivARB(
      obj,
      pname,
      params);
  }

  public static void glGetObjectParameterivARB(
    int obj,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetObjectParameterivARB(
      obj,
      pname,
      params,
      params_offset);
  }

  public static void glGetOcclusionQueryivNV(
    int id,
    int pname,
    IntBuffer params) {
    gl().glGetOcclusionQueryivNV(
      id,
      pname,
      params);
  }

  public static void glGetOcclusionQueryivNV(
    int id,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetOcclusionQueryivNV(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetOcclusionQueryuivNV(
    int id,
    int pname,
    IntBuffer params) {
    gl().glGetOcclusionQueryuivNV(
      id,
      pname,
      params);
  }

  public static void glGetOcclusionQueryuivNV(
    int id,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetOcclusionQueryuivNV(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetPerfMonitorCounterDataAMD(
    int monitor,
    int pname,
    int dataSize,
    IntBuffer data,
    IntBuffer bytesWritten) {
    gl().glGetPerfMonitorCounterDataAMD(
      monitor,
      pname,
      dataSize,
      data,
      bytesWritten);
  }

  public static void glGetPerfMonitorCounterDataAMD(
    int monitor,
    int pname,
    int dataSize,
    int[] data,
    int data_offset,
    int[] bytesWritten,
    int bytesWritten_offset) {
    gl().glGetPerfMonitorCounterDataAMD(
      monitor,
      pname,
      dataSize,
      data,
      data_offset,
      bytesWritten,
      bytesWritten_offset);
  }

  public static void glGetPerfMonitorCounterInfoAMD(
    int group,
    int counter,
    int pname,
    Buffer data) {
    gl().glGetPerfMonitorCounterInfoAMD(
      group,
      counter,
      pname,
      data);
  }

  public static void glGetPerfMonitorCounterStringAMD(
    int group,
    int counter,
    int bufSize,
    IntBuffer length,
    ByteBuffer counterString) {
    gl().glGetPerfMonitorCounterStringAMD(
      group,
      counter,
      bufSize,
      length,
      counterString);
  }

  public static void glGetPerfMonitorCounterStringAMD(
    int group,
    int counter,
    int bufSize,
    int[] length,
    int length_offset,
    byte[] counterString,
    int counterString_offset) {
    gl().glGetPerfMonitorCounterStringAMD(
      group,
      counter,
      bufSize,
      length,
      length_offset,
      counterString,
      counterString_offset);
  }

  public static void glGetPerfMonitorCountersAMD(
    int group,
    IntBuffer numCounters,
    IntBuffer maxActiveCounters,
    int counterSize,
    IntBuffer counters) {
    gl().glGetPerfMonitorCountersAMD(
      group,
      numCounters,
      maxActiveCounters,
      counterSize,
      counters);
  }

  public static void glGetPerfMonitorCountersAMD(
    int group,
    int[] numCounters,
    int numCounters_offset,
    int[] maxActiveCounters,
    int maxActiveCounters_offset,
    int counterSize,
    int[] counters,
    int counters_offset) {
    gl().glGetPerfMonitorCountersAMD(
      group,
      numCounters,
      numCounters_offset,
      maxActiveCounters,
      maxActiveCounters_offset,
      counterSize,
      counters,
      counters_offset);
  }

  public static void glGetPerfMonitorGroupStringAMD(
    int group,
    int bufSize,
    IntBuffer length,
    ByteBuffer groupString) {
    gl().glGetPerfMonitorGroupStringAMD(
      group,
      bufSize,
      length,
      groupString);
  }

  public static void glGetPerfMonitorGroupStringAMD(
    int group,
    int bufSize,
    int[] length,
    int length_offset,
    byte[] groupString,
    int groupString_offset) {
    gl().glGetPerfMonitorGroupStringAMD(
      group,
      bufSize,
      length,
      length_offset,
      groupString,
      groupString_offset);
  }

  public static void glGetPerfMonitorGroupsAMD(
    IntBuffer numGroups,
    int groupsSize,
    IntBuffer groups) {
    gl().glGetPerfMonitorGroupsAMD(
      numGroups,
      groupsSize,
      groups);
  }

  public static void glGetPerfMonitorGroupsAMD(
    int[] numGroups,
    int numGroups_offset,
    int groupsSize,
    int[] groups,
    int groups_offset) {
    gl().glGetPerfMonitorGroupsAMD(
      numGroups,
      numGroups_offset,
      groupsSize,
      groups,
      groups_offset);
  }

  public static void glGetPixelMapfv(
    int map,
    FloatBuffer values) {
    gl().glGetPixelMapfv(
      map,
      values);
  }

  public static void glGetPixelMapfv(
    int map,
    float[] values,
    int values_offset) {
    gl().glGetPixelMapfv(
      map,
      values,
      values_offset);
  }

  public static void glGetPixelMapfv(
    int map,
    long values_buffer_offset) {
    gl().glGetPixelMapfv(
      map,
      values_buffer_offset);
  }

  public static void glGetPixelMapuiv(
    int map,
    IntBuffer values) {
    gl().glGetPixelMapuiv(
      map,
      values);
  }

  public static void glGetPixelMapuiv(
    int map,
    int[] values,
    int values_offset) {
    gl().glGetPixelMapuiv(
      map,
      values,
      values_offset);
  }

  public static void glGetPixelMapuiv(
    int map,
    long values_buffer_offset) {
    gl().glGetPixelMapuiv(
      map,
      values_buffer_offset);
  }

  public static void glGetPixelMapusv(
    int map,
    ShortBuffer values) {
    gl().glGetPixelMapusv(
      map,
      values);
  }

  public static void glGetPixelMapusv(
    int map,
    short[] values,
    int values_offset) {
    gl().glGetPixelMapusv(
      map,
      values,
      values_offset);
  }

  public static void glGetPixelMapusv(
    int map,
    long values_buffer_offset) {
    gl().glGetPixelMapusv(
      map,
      values_buffer_offset);
  }

  public static void glGetPolygonStipple(
    ByteBuffer mask) {
    gl().glGetPolygonStipple(
      mask);
  }

  public static void glGetPolygonStipple(
    byte[] mask,
    int mask_offset) {
    gl().glGetPolygonStipple(
      mask,
      mask_offset);
  }

  public static void glGetPolygonStipple(
    long mask_buffer_offset) {
    gl().glGetPolygonStipple(
      mask_buffer_offset);
  }

  public static void glGetProgramEnvParameterIivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glGetProgramEnvParameterIivNV(
      target,
      index,
      params);
  }

  public static void glGetProgramEnvParameterIivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glGetProgramEnvParameterIivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramEnvParameterIuivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glGetProgramEnvParameterIuivNV(
      target,
      index,
      params);
  }

  public static void glGetProgramEnvParameterIuivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glGetProgramEnvParameterIuivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramEnvParameterdvARB(
    int target,
    int index,
    DoubleBuffer params) {
    gl().glGetProgramEnvParameterdvARB(
      target,
      index,
      params);
  }

  public static void glGetProgramEnvParameterdvARB(
    int target,
    int index,
    double[] params,
    int params_offset) {
    gl().glGetProgramEnvParameterdvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramEnvParameterfvARB(
    int target,
    int index,
    FloatBuffer params) {
    gl().glGetProgramEnvParameterfvARB(
      target,
      index,
      params);
  }

  public static void glGetProgramEnvParameterfvARB(
    int target,
    int index,
    float[] params,
    int params_offset) {
    gl().glGetProgramEnvParameterfvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramLocalParameterIivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glGetProgramLocalParameterIivNV(
      target,
      index,
      params);
  }

  public static void glGetProgramLocalParameterIivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glGetProgramLocalParameterIivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramLocalParameterIuivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glGetProgramLocalParameterIuivNV(
      target,
      index,
      params);
  }

  public static void glGetProgramLocalParameterIuivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glGetProgramLocalParameterIuivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramLocalParameterdvARB(
    int target,
    int index,
    DoubleBuffer params) {
    gl().glGetProgramLocalParameterdvARB(
      target,
      index,
      params);
  }

  public static void glGetProgramLocalParameterdvARB(
    int target,
    int index,
    double[] params,
    int params_offset) {
    gl().glGetProgramLocalParameterdvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramLocalParameterfvARB(
    int target,
    int index,
    FloatBuffer params) {
    gl().glGetProgramLocalParameterfvARB(
      target,
      index,
      params);
  }

  public static void glGetProgramLocalParameterfvARB(
    int target,
    int index,
    float[] params,
    int params_offset) {
    gl().glGetProgramLocalParameterfvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glGetProgramStringARB(
    int target,
    int pname,
    Buffer string) {
    gl().glGetProgramStringARB(
      target,
      pname,
      string);
  }

  public static void glGetProgramSubroutineParameteruivNV(
    int target,
    int index,
    IntBuffer param) {
    gl().glGetProgramSubroutineParameteruivNV(
      target,
      index,
      param);
  }

  public static void glGetProgramSubroutineParameteruivNV(
    int target,
    int index,
    int[] param,
    int param_offset) {
    gl().glGetProgramSubroutineParameteruivNV(
      target,
      index,
      param,
      param_offset);
  }

  public static void glGetProgramivARB(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetProgramivARB(
      target,
      pname,
      params);
  }

  public static void glGetProgramivARB(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetProgramivARB(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjecti64vEXT(
    int id,
    int pname,
    LongBuffer params) {
    gl().glGetQueryObjecti64vEXT(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjecti64vEXT(
    int id,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetQueryObjecti64vEXT(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectui64vEXT(
    int id,
    int pname,
    LongBuffer params) {
    gl().glGetQueryObjectui64vEXT(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjectui64vEXT(
    int id,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetQueryObjectui64vEXT(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetSeparableFilter(
    int target,
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

  public static void glGetSeparableFilter(
    int target,
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

  public static void glGetShaderSourceARB(
    int obj,
    int maxLength,
    IntBuffer length,
    ByteBuffer source) {
    gl().glGetShaderSourceARB(
      obj,
      maxLength,
      length,
      source);
  }

  public static void glGetShaderSourceARB(
    int obj,
    int maxLength,
    int[] length,
    int length_offset,
    byte[] source,
    int source_offset) {
    gl().glGetShaderSourceARB(
      obj,
      maxLength,
      length,
      length_offset,
      source,
      source_offset);
  }

  public static void glGetTexGendv(
    int coord,
    int pname,
    DoubleBuffer params) {
    gl().glGetTexGendv(
      coord,
      pname,
      params);
  }

  public static void glGetTexGendv(
    int coord,
    int pname,
    double[] params,
    int params_offset) {
    gl().glGetTexGendv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetTextureImageEXT(
    int texture,
    int target,
    int level,
    int format,
    int type,
    Buffer pixels) {
    gl().glGetTextureImageEXT(
      texture,
      target,
      level,
      format,
      type,
      pixels);
  }

  public static void glGetTextureLevelParameterfvEXT(
    int texture,
    int target,
    int level,
    int pname,
    FloatBuffer params) {
    gl().glGetTextureLevelParameterfvEXT(
      texture,
      target,
      level,
      pname,
      params);
  }

  public static void glGetTextureLevelParameterfvEXT(
    int texture,
    int target,
    int level,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetTextureLevelParameterfvEXT(
      texture,
      target,
      level,
      pname,
      params,
      params_offset);
  }

  public static void glGetTextureLevelParameterivEXT(
    int texture,
    int target,
    int level,
    int pname,
    IntBuffer params) {
    gl().glGetTextureLevelParameterivEXT(
      texture,
      target,
      level,
      pname,
      params);
  }

  public static void glGetTextureLevelParameterivEXT(
    int texture,
    int target,
    int level,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTextureLevelParameterivEXT(
      texture,
      target,
      level,
      pname,
      params,
      params_offset);
  }

  public static void glGetTextureParameterIivEXT(
    int texture,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetTextureParameterIivEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glGetTextureParameterIivEXT(
    int texture,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTextureParameterIivEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTextureParameterIuivEXT(
    int texture,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetTextureParameterIuivEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glGetTextureParameterIuivEXT(
    int texture,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTextureParameterIuivEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTextureParameterfvEXT(
    int texture,
    int target,
    int pname,
    FloatBuffer params) {
    gl().glGetTextureParameterfvEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glGetTextureParameterfvEXT(
    int texture,
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetTextureParameterfvEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTextureParameterivEXT(
    int texture,
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetTextureParameterivEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glGetTextureParameterivEXT(
    int texture,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTextureParameterivEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static int glGetUniformBufferSizeEXT(
    int program,
    int location) {
    return gl().glGetUniformBufferSizeEXT(
      program,
      location);
  }

  public static int glGetUniformLocationARB(
    int programObj,
    String name) {
    return gl().glGetUniformLocationARB(
      programObj,
      name);
  }

  public static long glGetUniformOffsetEXT(
    int program,
    int location) {
    return gl().glGetUniformOffsetEXT(
      program,
      location);
  }

  public static void glGetUniformfvARB(
    int programObj,
    int location,
    FloatBuffer params) {
    gl().glGetUniformfvARB(
      programObj,
      location,
      params);
  }

  public static void glGetUniformfvARB(
    int programObj,
    int location,
    float[] params,
    int params_offset) {
    gl().glGetUniformfvARB(
      programObj,
      location,
      params,
      params_offset);
  }

  public static void glGetUniformivARB(
    int programObj,
    int location,
    IntBuffer params) {
    gl().glGetUniformivARB(
      programObj,
      location,
      params);
  }

  public static void glGetUniformivARB(
    int programObj,
    int location,
    int[] params,
    int params_offset) {
    gl().glGetUniformivARB(
      programObj,
      location,
      params,
      params_offset);
  }

  public static void glGetVariantBooleanvEXT(
    int id,
    int value,
    ByteBuffer data) {
    gl().glGetVariantBooleanvEXT(
      id,
      value,
      data);
  }

  public static void glGetVariantBooleanvEXT(
    int id,
    int value,
    byte[] data,
    int data_offset) {
    gl().glGetVariantBooleanvEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetVariantFloatvEXT(
    int id,
    int value,
    FloatBuffer data) {
    gl().glGetVariantFloatvEXT(
      id,
      value,
      data);
  }

  public static void glGetVariantFloatvEXT(
    int id,
    int value,
    float[] data,
    int data_offset) {
    gl().glGetVariantFloatvEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetVariantIntegervEXT(
    int id,
    int value,
    IntBuffer data) {
    gl().glGetVariantIntegervEXT(
      id,
      value,
      data);
  }

  public static void glGetVariantIntegervEXT(
    int id,
    int value,
    int[] data,
    int data_offset) {
    gl().glGetVariantIntegervEXT(
      id,
      value,
      data,
      data_offset);
  }

  public static void glGetVertexAttribIivEXT(
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetVertexAttribIivEXT(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribIivEXT(
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVertexAttribIivEXT(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribIuivEXT(
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetVertexAttribIuivEXT(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribIuivEXT(
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVertexAttribIuivEXT(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribLi64vNV(
    int index,
    int pname,
    LongBuffer params) {
    gl().glGetVertexAttribLi64vNV(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribLi64vNV(
    int index,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetVertexAttribLi64vNV(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribLui64vNV(
    int index,
    int pname,
    LongBuffer params) {
    gl().glGetVertexAttribLui64vNV(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribLui64vNV(
    int index,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetVertexAttribLui64vNV(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribdvARB(
    int index,
    int pname,
    DoubleBuffer params) {
    gl().glGetVertexAttribdvARB(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribdvARB(
    int index,
    int pname,
    double[] params,
    int params_offset) {
    gl().glGetVertexAttribdvARB(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribfvARB(
    int index,
    int pname,
    FloatBuffer params) {
    gl().glGetVertexAttribfvARB(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribfvARB(
    int index,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetVertexAttribfvARB(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribivARB(
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetVertexAttribivARB(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribivARB(
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVertexAttribivARB(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVideoCaptureStreamdvNV(
    int video_capture_slot,
    int stream,
    int pname,
    DoubleBuffer params) {
    gl().glGetVideoCaptureStreamdvNV(
      video_capture_slot,
      stream,
      pname,
      params);
  }

  public static void glGetVideoCaptureStreamdvNV(
    int video_capture_slot,
    int stream,
    int pname,
    double[] params,
    int params_offset) {
    gl().glGetVideoCaptureStreamdvNV(
      video_capture_slot,
      stream,
      pname,
      params,
      params_offset);
  }

  public static void glGetVideoCaptureStreamfvNV(
    int video_capture_slot,
    int stream,
    int pname,
    FloatBuffer params) {
    gl().glGetVideoCaptureStreamfvNV(
      video_capture_slot,
      stream,
      pname,
      params);
  }

  public static void glGetVideoCaptureStreamfvNV(
    int video_capture_slot,
    int stream,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetVideoCaptureStreamfvNV(
      video_capture_slot,
      stream,
      pname,
      params,
      params_offset);
  }

  public static void glGetVideoCaptureStreamivNV(
    int video_capture_slot,
    int stream,
    int pname,
    IntBuffer params) {
    gl().glGetVideoCaptureStreamivNV(
      video_capture_slot,
      stream,
      pname,
      params);
  }

  public static void glGetVideoCaptureStreamivNV(
    int video_capture_slot,
    int stream,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVideoCaptureStreamivNV(
      video_capture_slot,
      stream,
      pname,
      params,
      params_offset);
  }

  public static void glGetVideoCaptureivNV(
    int video_capture_slot,
    int pname,
    IntBuffer params) {
    gl().glGetVideoCaptureivNV(
      video_capture_slot,
      pname,
      params);
  }

  public static void glGetVideoCaptureivNV(
    int video_capture_slot,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVideoCaptureivNV(
      video_capture_slot,
      pname,
      params,
      params_offset);
  }

  public static void glHintPGI(
    int target,
    int mode) {
    gl().glHintPGI(
      target,
      mode);
  }

  public static void glHistogram(
    int target,
    int width,
    int internalformat,
    boolean sink) {
    gl().glHistogram(
      target,
      width,
      internalformat,
      sink);
  }

  public static void glIndexFuncEXT(
    int func,
    float ref) {
    gl().glIndexFuncEXT(
      func,
      ref);
  }

  public static void glIndexMask(
    int mask) {
    gl().glIndexMask(
      mask);
  }

  public static void glIndexMaterialEXT(
    int face,
    int mode) {
    gl().glIndexMaterialEXT(
      face,
      mode);
  }

  public static void glIndexPointer(
    int type,
    int stride,
    Buffer ptr) {
    gl().glIndexPointer(
      type,
      stride,
      ptr);
  }

  public static void glIndexd(
    double c) {
    gl().glIndexd(
      c);
  }

  public static void glIndexdv(
    DoubleBuffer c) {
    gl().glIndexdv(
      c);
  }

  public static void glIndexdv(
    double[] c,
    int c_offset) {
    gl().glIndexdv(
      c,
      c_offset);
  }

  public static void glIndexf(
    float c) {
    gl().glIndexf(
      c);
  }

  public static void glIndexfv(
    FloatBuffer c) {
    gl().glIndexfv(
      c);
  }

  public static void glIndexfv(
    float[] c,
    int c_offset) {
    gl().glIndexfv(
      c,
      c_offset);
  }

  public static void glIndexi(
    int c) {
    gl().glIndexi(
      c);
  }

  public static void glIndexiv(
    IntBuffer c) {
    gl().glIndexiv(
      c);
  }

  public static void glIndexiv(
    int[] c,
    int c_offset) {
    gl().glIndexiv(
      c,
      c_offset);
  }

  public static void glIndexs(
    short c) {
    gl().glIndexs(
      c);
  }

  public static void glIndexsv(
    ShortBuffer c) {
    gl().glIndexsv(
      c);
  }

  public static void glIndexsv(
    short[] c,
    int c_offset) {
    gl().glIndexsv(
      c,
      c_offset);
  }

  public static void glIndexub(
    byte c) {
    gl().glIndexub(
      c);
  }

  public static void glIndexubv(
    ByteBuffer c) {
    gl().glIndexubv(
      c);
  }

  public static void glIndexubv(
    byte[] c,
    int c_offset) {
    gl().glIndexubv(
      c,
      c_offset);
  }

  public static void glInitNames(
    ) {
    gl().glInitNames();
  }

  public static void glInsertComponentEXT(
    int res,
    int src,
    int num) {
    gl().glInsertComponentEXT(
      res,
      src,
      num);
  }

  public static void glInterleavedArrays(
    int format,
    int stride,
    Buffer pointer) {
    gl().glInterleavedArrays(
      format,
      stride,
      pointer);
  }

  public static void glInterleavedArrays(
    int format,
    int stride,
    long pointer_buffer_offset) {
    gl().glInterleavedArrays(
      format,
      stride,
      pointer_buffer_offset);
  }

  public static boolean glIsEnabledIndexed(
    int target,
    int index) {
    return gl().glIsEnabledIndexed(
      target,
      index);
  }

  public static boolean glIsFenceAPPLE(
    int fence) {
    return gl().glIsFenceAPPLE(
      fence);
  }

  public static boolean glIsFenceNV(
    int fence) {
    return gl().glIsFenceNV(
      fence);
  }

  public static boolean glIsList(
    int list) {
    return gl().glIsList(
      list);
  }

  public static boolean glIsNameAMD(
    int identifier,
    int name) {
    return gl().glIsNameAMD(
      identifier,
      name);
  }

  public static boolean glIsOcclusionQueryNV(
    int id) {
    return gl().glIsOcclusionQueryNV(
      id);
  }

  public static boolean glIsProgramARB(
    int program) {
    return gl().glIsProgramARB(
      program);
  }

  public static boolean glIsTransformFeedbackNV(
    int id) {
    return gl().glIsTransformFeedbackNV(
      id);
  }

  public static boolean glIsVariantEnabledEXT(
    int id,
    int cap) {
    return gl().glIsVariantEnabledEXT(
      id,
      cap);
  }

  public static boolean glIsVertexAttribEnabledAPPLE(
    int index,
    int pname) {
    return gl().glIsVertexAttribEnabledAPPLE(
      index,
      pname);
  }

  public static void glLightModeli(
    int pname,
    int param) {
    gl().glLightModeli(
      pname,
      param);
  }

  public static void glLightModeliv(
    int pname,
    IntBuffer params) {
    gl().glLightModeliv(
      pname,
      params);
  }

  public static void glLightModeliv(
    int pname,
    int[] params,
    int params_offset) {
    gl().glLightModeliv(
      pname,
      params,
      params_offset);
  }

  public static void glLighti(
    int light,
    int pname,
    int param) {
    gl().glLighti(
      light,
      pname,
      param);
  }

  public static void glLightiv(
    int light,
    int pname,
    IntBuffer params) {
    gl().glLightiv(
      light,
      pname,
      params);
  }

  public static void glLightiv(
    int light,
    int pname,
    int[] params,
    int params_offset) {
    gl().glLightiv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glLineStipple(
    int factor,
    short pattern) {
    gl().glLineStipple(
      factor,
      pattern);
  }

  public static void glLinkProgramARB(
    int programObj) {
    gl().glLinkProgramARB(
      programObj);
  }

  public static void glListBase(
    int base) {
    gl().glListBase(
      base);
  }

  public static void glLoadMatrixd(
    DoubleBuffer m) {
    gl().glLoadMatrixd(
      m);
  }

  public static void glLoadMatrixd(
    double[] m,
    int m_offset) {
    gl().glLoadMatrixd(
      m,
      m_offset);
  }

  public static void glLoadName(
    int name) {
    gl().glLoadName(
      name);
  }

  public static void glLoadTransposeMatrixd(
    DoubleBuffer m) {
    gl().glLoadTransposeMatrixd(
      m);
  }

  public static void glLoadTransposeMatrixd(
    double[] m,
    int m_offset) {
    gl().glLoadTransposeMatrixd(
      m,
      m_offset);
  }

  public static void glLoadTransposeMatrixf(
    FloatBuffer m) {
    gl().glLoadTransposeMatrixf(
      m);
  }

  public static void glLoadTransposeMatrixf(
    float[] m,
    int m_offset) {
    gl().glLoadTransposeMatrixf(
      m,
      m_offset);
  }

  public static void glLockArraysEXT(
    int first,
    int count) {
    gl().glLockArraysEXT(
      first,
      count);
  }

  public static void glMap1d(
    int target,
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

  public static void glMap1d(
    int target,
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

  public static void glMap1f(
    int target,
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

  public static void glMap1f(
    int target,
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

  public static void glMap2d(
    int target,
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

  public static void glMap2d(
    int target,
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

  public static void glMap2f(
    int target,
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

  public static void glMap2f(
    int target,
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

  public static void glMapControlPointsNV(
    int target,
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

  public static void glMapGrid1d(
    int un,
    double u1,
    double u2) {
    gl().glMapGrid1d(
      un,
      u1,
      u2);
  }

  public static void glMapGrid1f(
    int un,
    float u1,
    float u2) {
    gl().glMapGrid1f(
      un,
      u1,
      u2);
  }

  public static void glMapGrid2d(
    int un,
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

  public static void glMapGrid2f(
    int un,
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

  public static ByteBuffer glMapNamedBufferEXT(
    int buffer,
    int access) {
    return gl().glMapNamedBufferEXT(
      buffer,
      access);
  }

  public static ByteBuffer glMapNamedBufferRangeEXT(
    int buffer,
    long offset,
    long length,
    int access) {
    return gl().glMapNamedBufferRangeEXT(
      buffer,
      offset,
      length,
      access);
  }

  public static void glMapParameterfvNV(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glMapParameterfvNV(
      target,
      pname,
      params);
  }

  public static void glMapParameterfvNV(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glMapParameterfvNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMapParameterivNV(
    int target,
    int pname,
    IntBuffer params) {
    gl().glMapParameterivNV(
      target,
      pname,
      params);
  }

  public static void glMapParameterivNV(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glMapParameterivNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMapVertexAttrib1dAPPLE(
    int index,
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

  public static void glMapVertexAttrib1dAPPLE(
    int index,
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

  public static void glMapVertexAttrib1fAPPLE(
    int index,
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

  public static void glMapVertexAttrib1fAPPLE(
    int index,
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

  public static void glMapVertexAttrib2dAPPLE(
    int index,
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

  public static void glMapVertexAttrib2dAPPLE(
    int index,
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

  public static void glMapVertexAttrib2fAPPLE(
    int index,
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

  public static void glMapVertexAttrib2fAPPLE(
    int index,
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

  public static void glMateriali(
    int face,
    int pname,
    int param) {
    gl().glMateriali(
      face,
      pname,
      param);
  }

  public static void glMaterialiv(
    int face,
    int pname,
    IntBuffer params) {
    gl().glMaterialiv(
      face,
      pname,
      params);
  }

  public static void glMaterialiv(
    int face,
    int pname,
    int[] params,
    int params_offset) {
    gl().glMaterialiv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glMatrixFrustumEXT(
    int mode,
    double left,
    double right,
    double bottom,
    double top,
    double zNear,
    double zFar) {
    gl().glMatrixFrustumEXT(
      mode,
      left,
      right,
      bottom,
      top,
      zNear,
      zFar);
  }

  public static void glMatrixIndexubvARB(
    int size,
    ByteBuffer indices) {
    gl().glMatrixIndexubvARB(
      size,
      indices);
  }

  public static void glMatrixIndexubvARB(
    int size,
    byte[] indices,
    int indices_offset) {
    gl().glMatrixIndexubvARB(
      size,
      indices,
      indices_offset);
  }

  public static void glMatrixIndexuivARB(
    int size,
    IntBuffer indices) {
    gl().glMatrixIndexuivARB(
      size,
      indices);
  }

  public static void glMatrixIndexuivARB(
    int size,
    int[] indices,
    int indices_offset) {
    gl().glMatrixIndexuivARB(
      size,
      indices,
      indices_offset);
  }

  public static void glMatrixIndexusvARB(
    int size,
    ShortBuffer indices) {
    gl().glMatrixIndexusvARB(
      size,
      indices);
  }

  public static void glMatrixIndexusvARB(
    int size,
    short[] indices,
    int indices_offset) {
    gl().glMatrixIndexusvARB(
      size,
      indices,
      indices_offset);
  }

  public static void glMatrixLoadIdentityEXT(
    int mode) {
    gl().glMatrixLoadIdentityEXT(
      mode);
  }

  public static void glMatrixLoadTransposedEXT(
    int mode,
    DoubleBuffer m) {
    gl().glMatrixLoadTransposedEXT(
      mode,
      m);
  }

  public static void glMatrixLoadTransposedEXT(
    int mode,
    double[] m,
    int m_offset) {
    gl().glMatrixLoadTransposedEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixLoadTransposefEXT(
    int mode,
    FloatBuffer m) {
    gl().glMatrixLoadTransposefEXT(
      mode,
      m);
  }

  public static void glMatrixLoadTransposefEXT(
    int mode,
    float[] m,
    int m_offset) {
    gl().glMatrixLoadTransposefEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixLoaddEXT(
    int mode,
    DoubleBuffer m) {
    gl().glMatrixLoaddEXT(
      mode,
      m);
  }

  public static void glMatrixLoaddEXT(
    int mode,
    double[] m,
    int m_offset) {
    gl().glMatrixLoaddEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixLoadfEXT(
    int mode,
    FloatBuffer m) {
    gl().glMatrixLoadfEXT(
      mode,
      m);
  }

  public static void glMatrixLoadfEXT(
    int mode,
    float[] m,
    int m_offset) {
    gl().glMatrixLoadfEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixMultTransposedEXT(
    int mode,
    DoubleBuffer m) {
    gl().glMatrixMultTransposedEXT(
      mode,
      m);
  }

  public static void glMatrixMultTransposedEXT(
    int mode,
    double[] m,
    int m_offset) {
    gl().glMatrixMultTransposedEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixMultTransposefEXT(
    int mode,
    FloatBuffer m) {
    gl().glMatrixMultTransposefEXT(
      mode,
      m);
  }

  public static void glMatrixMultTransposefEXT(
    int mode,
    float[] m,
    int m_offset) {
    gl().glMatrixMultTransposefEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixMultdEXT(
    int mode,
    DoubleBuffer m) {
    gl().glMatrixMultdEXT(
      mode,
      m);
  }

  public static void glMatrixMultdEXT(
    int mode,
    double[] m,
    int m_offset) {
    gl().glMatrixMultdEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixMultfEXT(
    int mode,
    FloatBuffer m) {
    gl().glMatrixMultfEXT(
      mode,
      m);
  }

  public static void glMatrixMultfEXT(
    int mode,
    float[] m,
    int m_offset) {
    gl().glMatrixMultfEXT(
      mode,
      m,
      m_offset);
  }

  public static void glMatrixOrthoEXT(
    int mode,
    double left,
    double right,
    double bottom,
    double top,
    double zNear,
    double zFar) {
    gl().glMatrixOrthoEXT(
      mode,
      left,
      right,
      bottom,
      top,
      zNear,
      zFar);
  }

  public static void glMatrixPopEXT(
    int mode) {
    gl().glMatrixPopEXT(
      mode);
  }

  public static void glMatrixPushEXT(
    int mode) {
    gl().glMatrixPushEXT(
      mode);
  }

  public static void glMatrixRotatedEXT(
    int mode,
    double angle,
    double x,
    double y,
    double z) {
    gl().glMatrixRotatedEXT(
      mode,
      angle,
      x,
      y,
      z);
  }

  public static void glMatrixRotatefEXT(
    int mode,
    float angle,
    float x,
    float y,
    float z) {
    gl().glMatrixRotatefEXT(
      mode,
      angle,
      x,
      y,
      z);
  }

  public static void glMatrixScaledEXT(
    int mode,
    double x,
    double y,
    double z) {
    gl().glMatrixScaledEXT(
      mode,
      x,
      y,
      z);
  }

  public static void glMatrixScalefEXT(
    int mode,
    float x,
    float y,
    float z) {
    gl().glMatrixScalefEXT(
      mode,
      x,
      y,
      z);
  }

  public static void glMatrixTranslatedEXT(
    int mode,
    double x,
    double y,
    double z) {
    gl().glMatrixTranslatedEXT(
      mode,
      x,
      y,
      z);
  }

  public static void glMatrixTranslatefEXT(
    int mode,
    float x,
    float y,
    float z) {
    gl().glMatrixTranslatefEXT(
      mode,
      x,
      y,
      z);
  }

  public static void glMinmax(
    int target,
    int internalformat,
    boolean sink) {
    gl().glMinmax(
      target,
      internalformat,
      sink);
  }

  public static void glMultMatrixd(
    DoubleBuffer m) {
    gl().glMultMatrixd(
      m);
  }

  public static void glMultMatrixd(
    double[] m,
    int m_offset) {
    gl().glMultMatrixd(
      m,
      m_offset);
  }

  public static void glMultTransposeMatrixd(
    DoubleBuffer m) {
    gl().glMultTransposeMatrixd(
      m);
  }

  public static void glMultTransposeMatrixd(
    double[] m,
    int m_offset) {
    gl().glMultTransposeMatrixd(
      m,
      m_offset);
  }

  public static void glMultTransposeMatrixf(
    FloatBuffer m) {
    gl().glMultTransposeMatrixf(
      m);
  }

  public static void glMultTransposeMatrixf(
    float[] m,
    int m_offset) {
    gl().glMultTransposeMatrixf(
      m,
      m_offset);
  }

  public static void glMultiTexBufferEXT(
    int texunit,
    int target,
    int internalformat,
    int buffer) {
    gl().glMultiTexBufferEXT(
      texunit,
      target,
      internalformat,
      buffer);
  }

  public static void glMultiTexCoord1d(
    int target,
    double s) {
    gl().glMultiTexCoord1d(
      target,
      s);
  }

  public static void glMultiTexCoord1dv(
    int target,
    DoubleBuffer v) {
    gl().glMultiTexCoord1dv(
      target,
      v);
  }

  public static void glMultiTexCoord1dv(
    int target,
    double[] v,
    int v_offset) {
    gl().glMultiTexCoord1dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1f(
    int target,
    float s) {
    gl().glMultiTexCoord1f(
      target,
      s);
  }

  public static void glMultiTexCoord1fv(
    int target,
    FloatBuffer v) {
    gl().glMultiTexCoord1fv(
      target,
      v);
  }

  public static void glMultiTexCoord1fv(
    int target,
    float[] v,
    int v_offset) {
    gl().glMultiTexCoord1fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1h(
    int target,
    short s) {
    gl().glMultiTexCoord1h(
      target,
      s);
  }

  public static void glMultiTexCoord1hv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord1hv(
      target,
      v);
  }

  public static void glMultiTexCoord1hv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord1hv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1i(
    int target,
    int s) {
    gl().glMultiTexCoord1i(
      target,
      s);
  }

  public static void glMultiTexCoord1iv(
    int target,
    IntBuffer v) {
    gl().glMultiTexCoord1iv(
      target,
      v);
  }

  public static void glMultiTexCoord1iv(
    int target,
    int[] v,
    int v_offset) {
    gl().glMultiTexCoord1iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord1s(
    int target,
    short s) {
    gl().glMultiTexCoord1s(
      target,
      s);
  }

  public static void glMultiTexCoord1sv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord1sv(
      target,
      v);
  }

  public static void glMultiTexCoord1sv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord1sv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2d(
    int target,
    double s,
    double t) {
    gl().glMultiTexCoord2d(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2dv(
    int target,
    DoubleBuffer v) {
    gl().glMultiTexCoord2dv(
      target,
      v);
  }

  public static void glMultiTexCoord2dv(
    int target,
    double[] v,
    int v_offset) {
    gl().glMultiTexCoord2dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2f(
    int target,
    float s,
    float t) {
    gl().glMultiTexCoord2f(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2fv(
    int target,
    FloatBuffer v) {
    gl().glMultiTexCoord2fv(
      target,
      v);
  }

  public static void glMultiTexCoord2fv(
    int target,
    float[] v,
    int v_offset) {
    gl().glMultiTexCoord2fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2h(
    int target,
    short s,
    short t) {
    gl().glMultiTexCoord2h(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2hv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord2hv(
      target,
      v);
  }

  public static void glMultiTexCoord2hv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord2hv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2i(
    int target,
    int s,
    int t) {
    gl().glMultiTexCoord2i(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2iv(
    int target,
    IntBuffer v) {
    gl().glMultiTexCoord2iv(
      target,
      v);
  }

  public static void glMultiTexCoord2iv(
    int target,
    int[] v,
    int v_offset) {
    gl().glMultiTexCoord2iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord2s(
    int target,
    short s,
    short t) {
    gl().glMultiTexCoord2s(
      target,
      s,
      t);
  }

  public static void glMultiTexCoord2sv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord2sv(
      target,
      v);
  }

  public static void glMultiTexCoord2sv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord2sv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3d(
    int target,
    double s,
    double t,
    double r) {
    gl().glMultiTexCoord3d(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3dv(
    int target,
    DoubleBuffer v) {
    gl().glMultiTexCoord3dv(
      target,
      v);
  }

  public static void glMultiTexCoord3dv(
    int target,
    double[] v,
    int v_offset) {
    gl().glMultiTexCoord3dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3f(
    int target,
    float s,
    float t,
    float r) {
    gl().glMultiTexCoord3f(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3fv(
    int target,
    FloatBuffer v) {
    gl().glMultiTexCoord3fv(
      target,
      v);
  }

  public static void glMultiTexCoord3fv(
    int target,
    float[] v,
    int v_offset) {
    gl().glMultiTexCoord3fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3h(
    int target,
    short s,
    short t,
    short r) {
    gl().glMultiTexCoord3h(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3hv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord3hv(
      target,
      v);
  }

  public static void glMultiTexCoord3hv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord3hv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3i(
    int target,
    int s,
    int t,
    int r) {
    gl().glMultiTexCoord3i(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3iv(
    int target,
    IntBuffer v) {
    gl().glMultiTexCoord3iv(
      target,
      v);
  }

  public static void glMultiTexCoord3iv(
    int target,
    int[] v,
    int v_offset) {
    gl().glMultiTexCoord3iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord3s(
    int target,
    short s,
    short t,
    short r) {
    gl().glMultiTexCoord3s(
      target,
      s,
      t,
      r);
  }

  public static void glMultiTexCoord3sv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord3sv(
      target,
      v);
  }

  public static void glMultiTexCoord3sv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord3sv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4d(
    int target,
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

  public static void glMultiTexCoord4dv(
    int target,
    DoubleBuffer v) {
    gl().glMultiTexCoord4dv(
      target,
      v);
  }

  public static void glMultiTexCoord4dv(
    int target,
    double[] v,
    int v_offset) {
    gl().glMultiTexCoord4dv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4fv(
    int target,
    FloatBuffer v) {
    gl().glMultiTexCoord4fv(
      target,
      v);
  }

  public static void glMultiTexCoord4fv(
    int target,
    float[] v,
    int v_offset) {
    gl().glMultiTexCoord4fv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4h(
    int target,
    short s,
    short t,
    short r,
    short q) {
    gl().glMultiTexCoord4h(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glMultiTexCoord4hv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord4hv(
      target,
      v);
  }

  public static void glMultiTexCoord4hv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord4hv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4i(
    int target,
    int s,
    int t,
    int r,
    int q) {
    gl().glMultiTexCoord4i(
      target,
      s,
      t,
      r,
      q);
  }

  public static void glMultiTexCoord4iv(
    int target,
    IntBuffer v) {
    gl().glMultiTexCoord4iv(
      target,
      v);
  }

  public static void glMultiTexCoord4iv(
    int target,
    int[] v,
    int v_offset) {
    gl().glMultiTexCoord4iv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoord4s(
    int target,
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

  public static void glMultiTexCoord4sv(
    int target,
    ShortBuffer v) {
    gl().glMultiTexCoord4sv(
      target,
      v);
  }

  public static void glMultiTexCoord4sv(
    int target,
    short[] v,
    int v_offset) {
    gl().glMultiTexCoord4sv(
      target,
      v,
      v_offset);
  }

  public static void glMultiTexCoordPointerEXT(
    int texunit,
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glMultiTexCoordPointerEXT(
      texunit,
      size,
      type,
      stride,
      pointer);
  }

  public static void glMultiTexEnvfEXT(
    int texunit,
    int target,
    int pname,
    float param) {
    gl().glMultiTexEnvfEXT(
      texunit,
      target,
      pname,
      param);
  }

  public static void glMultiTexEnvfvEXT(
    int texunit,
    int target,
    int pname,
    FloatBuffer params) {
    gl().glMultiTexEnvfvEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glMultiTexEnvfvEXT(
    int texunit,
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glMultiTexEnvfvEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexEnviEXT(
    int texunit,
    int target,
    int pname,
    int param) {
    gl().glMultiTexEnviEXT(
      texunit,
      target,
      pname,
      param);
  }

  public static void glMultiTexEnvivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glMultiTexEnvivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glMultiTexEnvivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glMultiTexEnvivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexGendEXT(
    int texunit,
    int coord,
    int pname,
    double param) {
    gl().glMultiTexGendEXT(
      texunit,
      coord,
      pname,
      param);
  }

  public static void glMultiTexGendvEXT(
    int texunit,
    int coord,
    int pname,
    DoubleBuffer params) {
    gl().glMultiTexGendvEXT(
      texunit,
      coord,
      pname,
      params);
  }

  public static void glMultiTexGendvEXT(
    int texunit,
    int coord,
    int pname,
    double[] params,
    int params_offset) {
    gl().glMultiTexGendvEXT(
      texunit,
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexGenfEXT(
    int texunit,
    int coord,
    int pname,
    float param) {
    gl().glMultiTexGenfEXT(
      texunit,
      coord,
      pname,
      param);
  }

  public static void glMultiTexGenfvEXT(
    int texunit,
    int coord,
    int pname,
    FloatBuffer params) {
    gl().glMultiTexGenfvEXT(
      texunit,
      coord,
      pname,
      params);
  }

  public static void glMultiTexGenfvEXT(
    int texunit,
    int coord,
    int pname,
    float[] params,
    int params_offset) {
    gl().glMultiTexGenfvEXT(
      texunit,
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexGeniEXT(
    int texunit,
    int coord,
    int pname,
    int param) {
    gl().glMultiTexGeniEXT(
      texunit,
      coord,
      pname,
      param);
  }

  public static void glMultiTexGenivEXT(
    int texunit,
    int coord,
    int pname,
    IntBuffer params) {
    gl().glMultiTexGenivEXT(
      texunit,
      coord,
      pname,
      params);
  }

  public static void glMultiTexGenivEXT(
    int texunit,
    int coord,
    int pname,
    int[] params,
    int params_offset) {
    gl().glMultiTexGenivEXT(
      texunit,
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexImage1DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int width,
    int border,
    int format,
    int type,
    Buffer pixels) {
    gl().glMultiTexImage1DEXT(
      texunit,
      target,
      level,
      internalformat,
      width,
      border,
      format,
      type,
      pixels);
  }

  public static void glMultiTexImage2DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int border,
    int format,
    int type,
    Buffer pixels) {
    gl().glMultiTexImage2DEXT(
      texunit,
      target,
      level,
      internalformat,
      width,
      height,
      border,
      format,
      type,
      pixels);
  }

  public static void glMultiTexImage3DEXT(
    int texunit,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int depth,
    int border,
    int format,
    int type,
    Buffer pixels) {
    gl().glMultiTexImage3DEXT(
      texunit,
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

  public static void glMultiTexParameterIivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glMultiTexParameterIivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glMultiTexParameterIivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glMultiTexParameterIivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexParameterIuivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glMultiTexParameterIuivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glMultiTexParameterIuivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glMultiTexParameterIuivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexParameterfEXT(
    int texunit,
    int target,
    int pname,
    float param) {
    gl().glMultiTexParameterfEXT(
      texunit,
      target,
      pname,
      param);
  }

  public static void glMultiTexParameterfvEXT(
    int texunit,
    int target,
    int pname,
    FloatBuffer params) {
    gl().glMultiTexParameterfvEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glMultiTexParameterfvEXT(
    int texunit,
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glMultiTexParameterfvEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexParameteriEXT(
    int texunit,
    int target,
    int pname,
    int param) {
    gl().glMultiTexParameteriEXT(
      texunit,
      target,
      pname,
      param);
  }

  public static void glMultiTexParameterivEXT(
    int texunit,
    int target,
    int pname,
    IntBuffer params) {
    gl().glMultiTexParameterivEXT(
      texunit,
      target,
      pname,
      params);
  }

  public static void glMultiTexParameterivEXT(
    int texunit,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glMultiTexParameterivEXT(
      texunit,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glMultiTexRenderbufferEXT(
    int texunit,
    int target,
    int renderbuffer) {
    gl().glMultiTexRenderbufferEXT(
      texunit,
      target,
      renderbuffer);
  }

  public static void glMultiTexSubImage1DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int width,
    int format,
    int type,
    Buffer pixels) {
    gl().glMultiTexSubImage1DEXT(
      texunit,
      target,
      level,
      xoffset,
      width,
      format,
      type,
      pixels);
  }

  public static void glMultiTexSubImage2DEXT(
    int texunit,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int width,
    int height,
    int format,
    int type,
    Buffer pixels) {
    gl().glMultiTexSubImage2DEXT(
      texunit,
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

  public static void glMultiTexSubImage3DEXT(
    int texunit,
    int target,
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
    gl().glMultiTexSubImage3DEXT(
      texunit,
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

  public static void glNamedBufferDataEXT(
    int buffer,
    long size,
    Buffer data,
    int usage) {
    gl().glNamedBufferDataEXT(
      buffer,
      size,
      data,
      usage);
  }

  public static void glNamedBufferSubDataEXT(
    int buffer,
    long offset,
    long size,
    Buffer data) {
    gl().glNamedBufferSubDataEXT(
      buffer,
      offset,
      size,
      data);
  }

  public static void glNamedCopyBufferSubDataEXT(
    int readBuffer,
    int writeBuffer,
    long readOffset,
    long writeOffset,
    long size) {
    gl().glNamedCopyBufferSubDataEXT(
      readBuffer,
      writeBuffer,
      readOffset,
      writeOffset,
      size);
  }

  public static void glNamedFramebufferRenderbufferEXT(
    int framebuffer,
    int attachment,
    int renderbuffertarget,
    int renderbuffer) {
    gl().glNamedFramebufferRenderbufferEXT(
      framebuffer,
      attachment,
      renderbuffertarget,
      renderbuffer);
  }

  public static void glNamedFramebufferTexture1DEXT(
    int framebuffer,
    int attachment,
    int textarget,
    int texture,
    int level) {
    gl().glNamedFramebufferTexture1DEXT(
      framebuffer,
      attachment,
      textarget,
      texture,
      level);
  }

  public static void glNamedFramebufferTexture2DEXT(
    int framebuffer,
    int attachment,
    int textarget,
    int texture,
    int level) {
    gl().glNamedFramebufferTexture2DEXT(
      framebuffer,
      attachment,
      textarget,
      texture,
      level);
  }

  public static void glNamedFramebufferTexture3DEXT(
    int framebuffer,
    int attachment,
    int textarget,
    int texture,
    int level,
    int zoffset) {
    gl().glNamedFramebufferTexture3DEXT(
      framebuffer,
      attachment,
      textarget,
      texture,
      level,
      zoffset);
  }

  public static void glNamedFramebufferTextureEXT(
    int framebuffer,
    int attachment,
    int texture,
    int level) {
    gl().glNamedFramebufferTextureEXT(
      framebuffer,
      attachment,
      texture,
      level);
  }

  public static void glNamedFramebufferTextureFaceEXT(
    int framebuffer,
    int attachment,
    int texture,
    int level,
    int face) {
    gl().glNamedFramebufferTextureFaceEXT(
      framebuffer,
      attachment,
      texture,
      level,
      face);
  }

  public static void glNamedFramebufferTextureLayerEXT(
    int framebuffer,
    int attachment,
    int texture,
    int level,
    int layer) {
    gl().glNamedFramebufferTextureLayerEXT(
      framebuffer,
      attachment,
      texture,
      level,
      layer);
  }

  public static void glNamedProgramLocalParameter4dEXT(
    int program,
    int target,
    int index,
    double x,
    double y,
    double z,
    double w) {
    gl().glNamedProgramLocalParameter4dEXT(
      program,
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glNamedProgramLocalParameter4dvEXT(
    int program,
    int target,
    int index,
    DoubleBuffer params) {
    gl().glNamedProgramLocalParameter4dvEXT(
      program,
      target,
      index,
      params);
  }

  public static void glNamedProgramLocalParameter4dvEXT(
    int program,
    int target,
    int index,
    double[] params,
    int params_offset) {
    gl().glNamedProgramLocalParameter4dvEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glNamedProgramLocalParameter4fEXT(
    int program,
    int target,
    int index,
    float x,
    float y,
    float z,
    float w) {
    gl().glNamedProgramLocalParameter4fEXT(
      program,
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glNamedProgramLocalParameter4fvEXT(
    int program,
    int target,
    int index,
    FloatBuffer params) {
    gl().glNamedProgramLocalParameter4fvEXT(
      program,
      target,
      index,
      params);
  }

  public static void glNamedProgramLocalParameter4fvEXT(
    int program,
    int target,
    int index,
    float[] params,
    int params_offset) {
    gl().glNamedProgramLocalParameter4fvEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glNamedProgramLocalParameterI4iEXT(
    int program,
    int target,
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glNamedProgramLocalParameterI4iEXT(
      program,
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glNamedProgramLocalParameterI4ivEXT(
    int program,
    int target,
    int index,
    IntBuffer params) {
    gl().glNamedProgramLocalParameterI4ivEXT(
      program,
      target,
      index,
      params);
  }

  public static void glNamedProgramLocalParameterI4ivEXT(
    int program,
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glNamedProgramLocalParameterI4ivEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glNamedProgramLocalParameterI4uiEXT(
    int program,
    int target,
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glNamedProgramLocalParameterI4uiEXT(
      program,
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glNamedProgramLocalParameterI4uivEXT(
    int program,
    int target,
    int index,
    IntBuffer params) {
    gl().glNamedProgramLocalParameterI4uivEXT(
      program,
      target,
      index,
      params);
  }

  public static void glNamedProgramLocalParameterI4uivEXT(
    int program,
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glNamedProgramLocalParameterI4uivEXT(
      program,
      target,
      index,
      params,
      params_offset);
  }

  public static void glNamedProgramLocalParameters4fvEXT(
    int program,
    int target,
    int index,
    int count,
    FloatBuffer params) {
    gl().glNamedProgramLocalParameters4fvEXT(
      program,
      target,
      index,
      count,
      params);
  }

  public static void glNamedProgramLocalParameters4fvEXT(
    int program,
    int target,
    int index,
    int count,
    float[] params,
    int params_offset) {
    gl().glNamedProgramLocalParameters4fvEXT(
      program,
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glNamedProgramLocalParametersI4ivEXT(
    int program,
    int target,
    int index,
    int count,
    IntBuffer params) {
    gl().glNamedProgramLocalParametersI4ivEXT(
      program,
      target,
      index,
      count,
      params);
  }

  public static void glNamedProgramLocalParametersI4ivEXT(
    int program,
    int target,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glNamedProgramLocalParametersI4ivEXT(
      program,
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glNamedProgramLocalParametersI4uivEXT(
    int program,
    int target,
    int index,
    int count,
    IntBuffer params) {
    gl().glNamedProgramLocalParametersI4uivEXT(
      program,
      target,
      index,
      count,
      params);
  }

  public static void glNamedProgramLocalParametersI4uivEXT(
    int program,
    int target,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glNamedProgramLocalParametersI4uivEXT(
      program,
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glNamedProgramStringEXT(
    int program,
    int target,
    int format,
    int len,
    Buffer string) {
    gl().glNamedProgramStringEXT(
      program,
      target,
      format,
      len,
      string);
  }

  public static void glNamedRenderbufferStorageEXT(
    int renderbuffer,
    int internalformat,
    int width,
    int height) {
    gl().glNamedRenderbufferStorageEXT(
      renderbuffer,
      internalformat,
      width,
      height);
  }

  public static void glNamedRenderbufferStorageMultisampleCoverageEXT(
    int renderbuffer,
    int coverageSamples,
    int colorSamples,
    int internalformat,
    int width,
    int height) {
    gl().glNamedRenderbufferStorageMultisampleCoverageEXT(
      renderbuffer,
      coverageSamples,
      colorSamples,
      internalformat,
      width,
      height);
  }

  public static void glNamedRenderbufferStorageMultisampleEXT(
    int renderbuffer,
    int samples,
    int internalformat,
    int width,
    int height) {
    gl().glNamedRenderbufferStorageMultisampleEXT(
      renderbuffer,
      samples,
      internalformat,
      width,
      height);
  }

  public static void glNewList(
    int list,
    int mode) {
    gl().glNewList(
      list,
      mode);
  }

  public static void glNormal3b(
    byte nx,
    byte ny,
    byte nz) {
    gl().glNormal3b(
      nx,
      ny,
      nz);
  }

  public static void glNormal3bv(
    ByteBuffer v) {
    gl().glNormal3bv(
      v);
  }

  public static void glNormal3bv(
    byte[] v,
    int v_offset) {
    gl().glNormal3bv(
      v,
      v_offset);
  }

  public static void glNormal3d(
    double nx,
    double ny,
    double nz) {
    gl().glNormal3d(
      nx,
      ny,
      nz);
  }

  public static void glNormal3dv(
    DoubleBuffer v) {
    gl().glNormal3dv(
      v);
  }

  public static void glNormal3dv(
    double[] v,
    int v_offset) {
    gl().glNormal3dv(
      v,
      v_offset);
  }

  public static void glNormal3fv(
    FloatBuffer v) {
    gl().glNormal3fv(
      v);
  }

  public static void glNormal3fv(
    float[] v,
    int v_offset) {
    gl().glNormal3fv(
      v,
      v_offset);
  }

  public static void glNormal3h(
    short nx,
    short ny,
    short nz) {
    gl().glNormal3h(
      nx,
      ny,
      nz);
  }

  public static void glNormal3hv(
    ShortBuffer v) {
    gl().glNormal3hv(
      v);
  }

  public static void glNormal3hv(
    short[] v,
    int v_offset) {
    gl().glNormal3hv(
      v,
      v_offset);
  }

  public static void glNormal3i(
    int nx,
    int ny,
    int nz) {
    gl().glNormal3i(
      nx,
      ny,
      nz);
  }

  public static void glNormal3iv(
    IntBuffer v) {
    gl().glNormal3iv(
      v);
  }

  public static void glNormal3iv(
    int[] v,
    int v_offset) {
    gl().glNormal3iv(
      v,
      v_offset);
  }

  public static void glNormal3s(
    short nx,
    short ny,
    short nz) {
    gl().glNormal3s(
      nx,
      ny,
      nz);
  }

  public static void glNormal3sv(
    ShortBuffer v) {
    gl().glNormal3sv(
      v);
  }

  public static void glNormal3sv(
    short[] v,
    int v_offset) {
    gl().glNormal3sv(
      v,
      v_offset);
  }

  public static int glObjectPurgeableAPPLE(
    int objectType,
    int name,
    int option) {
    return gl().glObjectPurgeableAPPLE(
      objectType,
      name,
      option);
  }

  public static int glObjectUnpurgeableAPPLE(
    int objectType,
    int name,
    int option) {
    return gl().glObjectUnpurgeableAPPLE(
      objectType,
      name,
      option);
  }

  public static void glPNTrianglesfATI(
    int pname,
    float param) {
    gl().glPNTrianglesfATI(
      pname,
      param);
  }

  public static void glPNTrianglesiATI(
    int pname,
    int param) {
    gl().glPNTrianglesiATI(
      pname,
      param);
  }

  public static void glPassThrough(
    float token) {
    gl().glPassThrough(
      token);
  }

  public static void glPauseTransformFeedbackNV(
    ) {
    gl().glPauseTransformFeedbackNV();
  }

  public static void glPixelDataRangeNV(
    int target,
    int length,
    Buffer pointer) {
    gl().glPixelDataRangeNV(
      target,
      length,
      pointer);
  }

  public static void glPixelMapfv(
    int map,
    int mapsize,
    FloatBuffer values) {
    gl().glPixelMapfv(
      map,
      mapsize,
      values);
  }

  public static void glPixelMapfv(
    int map,
    int mapsize,
    float[] values,
    int values_offset) {
    gl().glPixelMapfv(
      map,
      mapsize,
      values,
      values_offset);
  }

  public static void glPixelMapfv(
    int map,
    int mapsize,
    long values_buffer_offset) {
    gl().glPixelMapfv(
      map,
      mapsize,
      values_buffer_offset);
  }

  public static void glPixelMapuiv(
    int map,
    int mapsize,
    IntBuffer values) {
    gl().glPixelMapuiv(
      map,
      mapsize,
      values);
  }

  public static void glPixelMapuiv(
    int map,
    int mapsize,
    int[] values,
    int values_offset) {
    gl().glPixelMapuiv(
      map,
      mapsize,
      values,
      values_offset);
  }

  public static void glPixelMapuiv(
    int map,
    int mapsize,
    long values_buffer_offset) {
    gl().glPixelMapuiv(
      map,
      mapsize,
      values_buffer_offset);
  }

  public static void glPixelMapusv(
    int map,
    int mapsize,
    ShortBuffer values) {
    gl().glPixelMapusv(
      map,
      mapsize,
      values);
  }

  public static void glPixelMapusv(
    int map,
    int mapsize,
    short[] values,
    int values_offset) {
    gl().glPixelMapusv(
      map,
      mapsize,
      values,
      values_offset);
  }

  public static void glPixelMapusv(
    int map,
    int mapsize,
    long values_buffer_offset) {
    gl().glPixelMapusv(
      map,
      mapsize,
      values_buffer_offset);
  }

  public static void glPixelTransferf(
    int pname,
    float param) {
    gl().glPixelTransferf(
      pname,
      param);
  }

  public static void glPixelTransferi(
    int pname,
    int param) {
    gl().glPixelTransferi(
      pname,
      param);
  }

  public static void glPixelTransformParameterfEXT(
    int target,
    int pname,
    float param) {
    gl().glPixelTransformParameterfEXT(
      target,
      pname,
      param);
  }

  public static void glPixelTransformParameterfvEXT(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glPixelTransformParameterfvEXT(
      target,
      pname,
      params);
  }

  public static void glPixelTransformParameterfvEXT(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glPixelTransformParameterfvEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glPixelTransformParameteriEXT(
    int target,
    int pname,
    int param) {
    gl().glPixelTransformParameteriEXT(
      target,
      pname,
      param);
  }

  public static void glPixelTransformParameterivEXT(
    int target,
    int pname,
    IntBuffer params) {
    gl().glPixelTransformParameterivEXT(
      target,
      pname,
      params);
  }

  public static void glPixelTransformParameterivEXT(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glPixelTransformParameterivEXT(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glPixelZoom(
    float xfactor,
    float yfactor) {
    gl().glPixelZoom(
      xfactor,
      yfactor);
  }

  public static void glPolygonStipple(
    ByteBuffer mask) {
    gl().glPolygonStipple(
      mask);
  }

  public static void glPolygonStipple(
    byte[] mask,
    int mask_offset) {
    gl().glPolygonStipple(
      mask,
      mask_offset);
  }

  public static void glPolygonStipple(
    long mask_buffer_offset) {
    gl().glPolygonStipple(
      mask_buffer_offset);
  }

  public static void glPopAttrib(
    ) {
    gl().glPopAttrib();
  }

  public static void glPopClientAttrib(
    ) {
    gl().glPopClientAttrib();
  }

  public static void glPopName(
    ) {
    gl().glPopName();
  }

  public static void glPrimitiveRestartIndexNV(
    int index) {
    gl().glPrimitiveRestartIndexNV(
      index);
  }

  public static void glPrimitiveRestartNV(
    ) {
    gl().glPrimitiveRestartNV();
  }

  public static void glPrioritizeTextures(
    int n,
    IntBuffer textures,
    FloatBuffer priorities) {
    gl().glPrioritizeTextures(
      n,
      textures,
      priorities);
  }

  public static void glPrioritizeTextures(
    int n,
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

  public static void glProgramBufferParametersIivNV(
    int target,
    int buffer,
    int index,
    int count,
    IntBuffer params) {
    gl().glProgramBufferParametersIivNV(
      target,
      buffer,
      index,
      count,
      params);
  }

  public static void glProgramBufferParametersIivNV(
    int target,
    int buffer,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glProgramBufferParametersIivNV(
      target,
      buffer,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramBufferParametersIuivNV(
    int target,
    int buffer,
    int index,
    int count,
    IntBuffer params) {
    gl().glProgramBufferParametersIuivNV(
      target,
      buffer,
      index,
      count,
      params);
  }

  public static void glProgramBufferParametersIuivNV(
    int target,
    int buffer,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glProgramBufferParametersIuivNV(
      target,
      buffer,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramBufferParametersfvNV(
    int target,
    int buffer,
    int index,
    int count,
    FloatBuffer params) {
    gl().glProgramBufferParametersfvNV(
      target,
      buffer,
      index,
      count,
      params);
  }

  public static void glProgramBufferParametersfvNV(
    int target,
    int buffer,
    int index,
    int count,
    float[] params,
    int params_offset) {
    gl().glProgramBufferParametersfvNV(
      target,
      buffer,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramEnvParameter4dARB(
    int target,
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

  public static void glProgramEnvParameter4dvARB(
    int target,
    int index,
    DoubleBuffer params) {
    gl().glProgramEnvParameter4dvARB(
      target,
      index,
      params);
  }

  public static void glProgramEnvParameter4dvARB(
    int target,
    int index,
    double[] params,
    int params_offset) {
    gl().glProgramEnvParameter4dvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramEnvParameter4fARB(
    int target,
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

  public static void glProgramEnvParameter4fvARB(
    int target,
    int index,
    FloatBuffer params) {
    gl().glProgramEnvParameter4fvARB(
      target,
      index,
      params);
  }

  public static void glProgramEnvParameter4fvARB(
    int target,
    int index,
    float[] params,
    int params_offset) {
    gl().glProgramEnvParameter4fvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramEnvParameterI4iNV(
    int target,
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glProgramEnvParameterI4iNV(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramEnvParameterI4ivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glProgramEnvParameterI4ivNV(
      target,
      index,
      params);
  }

  public static void glProgramEnvParameterI4ivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glProgramEnvParameterI4ivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramEnvParameterI4uiNV(
    int target,
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glProgramEnvParameterI4uiNV(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramEnvParameterI4uivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glProgramEnvParameterI4uivNV(
      target,
      index,
      params);
  }

  public static void glProgramEnvParameterI4uivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glProgramEnvParameterI4uivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramEnvParameters4fvEXT(
    int target,
    int index,
    int count,
    FloatBuffer params) {
    gl().glProgramEnvParameters4fvEXT(
      target,
      index,
      count,
      params);
  }

  public static void glProgramEnvParameters4fvEXT(
    int target,
    int index,
    int count,
    float[] params,
    int params_offset) {
    gl().glProgramEnvParameters4fvEXT(
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramEnvParametersI4ivNV(
    int target,
    int index,
    int count,
    IntBuffer params) {
    gl().glProgramEnvParametersI4ivNV(
      target,
      index,
      count,
      params);
  }

  public static void glProgramEnvParametersI4ivNV(
    int target,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glProgramEnvParametersI4ivNV(
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramEnvParametersI4uivNV(
    int target,
    int index,
    int count,
    IntBuffer params) {
    gl().glProgramEnvParametersI4uivNV(
      target,
      index,
      count,
      params);
  }

  public static void glProgramEnvParametersI4uivNV(
    int target,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glProgramEnvParametersI4uivNV(
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramLocalParameter4dARB(
    int target,
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

  public static void glProgramLocalParameter4dvARB(
    int target,
    int index,
    DoubleBuffer params) {
    gl().glProgramLocalParameter4dvARB(
      target,
      index,
      params);
  }

  public static void glProgramLocalParameter4dvARB(
    int target,
    int index,
    double[] params,
    int params_offset) {
    gl().glProgramLocalParameter4dvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramLocalParameter4fARB(
    int target,
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

  public static void glProgramLocalParameter4fvARB(
    int target,
    int index,
    FloatBuffer params) {
    gl().glProgramLocalParameter4fvARB(
      target,
      index,
      params);
  }

  public static void glProgramLocalParameter4fvARB(
    int target,
    int index,
    float[] params,
    int params_offset) {
    gl().glProgramLocalParameter4fvARB(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramLocalParameterI4iNV(
    int target,
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glProgramLocalParameterI4iNV(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramLocalParameterI4ivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glProgramLocalParameterI4ivNV(
      target,
      index,
      params);
  }

  public static void glProgramLocalParameterI4ivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glProgramLocalParameterI4ivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramLocalParameterI4uiNV(
    int target,
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glProgramLocalParameterI4uiNV(
      target,
      index,
      x,
      y,
      z,
      w);
  }

  public static void glProgramLocalParameterI4uivNV(
    int target,
    int index,
    IntBuffer params) {
    gl().glProgramLocalParameterI4uivNV(
      target,
      index,
      params);
  }

  public static void glProgramLocalParameterI4uivNV(
    int target,
    int index,
    int[] params,
    int params_offset) {
    gl().glProgramLocalParameterI4uivNV(
      target,
      index,
      params,
      params_offset);
  }

  public static void glProgramLocalParameters4fvEXT(
    int target,
    int index,
    int count,
    FloatBuffer params) {
    gl().glProgramLocalParameters4fvEXT(
      target,
      index,
      count,
      params);
  }

  public static void glProgramLocalParameters4fvEXT(
    int target,
    int index,
    int count,
    float[] params,
    int params_offset) {
    gl().glProgramLocalParameters4fvEXT(
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramLocalParametersI4ivNV(
    int target,
    int index,
    int count,
    IntBuffer params) {
    gl().glProgramLocalParametersI4ivNV(
      target,
      index,
      count,
      params);
  }

  public static void glProgramLocalParametersI4ivNV(
    int target,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glProgramLocalParametersI4ivNV(
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramLocalParametersI4uivNV(
    int target,
    int index,
    int count,
    IntBuffer params) {
    gl().glProgramLocalParametersI4uivNV(
      target,
      index,
      count,
      params);
  }

  public static void glProgramLocalParametersI4uivNV(
    int target,
    int index,
    int count,
    int[] params,
    int params_offset) {
    gl().glProgramLocalParametersI4uivNV(
      target,
      index,
      count,
      params,
      params_offset);
  }

  public static void glProgramStringARB(
    int target,
    int format,
    int len,
    String string) {
    gl().glProgramStringARB(
      target,
      format,
      len,
      string);
  }

  public static void glProgramSubroutineParametersuivNV(
    int target,
    int count,
    IntBuffer params) {
    gl().glProgramSubroutineParametersuivNV(
      target,
      count,
      params);
  }

  public static void glProgramSubroutineParametersuivNV(
    int target,
    int count,
    int[] params,
    int params_offset) {
    gl().glProgramSubroutineParametersuivNV(
      target,
      count,
      params,
      params_offset);
  }

  public static void glProgramUniform1dEXT(
    int program,
    int location,
    double x) {
    gl().glProgramUniform1dEXT(
      program,
      location,
      x);
  }

  public static void glProgramUniform1dvEXT(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform1dvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1dvEXT(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform1dvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform1fEXT(
    int program,
    int location,
    float v0) {
    gl().glProgramUniform1fEXT(
      program,
      location,
      v0);
  }

  public static void glProgramUniform1fvEXT(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform1fvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1fvEXT(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform1fvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform1iEXT(
    int program,
    int location,
    int v0) {
    gl().glProgramUniform1iEXT(
      program,
      location,
      v0);
  }

  public static void glProgramUniform1ivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform1ivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1ivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform1ivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform1uiEXT(
    int program,
    int location,
    int v0) {
    gl().glProgramUniform1uiEXT(
      program,
      location,
      v0);
  }

  public static void glProgramUniform1uivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform1uivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1uivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform1uivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2dEXT(
    int program,
    int location,
    double x,
    double y) {
    gl().glProgramUniform2dEXT(
      program,
      location,
      x,
      y);
  }

  public static void glProgramUniform2dvEXT(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform2dvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2dvEXT(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform2dvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2fEXT(
    int program,
    int location,
    float v0,
    float v1) {
    gl().glProgramUniform2fEXT(
      program,
      location,
      v0,
      v1);
  }

  public static void glProgramUniform2fvEXT(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform2fvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2fvEXT(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform2fvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2iEXT(
    int program,
    int location,
    int v0,
    int v1) {
    gl().glProgramUniform2iEXT(
      program,
      location,
      v0,
      v1);
  }

  public static void glProgramUniform2ivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform2ivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2ivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform2ivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2uiEXT(
    int program,
    int location,
    int v0,
    int v1) {
    gl().glProgramUniform2uiEXT(
      program,
      location,
      v0,
      v1);
  }

  public static void glProgramUniform2uivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform2uivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2uivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform2uivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3dEXT(
    int program,
    int location,
    double x,
    double y,
    double z) {
    gl().glProgramUniform3dEXT(
      program,
      location,
      x,
      y,
      z);
  }

  public static void glProgramUniform3dvEXT(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform3dvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3dvEXT(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform3dvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3fEXT(
    int program,
    int location,
    float v0,
    float v1,
    float v2) {
    gl().glProgramUniform3fEXT(
      program,
      location,
      v0,
      v1,
      v2);
  }

  public static void glProgramUniform3fvEXT(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform3fvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3fvEXT(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform3fvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3iEXT(
    int program,
    int location,
    int v0,
    int v1,
    int v2) {
    gl().glProgramUniform3iEXT(
      program,
      location,
      v0,
      v1,
      v2);
  }

  public static void glProgramUniform3ivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform3ivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3ivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform3ivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3uiEXT(
    int program,
    int location,
    int v0,
    int v1,
    int v2) {
    gl().glProgramUniform3uiEXT(
      program,
      location,
      v0,
      v1,
      v2);
  }

  public static void glProgramUniform3uivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform3uivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3uivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform3uivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4dEXT(
    int program,
    int location,
    double x,
    double y,
    double z,
    double w) {
    gl().glProgramUniform4dEXT(
      program,
      location,
      x,
      y,
      z,
      w);
  }

  public static void glProgramUniform4dvEXT(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform4dvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4dvEXT(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform4dvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4fEXT(
    int program,
    int location,
    float v0,
    float v1,
    float v2,
    float v3) {
    gl().glProgramUniform4fEXT(
      program,
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glProgramUniform4fvEXT(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform4fvEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4fvEXT(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform4fvEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4iEXT(
    int program,
    int location,
    int v0,
    int v1,
    int v2,
    int v3) {
    gl().glProgramUniform4iEXT(
      program,
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glProgramUniform4ivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform4ivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4ivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform4ivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4uiEXT(
    int program,
    int location,
    int v0,
    int v1,
    int v2,
    int v3) {
    gl().glProgramUniform4uiEXT(
      program,
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glProgramUniform4uivEXT(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform4uivEXT(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4uivEXT(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform4uivEXT(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix2dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix2fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x3dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix2x3dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x3dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x3dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x3fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix2x3fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x3fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x3fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x4dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix2x4dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x4dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x4dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x4fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix2x4fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x4fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x4fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix3dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix3fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x2dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix3x2dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x2dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x2dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x2fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix3x2fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x2fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x2fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x4dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix3x4dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x4dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x4dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x4fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix3x4fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x4fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x4fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix4dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix4fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x2dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix4x2dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x2dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x2dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x2fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix4x2fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x2fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x2fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x3dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix4x3dvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x3dvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x3dvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x3fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix4x3fvEXT(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x3fvEXT(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x3fvEXT(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramVertexLimitNV(
    int target,
    int limit) {
    gl().glProgramVertexLimitNV(
      target,
      limit);
  }

  public static void glProvokingVertexEXT(
    int mode) {
    gl().glProvokingVertexEXT(
      mode);
  }

  public static void glPushAttrib(
    int mask) {
    gl().glPushAttrib(
      mask);
  }

  public static void glPushClientAttrib(
    int mask) {
    gl().glPushClientAttrib(
      mask);
  }

  public static void glPushClientAttribDefaultEXT(
    int mask) {
    gl().glPushClientAttribDefaultEXT(
      mask);
  }

  public static void glPushName(
    int name) {
    gl().glPushName(
      name);
  }

  public static void glRasterPos2d(
    double x,
    double y) {
    gl().glRasterPos2d(
      x,
      y);
  }

  public static void glRasterPos2dv(
    DoubleBuffer v) {
    gl().glRasterPos2dv(
      v);
  }

  public static void glRasterPos2dv(
    double[] v,
    int v_offset) {
    gl().glRasterPos2dv(
      v,
      v_offset);
  }

  public static void glRasterPos2f(
    float x,
    float y) {
    gl().glRasterPos2f(
      x,
      y);
  }

  public static void glRasterPos2fv(
    FloatBuffer v) {
    gl().glRasterPos2fv(
      v);
  }

  public static void glRasterPos2fv(
    float[] v,
    int v_offset) {
    gl().glRasterPos2fv(
      v,
      v_offset);
  }

  public static void glRasterPos2i(
    int x,
    int y) {
    gl().glRasterPos2i(
      x,
      y);
  }

  public static void glRasterPos2iv(
    IntBuffer v) {
    gl().glRasterPos2iv(
      v);
  }

  public static void glRasterPos2iv(
    int[] v,
    int v_offset) {
    gl().glRasterPos2iv(
      v,
      v_offset);
  }

  public static void glRasterPos2s(
    short x,
    short y) {
    gl().glRasterPos2s(
      x,
      y);
  }

  public static void glRasterPos2sv(
    ShortBuffer v) {
    gl().glRasterPos2sv(
      v);
  }

  public static void glRasterPos2sv(
    short[] v,
    int v_offset) {
    gl().glRasterPos2sv(
      v,
      v_offset);
  }

  public static void glRasterPos3d(
    double x,
    double y,
    double z) {
    gl().glRasterPos3d(
      x,
      y,
      z);
  }

  public static void glRasterPos3dv(
    DoubleBuffer v) {
    gl().glRasterPos3dv(
      v);
  }

  public static void glRasterPos3dv(
    double[] v,
    int v_offset) {
    gl().glRasterPos3dv(
      v,
      v_offset);
  }

  public static void glRasterPos3f(
    float x,
    float y,
    float z) {
    gl().glRasterPos3f(
      x,
      y,
      z);
  }

  public static void glRasterPos3fv(
    FloatBuffer v) {
    gl().glRasterPos3fv(
      v);
  }

  public static void glRasterPos3fv(
    float[] v,
    int v_offset) {
    gl().glRasterPos3fv(
      v,
      v_offset);
  }

  public static void glRasterPos3i(
    int x,
    int y,
    int z) {
    gl().glRasterPos3i(
      x,
      y,
      z);
  }

  public static void glRasterPos3iv(
    IntBuffer v) {
    gl().glRasterPos3iv(
      v);
  }

  public static void glRasterPos3iv(
    int[] v,
    int v_offset) {
    gl().glRasterPos3iv(
      v,
      v_offset);
  }

  public static void glRasterPos3s(
    short x,
    short y,
    short z) {
    gl().glRasterPos3s(
      x,
      y,
      z);
  }

  public static void glRasterPos3sv(
    ShortBuffer v) {
    gl().glRasterPos3sv(
      v);
  }

  public static void glRasterPos3sv(
    short[] v,
    int v_offset) {
    gl().glRasterPos3sv(
      v,
      v_offset);
  }

  public static void glRasterPos4d(
    double x,
    double y,
    double z,
    double w) {
    gl().glRasterPos4d(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4dv(
    DoubleBuffer v) {
    gl().glRasterPos4dv(
      v);
  }

  public static void glRasterPos4dv(
    double[] v,
    int v_offset) {
    gl().glRasterPos4dv(
      v,
      v_offset);
  }

  public static void glRasterPos4f(
    float x,
    float y,
    float z,
    float w) {
    gl().glRasterPos4f(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4fv(
    FloatBuffer v) {
    gl().glRasterPos4fv(
      v);
  }

  public static void glRasterPos4fv(
    float[] v,
    int v_offset) {
    gl().glRasterPos4fv(
      v,
      v_offset);
  }

  public static void glRasterPos4i(
    int x,
    int y,
    int z,
    int w) {
    gl().glRasterPos4i(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4iv(
    IntBuffer v) {
    gl().glRasterPos4iv(
      v);
  }

  public static void glRasterPos4iv(
    int[] v,
    int v_offset) {
    gl().glRasterPos4iv(
      v,
      v_offset);
  }

  public static void glRasterPos4s(
    short x,
    short y,
    short z,
    short w) {
    gl().glRasterPos4s(
      x,
      y,
      z,
      w);
  }

  public static void glRasterPos4sv(
    ShortBuffer v) {
    gl().glRasterPos4sv(
      v);
  }

  public static void glRasterPos4sv(
    short[] v,
    int v_offset) {
    gl().glRasterPos4sv(
      v,
      v_offset);
  }

  public static void glRectd(
    double x1,
    double y1,
    double x2,
    double y2) {
    gl().glRectd(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectdv(
    DoubleBuffer v1,
    DoubleBuffer v2) {
    gl().glRectdv(
      v1,
      v2);
  }

  public static void glRectdv(
    double[] v1,
    int v1_offset,
    double[] v2,
    int v2_offset) {
    gl().glRectdv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static void glRectf(
    float x1,
    float y1,
    float x2,
    float y2) {
    gl().glRectf(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectfv(
    FloatBuffer v1,
    FloatBuffer v2) {
    gl().glRectfv(
      v1,
      v2);
  }

  public static void glRectfv(
    float[] v1,
    int v1_offset,
    float[] v2,
    int v2_offset) {
    gl().glRectfv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static void glRecti(
    int x1,
    int y1,
    int x2,
    int y2) {
    gl().glRecti(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectiv(
    IntBuffer v1,
    IntBuffer v2) {
    gl().glRectiv(
      v1,
      v2);
  }

  public static void glRectiv(
    int[] v1,
    int v1_offset,
    int[] v2,
    int v2_offset) {
    gl().glRectiv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static void glRects(
    short x1,
    short y1,
    short x2,
    short y2) {
    gl().glRects(
      x1,
      y1,
      x2,
      y2);
  }

  public static void glRectsv(
    ShortBuffer v1,
    ShortBuffer v2) {
    gl().glRectsv(
      v1,
      v2);
  }

  public static void glRectsv(
    short[] v1,
    int v1_offset,
    short[] v2,
    int v2_offset) {
    gl().glRectsv(
      v1,
      v1_offset,
      v2,
      v2_offset);
  }

  public static int glRenderMode(
    int mode) {
    return gl().glRenderMode(
      mode);
  }

  public static void glRenderbufferStorageMultisampleCoverageNV(
    int target,
    int coverageSamples,
    int colorSamples,
    int internalformat,
    int width,
    int height) {
    gl().glRenderbufferStorageMultisampleCoverageNV(
      target,
      coverageSamples,
      colorSamples,
      internalformat,
      width,
      height);
  }

  public static void glResetHistogram(
    int target) {
    gl().glResetHistogram(
      target);
  }

  public static void glResetMinmax(
    int target) {
    gl().glResetMinmax(
      target);
  }

  public static void glResumeTransformFeedbackNV(
    ) {
    gl().glResumeTransformFeedbackNV();
  }

  public static void glRotated(
    double angle,
    double x,
    double y,
    double z) {
    gl().glRotated(
      angle,
      x,
      y,
      z);
  }

  public static void glSampleMaskIndexedNV(
    int index,
    int mask) {
    gl().glSampleMaskIndexedNV(
      index,
      mask);
  }

  public static void glScaled(
    double x,
    double y,
    double z) {
    gl().glScaled(
      x,
      y,
      z);
  }

  public static void glSecondaryColor3b(
    byte red,
    byte green,
    byte blue) {
    gl().glSecondaryColor3b(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3bv(
    ByteBuffer v) {
    gl().glSecondaryColor3bv(
      v);
  }

  public static void glSecondaryColor3bv(
    byte[] v,
    int v_offset) {
    gl().glSecondaryColor3bv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3d(
    double red,
    double green,
    double blue) {
    gl().glSecondaryColor3d(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3dv(
    DoubleBuffer v) {
    gl().glSecondaryColor3dv(
      v);
  }

  public static void glSecondaryColor3dv(
    double[] v,
    int v_offset) {
    gl().glSecondaryColor3dv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3f(
    float red,
    float green,
    float blue) {
    gl().glSecondaryColor3f(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3fv(
    FloatBuffer v) {
    gl().glSecondaryColor3fv(
      v);
  }

  public static void glSecondaryColor3fv(
    float[] v,
    int v_offset) {
    gl().glSecondaryColor3fv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3h(
    short red,
    short green,
    short blue) {
    gl().glSecondaryColor3h(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3hv(
    ShortBuffer v) {
    gl().glSecondaryColor3hv(
      v);
  }

  public static void glSecondaryColor3hv(
    short[] v,
    int v_offset) {
    gl().glSecondaryColor3hv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3i(
    int red,
    int green,
    int blue) {
    gl().glSecondaryColor3i(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3iv(
    IntBuffer v) {
    gl().glSecondaryColor3iv(
      v);
  }

  public static void glSecondaryColor3iv(
    int[] v,
    int v_offset) {
    gl().glSecondaryColor3iv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3s(
    short red,
    short green,
    short blue) {
    gl().glSecondaryColor3s(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3sv(
    ShortBuffer v) {
    gl().glSecondaryColor3sv(
      v);
  }

  public static void glSecondaryColor3sv(
    short[] v,
    int v_offset) {
    gl().glSecondaryColor3sv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3ub(
    byte red,
    byte green,
    byte blue) {
    gl().glSecondaryColor3ub(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3ubv(
    ByteBuffer v) {
    gl().glSecondaryColor3ubv(
      v);
  }

  public static void glSecondaryColor3ubv(
    byte[] v,
    int v_offset) {
    gl().glSecondaryColor3ubv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3ui(
    int red,
    int green,
    int blue) {
    gl().glSecondaryColor3ui(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3uiv(
    IntBuffer v) {
    gl().glSecondaryColor3uiv(
      v);
  }

  public static void glSecondaryColor3uiv(
    int[] v,
    int v_offset) {
    gl().glSecondaryColor3uiv(
      v,
      v_offset);
  }

  public static void glSecondaryColor3us(
    short red,
    short green,
    short blue) {
    gl().glSecondaryColor3us(
      red,
      green,
      blue);
  }

  public static void glSecondaryColor3usv(
    ShortBuffer v) {
    gl().glSecondaryColor3usv(
      v);
  }

  public static void glSecondaryColor3usv(
    short[] v,
    int v_offset) {
    gl().glSecondaryColor3usv(
      v,
      v_offset);
  }

  public static void glSecondaryColorPointer(
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glSecondaryColorPointer(
      size,
      type,
      stride,
      pointer);
  }

  public static void glSecondaryColorPointer(
    int size,
    int type,
    int stride,
    long pointer_buffer_offset) {
    gl().glSecondaryColorPointer(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glSelectBuffer(
    int size,
    IntBuffer buffer) {
    gl().glSelectBuffer(
      size,
      buffer);
  }

/*
  public static void glSelectBuffer(
    int size,
    int[] buffer,
    int buffer_offset) {
    gl().glSelectBuffer(
      size,
      buffer,
      buffer_offset);
  }
*/

  public static void glSelectPerfMonitorCountersAMD(
    int monitor,
    boolean enable,
    int group,
    int numCounters,
    IntBuffer counterList) {
    gl().glSelectPerfMonitorCountersAMD(
      monitor,
      enable,
      group,
      numCounters,
      counterList);
  }

  public static void glSelectPerfMonitorCountersAMD(
    int monitor,
    boolean enable,
    int group,
    int numCounters,
    int[] counterList,
    int counterList_offset) {
    gl().glSelectPerfMonitorCountersAMD(
      monitor,
      enable,
      group,
      numCounters,
      counterList,
      counterList_offset);
  }

  public static void glSeparableFilter2D(
    int target,
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

  public static void glSeparableFilter2D(
    int target,
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

  public static void glSetFenceAPPLE(
    int fence) {
    gl().glSetFenceAPPLE(
      fence);
  }

  public static void glSetFenceNV(
    int fence,
    int condition) {
    gl().glSetFenceNV(
      fence,
      condition);
  }

  public static void glSetInvariantEXT(
    int id,
    int type,
    Buffer addr) {
    gl().glSetInvariantEXT(
      id,
      type,
      addr);
  }

  public static void glSetLocalConstantEXT(
    int id,
    int type,
    Buffer addr) {
    gl().glSetLocalConstantEXT(
      id,
      type,
      addr);
  }

  public static void glShaderOp1EXT(
    int op,
    int res,
    int arg1) {
    gl().glShaderOp1EXT(
      op,
      res,
      arg1);
  }

  public static void glShaderOp2EXT(
    int op,
    int res,
    int arg1,
    int arg2) {
    gl().glShaderOp2EXT(
      op,
      res,
      arg1,
      arg2);
  }

  public static void glShaderOp3EXT(
    int op,
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

  public static void glShaderSourceARB(
    int shaderObj,
    int count,
    String[] string,
    IntBuffer length) {
    gl().glShaderSourceARB(
      shaderObj,
      count,
      string,
      length);
  }

  public static void glShaderSourceARB(
    int shaderObj,
    int count,
    String[] string,
    int[] length,
    int length_offset) {
    gl().glShaderSourceARB(
      shaderObj,
      count,
      string,
      length,
      length_offset);
  }

  public static void glStencilClearTagEXT(
    int stencilTagBits,
    int stencilClearTag) {
    gl().glStencilClearTagEXT(
      stencilTagBits,
      stencilClearTag);
  }

  public static void glStringMarkerGREMEDY(
    int len,
    Buffer string) {
    gl().glStringMarkerGREMEDY(
      len,
      string);
  }

  public static void glSwapAPPLE(
    ) {
    gl().glSwapAPPLE();
  }

  public static void glSwizzleEXT(
    int res,
    int in,
    int outX,
    int outY,
    int outZ,
    int outW) {
    gl().glSwizzleEXT(
      res,
      in,
      outX,
      outY,
      outZ,
      outW);
  }

  public static boolean glTestFenceAPPLE(
    int fence) {
    return gl().glTestFenceAPPLE(
      fence);
  }

  public static boolean glTestFenceNV(
    int fence) {
    return gl().glTestFenceNV(
      fence);
  }

  public static boolean glTestObjectAPPLE(
    int object,
    int name) {
    return gl().glTestObjectAPPLE(
      object,
      name);
  }

  public static void glTexCoord1d(
    double s) {
    gl().glTexCoord1d(
      s);
  }

  public static void glTexCoord1dv(
    DoubleBuffer v) {
    gl().glTexCoord1dv(
      v);
  }

  public static void glTexCoord1dv(
    double[] v,
    int v_offset) {
    gl().glTexCoord1dv(
      v,
      v_offset);
  }

  public static void glTexCoord1f(
    float s) {
    gl().glTexCoord1f(
      s);
  }

  public static void glTexCoord1fv(
    FloatBuffer v) {
    gl().glTexCoord1fv(
      v);
  }

  public static void glTexCoord1fv(
    float[] v,
    int v_offset) {
    gl().glTexCoord1fv(
      v,
      v_offset);
  }

  public static void glTexCoord1h(
    short s) {
    gl().glTexCoord1h(
      s);
  }

  public static void glTexCoord1hv(
    ShortBuffer v) {
    gl().glTexCoord1hv(
      v);
  }

  public static void glTexCoord1hv(
    short[] v,
    int v_offset) {
    gl().glTexCoord1hv(
      v,
      v_offset);
  }

  public static void glTexCoord1i(
    int s) {
    gl().glTexCoord1i(
      s);
  }

  public static void glTexCoord1iv(
    IntBuffer v) {
    gl().glTexCoord1iv(
      v);
  }

  public static void glTexCoord1iv(
    int[] v,
    int v_offset) {
    gl().glTexCoord1iv(
      v,
      v_offset);
  }

  public static void glTexCoord1s(
    short s) {
    gl().glTexCoord1s(
      s);
  }

  public static void glTexCoord1sv(
    ShortBuffer v) {
    gl().glTexCoord1sv(
      v);
  }

  public static void glTexCoord1sv(
    short[] v,
    int v_offset) {
    gl().glTexCoord1sv(
      v,
      v_offset);
  }

  public static void glTexCoord2d(
    double s,
    double t) {
    gl().glTexCoord2d(
      s,
      t);
  }

  public static void glTexCoord2dv(
    DoubleBuffer v) {
    gl().glTexCoord2dv(
      v);
  }

  public static void glTexCoord2dv(
    double[] v,
    int v_offset) {
    gl().glTexCoord2dv(
      v,
      v_offset);
  }

  public static void glTexCoord2f(
    float s,
    float t) {
    gl().glTexCoord2f(
      s,
      t);
  }

  public static void glTexCoord2fv(
    FloatBuffer v) {
    gl().glTexCoord2fv(
      v);
  }

  public static void glTexCoord2fv(
    float[] v,
    int v_offset) {
    gl().glTexCoord2fv(
      v,
      v_offset);
  }

  public static void glTexCoord2h(
    short s,
    short t) {
    gl().glTexCoord2h(
      s,
      t);
  }

  public static void glTexCoord2hv(
    ShortBuffer v) {
    gl().glTexCoord2hv(
      v);
  }

  public static void glTexCoord2hv(
    short[] v,
    int v_offset) {
    gl().glTexCoord2hv(
      v,
      v_offset);
  }

  public static void glTexCoord2i(
    int s,
    int t) {
    gl().glTexCoord2i(
      s,
      t);
  }

  public static void glTexCoord2iv(
    IntBuffer v) {
    gl().glTexCoord2iv(
      v);
  }

  public static void glTexCoord2iv(
    int[] v,
    int v_offset) {
    gl().glTexCoord2iv(
      v,
      v_offset);
  }

  public static void glTexCoord2s(
    short s,
    short t) {
    gl().glTexCoord2s(
      s,
      t);
  }

  public static void glTexCoord2sv(
    ShortBuffer v) {
    gl().glTexCoord2sv(
      v);
  }

  public static void glTexCoord2sv(
    short[] v,
    int v_offset) {
    gl().glTexCoord2sv(
      v,
      v_offset);
  }

  public static void glTexCoord3d(
    double s,
    double t,
    double r) {
    gl().glTexCoord3d(
      s,
      t,
      r);
  }

  public static void glTexCoord3dv(
    DoubleBuffer v) {
    gl().glTexCoord3dv(
      v);
  }

  public static void glTexCoord3dv(
    double[] v,
    int v_offset) {
    gl().glTexCoord3dv(
      v,
      v_offset);
  }

  public static void glTexCoord3f(
    float s,
    float t,
    float r) {
    gl().glTexCoord3f(
      s,
      t,
      r);
  }

  public static void glTexCoord3fv(
    FloatBuffer v) {
    gl().glTexCoord3fv(
      v);
  }

  public static void glTexCoord3fv(
    float[] v,
    int v_offset) {
    gl().glTexCoord3fv(
      v,
      v_offset);
  }

  public static void glTexCoord3h(
    short s,
    short t,
    short r) {
    gl().glTexCoord3h(
      s,
      t,
      r);
  }

  public static void glTexCoord3hv(
    ShortBuffer v) {
    gl().glTexCoord3hv(
      v);
  }

  public static void glTexCoord3hv(
    short[] v,
    int v_offset) {
    gl().glTexCoord3hv(
      v,
      v_offset);
  }

  public static void glTexCoord3i(
    int s,
    int t,
    int r) {
    gl().glTexCoord3i(
      s,
      t,
      r);
  }

  public static void glTexCoord3iv(
    IntBuffer v) {
    gl().glTexCoord3iv(
      v);
  }

  public static void glTexCoord3iv(
    int[] v,
    int v_offset) {
    gl().glTexCoord3iv(
      v,
      v_offset);
  }

  public static void glTexCoord3s(
    short s,
    short t,
    short r) {
    gl().glTexCoord3s(
      s,
      t,
      r);
  }

  public static void glTexCoord3sv(
    ShortBuffer v) {
    gl().glTexCoord3sv(
      v);
  }

  public static void glTexCoord3sv(
    short[] v,
    int v_offset) {
    gl().glTexCoord3sv(
      v,
      v_offset);
  }

  public static void glTexCoord4d(
    double s,
    double t,
    double r,
    double q) {
    gl().glTexCoord4d(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4dv(
    DoubleBuffer v) {
    gl().glTexCoord4dv(
      v);
  }

  public static void glTexCoord4dv(
    double[] v,
    int v_offset) {
    gl().glTexCoord4dv(
      v,
      v_offset);
  }

  public static void glTexCoord4f(
    float s,
    float t,
    float r,
    float q) {
    gl().glTexCoord4f(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4fv(
    FloatBuffer v) {
    gl().glTexCoord4fv(
      v);
  }

  public static void glTexCoord4fv(
    float[] v,
    int v_offset) {
    gl().glTexCoord4fv(
      v,
      v_offset);
  }

  public static void glTexCoord4h(
    short s,
    short t,
    short r,
    short q) {
    gl().glTexCoord4h(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4hv(
    ShortBuffer v) {
    gl().glTexCoord4hv(
      v);
  }

  public static void glTexCoord4hv(
    short[] v,
    int v_offset) {
    gl().glTexCoord4hv(
      v,
      v_offset);
  }

  public static void glTexCoord4i(
    int s,
    int t,
    int r,
    int q) {
    gl().glTexCoord4i(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4iv(
    IntBuffer v) {
    gl().glTexCoord4iv(
      v);
  }

  public static void glTexCoord4iv(
    int[] v,
    int v_offset) {
    gl().glTexCoord4iv(
      v,
      v_offset);
  }

  public static void glTexCoord4s(
    short s,
    short t,
    short r,
    short q) {
    gl().glTexCoord4s(
      s,
      t,
      r,
      q);
  }

  public static void glTexCoord4sv(
    ShortBuffer v) {
    gl().glTexCoord4sv(
      v);
  }

  public static void glTexCoord4sv(
    short[] v,
    int v_offset) {
    gl().glTexCoord4sv(
      v,
      v_offset);
  }

  public static void glTexGend(
    int coord,
    int pname,
    double param) {
    gl().glTexGend(
      coord,
      pname,
      param);
  }

  public static void glTexGendv(
    int coord,
    int pname,
    DoubleBuffer params) {
    gl().glTexGendv(
      coord,
      pname,
      params);
  }

  public static void glTexGendv(
    int coord,
    int pname,
    double[] params,
    int params_offset) {
    gl().glTexGendv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glTexRenderbufferNV(
    int target,
    int renderbuffer) {
    gl().glTexRenderbufferNV(
      target,
      renderbuffer);
  }

  public static void glTextureBarrierNV(
    ) {
    gl().glTextureBarrierNV();
  }

  public static void glTextureBufferEXT(
    int texture,
    int target,
    int internalformat,
    int buffer) {
    gl().glTextureBufferEXT(
      texture,
      target,
      internalformat,
      buffer);
  }

  public static void glTextureImage1DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int width,
    int border,
    int format,
    int type,
    Buffer pixels) {
    gl().glTextureImage1DEXT(
      texture,
      target,
      level,
      internalformat,
      width,
      border,
      format,
      type,
      pixels);
  }

  public static void glTextureImage2DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int border,
    int format,
    int type,
    Buffer pixels) {
    gl().glTextureImage2DEXT(
      texture,
      target,
      level,
      internalformat,
      width,
      height,
      border,
      format,
      type,
      pixels);
  }

  public static void glTextureImage3DEXT(
    int texture,
    int target,
    int level,
    int internalformat,
    int width,
    int height,
    int depth,
    int border,
    int format,
    int type,
    Buffer pixels) {
    gl().glTextureImage3DEXT(
      texture,
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

  public static void glTextureLightEXT(
    int pname) {
    gl().glTextureLightEXT(
      pname);
  }

  public static void glTextureMaterialEXT(
    int face,
    int mode) {
    gl().glTextureMaterialEXT(
      face,
      mode);
  }

  public static void glTextureNormalEXT(
    int mode) {
    gl().glTextureNormalEXT(
      mode);
  }

  public static void glTextureParameterIivEXT(
    int texture,
    int target,
    int pname,
    IntBuffer params) {
    gl().glTextureParameterIivEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glTextureParameterIivEXT(
    int texture,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTextureParameterIivEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTextureParameterIuivEXT(
    int texture,
    int target,
    int pname,
    IntBuffer params) {
    gl().glTextureParameterIuivEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glTextureParameterIuivEXT(
    int texture,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTextureParameterIuivEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTextureParameterfEXT(
    int texture,
    int target,
    int pname,
    float param) {
    gl().glTextureParameterfEXT(
      texture,
      target,
      pname,
      param);
  }

  public static void glTextureParameterfvEXT(
    int texture,
    int target,
    int pname,
    FloatBuffer params) {
    gl().glTextureParameterfvEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glTextureParameterfvEXT(
    int texture,
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glTextureParameterfvEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTextureParameteriEXT(
    int texture,
    int target,
    int pname,
    int param) {
    gl().glTextureParameteriEXT(
      texture,
      target,
      pname,
      param);
  }

  public static void glTextureParameterivEXT(
    int texture,
    int target,
    int pname,
    IntBuffer params) {
    gl().glTextureParameterivEXT(
      texture,
      target,
      pname,
      params);
  }

  public static void glTextureParameterivEXT(
    int texture,
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTextureParameterivEXT(
      texture,
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTextureRangeAPPLE(
    int target,
    int length,
    Buffer pointer) {
    gl().glTextureRangeAPPLE(
      target,
      length,
      pointer);
  }

  public static void glTextureRenderbufferEXT(
    int texture,
    int target,
    int renderbuffer) {
    gl().glTextureRenderbufferEXT(
      texture,
      target,
      renderbuffer);
  }

  public static void glTextureSubImage1DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int width,
    int format,
    int type,
    Buffer pixels) {
    gl().glTextureSubImage1DEXT(
      texture,
      target,
      level,
      xoffset,
      width,
      format,
      type,
      pixels);
  }

  public static void glTextureSubImage2DEXT(
    int texture,
    int target,
    int level,
    int xoffset,
    int yoffset,
    int width,
    int height,
    int format,
    int type,
    Buffer pixels) {
    gl().glTextureSubImage2DEXT(
      texture,
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

  public static void glTextureSubImage3DEXT(
    int texture,
    int target,
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
    gl().glTextureSubImage3DEXT(
      texture,
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

  public static void glTranslated(
    double x,
    double y,
    double z) {
    gl().glTranslated(
      x,
      y,
      z);
  }

  public static void glUniform1fARB(
    int location,
    float v0) {
    gl().glUniform1fARB(
      location,
      v0);
  }

  public static void glUniform1fvARB(
    int location,
    int count,
    FloatBuffer value) {
    gl().glUniform1fvARB(
      location,
      count,
      value);
  }

  public static void glUniform1fvARB(
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glUniform1fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform1iARB(
    int location,
    int v0) {
    gl().glUniform1iARB(
      location,
      v0);
  }

  public static void glUniform1ivARB(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform1ivARB(
      location,
      count,
      value);
  }

  public static void glUniform1ivARB(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform1ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform2fARB(
    int location,
    float v0,
    float v1) {
    gl().glUniform2fARB(
      location,
      v0,
      v1);
  }

  public static void glUniform2fvARB(
    int location,
    int count,
    FloatBuffer value) {
    gl().glUniform2fvARB(
      location,
      count,
      value);
  }

  public static void glUniform2fvARB(
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glUniform2fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform2iARB(
    int location,
    int v0,
    int v1) {
    gl().glUniform2iARB(
      location,
      v0,
      v1);
  }

  public static void glUniform2ivARB(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform2ivARB(
      location,
      count,
      value);
  }

  public static void glUniform2ivARB(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform2ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform3fARB(
    int location,
    float v0,
    float v1,
    float v2) {
    gl().glUniform3fARB(
      location,
      v0,
      v1,
      v2);
  }

  public static void glUniform3fvARB(
    int location,
    int count,
    FloatBuffer value) {
    gl().glUniform3fvARB(
      location,
      count,
      value);
  }

  public static void glUniform3fvARB(
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glUniform3fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform3iARB(
    int location,
    int v0,
    int v1,
    int v2) {
    gl().glUniform3iARB(
      location,
      v0,
      v1,
      v2);
  }

  public static void glUniform3ivARB(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform3ivARB(
      location,
      count,
      value);
  }

  public static void glUniform3ivARB(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform3ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform4fARB(
    int location,
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

  public static void glUniform4fvARB(
    int location,
    int count,
    FloatBuffer value) {
    gl().glUniform4fvARB(
      location,
      count,
      value);
  }

  public static void glUniform4fvARB(
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glUniform4fvARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform4iARB(
    int location,
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

  public static void glUniform4ivARB(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform4ivARB(
      location,
      count,
      value);
  }

  public static void glUniform4ivARB(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform4ivARB(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniformBufferEXT(
    int program,
    int location,
    int buffer) {
    gl().glUniformBufferEXT(
      program,
      location,
      buffer);
  }

  public static void glUniformMatrix2fvARB(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix2fvARB(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix2fvARB(
    int location,
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

  public static void glUniformMatrix3fvARB(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix3fvARB(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix3fvARB(
    int location,
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

  public static void glUniformMatrix4fvARB(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix4fvARB(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix4fvARB(
    int location,
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

  public static void glUnlockArraysEXT(
    ) {
    gl().glUnlockArraysEXT();
  }

  public static boolean glUnmapNamedBufferEXT(
    int buffer) {
    return gl().glUnmapNamedBufferEXT(
      buffer);
  }

  public static void glUseProgramObjectARB(
    int programObj) {
    gl().glUseProgramObjectARB(
      programObj);
  }

  public static void glVDPAUFiniNV(
    ) {
    gl().glVDPAUFiniNV();
  }

  public static void glVDPAUGetSurfaceivNV(
    long surface,
    int pname,
    int bufSize,
    IntBuffer length,
    IntBuffer values) {
    gl().glVDPAUGetSurfaceivNV(
      surface,
      pname,
      bufSize,
      length,
      values);
  }

  public static void glVDPAUGetSurfaceivNV(
    long surface,
    int pname,
    int bufSize,
    int[] length,
    int length_offset,
    int[] values,
    int values_offset) {
    gl().glVDPAUGetSurfaceivNV(
      surface,
      pname,
      bufSize,
      length,
      length_offset,
      values,
      values_offset);
  }

  public static void glVDPAUInitNV(
    Buffer vdpDevice,
    Buffer getProcAddress) {
    gl().glVDPAUInitNV(
      vdpDevice,
      getProcAddress);
  }

  public static void glVDPAUIsSurfaceNV(
    long surface) {
    gl().glVDPAUIsSurfaceNV(
      surface);
  }

  public static void glVDPAUMapSurfacesNV(
    int numSurfaces,
    LongBuffer surfaces) {
    gl().glVDPAUMapSurfacesNV(
      numSurfaces,
      surfaces);
  }

  public static void glVDPAUMapSurfacesNV(
    int numSurfaces,
    long[] surfaces,
    int surfaces_offset) {
    gl().glVDPAUMapSurfacesNV(
      numSurfaces,
      surfaces,
      surfaces_offset);
  }

  public static long glVDPAURegisterOutputSurfaceNV(
    Buffer vdpSurface,
    int target,
    int numTextureNames,
    IntBuffer textureNames) {
    return gl().glVDPAURegisterOutputSurfaceNV(
      vdpSurface,
      target,
      numTextureNames,
      textureNames);
  }

  public static long glVDPAURegisterOutputSurfaceNV(
    Buffer vdpSurface,
    int target,
    int numTextureNames,
    int[] textureNames,
    int textureNames_offset) {
    return gl().glVDPAURegisterOutputSurfaceNV(
      vdpSurface,
      target,
      numTextureNames,
      textureNames,
      textureNames_offset);
  }

  public static long glVDPAURegisterVideoSurfaceNV(
    Buffer vdpSurface,
    int target,
    int numTextureNames,
    IntBuffer textureNames) {
    return gl().glVDPAURegisterVideoSurfaceNV(
      vdpSurface,
      target,
      numTextureNames,
      textureNames);
  }

  public static long glVDPAURegisterVideoSurfaceNV(
    Buffer vdpSurface,
    int target,
    int numTextureNames,
    int[] textureNames,
    int textureNames_offset) {
    return gl().glVDPAURegisterVideoSurfaceNV(
      vdpSurface,
      target,
      numTextureNames,
      textureNames,
      textureNames_offset);
  }

  public static void glVDPAUSurfaceAccessNV(
    long surface,
    int access) {
    gl().glVDPAUSurfaceAccessNV(
      surface,
      access);
  }

  public static void glVDPAUUnmapSurfacesNV(
    int numSurface,
    LongBuffer surfaces) {
    gl().glVDPAUUnmapSurfacesNV(
      numSurface,
      surfaces);
  }

  public static void glVDPAUUnmapSurfacesNV(
    int numSurface,
    long[] surfaces,
    int surfaces_offset) {
    gl().glVDPAUUnmapSurfacesNV(
      numSurface,
      surfaces,
      surfaces_offset);
  }

  public static void glVDPAUUnregisterSurfaceNV(
    long surface) {
    gl().glVDPAUUnregisterSurfaceNV(
      surface);
  }

  public static void glValidateProgramARB(
    int programObj) {
    gl().glValidateProgramARB(
      programObj);
  }

  public static void glVariantPointerEXT(
    int id,
    int type,
    int stride,
    Buffer addr) {
    gl().glVariantPointerEXT(
      id,
      type,
      stride,
      addr);
  }

  public static void glVariantPointerEXT(
    int id,
    int type,
    int stride,
    long addr_buffer_offset) {
    gl().glVariantPointerEXT(
      id,
      type,
      stride,
      addr_buffer_offset);
  }

  public static void glVariantbvEXT(
    int id,
    ByteBuffer addr) {
    gl().glVariantbvEXT(
      id,
      addr);
  }

  public static void glVariantbvEXT(
    int id,
    byte[] addr,
    int addr_offset) {
    gl().glVariantbvEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVariantdvEXT(
    int id,
    DoubleBuffer addr) {
    gl().glVariantdvEXT(
      id,
      addr);
  }

  public static void glVariantdvEXT(
    int id,
    double[] addr,
    int addr_offset) {
    gl().glVariantdvEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVariantfvEXT(
    int id,
    FloatBuffer addr) {
    gl().glVariantfvEXT(
      id,
      addr);
  }

  public static void glVariantfvEXT(
    int id,
    float[] addr,
    int addr_offset) {
    gl().glVariantfvEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVariantivEXT(
    int id,
    IntBuffer addr) {
    gl().glVariantivEXT(
      id,
      addr);
  }

  public static void glVariantivEXT(
    int id,
    int[] addr,
    int addr_offset) {
    gl().glVariantivEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVariantsvEXT(
    int id,
    ShortBuffer addr) {
    gl().glVariantsvEXT(
      id,
      addr);
  }

  public static void glVariantsvEXT(
    int id,
    short[] addr,
    int addr_offset) {
    gl().glVariantsvEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVariantubvEXT(
    int id,
    ByteBuffer addr) {
    gl().glVariantubvEXT(
      id,
      addr);
  }

  public static void glVariantubvEXT(
    int id,
    byte[] addr,
    int addr_offset) {
    gl().glVariantubvEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVariantuivEXT(
    int id,
    IntBuffer addr) {
    gl().glVariantuivEXT(
      id,
      addr);
  }

  public static void glVariantuivEXT(
    int id,
    int[] addr,
    int addr_offset) {
    gl().glVariantuivEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVariantusvEXT(
    int id,
    ShortBuffer addr) {
    gl().glVariantusvEXT(
      id,
      addr);
  }

  public static void glVariantusvEXT(
    int id,
    short[] addr,
    int addr_offset) {
    gl().glVariantusvEXT(
      id,
      addr,
      addr_offset);
  }

  public static void glVertex2d(
    double x,
    double y) {
    gl().glVertex2d(
      x,
      y);
  }

  public static void glVertex2dv(
    DoubleBuffer v) {
    gl().glVertex2dv(
      v);
  }

  public static void glVertex2dv(
    double[] v,
    int v_offset) {
    gl().glVertex2dv(
      v,
      v_offset);
  }

  public static void glVertex2f(
    float x,
    float y) {
    gl().glVertex2f(
      x,
      y);
  }

  public static void glVertex2fv(
    FloatBuffer v) {
    gl().glVertex2fv(
      v);
  }

  public static void glVertex2fv(
    float[] v,
    int v_offset) {
    gl().glVertex2fv(
      v,
      v_offset);
  }

  public static void glVertex2h(
    short x,
    short y) {
    gl().glVertex2h(
      x,
      y);
  }

  public static void glVertex2hv(
    ShortBuffer v) {
    gl().glVertex2hv(
      v);
  }

  public static void glVertex2hv(
    short[] v,
    int v_offset) {
    gl().glVertex2hv(
      v,
      v_offset);
  }

  public static void glVertex2i(
    int x,
    int y) {
    gl().glVertex2i(
      x,
      y);
  }

  public static void glVertex2iv(
    IntBuffer v) {
    gl().glVertex2iv(
      v);
  }

  public static void glVertex2iv(
    int[] v,
    int v_offset) {
    gl().glVertex2iv(
      v,
      v_offset);
  }

  public static void glVertex2s(
    short x,
    short y) {
    gl().glVertex2s(
      x,
      y);
  }

  public static void glVertex2sv(
    ShortBuffer v) {
    gl().glVertex2sv(
      v);
  }

  public static void glVertex2sv(
    short[] v,
    int v_offset) {
    gl().glVertex2sv(
      v,
      v_offset);
  }

  public static void glVertex3d(
    double x,
    double y,
    double z) {
    gl().glVertex3d(
      x,
      y,
      z);
  }

  public static void glVertex3dv(
    DoubleBuffer v) {
    gl().glVertex3dv(
      v);
  }

  public static void glVertex3dv(
    double[] v,
    int v_offset) {
    gl().glVertex3dv(
      v,
      v_offset);
  }

  public static void glVertex3f(
    float x,
    float y,
    float z) {
    gl().glVertex3f(
      x,
      y,
      z);
  }

  public static void glVertex3fv(
    FloatBuffer v) {
    gl().glVertex3fv(
      v);
  }

  public static void glVertex3fv(
    float[] v,
    int v_offset) {
    gl().glVertex3fv(
      v,
      v_offset);
  }

  public static void glVertex3h(
    short x,
    short y,
    short z) {
    gl().glVertex3h(
      x,
      y,
      z);
  }

  public static void glVertex3hv(
    ShortBuffer v) {
    gl().glVertex3hv(
      v);
  }

  public static void glVertex3hv(
    short[] v,
    int v_offset) {
    gl().glVertex3hv(
      v,
      v_offset);
  }

  public static void glVertex3i(
    int x,
    int y,
    int z) {
    gl().glVertex3i(
      x,
      y,
      z);
  }

  public static void glVertex3iv(
    IntBuffer v) {
    gl().glVertex3iv(
      v);
  }

  public static void glVertex3iv(
    int[] v,
    int v_offset) {
    gl().glVertex3iv(
      v,
      v_offset);
  }

  public static void glVertex3s(
    short x,
    short y,
    short z) {
    gl().glVertex3s(
      x,
      y,
      z);
  }

  public static void glVertex3sv(
    ShortBuffer v) {
    gl().glVertex3sv(
      v);
  }

  public static void glVertex3sv(
    short[] v,
    int v_offset) {
    gl().glVertex3sv(
      v,
      v_offset);
  }

  public static void glVertex4d(
    double x,
    double y,
    double z,
    double w) {
    gl().glVertex4d(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4dv(
    DoubleBuffer v) {
    gl().glVertex4dv(
      v);
  }

  public static void glVertex4dv(
    double[] v,
    int v_offset) {
    gl().glVertex4dv(
      v,
      v_offset);
  }

  public static void glVertex4f(
    float x,
    float y,
    float z,
    float w) {
    gl().glVertex4f(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4fv(
    FloatBuffer v) {
    gl().glVertex4fv(
      v);
  }

  public static void glVertex4fv(
    float[] v,
    int v_offset) {
    gl().glVertex4fv(
      v,
      v_offset);
  }

  public static void glVertex4h(
    short x,
    short y,
    short z,
    short w) {
    gl().glVertex4h(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4hv(
    ShortBuffer v) {
    gl().glVertex4hv(
      v);
  }

  public static void glVertex4hv(
    short[] v,
    int v_offset) {
    gl().glVertex4hv(
      v,
      v_offset);
  }

  public static void glVertex4i(
    int x,
    int y,
    int z,
    int w) {
    gl().glVertex4i(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4iv(
    IntBuffer v) {
    gl().glVertex4iv(
      v);
  }

  public static void glVertex4iv(
    int[] v,
    int v_offset) {
    gl().glVertex4iv(
      v,
      v_offset);
  }

  public static void glVertex4s(
    short x,
    short y,
    short z,
    short w) {
    gl().glVertex4s(
      x,
      y,
      z,
      w);
  }

  public static void glVertex4sv(
    ShortBuffer v) {
    gl().glVertex4sv(
      v);
  }

  public static void glVertex4sv(
    short[] v,
    int v_offset) {
    gl().glVertex4sv(
      v,
      v_offset);
  }

  public static void glVertexArrayParameteriAPPLE(
    int pname,
    int param) {
    gl().glVertexArrayParameteriAPPLE(
      pname,
      param);
  }

  public static void glVertexArrayRangeAPPLE(
    int length,
    Buffer pointer) {
    gl().glVertexArrayRangeAPPLE(
      length,
      pointer);
  }

  public static void glVertexArrayRangeNV(
    int length,
    Buffer pointer) {
    gl().glVertexArrayRangeNV(
      length,
      pointer);
  }

  public static void glVertexAttrib1dARB(
    int index,
    double x) {
    gl().glVertexAttrib1dARB(
      index,
      x);
  }

  public static void glVertexAttrib1dvARB(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib1dvARB(
      index,
      v);
  }

  public static void glVertexAttrib1dvARB(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib1dvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib1fARB(
    int index,
    float x) {
    gl().glVertexAttrib1fARB(
      index,
      x);
  }

  public static void glVertexAttrib1fvARB(
    int index,
    FloatBuffer v) {
    gl().glVertexAttrib1fvARB(
      index,
      v);
  }

  public static void glVertexAttrib1fvARB(
    int index,
    float[] v,
    int v_offset) {
    gl().glVertexAttrib1fvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib1h(
    int index,
    short x) {
    gl().glVertexAttrib1h(
      index,
      x);
  }

  public static void glVertexAttrib1hv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib1hv(
      index,
      v);
  }

  public static void glVertexAttrib1hv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib1hv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib1sARB(
    int index,
    short x) {
    gl().glVertexAttrib1sARB(
      index,
      x);
  }

  public static void glVertexAttrib1svARB(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib1svARB(
      index,
      v);
  }

  public static void glVertexAttrib1svARB(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib1svARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib2dARB(
    int index,
    double x,
    double y) {
    gl().glVertexAttrib2dARB(
      index,
      x,
      y);
  }

  public static void glVertexAttrib2dvARB(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib2dvARB(
      index,
      v);
  }

  public static void glVertexAttrib2dvARB(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib2dvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib2fARB(
    int index,
    float x,
    float y) {
    gl().glVertexAttrib2fARB(
      index,
      x,
      y);
  }

  public static void glVertexAttrib2fvARB(
    int index,
    FloatBuffer v) {
    gl().glVertexAttrib2fvARB(
      index,
      v);
  }

  public static void glVertexAttrib2fvARB(
    int index,
    float[] v,
    int v_offset) {
    gl().glVertexAttrib2fvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib2h(
    int index,
    short x,
    short y) {
    gl().glVertexAttrib2h(
      index,
      x,
      y);
  }

  public static void glVertexAttrib2hv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib2hv(
      index,
      v);
  }

  public static void glVertexAttrib2hv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib2hv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib2sARB(
    int index,
    short x,
    short y) {
    gl().glVertexAttrib2sARB(
      index,
      x,
      y);
  }

  public static void glVertexAttrib2svARB(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib2svARB(
      index,
      v);
  }

  public static void glVertexAttrib2svARB(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib2svARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib3dARB(
    int index,
    double x,
    double y,
    double z) {
    gl().glVertexAttrib3dARB(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttrib3dvARB(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib3dvARB(
      index,
      v);
  }

  public static void glVertexAttrib3dvARB(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib3dvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib3fARB(
    int index,
    float x,
    float y,
    float z) {
    gl().glVertexAttrib3fARB(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttrib3fvARB(
    int index,
    FloatBuffer v) {
    gl().glVertexAttrib3fvARB(
      index,
      v);
  }

  public static void glVertexAttrib3fvARB(
    int index,
    float[] v,
    int v_offset) {
    gl().glVertexAttrib3fvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib3h(
    int index,
    short x,
    short y,
    short z) {
    gl().glVertexAttrib3h(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttrib3hv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib3hv(
      index,
      v);
  }

  public static void glVertexAttrib3hv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib3hv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib3sARB(
    int index,
    short x,
    short y,
    short z) {
    gl().glVertexAttrib3sARB(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttrib3svARB(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib3svARB(
      index,
      v);
  }

  public static void glVertexAttrib3svARB(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib3svARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NbvARB(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4NbvARB(
      index,
      v);
  }

  public static void glVertexAttrib4NbvARB(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4NbvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NivARB(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4NivARB(
      index,
      v);
  }

  public static void glVertexAttrib4NivARB(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4NivARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NsvARB(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4NsvARB(
      index,
      v);
  }

  public static void glVertexAttrib4NsvARB(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4NsvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NubARB(
    int index,
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

  public static void glVertexAttrib4NubvARB(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4NubvARB(
      index,
      v);
  }

  public static void glVertexAttrib4NubvARB(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4NubvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NuivARB(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4NuivARB(
      index,
      v);
  }

  public static void glVertexAttrib4NuivARB(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4NuivARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4NusvARB(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4NusvARB(
      index,
      v);
  }

  public static void glVertexAttrib4NusvARB(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4NusvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4bvARB(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4bvARB(
      index,
      v);
  }

  public static void glVertexAttrib4bvARB(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4bvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4dARB(
    int index,
    double x,
    double y,
    double z,
    double w) {
    gl().glVertexAttrib4dARB(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4dvARB(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib4dvARB(
      index,
      v);
  }

  public static void glVertexAttrib4dvARB(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib4dvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4fARB(
    int index,
    float x,
    float y,
    float z,
    float w) {
    gl().glVertexAttrib4fARB(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4fvARB(
    int index,
    FloatBuffer v) {
    gl().glVertexAttrib4fvARB(
      index,
      v);
  }

  public static void glVertexAttrib4fvARB(
    int index,
    float[] v,
    int v_offset) {
    gl().glVertexAttrib4fvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4h(
    int index,
    short x,
    short y,
    short z,
    short w) {
    gl().glVertexAttrib4h(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4hv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4hv(
      index,
      v);
  }

  public static void glVertexAttrib4hv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4hv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4ivARB(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4ivARB(
      index,
      v);
  }

  public static void glVertexAttrib4ivARB(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4ivARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4sARB(
    int index,
    short x,
    short y,
    short z,
    short w) {
    gl().glVertexAttrib4sARB(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4svARB(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4svARB(
      index,
      v);
  }

  public static void glVertexAttrib4svARB(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4svARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4ubvARB(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4ubvARB(
      index,
      v);
  }

  public static void glVertexAttrib4ubvARB(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4ubvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4uivARB(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4uivARB(
      index,
      v);
  }

  public static void glVertexAttrib4uivARB(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4uivARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4usvARB(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4usvARB(
      index,
      v);
  }

  public static void glVertexAttrib4usvARB(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4usvARB(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI1iEXT(
    int index,
    int x) {
    gl().glVertexAttribI1iEXT(
      index,
      x);
  }

  public static void glVertexAttribI1ivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI1ivEXT(
      index,
      v);
  }

  public static void glVertexAttribI1ivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI1ivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI1uiEXT(
    int index,
    int x) {
    gl().glVertexAttribI1uiEXT(
      index,
      x);
  }

  public static void glVertexAttribI1uivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI1uivEXT(
      index,
      v);
  }

  public static void glVertexAttribI1uivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI1uivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI2iEXT(
    int index,
    int x,
    int y) {
    gl().glVertexAttribI2iEXT(
      index,
      x,
      y);
  }

  public static void glVertexAttribI2ivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI2ivEXT(
      index,
      v);
  }

  public static void glVertexAttribI2ivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI2ivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI2uiEXT(
    int index,
    int x,
    int y) {
    gl().glVertexAttribI2uiEXT(
      index,
      x,
      y);
  }

  public static void glVertexAttribI2uivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI2uivEXT(
      index,
      v);
  }

  public static void glVertexAttribI2uivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI2uivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI3iEXT(
    int index,
    int x,
    int y,
    int z) {
    gl().glVertexAttribI3iEXT(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttribI3ivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI3ivEXT(
      index,
      v);
  }

  public static void glVertexAttribI3ivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI3ivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI3uiEXT(
    int index,
    int x,
    int y,
    int z) {
    gl().glVertexAttribI3uiEXT(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttribI3uivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI3uivEXT(
      index,
      v);
  }

  public static void glVertexAttribI3uivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI3uivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4bvEXT(
    int index,
    ByteBuffer v) {
    gl().glVertexAttribI4bvEXT(
      index,
      v);
  }

  public static void glVertexAttribI4bvEXT(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttribI4bvEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4iEXT(
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glVertexAttribI4iEXT(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttribI4ivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI4ivEXT(
      index,
      v);
  }

  public static void glVertexAttribI4ivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI4ivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4svEXT(
    int index,
    ShortBuffer v) {
    gl().glVertexAttribI4svEXT(
      index,
      v);
  }

  public static void glVertexAttribI4svEXT(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttribI4svEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4ubvEXT(
    int index,
    ByteBuffer v) {
    gl().glVertexAttribI4ubvEXT(
      index,
      v);
  }

  public static void glVertexAttribI4ubvEXT(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttribI4ubvEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4uiEXT(
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glVertexAttribI4uiEXT(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttribI4uivEXT(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI4uivEXT(
      index,
      v);
  }

  public static void glVertexAttribI4uivEXT(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI4uivEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4usvEXT(
    int index,
    ShortBuffer v) {
    gl().glVertexAttribI4usvEXT(
      index,
      v);
  }

  public static void glVertexAttribI4usvEXT(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttribI4usvEXT(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribIPointerEXT(
    int index,
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glVertexAttribIPointerEXT(
      index,
      size,
      type,
      stride,
      pointer);
  }

  public static void glVertexAttribL1i64NV(
    int index,
    long x) {
    gl().glVertexAttribL1i64NV(
      index,
      x);
  }

  public static void glVertexAttribL1i64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL1i64vNV(
      index,
      v);
  }

  public static void glVertexAttribL1i64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL1i64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL1ui64NV(
    int index,
    long x) {
    gl().glVertexAttribL1ui64NV(
      index,
      x);
  }

  public static void glVertexAttribL1ui64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL1ui64vNV(
      index,
      v);
  }

  public static void glVertexAttribL1ui64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL1ui64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL2i64NV(
    int index,
    long x,
    long y) {
    gl().glVertexAttribL2i64NV(
      index,
      x,
      y);
  }

  public static void glVertexAttribL2i64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL2i64vNV(
      index,
      v);
  }

  public static void glVertexAttribL2i64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL2i64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL2ui64NV(
    int index,
    long x,
    long y) {
    gl().glVertexAttribL2ui64NV(
      index,
      x,
      y);
  }

  public static void glVertexAttribL2ui64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL2ui64vNV(
      index,
      v);
  }

  public static void glVertexAttribL2ui64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL2ui64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL3i64NV(
    int index,
    long x,
    long y,
    long z) {
    gl().glVertexAttribL3i64NV(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttribL3i64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL3i64vNV(
      index,
      v);
  }

  public static void glVertexAttribL3i64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL3i64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL3ui64NV(
    int index,
    long x,
    long y,
    long z) {
    gl().glVertexAttribL3ui64NV(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttribL3ui64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL3ui64vNV(
      index,
      v);
  }

  public static void glVertexAttribL3ui64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL3ui64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL4i64NV(
    int index,
    long x,
    long y,
    long z,
    long w) {
    gl().glVertexAttribL4i64NV(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttribL4i64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL4i64vNV(
      index,
      v);
  }

  public static void glVertexAttribL4i64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL4i64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL4ui64NV(
    int index,
    long x,
    long y,
    long z,
    long w) {
    gl().glVertexAttribL4ui64NV(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttribL4ui64vNV(
    int index,
    LongBuffer v) {
    gl().glVertexAttribL4ui64vNV(
      index,
      v);
  }

  public static void glVertexAttribL4ui64vNV(
    int index,
    long[] v,
    int v_offset) {
    gl().glVertexAttribL4ui64vNV(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribLFormatNV(
    int index,
    int size,
    int type,
    int stride) {
    gl().glVertexAttribLFormatNV(
      index,
      size,
      type,
      stride);
  }

  public static void glVertexAttribPointerARB(
    int index,
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

  public static void glVertexAttribPointerARB(
    int index,
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

  public static void glVertexAttribs1hv(
    int index,
    int n,
    ShortBuffer v) {
    gl().glVertexAttribs1hv(
      index,
      n,
      v);
  }

  public static void glVertexAttribs1hv(
    int index,
    int n,
    short[] v,
    int v_offset) {
    gl().glVertexAttribs1hv(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexAttribs2hv(
    int index,
    int n,
    ShortBuffer v) {
    gl().glVertexAttribs2hv(
      index,
      n,
      v);
  }

  public static void glVertexAttribs2hv(
    int index,
    int n,
    short[] v,
    int v_offset) {
    gl().glVertexAttribs2hv(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexAttribs3hv(
    int index,
    int n,
    ShortBuffer v) {
    gl().glVertexAttribs3hv(
      index,
      n,
      v);
  }

  public static void glVertexAttribs3hv(
    int index,
    int n,
    short[] v,
    int v_offset) {
    gl().glVertexAttribs3hv(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexAttribs4hv(
    int index,
    int n,
    ShortBuffer v) {
    gl().glVertexAttribs4hv(
      index,
      n,
      v);
  }

  public static void glVertexAttribs4hv(
    int index,
    int n,
    short[] v,
    int v_offset) {
    gl().glVertexAttribs4hv(
      index,
      n,
      v,
      v_offset);
  }

  public static void glVertexBlendARB(
    int count) {
    gl().glVertexBlendARB(
      count);
  }

  public static void glVertexWeightPointerEXT(
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glVertexWeightPointerEXT(
      size,
      type,
      stride,
      pointer);
  }

  public static void glVertexWeightPointerEXT(
    int size,
    int type,
    int stride,
    long pointer_buffer_offset) {
    gl().glVertexWeightPointerEXT(
      size,
      type,
      stride,
      pointer_buffer_offset);
  }

  public static void glVertexWeightfEXT(
    float weight) {
    gl().glVertexWeightfEXT(
      weight);
  }

  public static void glVertexWeightfvEXT(
    FloatBuffer weight) {
    gl().glVertexWeightfvEXT(
      weight);
  }

  public static void glVertexWeightfvEXT(
    float[] weight,
    int weight_offset) {
    gl().glVertexWeightfvEXT(
      weight,
      weight_offset);
  }

  public static void glVertexWeighth(
    short weight) {
    gl().glVertexWeighth(
      weight);
  }

  public static void glVertexWeighthv(
    ShortBuffer weight) {
    gl().glVertexWeighthv(
      weight);
  }

  public static void glVertexWeighthv(
    short[] weight,
    int weight_offset) {
    gl().glVertexWeighthv(
      weight,
      weight_offset);
  }

  public static int glVideoCaptureNV(
    int video_capture_slot,
    IntBuffer sequence_num,
    LongBuffer capture_time) {
    return gl().glVideoCaptureNV(
      video_capture_slot,
      sequence_num,
      capture_time);
  }

  public static int glVideoCaptureNV(
    int video_capture_slot,
    int[] sequence_num,
    int sequence_num_offset,
    long[] capture_time,
    int capture_time_offset) {
    return gl().glVideoCaptureNV(
      video_capture_slot,
      sequence_num,
      sequence_num_offset,
      capture_time,
      capture_time_offset);
  }

  public static void glVideoCaptureStreamParameterdvNV(
    int video_capture_slot,
    int stream,
    int pname,
    DoubleBuffer params) {
    gl().glVideoCaptureStreamParameterdvNV(
      video_capture_slot,
      stream,
      pname,
      params);
  }

  public static void glVideoCaptureStreamParameterdvNV(
    int video_capture_slot,
    int stream,
    int pname,
    double[] params,
    int params_offset) {
    gl().glVideoCaptureStreamParameterdvNV(
      video_capture_slot,
      stream,
      pname,
      params,
      params_offset);
  }

  public static void glVideoCaptureStreamParameterfvNV(
    int video_capture_slot,
    int stream,
    int pname,
    FloatBuffer params) {
    gl().glVideoCaptureStreamParameterfvNV(
      video_capture_slot,
      stream,
      pname,
      params);
  }

  public static void glVideoCaptureStreamParameterfvNV(
    int video_capture_slot,
    int stream,
    int pname,
    float[] params,
    int params_offset) {
    gl().glVideoCaptureStreamParameterfvNV(
      video_capture_slot,
      stream,
      pname,
      params,
      params_offset);
  }

  public static void glVideoCaptureStreamParameterivNV(
    int video_capture_slot,
    int stream,
    int pname,
    IntBuffer params) {
    gl().glVideoCaptureStreamParameterivNV(
      video_capture_slot,
      stream,
      pname,
      params);
  }

  public static void glVideoCaptureStreamParameterivNV(
    int video_capture_slot,
    int stream,
    int pname,
    int[] params,
    int params_offset) {
    gl().glVideoCaptureStreamParameterivNV(
      video_capture_slot,
      stream,
      pname,
      params,
      params_offset);
  }

  public static void glWeightbvARB(
    int size,
    ByteBuffer weights) {
    gl().glWeightbvARB(
      size,
      weights);
  }

  public static void glWeightbvARB(
    int size,
    byte[] weights,
    int weights_offset) {
    gl().glWeightbvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightdvARB(
    int size,
    DoubleBuffer weights) {
    gl().glWeightdvARB(
      size,
      weights);
  }

  public static void glWeightdvARB(
    int size,
    double[] weights,
    int weights_offset) {
    gl().glWeightdvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightfvARB(
    int size,
    FloatBuffer weights) {
    gl().glWeightfvARB(
      size,
      weights);
  }

  public static void glWeightfvARB(
    int size,
    float[] weights,
    int weights_offset) {
    gl().glWeightfvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightivARB(
    int size,
    IntBuffer weights) {
    gl().glWeightivARB(
      size,
      weights);
  }

  public static void glWeightivARB(
    int size,
    int[] weights,
    int weights_offset) {
    gl().glWeightivARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightsvARB(
    int size,
    ShortBuffer weights) {
    gl().glWeightsvARB(
      size,
      weights);
  }

  public static void glWeightsvARB(
    int size,
    short[] weights,
    int weights_offset) {
    gl().glWeightsvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightubvARB(
    int size,
    ByteBuffer weights) {
    gl().glWeightubvARB(
      size,
      weights);
  }

  public static void glWeightubvARB(
    int size,
    byte[] weights,
    int weights_offset) {
    gl().glWeightubvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightuivARB(
    int size,
    IntBuffer weights) {
    gl().glWeightuivARB(
      size,
      weights);
  }

  public static void glWeightuivARB(
    int size,
    int[] weights,
    int weights_offset) {
    gl().glWeightuivARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWeightusvARB(
    int size,
    ShortBuffer weights) {
    gl().glWeightusvARB(
      size,
      weights);
  }

  public static void glWeightusvARB(
    int size,
    short[] weights,
    int weights_offset) {
    gl().glWeightusvARB(
      size,
      weights,
      weights_offset);
  }

  public static void glWindowPos2d(
    double x,
    double y) {
    gl().glWindowPos2d(
      x,
      y);
  }

  public static void glWindowPos2dv(
    DoubleBuffer v) {
    gl().glWindowPos2dv(
      v);
  }

  public static void glWindowPos2dv(
    double[] v,
    int v_offset) {
    gl().glWindowPos2dv(
      v,
      v_offset);
  }

  public static void glWindowPos2f(
    float x,
    float y) {
    gl().glWindowPos2f(
      x,
      y);
  }

  public static void glWindowPos2fv(
    FloatBuffer v) {
    gl().glWindowPos2fv(
      v);
  }

  public static void glWindowPos2fv(
    float[] v,
    int v_offset) {
    gl().glWindowPos2fv(
      v,
      v_offset);
  }

  public static void glWindowPos2i(
    int x,
    int y) {
    gl().glWindowPos2i(
      x,
      y);
  }

  public static void glWindowPos2iv(
    IntBuffer v) {
    gl().glWindowPos2iv(
      v);
  }

  public static void glWindowPos2iv(
    int[] v,
    int v_offset) {
    gl().glWindowPos2iv(
      v,
      v_offset);
  }

  public static void glWindowPos2s(
    short x,
    short y) {
    gl().glWindowPos2s(
      x,
      y);
  }

  public static void glWindowPos2sv(
    ShortBuffer v) {
    gl().glWindowPos2sv(
      v);
  }

  public static void glWindowPos2sv(
    short[] v,
    int v_offset) {
    gl().glWindowPos2sv(
      v,
      v_offset);
  }

  public static void glWindowPos3d(
    double x,
    double y,
    double z) {
    gl().glWindowPos3d(
      x,
      y,
      z);
  }

  public static void glWindowPos3dv(
    DoubleBuffer v) {
    gl().glWindowPos3dv(
      v);
  }

  public static void glWindowPos3dv(
    double[] v,
    int v_offset) {
    gl().glWindowPos3dv(
      v,
      v_offset);
  }

  public static void glWindowPos3f(
    float x,
    float y,
    float z) {
    gl().glWindowPos3f(
      x,
      y,
      z);
  }

  public static void glWindowPos3fv(
    FloatBuffer v) {
    gl().glWindowPos3fv(
      v);
  }

  public static void glWindowPos3fv(
    float[] v,
    int v_offset) {
    gl().glWindowPos3fv(
      v,
      v_offset);
  }

  public static void glWindowPos3i(
    int x,
    int y,
    int z) {
    gl().glWindowPos3i(
      x,
      y,
      z);
  }

  public static void glWindowPos3iv(
    IntBuffer v) {
    gl().glWindowPos3iv(
      v);
  }

  public static void glWindowPos3iv(
    int[] v,
    int v_offset) {
    gl().glWindowPos3iv(
      v,
      v_offset);
  }

  public static void glWindowPos3s(
    short x,
    short y,
    short z) {
    gl().glWindowPos3s(
      x,
      y,
      z);
  }

  public static void glWindowPos3sv(
    ShortBuffer v) {
    gl().glWindowPos3sv(
      v);
  }

  public static void glWindowPos3sv(
    short[] v,
    int v_offset) {
    gl().glWindowPos3sv(
      v,
      v_offset);
  }

  public static void glWriteMaskEXT(
    int res,
    int in,
    int outX,
    int outY,
    int outZ,
    int outW) {
    gl().glWriteMaskEXT(
      res,
      in,
      outX,
      outY,
      outZ,
      outW);
  }

  public static boolean glIsPBOPackEnabled(
    ) {
    return gl().glIsPBOPackEnabled();
  }

  public static boolean glIsPBOUnpackEnabled(
    ) {
    return gl().glIsPBOUnpackEnabled();
  }


  // Generated from GL2ES1.html

  public static final int GL_CLIP_PLANE0
    = GL2.GL_CLIP_PLANE0;

  public static final int GL_CLIP_PLANE1
    = GL2.GL_CLIP_PLANE1;

  public static final int GL_CLIP_PLANE2
    = GL2.GL_CLIP_PLANE2;

  public static final int GL_CLIP_PLANE3
    = GL2.GL_CLIP_PLANE3;

  public static final int GL_CLIP_PLANE4
    = GL2.GL_CLIP_PLANE4;

  public static final int GL_CLIP_PLANE5
    = GL2.GL_CLIP_PLANE5;

  public static final int GL_FOG
    = GL2.GL_FOG;

  public static final int GL_ALPHA_TEST
    = GL2.GL_ALPHA_TEST;

  public static final int GL_POINT_SMOOTH
    = GL2.GL_POINT_SMOOTH;

  public static final int GL_RESCALE_NORMAL
    = GL2.GL_RESCALE_NORMAL;

  public static final int GL_STACK_OVERFLOW
    = GL2.GL_STACK_OVERFLOW;

  public static final int GL_STACK_UNDERFLOW
    = GL2.GL_STACK_UNDERFLOW;

  public static final int GL_EXP
    = GL2.GL_EXP;

  public static final int GL_EXP2
    = GL2.GL_EXP2;

  public static final int GL_FOG_DENSITY
    = GL2.GL_FOG_DENSITY;

  public static final int GL_FOG_START
    = GL2.GL_FOG_START;

  public static final int GL_FOG_END
    = GL2.GL_FOG_END;

  public static final int GL_FOG_MODE
    = GL2.GL_FOG_MODE;

  public static final int GL_FOG_COLOR
    = GL2.GL_FOG_COLOR;

  public static final int GL_CURRENT_COLOR
    = GL2.GL_CURRENT_COLOR;

  public static final int GL_CURRENT_NORMAL
    = GL2.GL_CURRENT_NORMAL;

  public static final int GL_CURRENT_TEXTURE_COORDS
    = GL2.GL_CURRENT_TEXTURE_COORDS;

  public static final int GL_POINT_SIZE_MIN
    = GL2.GL_POINT_SIZE_MIN;

  public static final int GL_POINT_SIZE_MAX
    = GL2.GL_POINT_SIZE_MAX;

  public static final int GL_POINT_DISTANCE_ATTENUATION
    = GL2.GL_POINT_DISTANCE_ATTENUATION;

  public static final int GL_SHADE_MODEL
    = GL2.GL_SHADE_MODEL;

  public static final int GL_MODELVIEW_STACK_DEPTH
    = GL2.GL_MODELVIEW_STACK_DEPTH;

  public static final int GL_PROJECTION_STACK_DEPTH
    = GL2.GL_PROJECTION_STACK_DEPTH;

  public static final int GL_TEXTURE_STACK_DEPTH
    = GL2.GL_TEXTURE_STACK_DEPTH;

  public static final int GL_ALPHA_TEST_FUNC
    = GL2.GL_ALPHA_TEST_FUNC;

  public static final int GL_ALPHA_TEST_REF
    = GL2.GL_ALPHA_TEST_REF;

  public static final int GL_MAX_LIGHTS
    = GL2.GL_MAX_LIGHTS;

  public static final int GL_MAX_CLIP_PLANES
    = GL2.GL_MAX_CLIP_PLANES;

  public static final int GL_MAX_MODELVIEW_STACK_DEPTH
    = GL2.GL_MAX_MODELVIEW_STACK_DEPTH;

  public static final int GL_MAX_PROJECTION_STACK_DEPTH
    = GL2.GL_MAX_PROJECTION_STACK_DEPTH;

  public static final int GL_MAX_TEXTURE_STACK_DEPTH
    = GL2.GL_MAX_TEXTURE_STACK_DEPTH;

  public static final int GL_MAX_TEXTURE_UNITS
    = GL2.GL_MAX_TEXTURE_UNITS;

  public static final int GL_VERTEX_ARRAY_SIZE
    = GL2.GL_VERTEX_ARRAY_SIZE;

  public static final int GL_VERTEX_ARRAY_TYPE
    = GL2.GL_VERTEX_ARRAY_TYPE;

  public static final int GL_VERTEX_ARRAY_STRIDE
    = GL2.GL_VERTEX_ARRAY_STRIDE;

  public static final int GL_NORMAL_ARRAY_TYPE
    = GL2.GL_NORMAL_ARRAY_TYPE;

  public static final int GL_NORMAL_ARRAY_STRIDE
    = GL2.GL_NORMAL_ARRAY_STRIDE;

  public static final int GL_COLOR_ARRAY_SIZE
    = GL2.GL_COLOR_ARRAY_SIZE;

  public static final int GL_COLOR_ARRAY_TYPE
    = GL2.GL_COLOR_ARRAY_TYPE;

  public static final int GL_COLOR_ARRAY_STRIDE
    = GL2.GL_COLOR_ARRAY_STRIDE;

  public static final int GL_TEXTURE_COORD_ARRAY_SIZE
    = GL2.GL_TEXTURE_COORD_ARRAY_SIZE;

  public static final int GL_TEXTURE_COORD_ARRAY_TYPE
    = GL2.GL_TEXTURE_COORD_ARRAY_TYPE;

  public static final int GL_TEXTURE_COORD_ARRAY_STRIDE
    = GL2.GL_TEXTURE_COORD_ARRAY_STRIDE;

  public static final int GL_VERTEX_ARRAY_POINTER
    = GL2.GL_VERTEX_ARRAY_POINTER;

  public static final int GL_NORMAL_ARRAY_POINTER
    = GL2.GL_NORMAL_ARRAY_POINTER;

  public static final int GL_COLOR_ARRAY_POINTER
    = GL2.GL_COLOR_ARRAY_POINTER;

  public static final int GL_TEXTURE_COORD_ARRAY_POINTER
    = GL2.GL_TEXTURE_COORD_ARRAY_POINTER;

  public static final int GL_PERSPECTIVE_CORRECTION_HINT
    = GL2.GL_PERSPECTIVE_CORRECTION_HINT;

  public static final int GL_POINT_SMOOTH_HINT
    = GL2.GL_POINT_SMOOTH_HINT;

  public static final int GL_FOG_HINT
    = GL2.GL_FOG_HINT;

  public static final int GL_LIGHT_MODEL_AMBIENT
    = GL2.GL_LIGHT_MODEL_AMBIENT;

  public static final int GL_LIGHT_MODEL_TWO_SIDE
    = GL2.GL_LIGHT_MODEL_TWO_SIDE;

  public static final int GL_MODULATE
    = GL2.GL_MODULATE;

  public static final int GL_DECAL
    = GL2.GL_DECAL;

  public static final int GL_ADD
    = GL2.GL_ADD;

  public static final int GL_TEXTURE_ENV_MODE
    = GL2.GL_TEXTURE_ENV_MODE;

  public static final int GL_TEXTURE_ENV_COLOR
    = GL2.GL_TEXTURE_ENV_COLOR;

  public static final int GL_TEXTURE_ENV
    = GL2.GL_TEXTURE_ENV;

  public static final int GL_GENERATE_MIPMAP
    = GL2.GL_GENERATE_MIPMAP;

  public static final int GL_CLIENT_ACTIVE_TEXTURE
    = GL2.GL_CLIENT_ACTIVE_TEXTURE;

  public static final int GL_VERTEX_ARRAY_BUFFER_BINDING
    = GL2.GL_VERTEX_ARRAY_BUFFER_BINDING;

  public static final int GL_NORMAL_ARRAY_BUFFER_BINDING
    = GL2.GL_NORMAL_ARRAY_BUFFER_BINDING;

  public static final int GL_COLOR_ARRAY_BUFFER_BINDING
    = GL2.GL_COLOR_ARRAY_BUFFER_BINDING;

  public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING
    = GL2.GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING;

  public static final int GL_SUBTRACT
    = GL2.GL_SUBTRACT;

  public static final int GL_COMBINE
    = GL2.GL_COMBINE;

  public static final int GL_COMBINE_RGB
    = GL2.GL_COMBINE_RGB;

  public static final int GL_COMBINE_ALPHA
    = GL2.GL_COMBINE_ALPHA;

  public static final int GL_RGB_SCALE
    = GL2.GL_RGB_SCALE;

  public static final int GL_ADD_SIGNED
    = GL2.GL_ADD_SIGNED;

  public static final int GL_INTERPOLATE
    = GL2.GL_INTERPOLATE;

  public static final int GL_CONSTANT
    = GL2.GL_CONSTANT;

  public static final int GL_PRIMARY_COLOR
    = GL2.GL_PRIMARY_COLOR;

  public static final int GL_PREVIOUS
    = GL2.GL_PREVIOUS;

  public static final int GL_OPERAND0_RGB
    = GL2.GL_OPERAND0_RGB;

  public static final int GL_OPERAND1_RGB
    = GL2.GL_OPERAND1_RGB;

  public static final int GL_OPERAND2_RGB
    = GL2.GL_OPERAND2_RGB;

  public static final int GL_OPERAND0_ALPHA
    = GL2.GL_OPERAND0_ALPHA;

  public static final int GL_OPERAND1_ALPHA
    = GL2.GL_OPERAND1_ALPHA;

  public static final int GL_OPERAND2_ALPHA
    = GL2.GL_OPERAND2_ALPHA;

  public static final int GL_ALPHA_SCALE
    = GL2.GL_ALPHA_SCALE;

  public static final int GL_SRC0_RGB
    = GL2.GL_SRC0_RGB;

  public static final int GL_SRC1_RGB
    = GL2.GL_SRC1_RGB;

  public static final int GL_SRC2_RGB
    = GL2.GL_SRC2_RGB;

  public static final int GL_SRC0_ALPHA
    = GL2.GL_SRC0_ALPHA;

  public static final int GL_SRC1_ALPHA
    = GL2.GL_SRC1_ALPHA;

  public static final int GL_SRC2_ALPHA
    = GL2.GL_SRC2_ALPHA;

  public static final int GL_DOT3_RGB
    = GL2.GL_DOT3_RGB;

  public static final int GL_DOT3_RGBA
    = GL2.GL_DOT3_RGBA;

  public static final int GL_POINT_SPRITE
    = GL2.GL_POINT_SPRITE;

  public static final int GL_COORD_REPLACE
    = GL2.GL_COORD_REPLACE;

  public static final int GL_MAX_VERTEX_UNITS
    = GL2.GL_MAX_VERTEX_UNITS;

  public static final int GL_MAX_PALETTE_MATRICES
    = GL2.GL_MAX_PALETTE_MATRICES;

  public static final int GL_MATRIX_PALETTE
    = GL2.GL_MATRIX_PALETTE;

  public static final int GL_MATRIX_INDEX_ARRAY
    = GL2.GL_MATRIX_INDEX_ARRAY;

  public static final int GL_WEIGHT_ARRAY
    = GL2.GL_WEIGHT_ARRAY;

  public static final int GL_CURRENT_PALETTE_MATRIX
    = GL2.GL_CURRENT_PALETTE_MATRIX;

  public static final int GL_MATRIX_INDEX_ARRAY_SIZE
    = GL2.GL_MATRIX_INDEX_ARRAY_SIZE;

  public static final int GL_MATRIX_INDEX_ARRAY_TYPE
    = GL2.GL_MATRIX_INDEX_ARRAY_TYPE;

  public static final int GL_MATRIX_INDEX_ARRAY_STRIDE
    = GL2.GL_MATRIX_INDEX_ARRAY_STRIDE;

  public static final int GL_MATRIX_INDEX_ARRAY_POINTER
    = GL2.GL_MATRIX_INDEX_ARRAY_POINTER;

  public static final int GL_WEIGHT_ARRAY_SIZE
    = GL2.GL_WEIGHT_ARRAY_SIZE;

  public static final int GL_WEIGHT_ARRAY_TYPE
    = GL2.GL_WEIGHT_ARRAY_TYPE;

  public static final int GL_WEIGHT_ARRAY_STRIDE
    = GL2.GL_WEIGHT_ARRAY_STRIDE;

  public static final int GL_WEIGHT_ARRAY_POINTER
    = GL2.GL_WEIGHT_ARRAY_POINTER;

  public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING
    = GL2.GL_WEIGHT_ARRAY_BUFFER_BINDING;

  public static final int GL_NORMAL_MAP
    = GL2.GL_NORMAL_MAP;

  public static final int GL_REFLECTION_MAP
    = GL2.GL_REFLECTION_MAP;

  public static final int GL_TEXTURE_GEN_MODE
    = GL2.GL_TEXTURE_GEN_MODE;

  public static final int GL_TEXTURE_GEN_STR
    = GL2.GL_TEXTURE_GEN_STR;

  public static final int GL_CONTEXT_ROBUST_ACCESS
    = GL2ES1.GL_CONTEXT_ROBUST_ACCESS;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING
    = GL2ES1.GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING;

  public static final int GL_RGB16F
    = GL2ES1.GL_RGB16F;

  public static final int GL_BGRA8_EXT
    = GL2ES1.GL_BGRA8_EXT;

  public static void glAlphaFunc(
    int func,
    float ref) {
    gl().glAlphaFunc(
      func,
      ref);
  }

  public static void glClientActiveTexture(
    int texture) {
    gl().glClientActiveTexture(
      texture);
  }

  public static void glColor4ub(
    byte red,
    byte green,
    byte blue,
    byte alpha) {
    gl().glColor4ub(
      red,
      green,
      blue,
      alpha);
  }

  public static void glCurrentPaletteMatrix(
    int matrixpaletteindex) {
    gl().glCurrentPaletteMatrix(
      matrixpaletteindex);
  }

  public static void glFogf(
    int pname,
    float param) {
    gl().glFogf(
      pname,
      param);
  }

  public static void glFogfv(
    int pname,
    FloatBuffer params) {
    gl().glFogfv(
      pname,
      params);
  }

  public static void glFogfv(
    int pname,
    float[] params,
    int params_offset) {
    gl().glFogfv(
      pname,
      params,
      params_offset);
  }

  public static void glGetLightfv(
    int light,
    int pname,
    FloatBuffer params) {
    gl().glGetLightfv(
      light,
      pname,
      params);
  }

  public static void glGetLightfv(
    int light,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetLightfv(
      light,
      pname,
      params,
      params_offset);
  }

  public static void glGetMaterialfv(
    int face,
    int pname,
    FloatBuffer params) {
    gl().glGetMaterialfv(
      face,
      pname,
      params);
  }

  public static void glGetMaterialfv(
    int face,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetMaterialfv(
      face,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexEnvfv(
    int tenv,
    int pname,
    FloatBuffer params) {
    gl().glGetTexEnvfv(
      tenv,
      pname,
      params);
  }

  public static void glGetTexEnvfv(
    int tenv,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetTexEnvfv(
      tenv,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexEnviv(
    int tenv,
    int pname,
    IntBuffer params) {
    gl().glGetTexEnviv(
      tenv,
      pname,
      params);
  }

  public static void glGetTexEnviv(
    int tenv,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTexEnviv(
      tenv,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexGenfv(
    int coord,
    int pname,
    FloatBuffer params) {
    gl().glGetTexGenfv(
      coord,
      pname,
      params);
  }

  public static void glGetTexGenfv(
    int coord,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetTexGenfv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexGeniv(
    int coord,
    int pname,
    IntBuffer params) {
    gl().glGetTexGeniv(
      coord,
      pname,
      params);
  }

  public static void glGetTexGeniv(
    int coord,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTexGeniv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glLightModelf(
    int pname,
    float param) {
    gl().glLightModelf(
      pname,
      param);
  }

  public static void glLightModelfv(
    int pname,
    FloatBuffer params) {
    gl().glLightModelfv(
      pname,
      params);
  }

  public static void glLightModelfv(
    int pname,
    float[] params,
    int params_offset) {
    gl().glLightModelfv(
      pname,
      params,
      params_offset);
  }

  public static void glLightf(
    int light,
    int pname,
    float param) {
    gl().glLightf(
      light,
      pname,
      param);
  }

  public static void glLogicOp(
    int opcode) {
    gl().glLogicOp(
      opcode);
  }

  public static void glMatrixIndexPointer(
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glMatrixIndexPointer(
      size,
      type,
      stride,
      pointer);
  }

  public static void glMultiTexCoord4f(
    int target,
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

  public static void glNormal3f(
    float nx,
    float ny,
    float nz) {
    gl().glNormal3f(
      nx,
      ny,
      nz);
  }

  public static void glPointParameterf(
    int pname,
    float param) {
    gl().glPointParameterf(
      pname,
      param);
  }

  public static void glPointParameterfv(
    int pname,
    FloatBuffer params) {
    gl().glPointParameterfv(
      pname,
      params);
  }

  public static void glPointParameterfv(
    int pname,
    float[] params,
    int params_offset) {
    gl().glPointParameterfv(
      pname,
      params,
      params_offset);
  }

  public static void glPointSize(
    float size) {
    gl().glPointSize(
      size);
  }

  public static void glTexEnvf(
    int target,
    int pname,
    float param) {
    gl().glTexEnvf(
      target,
      pname,
      param);
  }

  public static void glTexEnvfv(
    int target,
    int pname,
    FloatBuffer params) {
    gl().glTexEnvfv(
      target,
      pname,
      params);
  }

  public static void glTexEnvfv(
    int target,
    int pname,
    float[] params,
    int params_offset) {
    gl().glTexEnvfv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexEnvi(
    int target,
    int pname,
    int param) {
    gl().glTexEnvi(
      target,
      pname,
      param);
  }

  public static void glTexEnviv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glTexEnviv(
      target,
      pname,
      params);
  }

  public static void glTexEnviv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTexEnviv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexGenf(
    int coord,
    int pname,
    float param) {
    gl().glTexGenf(
      coord,
      pname,
      param);
  }

  public static void glTexGenfv(
    int coord,
    int pname,
    FloatBuffer params) {
    gl().glTexGenfv(
      coord,
      pname,
      params);
  }

  public static void glTexGenfv(
    int coord,
    int pname,
    float[] params,
    int params_offset) {
    gl().glTexGenfv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glTexGeni(
    int coord,
    int pname,
    int param) {
    gl().glTexGeni(
      coord,
      pname,
      param);
  }

  public static void glTexGeniv(
    int coord,
    int pname,
    IntBuffer params) {
    gl().glTexGeniv(
      coord,
      pname,
      params);
  }

  public static void glTexGeniv(
    int coord,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTexGeniv(
      coord,
      pname,
      params,
      params_offset);
  }

  public static void glWeightPointer(
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glWeightPointer(
      size,
      type,
      stride,
      pointer);
  }

  public static void glOrtho(
    double left,
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

  public static void glFrustum(
    double left,
    double right,
    double bottom,
    double top,
    double zNear,
    double zFar) {
    gl().glFrustum(
      left,
      right,
      bottom,
      top,
      zNear,
      zFar);
  }


  // Generated from GL2ES2.html

  public static final int GL_CONSTANT_COLOR
    = GL2.GL_CONSTANT_COLOR;

  public static final int GL_ONE_MINUS_CONSTANT_COLOR
    = GL2.GL_ONE_MINUS_CONSTANT_COLOR;

  public static final int GL_CONSTANT_ALPHA
    = GL2.GL_CONSTANT_ALPHA;

  public static final int GL_ONE_MINUS_CONSTANT_ALPHA
    = GL2.GL_ONE_MINUS_CONSTANT_ALPHA;

  public static final int GL_BLEND_COLOR
    = GL2.GL_BLEND_COLOR;

  public static final int GL_STREAM_DRAW
    = GL2.GL_STREAM_DRAW;

  public static final int GL_CURRENT_VERTEX_ATTRIB
    = GL2.GL_CURRENT_VERTEX_ATTRIB;

  public static final int GL_STENCIL_BACK_FUNC
    = GL2.GL_STENCIL_BACK_FUNC;

  public static final int GL_STENCIL_BACK_FAIL
    = GL2.GL_STENCIL_BACK_FAIL;

  public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL
    = GL2.GL_STENCIL_BACK_PASS_DEPTH_FAIL;

  public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS
    = GL2.GL_STENCIL_BACK_PASS_DEPTH_PASS;

  public static final int GL_STENCIL_BACK_REF
    = GL2.GL_STENCIL_BACK_REF;

  public static final int GL_STENCIL_BACK_VALUE_MASK
    = GL2.GL_STENCIL_BACK_VALUE_MASK;

  public static final int GL_STENCIL_BACK_WRITEMASK
    = GL2.GL_STENCIL_BACK_WRITEMASK;

  public static final int GL_INT
    = GL2.GL_INT;

  public static final int GL_DEPTH_COMPONENT
    = GL2.GL_DEPTH_COMPONENT;

  public static final int GL_FRAGMENT_SHADER
    = GL2.GL_FRAGMENT_SHADER;

  public static final int GL_VERTEX_SHADER
    = GL2.GL_VERTEX_SHADER;

  public static final int GL_MAX_VERTEX_ATTRIBS
    = GL2.GL_MAX_VERTEX_ATTRIBS;

  public static final int GL_MAX_VERTEX_UNIFORM_VECTORS
    = GL2.GL_MAX_VERTEX_UNIFORM_VECTORS;

  public static final int GL_MAX_VARYING_VECTORS
    = GL2.GL_MAX_VARYING_VECTORS;

  public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS
    = GL2.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;

  public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS
    = GL2.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS;

  public static final int GL_MAX_TEXTURE_IMAGE_UNITS
    = GL2.GL_MAX_TEXTURE_IMAGE_UNITS;

  public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS
    = GL2.GL_MAX_FRAGMENT_UNIFORM_VECTORS;

  public static final int GL_SHADER_TYPE
    = GL2.GL_SHADER_TYPE;

  public static final int GL_DELETE_STATUS
    = GL2.GL_DELETE_STATUS;

  public static final int GL_LINK_STATUS
    = GL2.GL_LINK_STATUS;

  public static final int GL_VALIDATE_STATUS
    = GL2.GL_VALIDATE_STATUS;

  public static final int GL_ATTACHED_SHADERS
    = GL2.GL_ATTACHED_SHADERS;

  public static final int GL_ACTIVE_UNIFORMS
    = GL2.GL_ACTIVE_UNIFORMS;

  public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH
    = GL2.GL_ACTIVE_UNIFORM_MAX_LENGTH;

  public static final int GL_ACTIVE_ATTRIBUTES
    = GL2.GL_ACTIVE_ATTRIBUTES;

  public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH
    = GL2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH;

  public static final int GL_SHADING_LANGUAGE_VERSION
    = GL2.GL_SHADING_LANGUAGE_VERSION;

  public static final int GL_CURRENT_PROGRAM
    = GL2.GL_CURRENT_PROGRAM;

  public static final int GL_FLOAT_VEC2
    = GL2.GL_FLOAT_VEC2;

  public static final int GL_FLOAT_VEC3
    = GL2.GL_FLOAT_VEC3;

  public static final int GL_FLOAT_VEC4
    = GL2.GL_FLOAT_VEC4;

  public static final int GL_INT_VEC2
    = GL2.GL_INT_VEC2;

  public static final int GL_INT_VEC3
    = GL2.GL_INT_VEC3;

  public static final int GL_INT_VEC4
    = GL2.GL_INT_VEC4;

  public static final int GL_BOOL
    = GL2.GL_BOOL;

  public static final int GL_BOOL_VEC2
    = GL2.GL_BOOL_VEC2;

  public static final int GL_BOOL_VEC3
    = GL2.GL_BOOL_VEC3;

  public static final int GL_BOOL_VEC4
    = GL2.GL_BOOL_VEC4;

  public static final int GL_FLOAT_MAT2
    = GL2.GL_FLOAT_MAT2;

  public static final int GL_FLOAT_MAT3
    = GL2.GL_FLOAT_MAT3;

  public static final int GL_FLOAT_MAT4
    = GL2.GL_FLOAT_MAT4;

  public static final int GL_SAMPLER_2D
    = GL2.GL_SAMPLER_2D;

  public static final int GL_SAMPLER_CUBE
    = GL2.GL_SAMPLER_CUBE;

  public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED
    = GL2.GL_VERTEX_ATTRIB_ARRAY_ENABLED;

  public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE
    = GL2.GL_VERTEX_ATTRIB_ARRAY_SIZE;

  public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE
    = GL2.GL_VERTEX_ATTRIB_ARRAY_STRIDE;

  public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE
    = GL2.GL_VERTEX_ATTRIB_ARRAY_TYPE;

  public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED
    = GL2.GL_VERTEX_ATTRIB_ARRAY_NORMALIZED;

  public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER
    = GL2.GL_VERTEX_ATTRIB_ARRAY_POINTER;

  public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING
    = GL2.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING;

  public static final int GL_COMPILE_STATUS
    = GL2.GL_COMPILE_STATUS;

  public static final int GL_INFO_LOG_LENGTH
    = GL2.GL_INFO_LOG_LENGTH;

  public static final int GL_SHADER_SOURCE_LENGTH
    = GL2.GL_SHADER_SOURCE_LENGTH;

  public static final int GL_SHADER_COMPILER
    = GL2.GL_SHADER_COMPILER;

  public static final int GL_SHADER_BINARY_FORMATS
    = GL2.GL_SHADER_BINARY_FORMATS;

  public static final int GL_NUM_SHADER_BINARY_FORMATS
    = GL2.GL_NUM_SHADER_BINARY_FORMATS;

  public static final int GL_LOW_FLOAT
    = GL2.GL_LOW_FLOAT;

  public static final int GL_MEDIUM_FLOAT
    = GL2.GL_MEDIUM_FLOAT;

  public static final int GL_HIGH_FLOAT
    = GL2.GL_HIGH_FLOAT;

  public static final int GL_LOW_INT
    = GL2.GL_LOW_INT;

  public static final int GL_MEDIUM_INT
    = GL2.GL_MEDIUM_INT;

  public static final int GL_HIGH_INT
    = GL2.GL_HIGH_INT;

  public static final int GL_STENCIL_INDEX
    = GL2.GL_STENCIL_INDEX;

  public static final int GL_PROGRAM_BINARY_LENGTH
    = GL2.GL_PROGRAM_BINARY_LENGTH;

  public static final int GL_NUM_PROGRAM_BINARY_FORMATS
    = GL2.GL_NUM_PROGRAM_BINARY_FORMATS;

  public static final int GL_PROGRAM_BINARY_FORMATS
    = GL2.GL_PROGRAM_BINARY_FORMATS;

  public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT
    = GL2.GL_FRAGMENT_SHADER_DERIVATIVE_HINT;

  public static final int GL_TEXTURE_WRAP_R
    = GL2.GL_TEXTURE_WRAP_R;

  public static final int GL_TEXTURE_3D
    = GL2.GL_TEXTURE_3D;

  public static final int GL_TEXTURE_BINDING_3D
    = GL2.GL_TEXTURE_BINDING_3D;

  public static final int GL_MAX_3D_TEXTURE_SIZE
    = GL2.GL_MAX_3D_TEXTURE_SIZE;

  public static final int GL_SAMPLER_3D
    = GL2.GL_SAMPLER_3D;

  public static final int GL_UNSIGNED_INT_10_10_10_2
    = GL2.GL_UNSIGNED_INT_10_10_10_2;

  public static final int GL_INT_10_10_10_2
    = GL2.GL_INT_10_10_10_2;

  public static final int GL_RGBA16F
    = GL2.GL_RGBA16F;

  public static final int GL_RG16F
    = GL2.GL_RG16F;

  public static final int GL_R16F
    = GL2.GL_R16F;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE;

  public static final int GL_UNSIGNED_NORMALIZED
    = GL2.GL_UNSIGNED_NORMALIZED;

  public static final int GL_ANY_SAMPLES_PASSED
    = GL2.GL_ANY_SAMPLES_PASSED;

  public static final int GL_ANY_SAMPLES_PASSED_CONSERVATIVE
    = GL2.GL_ANY_SAMPLES_PASSED_CONSERVATIVE;

  public static final int GL_CURRENT_QUERY
    = GL2.GL_CURRENT_QUERY;

  public static final int GL_QUERY_RESULT
    = GL2.GL_QUERY_RESULT;

  public static final int GL_QUERY_RESULT_AVAILABLE
    = GL2.GL_QUERY_RESULT_AVAILABLE;

  public static final int GL_VERTEX_SHADER_BIT
    = GL2.GL_VERTEX_SHADER_BIT;

  public static final int GL_FRAGMENT_SHADER_BIT
    = GL2.GL_FRAGMENT_SHADER_BIT;

  public static final int GL_PROGRAM_SEPARABLE
    = GL2.GL_PROGRAM_SEPARABLE;

  public static final int GL_ACTIVE_PROGRAM
    = GL2.GL_ACTIVE_PROGRAM;

  public static final int GL_PROGRAM_PIPELINE_BINDING
    = GL2.GL_PROGRAM_PIPELINE_BINDING;

  public static final int GL_TEXTURE_COMPARE_MODE
    = GL2.GL_TEXTURE_COMPARE_MODE;

  public static final int GL_TEXTURE_COMPARE_FUNC
    = GL2.GL_TEXTURE_COMPARE_FUNC;

  public static final int GL_COMPARE_REF_TO_TEXTURE
    = GL2.GL_COMPARE_REF_TO_TEXTURE;

  public static final int GL_SAMPLER_2D_SHADOW
    = GL2.GL_SAMPLER_2D_SHADOW;

  public static final int GL_RED
    = GL2.GL_RED;

  public static final int GL_RG
    = GL2.GL_RG;

  public static final int GL_R8
    = GL2.GL_R8;

  public static final int GL_RG8
    = GL2.GL_RG8;

  public static final int GL_UNSIGNED_INT_2_10_10_10_REV
    = GL2.GL_UNSIGNED_INT_2_10_10_10_REV;

  public static final int GL_UNPACK_ROW_LENGTH
    = GL2.GL_UNPACK_ROW_LENGTH;

  public static final int GL_UNPACK_SKIP_ROWS
    = GL2.GL_UNPACK_SKIP_ROWS;

  public static final int GL_UNPACK_SKIP_PIXELS
    = GL2.GL_UNPACK_SKIP_PIXELS;

  public static final int GL_SHADER_BINARY_DMP
    = GL2.GL_SHADER_BINARY_DMP;

  public static final int GL_MAX_DRAW_BUFFERS
    = GL2.GL_MAX_DRAW_BUFFERS;

  public static final int GL_DRAW_BUFFER0
    = GL2.GL_DRAW_BUFFER0;

  public static final int GL_DRAW_BUFFER1
    = GL2.GL_DRAW_BUFFER1;

  public static final int GL_DRAW_BUFFER2
    = GL2.GL_DRAW_BUFFER2;

  public static final int GL_DRAW_BUFFER3
    = GL2.GL_DRAW_BUFFER3;

  public static final int GL_DRAW_BUFFER4
    = GL2.GL_DRAW_BUFFER4;

  public static final int GL_DRAW_BUFFER5
    = GL2.GL_DRAW_BUFFER5;

  public static final int GL_DRAW_BUFFER6
    = GL2.GL_DRAW_BUFFER6;

  public static final int GL_DRAW_BUFFER7
    = GL2.GL_DRAW_BUFFER7;

  public static final int GL_DRAW_BUFFER8
    = GL2.GL_DRAW_BUFFER8;

  public static final int GL_DRAW_BUFFER9
    = GL2.GL_DRAW_BUFFER9;

  public static final int GL_DRAW_BUFFER10
    = GL2.GL_DRAW_BUFFER10;

  public static final int GL_DRAW_BUFFER11
    = GL2.GL_DRAW_BUFFER11;

  public static final int GL_DRAW_BUFFER12
    = GL2.GL_DRAW_BUFFER12;

  public static final int GL_DRAW_BUFFER13
    = GL2.GL_DRAW_BUFFER13;

  public static final int GL_DRAW_BUFFER14
    = GL2.GL_DRAW_BUFFER14;

  public static final int GL_DRAW_BUFFER15
    = GL2.GL_DRAW_BUFFER15;

  public static final int GL_COLOR_ATTACHMENT1
    = GL2.GL_COLOR_ATTACHMENT1;

  public static final int GL_COLOR_ATTACHMENT2
    = GL2.GL_COLOR_ATTACHMENT2;

  public static final int GL_COLOR_ATTACHMENT3
    = GL2.GL_COLOR_ATTACHMENT3;

  public static final int GL_COLOR_ATTACHMENT4
    = GL2.GL_COLOR_ATTACHMENT4;

  public static final int GL_COLOR_ATTACHMENT5
    = GL2.GL_COLOR_ATTACHMENT5;

  public static final int GL_COLOR_ATTACHMENT6
    = GL2.GL_COLOR_ATTACHMENT6;

  public static final int GL_COLOR_ATTACHMENT7
    = GL2.GL_COLOR_ATTACHMENT7;

  public static final int GL_COLOR_ATTACHMENT8
    = GL2.GL_COLOR_ATTACHMENT8;

  public static final int GL_COLOR_ATTACHMENT9
    = GL2.GL_COLOR_ATTACHMENT9;

  public static final int GL_COLOR_ATTACHMENT10
    = GL2.GL_COLOR_ATTACHMENT10;

  public static final int GL_COLOR_ATTACHMENT11
    = GL2.GL_COLOR_ATTACHMENT11;

  public static final int GL_COLOR_ATTACHMENT12
    = GL2.GL_COLOR_ATTACHMENT12;

  public static final int GL_COLOR_ATTACHMENT13
    = GL2.GL_COLOR_ATTACHMENT13;

  public static final int GL_COLOR_ATTACHMENT14
    = GL2.GL_COLOR_ATTACHMENT14;

  public static final int GL_COLOR_ATTACHMENT15
    = GL2.GL_COLOR_ATTACHMENT15;

  public static final int GL_MAX_COLOR_ATTACHMENTS
    = GL2.GL_MAX_COLOR_ATTACHMENTS;

  public static final int GL_ALL_SHADER_BITS
    = GL2.GL_ALL_SHADER_BITS;

  public static void glAttachShader(
    int program,
    int shader) {
    gl().glAttachShader(
      program,
      shader);
  }

  public static void glBeginQuery(
    int target,
    int id) {
    gl().glBeginQuery(
      target,
      id);
  }

  public static void glBindAttribLocation(
    int program,
    int index,
    String name) {
    gl().glBindAttribLocation(
      program,
      index,
      name);
  }

  public static void glBlendColor(
    float red,
    float green,
    float blue,
    float alpha) {
    gl().glBlendColor(
      red,
      green,
      blue,
      alpha);
  }

  public static void glCompileShader(
    int shader) {
    gl().glCompileShader(
      shader);
  }

  public static void glCompressedTexImage3D(
    int target,
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

  public static void glCompressedTexImage3D(
    int target,
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

  public static void glCompressedTexSubImage3D(
    int target,
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

  public static void glCompressedTexSubImage3D(
    int target,
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

  public static void glCopyTexSubImage3D(
    int target,
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

  public static int glCreateProgram(
    ) {
    return gl().glCreateProgram();
  }

  public static int glCreateShader(
    int type) {
    return gl().glCreateShader(
      type);
  }

  public static void glDeleteProgram(
    int program) {
    gl().glDeleteProgram(
      program);
  }

  public static void glDeleteQueries(
    int n,
    IntBuffer ids) {
    gl().glDeleteQueries(
      n,
      ids);
  }

  public static void glDeleteQueries(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glDeleteQueries(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteShader(
    int shader) {
    gl().glDeleteShader(
      shader);
  }

  public static void glDetachShader(
    int program,
    int shader) {
    gl().glDetachShader(
      program,
      shader);
  }

  public static void glDisableVertexAttribArray(
    int index) {
    gl().glDisableVertexAttribArray(
      index);
  }

  public static void glEnableVertexAttribArray(
    int index) {
    gl().glEnableVertexAttribArray(
      index);
  }

  public static void glEndQuery(
    int target) {
    gl().glEndQuery(
      target);
  }

  public static void glFramebufferTexture3D(
    int target,
    int attachment,
    int textarget,
    int texture,
    int level,
    int zoffset) {
    gl().glFramebufferTexture3D(
      target,
      attachment,
      textarget,
      texture,
      level,
      zoffset);
  }

  public static void glGenQueries(
    int n,
    IntBuffer ids) {
    gl().glGenQueries(
      n,
      ids);
  }

  public static void glGenQueries(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glGenQueries(
      n,
      ids,
      ids_offset);
  }

  public static void glGetActiveAttrib(
    int program,
    int index,
    int bufsize,
    IntBuffer length,
    IntBuffer size,
    IntBuffer type,
    ByteBuffer name) {
    gl().glGetActiveAttrib(
      program,
      index,
      bufsize,
      length,
      size,
      type,
      name);
  }

  public static void glGetActiveAttrib(
    int program,
    int index,
    int bufsize,
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
      bufsize,
      length,
      length_offset,
      size,
      size_offset,
      type,
      type_offset,
      name,
      name_offset);
  }

  public static void glGetActiveUniform(
    int program,
    int index,
    int bufsize,
    IntBuffer length,
    IntBuffer size,
    IntBuffer type,
    ByteBuffer name) {
    gl().glGetActiveUniform(
      program,
      index,
      bufsize,
      length,
      size,
      type,
      name);
  }

  public static void glGetActiveUniform(
    int program,
    int index,
    int bufsize,
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
      bufsize,
      length,
      length_offset,
      size,
      size_offset,
      type,
      type_offset,
      name,
      name_offset);
  }

  public static void glGetAttachedShaders(
    int program,
    int maxcount,
    IntBuffer count,
    IntBuffer shaders) {
    gl().glGetAttachedShaders(
      program,
      maxcount,
      count,
      shaders);
  }

  public static void glGetAttachedShaders(
    int program,
    int maxcount,
    int[] count,
    int count_offset,
    int[] shaders,
    int shaders_offset) {
    gl().glGetAttachedShaders(
      program,
      maxcount,
      count,
      count_offset,
      shaders,
      shaders_offset);
  }

  public static int glGetAttribLocation(
    int program,
    String name) {
    return gl().glGetAttribLocation(
      program,
      name);
  }

  public static void glGetProgramBinary(
    int program,
    int bufSize,
    IntBuffer length,
    IntBuffer binaryFormat,
    Buffer binary) {
    gl().glGetProgramBinary(
      program,
      bufSize,
      length,
      binaryFormat,
      binary);
  }

  public static void glGetProgramBinary(
    int program,
    int bufSize,
    int[] length,
    int length_offset,
    int[] binaryFormat,
    int binaryFormat_offset,
    Buffer binary) {
    gl().glGetProgramBinary(
      program,
      bufSize,
      length,
      length_offset,
      binaryFormat,
      binaryFormat_offset,
      binary);
  }

  public static void glGetProgramInfoLog(
    int program,
    int bufsize,
    IntBuffer length,
    ByteBuffer infolog) {
    gl().glGetProgramInfoLog(
      program,
      bufsize,
      length,
      infolog);
  }

  public static void glGetProgramInfoLog(
    int program,
    int bufsize,
    int[] length,
    int length_offset,
    byte[] infolog,
    int infolog_offset) {
    gl().glGetProgramInfoLog(
      program,
      bufsize,
      length,
      length_offset,
      infolog,
      infolog_offset);
  }

  public static void glGetProgramiv(
    int program,
    int pname,
    IntBuffer params) {
    gl().glGetProgramiv(
      program,
      pname,
      params);
  }

  public static void glGetProgramiv(
    int program,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetProgramiv(
      program,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectuiv(
    int id,
    int pname,
    IntBuffer params) {
    gl().glGetQueryObjectuiv(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjectuiv(
    int id,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetQueryObjectuiv(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryiv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetQueryiv(
      target,
      pname,
      params);
  }

  public static void glGetQueryiv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetQueryiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetShaderInfoLog(
    int shader,
    int bufsize,
    IntBuffer length,
    ByteBuffer infolog) {
    gl().glGetShaderInfoLog(
      shader,
      bufsize,
      length,
      infolog);
  }

  public static void glGetShaderInfoLog(
    int shader,
    int bufsize,
    int[] length,
    int length_offset,
    byte[] infolog,
    int infolog_offset) {
    gl().glGetShaderInfoLog(
      shader,
      bufsize,
      length,
      length_offset,
      infolog,
      infolog_offset);
  }

  public static void glGetShaderSource(
    int shader,
    int bufsize,
    IntBuffer length,
    ByteBuffer source) {
    gl().glGetShaderSource(
      shader,
      bufsize,
      length,
      source);
  }

  public static void glGetShaderSource(
    int shader,
    int bufsize,
    int[] length,
    int length_offset,
    byte[] source,
    int source_offset) {
    gl().glGetShaderSource(
      shader,
      bufsize,
      length,
      length_offset,
      source,
      source_offset);
  }

  public static void glGetShaderiv(
    int shader,
    int pname,
    IntBuffer params) {
    gl().glGetShaderiv(
      shader,
      pname,
      params);
  }

  public static void glGetShaderiv(
    int shader,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetShaderiv(
      shader,
      pname,
      params,
      params_offset);
  }

  public static int glGetUniformLocation(
    int program,
    String name) {
    return gl().glGetUniformLocation(
      program,
      name);
  }

  public static void glGetUniformfv(
    int program,
    int location,
    FloatBuffer params) {
    gl().glGetUniformfv(
      program,
      location,
      params);
  }

  public static void glGetUniformfv(
    int program,
    int location,
    float[] params,
    int params_offset) {
    gl().glGetUniformfv(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetUniformiv(
    int program,
    int location,
    IntBuffer params) {
    gl().glGetUniformiv(
      program,
      location,
      params);
  }

  public static void glGetUniformiv(
    int program,
    int location,
    int[] params,
    int params_offset) {
    gl().glGetUniformiv(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetVertexAttribfv(
    int index,
    int pname,
    FloatBuffer params) {
    gl().glGetVertexAttribfv(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribfv(
    int index,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetVertexAttribfv(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribiv(
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetVertexAttribiv(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribiv(
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVertexAttribiv(
      index,
      pname,
      params,
      params_offset);
  }

  public static boolean glIsProgram(
    int program) {
    return gl().glIsProgram(
      program);
  }

  public static boolean glIsQuery(
    int id) {
    return gl().glIsQuery(
      id);
  }

  public static boolean glIsShader(
    int shader) {
    return gl().glIsShader(
      shader);
  }

  public static void glLinkProgram(
    int program) {
    gl().glLinkProgram(
      program);
  }

  public static void glProgramBinary(
    int program,
    int binaryFormat,
    Buffer binary,
    int length) {
    gl().glProgramBinary(
      program,
      binaryFormat,
      binary,
      length);
  }

  public static void glShaderSource(
    int shader,
    int count,
    String[] string,
    IntBuffer length) {
    gl().glShaderSource(
      shader,
      count,
      string,
      length);
  }

  public static void glShaderSource(
    int shader,
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

  public static void glStencilFuncSeparate(
    int face,
    int func,
    int ref,
    int mask) {
    gl().glStencilFuncSeparate(
      face,
      func,
      ref,
      mask);
  }

  public static void glStencilMaskSeparate(
    int face,
    int mask) {
    gl().glStencilMaskSeparate(
      face,
      mask);
  }

  public static void glStencilOpSeparate(
    int face,
    int fail,
    int zfail,
    int zpass) {
    gl().glStencilOpSeparate(
      face,
      fail,
      zfail,
      zpass);
  }

  public static void glTexImage3D(
    int target,
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

  public static void glTexImage3D(
    int target,
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

  public static void glTexSubImage3D(
    int target,
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

  public static void glTexSubImage3D(
    int target,
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

  public static void glUniform1f(
    int location,
    float x) {
    gl().glUniform1f(
      location,
      x);
  }

  public static void glUniform1fv(
    int location,
    int count,
    FloatBuffer v) {
    gl().glUniform1fv(
      location,
      count,
      v);
  }

  public static void glUniform1fv(
    int location,
    int count,
    float[] v,
    int v_offset) {
    gl().glUniform1fv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniform1i(
    int location,
    int x) {
    gl().glUniform1i(
      location,
      x);
  }

  public static void glUniform1iv(
    int location,
    int count,
    IntBuffer v) {
    gl().glUniform1iv(
      location,
      count,
      v);
  }

  public static void glUniform1iv(
    int location,
    int count,
    int[] v,
    int v_offset) {
    gl().glUniform1iv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniform2f(
    int location,
    float x,
    float y) {
    gl().glUniform2f(
      location,
      x,
      y);
  }

  public static void glUniform2fv(
    int location,
    int count,
    FloatBuffer v) {
    gl().glUniform2fv(
      location,
      count,
      v);
  }

  public static void glUniform2fv(
    int location,
    int count,
    float[] v,
    int v_offset) {
    gl().glUniform2fv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniform2i(
    int location,
    int x,
    int y) {
    gl().glUniform2i(
      location,
      x,
      y);
  }

  public static void glUniform2iv(
    int location,
    int count,
    IntBuffer v) {
    gl().glUniform2iv(
      location,
      count,
      v);
  }

  public static void glUniform2iv(
    int location,
    int count,
    int[] v,
    int v_offset) {
    gl().glUniform2iv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniform3f(
    int location,
    float x,
    float y,
    float z) {
    gl().glUniform3f(
      location,
      x,
      y,
      z);
  }

  public static void glUniform3fv(
    int location,
    int count,
    FloatBuffer v) {
    gl().glUniform3fv(
      location,
      count,
      v);
  }

  public static void glUniform3fv(
    int location,
    int count,
    float[] v,
    int v_offset) {
    gl().glUniform3fv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniform3i(
    int location,
    int x,
    int y,
    int z) {
    gl().glUniform3i(
      location,
      x,
      y,
      z);
  }

  public static void glUniform3iv(
    int location,
    int count,
    IntBuffer v) {
    gl().glUniform3iv(
      location,
      count,
      v);
  }

  public static void glUniform3iv(
    int location,
    int count,
    int[] v,
    int v_offset) {
    gl().glUniform3iv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniform4f(
    int location,
    float x,
    float y,
    float z,
    float w) {
    gl().glUniform4f(
      location,
      x,
      y,
      z,
      w);
  }

  public static void glUniform4fv(
    int location,
    int count,
    FloatBuffer v) {
    gl().glUniform4fv(
      location,
      count,
      v);
  }

  public static void glUniform4fv(
    int location,
    int count,
    float[] v,
    int v_offset) {
    gl().glUniform4fv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniform4i(
    int location,
    int x,
    int y,
    int z,
    int w) {
    gl().glUniform4i(
      location,
      x,
      y,
      z,
      w);
  }

  public static void glUniform4iv(
    int location,
    int count,
    IntBuffer v) {
    gl().glUniform4iv(
      location,
      count,
      v);
  }

  public static void glUniform4iv(
    int location,
    int count,
    int[] v,
    int v_offset) {
    gl().glUniform4iv(
      location,
      count,
      v,
      v_offset);
  }

  public static void glUniformMatrix2fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix2fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix2fv(
    int location,
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

  public static void glUniformMatrix3fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix3fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix3fv(
    int location,
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

  public static void glUniformMatrix4fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix4fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix4fv(
    int location,
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

  public static void glUseProgram(
    int program) {
    gl().glUseProgram(
      program);
  }

  public static void glValidateProgram(
    int program) {
    gl().glValidateProgram(
      program);
  }

  public static void glVertexAttrib1f(
    int indx,
    float x) {
    gl().glVertexAttrib1f(
      indx,
      x);
  }

  public static void glVertexAttrib1fv(
    int indx,
    FloatBuffer values) {
    gl().glVertexAttrib1fv(
      indx,
      values);
  }

  public static void glVertexAttrib1fv(
    int indx,
    float[] values,
    int values_offset) {
    gl().glVertexAttrib1fv(
      indx,
      values,
      values_offset);
  }

  public static void glVertexAttrib2f(
    int indx,
    float x,
    float y) {
    gl().glVertexAttrib2f(
      indx,
      x,
      y);
  }

  public static void glVertexAttrib2fv(
    int indx,
    FloatBuffer values) {
    gl().glVertexAttrib2fv(
      indx,
      values);
  }

  public static void glVertexAttrib2fv(
    int indx,
    float[] values,
    int values_offset) {
    gl().glVertexAttrib2fv(
      indx,
      values,
      values_offset);
  }

  public static void glVertexAttrib3f(
    int indx,
    float x,
    float y,
    float z) {
    gl().glVertexAttrib3f(
      indx,
      x,
      y,
      z);
  }

  public static void glVertexAttrib3fv(
    int indx,
    FloatBuffer values) {
    gl().glVertexAttrib3fv(
      indx,
      values);
  }

  public static void glVertexAttrib3fv(
    int indx,
    float[] values,
    int values_offset) {
    gl().glVertexAttrib3fv(
      indx,
      values,
      values_offset);
  }

  public static void glVertexAttrib4f(
    int indx,
    float x,
    float y,
    float z,
    float w) {
    gl().glVertexAttrib4f(
      indx,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4fv(
    int indx,
    FloatBuffer values) {
    gl().glVertexAttrib4fv(
      indx,
      values);
  }

  public static void glVertexAttrib4fv(
    int indx,
    float[] values,
    int values_offset) {
    gl().glVertexAttrib4fv(
      indx,
      values,
      values_offset);
  }

  public static void glVertexAttribPointer(
    int indx,
    int size,
    int type,
    boolean normalized,
    int stride,
    Buffer ptr) {
    gl().glVertexAttribPointer(
      indx,
      size,
      type,
      normalized,
      stride,
      ptr);
  }

  public static void glVertexAttribPointer(
    int indx,
    int size,
    int type,
    boolean normalized,
    int stride,
    long ptr_buffer_offset) {
    gl().glVertexAttribPointer(
      indx,
      size,
      type,
      normalized,
      stride,
      ptr_buffer_offset);
  }

  public static void glReleaseShaderCompiler(
    ) {
    gl().glReleaseShaderCompiler();
  }

  public static void glShaderBinary(
    int n,
    IntBuffer shaders,
    int binaryformat,
    Buffer binary,
    int length) {
    gl().glShaderBinary(
      n,
      shaders,
      binaryformat,
      binary,
      length);
  }

  public static void glShaderBinary(
    int n,
    int[] shaders,
    int shaders_offset,
    int binaryformat,
    Buffer binary,
    int length) {
    gl().glShaderBinary(
      n,
      shaders,
      shaders_offset,
      binaryformat,
      binary,
      length);
  }

  public static void glGetShaderPrecisionFormat(
    int shadertype,
    int precisiontype,
    IntBuffer range,
    IntBuffer precision) {
    gl().glGetShaderPrecisionFormat(
      shadertype,
      precisiontype,
      range,
      precision);
  }

  public static void glGetShaderPrecisionFormat(
    int shadertype,
    int precisiontype,
    int[] range,
    int range_offset,
    int[] precision,
    int precision_offset) {
    gl().glGetShaderPrecisionFormat(
      shadertype,
      precisiontype,
      range,
      range_offset,
      precision,
      precision_offset);
  }

  public static void glVertexAttribPointer(
    GLArrayData array) {
    gl().glVertexAttribPointer(
      array);
  }

  public static void glUniform(
    GLUniformData data) {
    gl().glUniform(
      data);
  }


  // Generated from GL2GL3.html

  public static final int GL_DOUBLE
    = GL2.GL_DOUBLE;

  public static final int GL_POINT_SIZE_GRANULARITY
    = GL2.GL_POINT_SIZE_GRANULARITY;

  public static final int GL_POINT_SIZE_RANGE
    = GL2.GL_POINT_SIZE_RANGE;

  public static final int GL_LINE_WIDTH_GRANULARITY
    = GL2.GL_LINE_WIDTH_GRANULARITY;

  public static final int GL_LINE_WIDTH_RANGE
    = GL2.GL_LINE_WIDTH_RANGE;

  public static final int GL_POINT
    = GL2.GL_POINT;

  public static final int GL_LINE
    = GL2.GL_LINE;

  public static final int GL_FILL
    = GL2.GL_FILL;

  public static final int GL_POLYGON_SMOOTH
    = GL2.GL_POLYGON_SMOOTH;

  public static final int GL_POLYGON_OFFSET_POINT
    = GL2.GL_POLYGON_OFFSET_POINT;

  public static final int GL_POLYGON_OFFSET_LINE
    = GL2.GL_POLYGON_OFFSET_LINE;

  public static final int GL_LEFT
    = GL2.GL_LEFT;

  public static final int GL_RIGHT
    = GL2.GL_RIGHT;

  public static final int GL_FRONT_LEFT
    = GL2.GL_FRONT_LEFT;

  public static final int GL_FRONT_RIGHT
    = GL2.GL_FRONT_RIGHT;

  public static final int GL_BACK_LEFT
    = GL2.GL_BACK_LEFT;

  public static final int GL_BACK_RIGHT
    = GL2.GL_BACK_RIGHT;

  public static final int GL_GREEN
    = GL2.GL_GREEN;

  public static final int GL_BLUE
    = GL2.GL_BLUE;

  public static final int GL_READ_BUFFER
    = GL2.GL_READ_BUFFER;

  public static final int GL_DRAW_BUFFER
    = GL2.GL_DRAW_BUFFER;

  public static final int GL_DOUBLEBUFFER
    = GL2.GL_DOUBLEBUFFER;

  public static final int GL_STEREO
    = GL2.GL_STEREO;

  public static final int GL_COLOR
    = GL2.GL_COLOR;

  public static final int GL_DEPTH
    = GL2.GL_DEPTH;

  public static final int GL_STENCIL
    = GL2.GL_STENCIL;

  public static final int GL_POLYGON_SMOOTH_HINT
    = GL2.GL_POLYGON_SMOOTH_HINT;

  public static final int GL_PACK_LSB_FIRST
    = GL2.GL_PACK_LSB_FIRST;

  public static final int GL_PACK_ROW_LENGTH
    = GL2.GL_PACK_ROW_LENGTH;

  public static final int GL_PACK_SKIP_PIXELS
    = GL2.GL_PACK_SKIP_PIXELS;

  public static final int GL_PACK_SKIP_ROWS
    = GL2.GL_PACK_SKIP_ROWS;

  public static final int GL_PACK_SWAP_BYTES
    = GL2.GL_PACK_SWAP_BYTES;

  public static final int GL_UNPACK_LSB_FIRST
    = GL2.GL_UNPACK_LSB_FIRST;

  public static final int GL_UNPACK_SWAP_BYTES
    = GL2.GL_UNPACK_SWAP_BYTES;

  public static final int GL_TEXTURE_1D
    = GL2.GL_TEXTURE_1D;

  public static final int GL_TEXTURE_BORDER_COLOR
    = GL2.GL_TEXTURE_BORDER_COLOR;

  public static final int GL_TEXTURE_WIDTH
    = GL2.GL_TEXTURE_WIDTH;

  public static final int GL_TEXTURE_HEIGHT
    = GL2.GL_TEXTURE_HEIGHT;

  public static final int GL_TEXTURE_RED_SIZE
    = GL2.GL_TEXTURE_RED_SIZE;

  public static final int GL_TEXTURE_GREEN_SIZE
    = GL2.GL_TEXTURE_GREEN_SIZE;

  public static final int GL_TEXTURE_BLUE_SIZE
    = GL2.GL_TEXTURE_BLUE_SIZE;

  public static final int GL_TEXTURE_ALPHA_SIZE
    = GL2.GL_TEXTURE_ALPHA_SIZE;

  public static final int GL_PROXY_TEXTURE_1D
    = GL2.GL_PROXY_TEXTURE_1D;

  public static final int GL_PROXY_TEXTURE_2D
    = GL2.GL_PROXY_TEXTURE_2D;

  public static final int GL_TEXTURE_BINDING_1D
    = GL2.GL_TEXTURE_BINDING_1D;

  public static final int GL_TEXTURE_INTERNAL_FORMAT
    = GL2.GL_TEXTURE_INTERNAL_FORMAT;

  public static final int GL_R3_G3_B2
    = GL2.GL_R3_G3_B2;

  public static final int GL_RGB4
    = GL2.GL_RGB4;

  public static final int GL_RGB5
    = GL2.GL_RGB5;

  public static final int GL_RGB12
    = GL2.GL_RGB12;

  public static final int GL_RGB16
    = GL2.GL_RGB16;

  public static final int GL_RGBA2
    = GL2.GL_RGBA2;

  public static final int GL_RGBA12
    = GL2.GL_RGBA12;

  public static final int GL_RGBA16
    = GL2.GL_RGBA16;

  public static final int GL_UNSIGNED_BYTE_3_3_2
    = GL2.GL_UNSIGNED_BYTE_3_3_2;

  public static final int GL_UNSIGNED_INT_8_8_8_8
    = GL2.GL_UNSIGNED_INT_8_8_8_8;

  public static final int GL_PACK_SKIP_IMAGES
    = GL2.GL_PACK_SKIP_IMAGES;

  public static final int GL_PACK_IMAGE_HEIGHT
    = GL2.GL_PACK_IMAGE_HEIGHT;

  public static final int GL_UNPACK_SKIP_IMAGES
    = GL2.GL_UNPACK_SKIP_IMAGES;

  public static final int GL_UNPACK_IMAGE_HEIGHT
    = GL2.GL_UNPACK_IMAGE_HEIGHT;

  public static final int GL_PROXY_TEXTURE_3D
    = GL2.GL_PROXY_TEXTURE_3D;

  public static final int GL_TEXTURE_DEPTH
    = GL2.GL_TEXTURE_DEPTH;

  public static final int GL_UNSIGNED_BYTE_2_3_3_REV
    = GL2.GL_UNSIGNED_BYTE_2_3_3_REV;

  public static final int GL_UNSIGNED_SHORT_5_6_5_REV
    = GL2.GL_UNSIGNED_SHORT_5_6_5_REV;

  public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV
    = GL2.GL_UNSIGNED_SHORT_4_4_4_4_REV;

  public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV
    = GL2.GL_UNSIGNED_SHORT_1_5_5_5_REV;

  public static final int GL_UNSIGNED_INT_8_8_8_8_REV
    = GL2.GL_UNSIGNED_INT_8_8_8_8_REV;

  public static final int GL_BGR
    = GL2.GL_BGR;

  public static final int GL_MAX_ELEMENTS_VERTICES
    = GL2.GL_MAX_ELEMENTS_VERTICES;

  public static final int GL_MAX_ELEMENTS_INDICES
    = GL2.GL_MAX_ELEMENTS_INDICES;

  public static final int GL_TEXTURE_MIN_LOD
    = GL2.GL_TEXTURE_MIN_LOD;

  public static final int GL_TEXTURE_MAX_LOD
    = GL2.GL_TEXTURE_MAX_LOD;

  public static final int GL_TEXTURE_BASE_LEVEL
    = GL2.GL_TEXTURE_BASE_LEVEL;

  public static final int GL_TEXTURE_MAX_LEVEL
    = GL2.GL_TEXTURE_MAX_LEVEL;

  public static final int GL_SMOOTH_POINT_SIZE_GRANULARITY
    = GL2.GL_SMOOTH_POINT_SIZE_GRANULARITY;

  public static final int GL_SMOOTH_LINE_WIDTH_GRANULARITY
    = GL2.GL_SMOOTH_LINE_WIDTH_GRANULARITY;

  public static final int GL_MIN
    = GL2.GL_MIN;

  public static final int GL_MAX
    = GL2.GL_MAX;

  public static final int GL_PROXY_TEXTURE_CUBE_MAP
    = GL2.GL_PROXY_TEXTURE_CUBE_MAP;

  public static final int GL_COMPRESSED_RGB
    = GL2.GL_COMPRESSED_RGB;

  public static final int GL_COMPRESSED_RGBA
    = GL2.GL_COMPRESSED_RGBA;

  public static final int GL_TEXTURE_COMPRESSION_HINT
    = GL2.GL_TEXTURE_COMPRESSION_HINT;

  public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE
    = GL2.GL_TEXTURE_COMPRESSED_IMAGE_SIZE;

  public static final int GL_TEXTURE_COMPRESSED
    = GL2.GL_TEXTURE_COMPRESSED;

  public static final int GL_CLAMP_TO_BORDER
    = GL2.GL_CLAMP_TO_BORDER;

  public static final int GL_MAX_TEXTURE_LOD_BIAS
    = GL2.GL_MAX_TEXTURE_LOD_BIAS;

  public static final int GL_TEXTURE_LOD_BIAS
    = GL2.GL_TEXTURE_LOD_BIAS;

  public static final int GL_TEXTURE_DEPTH_SIZE
    = GL2.GL_TEXTURE_DEPTH_SIZE;

  public static final int GL_QUERY_COUNTER_BITS
    = GL2.GL_QUERY_COUNTER_BITS;

  public static final int GL_READ_ONLY
    = GL2.GL_READ_ONLY;

  public static final int GL_READ_WRITE
    = GL2.GL_READ_WRITE;

  public static final int GL_STREAM_READ
    = GL2.GL_STREAM_READ;

  public static final int GL_STREAM_COPY
    = GL2.GL_STREAM_COPY;

  public static final int GL_STATIC_READ
    = GL2.GL_STATIC_READ;

  public static final int GL_STATIC_COPY
    = GL2.GL_STATIC_COPY;

  public static final int GL_DYNAMIC_READ
    = GL2.GL_DYNAMIC_READ;

  public static final int GL_DYNAMIC_COPY
    = GL2.GL_DYNAMIC_COPY;

  public static final int GL_SAMPLES_PASSED
    = GL2.GL_SAMPLES_PASSED;

  public static final int GL_VERTEX_PROGRAM_POINT_SIZE
    = GL2.GL_VERTEX_PROGRAM_POINT_SIZE;

  public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS
    = GL2.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS;

  public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS
    = GL2.GL_MAX_VERTEX_UNIFORM_COMPONENTS;

  public static final int GL_MAX_VARYING_FLOATS
    = GL2.GL_MAX_VARYING_FLOATS;

  public static final int GL_SAMPLER_1D
    = GL2.GL_SAMPLER_1D;

  public static final int GL_SAMPLER_1D_SHADOW
    = GL2.GL_SAMPLER_1D_SHADOW;

  public static final int GL_POINT_SPRITE_COORD_ORIGIN
    = GL2.GL_POINT_SPRITE_COORD_ORIGIN;

  public static final int GL_LOWER_LEFT
    = GL2.GL_LOWER_LEFT;

  public static final int GL_UPPER_LEFT
    = GL2.GL_UPPER_LEFT;

  public static final int GL_PIXEL_PACK_BUFFER
    = GL2.GL_PIXEL_PACK_BUFFER;

  public static final int GL_PIXEL_UNPACK_BUFFER
    = GL2.GL_PIXEL_UNPACK_BUFFER;

  public static final int GL_PIXEL_PACK_BUFFER_BINDING
    = GL2.GL_PIXEL_PACK_BUFFER_BINDING;

  public static final int GL_PIXEL_UNPACK_BUFFER_BINDING
    = GL2.GL_PIXEL_UNPACK_BUFFER_BINDING;

  public static final int GL_FLOAT_MAT2x3
    = GL2.GL_FLOAT_MAT2x3;

  public static final int GL_FLOAT_MAT2x4
    = GL2.GL_FLOAT_MAT2x4;

  public static final int GL_FLOAT_MAT3x2
    = GL2.GL_FLOAT_MAT3x2;

  public static final int GL_FLOAT_MAT3x4
    = GL2.GL_FLOAT_MAT3x4;

  public static final int GL_FLOAT_MAT4x2
    = GL2.GL_FLOAT_MAT4x2;

  public static final int GL_FLOAT_MAT4x3
    = GL2.GL_FLOAT_MAT4x3;

  public static final int GL_SRGB8
    = GL2.GL_SRGB8;

  public static final int GL_COMPRESSED_SRGB
    = GL2.GL_COMPRESSED_SRGB;

  public static final int GL_COMPRESSED_SRGB_ALPHA
    = GL2.GL_COMPRESSED_SRGB_ALPHA;

  public static final int GL_CLIP_DISTANCE0
    = GL2.GL_CLIP_DISTANCE0;

  public static final int GL_CLIP_DISTANCE1
    = GL2.GL_CLIP_DISTANCE1;

  public static final int GL_CLIP_DISTANCE2
    = GL2.GL_CLIP_DISTANCE2;

  public static final int GL_CLIP_DISTANCE3
    = GL2.GL_CLIP_DISTANCE3;

  public static final int GL_CLIP_DISTANCE4
    = GL2.GL_CLIP_DISTANCE4;

  public static final int GL_CLIP_DISTANCE5
    = GL2.GL_CLIP_DISTANCE5;

  public static final int GL_CLIP_DISTANCE6
    = GL2.GL_CLIP_DISTANCE6;

  public static final int GL_CLIP_DISTANCE7
    = GL2.GL_CLIP_DISTANCE7;

  public static final int GL_MAX_CLIP_DISTANCES
    = GL2.GL_MAX_CLIP_DISTANCES;

  public static final int GL_MAJOR_VERSION
    = GL2.GL_MAJOR_VERSION;

  public static final int GL_MINOR_VERSION
    = GL2.GL_MINOR_VERSION;

  public static final int GL_NUM_EXTENSIONS
    = GL2.GL_NUM_EXTENSIONS;

  public static final int GL_CONTEXT_FLAGS
    = GL2.GL_CONTEXT_FLAGS;

  public static final int GL_DEPTH_BUFFER
    = GL2.GL_DEPTH_BUFFER;

  public static final int GL_STENCIL_BUFFER
    = GL2.GL_STENCIL_BUFFER;

  public static final int GL_COMPRESSED_RED
    = GL2.GL_COMPRESSED_RED;

  public static final int GL_COMPRESSED_RG
    = GL2.GL_COMPRESSED_RG;

  public static final int GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT
    = GL2.GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT;

  public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER
    = GL2.GL_VERTEX_ATTRIB_ARRAY_INTEGER;

  public static final int GL_MIN_PROGRAM_TEXEL_OFFSET
    = GL2.GL_MIN_PROGRAM_TEXEL_OFFSET;

  public static final int GL_MAX_PROGRAM_TEXEL_OFFSET
    = GL2.GL_MAX_PROGRAM_TEXEL_OFFSET;

  public static final int GL_CLAMP_READ_COLOR
    = GL2.GL_CLAMP_READ_COLOR;

  public static final int GL_FIXED_ONLY
    = GL2.GL_FIXED_ONLY;

  public static final int GL_MAX_VARYING_COMPONENTS
    = GL2.GL_MAX_VARYING_COMPONENTS;

  public static final int GL_TEXTURE_1D_ARRAY
    = GL2.GL_TEXTURE_1D_ARRAY;

  public static final int GL_PROXY_TEXTURE_1D_ARRAY
    = GL2.GL_PROXY_TEXTURE_1D_ARRAY;

  public static final int GL_PROXY_TEXTURE_2D_ARRAY
    = GL2.GL_PROXY_TEXTURE_2D_ARRAY;

  public static final int GL_TEXTURE_BINDING_1D_ARRAY
    = GL2.GL_TEXTURE_BINDING_1D_ARRAY;

  public static final int GL_RGB9_E5
    = GL2.GL_RGB9_E5;

  public static final int GL_UNSIGNED_INT_5_9_9_9_REV
    = GL2.GL_UNSIGNED_INT_5_9_9_9_REV;

  public static final int GL_TEXTURE_SHARED_SIZE
    = GL2.GL_TEXTURE_SHARED_SIZE;

  public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH
    = GL2.GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_MODE;

  public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS
    = GL2.GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS;

  public static final int GL_TRANSFORM_FEEDBACK_VARYINGS
    = GL2.GL_TRANSFORM_FEEDBACK_VARYINGS;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_START;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_SIZE;

  public static final int GL_PRIMITIVES_GENERATED
    = GL2.GL_PRIMITIVES_GENERATED;

  public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN
    = GL2.GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN;

  public static final int GL_RASTERIZER_DISCARD
    = GL2.GL_RASTERIZER_DISCARD;

  public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS
    = GL2.GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS;

  public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS
    = GL2.GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS;

  public static final int GL_INTERLEAVED_ATTRIBS
    = GL2.GL_INTERLEAVED_ATTRIBS;

  public static final int GL_SEPARATE_ATTRIBS
    = GL2.GL_SEPARATE_ATTRIBS;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_BINDING;

  public static final int GL_RGBA32UI
    = GL2.GL_RGBA32UI;

  public static final int GL_RGB32UI
    = GL2.GL_RGB32UI;

  public static final int GL_RGBA16UI
    = GL2.GL_RGBA16UI;

  public static final int GL_RGB16UI
    = GL2.GL_RGB16UI;

  public static final int GL_RGBA8UI
    = GL2.GL_RGBA8UI;

  public static final int GL_RGB8UI
    = GL2.GL_RGB8UI;

  public static final int GL_RGBA32I
    = GL2.GL_RGBA32I;

  public static final int GL_RGB32I
    = GL2.GL_RGB32I;

  public static final int GL_RGBA16I
    = GL2.GL_RGBA16I;

  public static final int GL_RGB16I
    = GL2.GL_RGB16I;

  public static final int GL_RGBA8I
    = GL2.GL_RGBA8I;

  public static final int GL_RGB8I
    = GL2.GL_RGB8I;

  public static final int GL_RED_INTEGER
    = GL2.GL_RED_INTEGER;

  public static final int GL_GREEN_INTEGER
    = GL2.GL_GREEN_INTEGER;

  public static final int GL_BLUE_INTEGER
    = GL2.GL_BLUE_INTEGER;

  public static final int GL_RGB_INTEGER
    = GL2.GL_RGB_INTEGER;

  public static final int GL_RGBA_INTEGER
    = GL2.GL_RGBA_INTEGER;

  public static final int GL_BGR_INTEGER
    = GL2.GL_BGR_INTEGER;

  public static final int GL_BGRA_INTEGER
    = GL2.GL_BGRA_INTEGER;

  public static final int GL_SAMPLER_1D_ARRAY
    = GL2.GL_SAMPLER_1D_ARRAY;

  public static final int GL_SAMPLER_1D_ARRAY_SHADOW
    = GL2.GL_SAMPLER_1D_ARRAY_SHADOW;

  public static final int GL_SAMPLER_2D_ARRAY_SHADOW
    = GL2.GL_SAMPLER_2D_ARRAY_SHADOW;

  public static final int GL_SAMPLER_CUBE_SHADOW
    = GL2.GL_SAMPLER_CUBE_SHADOW;

  public static final int GL_UNSIGNED_INT_VEC2
    = GL2.GL_UNSIGNED_INT_VEC2;

  public static final int GL_UNSIGNED_INT_VEC3
    = GL2.GL_UNSIGNED_INT_VEC3;

  public static final int GL_UNSIGNED_INT_VEC4
    = GL2.GL_UNSIGNED_INT_VEC4;

  public static final int GL_INT_SAMPLER_1D
    = GL2.GL_INT_SAMPLER_1D;

  public static final int GL_INT_SAMPLER_2D
    = GL2.GL_INT_SAMPLER_2D;

  public static final int GL_INT_SAMPLER_3D
    = GL2.GL_INT_SAMPLER_3D;

  public static final int GL_INT_SAMPLER_CUBE
    = GL2.GL_INT_SAMPLER_CUBE;

  public static final int GL_INT_SAMPLER_1D_ARRAY
    = GL2.GL_INT_SAMPLER_1D_ARRAY;

  public static final int GL_INT_SAMPLER_2D_ARRAY
    = GL2.GL_INT_SAMPLER_2D_ARRAY;

  public static final int GL_UNSIGNED_INT_SAMPLER_1D
    = GL2.GL_UNSIGNED_INT_SAMPLER_1D;

  public static final int GL_UNSIGNED_INT_SAMPLER_2D
    = GL2.GL_UNSIGNED_INT_SAMPLER_2D;

  public static final int GL_UNSIGNED_INT_SAMPLER_3D
    = GL2.GL_UNSIGNED_INT_SAMPLER_3D;

  public static final int GL_UNSIGNED_INT_SAMPLER_CUBE
    = GL2.GL_UNSIGNED_INT_SAMPLER_CUBE;

  public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY
    = GL2.GL_UNSIGNED_INT_SAMPLER_1D_ARRAY;

  public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY
    = GL2.GL_UNSIGNED_INT_SAMPLER_2D_ARRAY;

  public static final int GL_QUERY_WAIT
    = GL2.GL_QUERY_WAIT;

  public static final int GL_QUERY_NO_WAIT
    = GL2.GL_QUERY_NO_WAIT;

  public static final int GL_QUERY_BY_REGION_WAIT
    = GL2.GL_QUERY_BY_REGION_WAIT;

  public static final int GL_QUERY_BY_REGION_NO_WAIT
    = GL2.GL_QUERY_BY_REGION_NO_WAIT;

  public static final int GL_BUFFER_ACCESS_FLAGS
    = GL2.GL_BUFFER_ACCESS_FLAGS;

  public static final int GL_BUFFER_MAP_LENGTH
    = GL2.GL_BUFFER_MAP_LENGTH;

  public static final int GL_BUFFER_MAP_OFFSET
    = GL2.GL_BUFFER_MAP_OFFSET;

  public static final int GL_SAMPLER_BUFFER
    = GL2.GL_SAMPLER_BUFFER;

  public static final int GL_INT_SAMPLER_2D_RECT
    = GL2.GL_INT_SAMPLER_2D_RECT;

  public static final int GL_INT_SAMPLER_BUFFER
    = GL2.GL_INT_SAMPLER_BUFFER;

  public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT
    = GL2.GL_UNSIGNED_INT_SAMPLER_2D_RECT;

  public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER
    = GL2.GL_UNSIGNED_INT_SAMPLER_BUFFER;

  public static final int GL_PRIMITIVE_RESTART
    = GL2.GL_PRIMITIVE_RESTART;

  public static final int GL_PRIMITIVE_RESTART_INDEX
    = GL2.GL_PRIMITIVE_RESTART_INDEX;

  public static final int GL_SAMPLER_2D_RECT_ARB
    = GL2.GL_SAMPLER_2D_RECT_ARB;

  public static final int GL_SAMPLER_2D_RECT_SHADOW_ARB
    = GL2.GL_SAMPLER_2D_RECT_SHADOW_ARB;

  public static final int GL_TEXTURE_RECTANGLE_ARB
    = GL2.GL_TEXTURE_RECTANGLE_ARB;

  public static final int GL_TEXTURE_BINDING_RECTANGLE_ARB
    = GL2.GL_TEXTURE_BINDING_RECTANGLE_ARB;

  public static final int GL_PROXY_TEXTURE_RECTANGLE_ARB
    = GL2.GL_PROXY_TEXTURE_RECTANGLE_ARB;

  public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB
    = GL2.GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB;

  public static final int GL_TEXTURE_RED_TYPE
    = GL2.GL_TEXTURE_RED_TYPE;

  public static final int GL_TEXTURE_GREEN_TYPE
    = GL2.GL_TEXTURE_GREEN_TYPE;

  public static final int GL_TEXTURE_BLUE_TYPE
    = GL2.GL_TEXTURE_BLUE_TYPE;

  public static final int GL_TEXTURE_ALPHA_TYPE
    = GL2.GL_TEXTURE_ALPHA_TYPE;

  public static final int GL_TEXTURE_DEPTH_TYPE
    = GL2.GL_TEXTURE_DEPTH_TYPE;

  public static final int GL_DEPTH_COMPONENT32F
    = GL2.GL_DEPTH_COMPONENT32F;

  public static final int GL_DEPTH32F_STENCIL8
    = GL2.GL_DEPTH32F_STENCIL8;

  public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV
    = GL2.GL_FLOAT_32_UNSIGNED_INT_24_8_REV;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE;

  public static final int GL_FRAMEBUFFER_DEFAULT
    = GL2.GL_FRAMEBUFFER_DEFAULT;

  public static final int GL_FRAMEBUFFER_UNDEFINED
    = GL2.GL_FRAMEBUFFER_UNDEFINED;

  public static final int GL_DEPTH_STENCIL_ATTACHMENT
    = GL2.GL_DEPTH_STENCIL_ATTACHMENT;

  public static final int GL_TEXTURE_STENCIL_SIZE
    = GL2.GL_TEXTURE_STENCIL_SIZE;

  public static final int GL_DRAW_FRAMEBUFFER_BINDING
    = GL2.GL_DRAW_FRAMEBUFFER_BINDING;

  public static final int GL_READ_FRAMEBUFFER
    = GL2.GL_READ_FRAMEBUFFER;

  public static final int GL_DRAW_FRAMEBUFFER
    = GL2.GL_DRAW_FRAMEBUFFER;

  public static final int GL_READ_FRAMEBUFFER_BINDING
    = GL2.GL_READ_FRAMEBUFFER_BINDING;

  public static final int GL_RENDERBUFFER_SAMPLES
    = GL2.GL_RENDERBUFFER_SAMPLES;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;

  public static final int GL_STENCIL_INDEX16
    = GL2.GL_STENCIL_INDEX16;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE;

  public static final int GL_MAX_SAMPLES
    = GL2.GL_MAX_SAMPLES;

  public static final int GL_FRAMEBUFFER_SRGB
    = GL2.GL_FRAMEBUFFER_SRGB;

  public static final int GL_LINES_ADJACENCY_ARB
    = GL2.GL_LINES_ADJACENCY_ARB;

  public static final int GL_LINE_STRIP_ADJACENCY_ARB
    = GL2.GL_LINE_STRIP_ADJACENCY_ARB;

  public static final int GL_TRIANGLES_ADJACENCY_ARB
    = GL2.GL_TRIANGLES_ADJACENCY_ARB;

  public static final int GL_TRIANGLE_STRIP_ADJACENCY_ARB
    = GL2.GL_TRIANGLE_STRIP_ADJACENCY_ARB;

  public static final int GL_PROGRAM_POINT_SIZE_ARB
    = GL2.GL_PROGRAM_POINT_SIZE_ARB;

  public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_ARB
    = GL2.GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_ARB;

  public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_ARB
    = GL2.GL_FRAMEBUFFER_ATTACHMENT_LAYERED_ARB;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_ARB
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_ARB;

  public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_ARB
    = GL2.GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_ARB;

  public static final int GL_GEOMETRY_SHADER_ARB
    = GL2.GL_GEOMETRY_SHADER_ARB;

  public static final int GL_GEOMETRY_VERTICES_OUT_ARB
    = GL2.GL_GEOMETRY_VERTICES_OUT_ARB;

  public static final int GL_GEOMETRY_INPUT_TYPE_ARB
    = GL2.GL_GEOMETRY_INPUT_TYPE_ARB;

  public static final int GL_GEOMETRY_OUTPUT_TYPE_ARB
    = GL2.GL_GEOMETRY_OUTPUT_TYPE_ARB;

  public static final int GL_MAX_GEOMETRY_VARYING_COMPONENTS_ARB
    = GL2.GL_MAX_GEOMETRY_VARYING_COMPONENTS_ARB;

  public static final int GL_MAX_VERTEX_VARYING_COMPONENTS_ARB
    = GL2.GL_MAX_VERTEX_VARYING_COMPONENTS_ARB;

  public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_ARB
    = GL2.GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_ARB;

  public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB
    = GL2.GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB;

  public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_ARB
    = GL2.GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_ARB;

  public static final int GL_MAP_READ_BIT
    = GL2.GL_MAP_READ_BIT;

  public static final int GL_MAP_WRITE_BIT
    = GL2.GL_MAP_WRITE_BIT;

  public static final int GL_MAP_INVALIDATE_RANGE_BIT
    = GL2.GL_MAP_INVALIDATE_RANGE_BIT;

  public static final int GL_MAP_INVALIDATE_BUFFER_BIT
    = GL2.GL_MAP_INVALIDATE_BUFFER_BIT;

  public static final int GL_MAP_FLUSH_EXPLICIT_BIT
    = GL2.GL_MAP_FLUSH_EXPLICIT_BIT;

  public static final int GL_MAP_UNSYNCHRONIZED_BIT
    = GL2.GL_MAP_UNSYNCHRONIZED_BIT;

  public static final int GL_TEXTURE_BUFFER
    = GL2.GL_TEXTURE_BUFFER;

  public static final int GL_MAX_TEXTURE_BUFFER_SIZE
    = GL2.GL_MAX_TEXTURE_BUFFER_SIZE;

  public static final int GL_TEXTURE_BINDING_BUFFER
    = GL2.GL_TEXTURE_BINDING_BUFFER;

  public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING
    = GL2.GL_TEXTURE_BUFFER_DATA_STORE_BINDING;

  public static final int GL_TEXTURE_BUFFER_FORMAT
    = GL2.GL_TEXTURE_BUFFER_FORMAT;

  public static final int GL_COMPRESSED_RED_RGTC1
    = GL2.GL_COMPRESSED_RED_RGTC1;

  public static final int GL_COMPRESSED_SIGNED_RED_RGTC1
    = GL2.GL_COMPRESSED_SIGNED_RED_RGTC1;

  public static final int GL_COMPRESSED_RG_RGTC2
    = GL2.GL_COMPRESSED_RG_RGTC2;

  public static final int GL_COMPRESSED_SIGNED_RG_RGTC2
    = GL2.GL_COMPRESSED_SIGNED_RG_RGTC2;

  public static final int GL_RG_INTEGER
    = GL2.GL_RG_INTEGER;

  public static final int GL_R16
    = GL2.GL_R16;

  public static final int GL_RG16
    = GL2.GL_RG16;

  public static final int GL_R32F
    = GL2.GL_R32F;

  public static final int GL_RG32F
    = GL2.GL_RG32F;

  public static final int GL_R8I
    = GL2.GL_R8I;

  public static final int GL_R8UI
    = GL2.GL_R8UI;

  public static final int GL_R16I
    = GL2.GL_R16I;

  public static final int GL_R16UI
    = GL2.GL_R16UI;

  public static final int GL_R32I
    = GL2.GL_R32I;

  public static final int GL_R32UI
    = GL2.GL_R32UI;

  public static final int GL_RG8I
    = GL2.GL_RG8I;

  public static final int GL_RG8UI
    = GL2.GL_RG8UI;

  public static final int GL_RG16I
    = GL2.GL_RG16I;

  public static final int GL_RG16UI
    = GL2.GL_RG16UI;

  public static final int GL_RG32I
    = GL2.GL_RG32I;

  public static final int GL_RG32UI
    = GL2.GL_RG32UI;

  public static final int GL_VERTEX_ARRAY_BINDING
    = GL2.GL_VERTEX_ARRAY_BINDING;

  public static final int GL_UNIFORM_BUFFER
    = GL2.GL_UNIFORM_BUFFER;

  public static final int GL_UNIFORM_BUFFER_BINDING
    = GL2.GL_UNIFORM_BUFFER_BINDING;

  public static final int GL_UNIFORM_BUFFER_START
    = GL2.GL_UNIFORM_BUFFER_START;

  public static final int GL_UNIFORM_BUFFER_SIZE
    = GL2.GL_UNIFORM_BUFFER_SIZE;

  public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS
    = GL2.GL_MAX_VERTEX_UNIFORM_BLOCKS;

  public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS
    = GL2.GL_MAX_GEOMETRY_UNIFORM_BLOCKS;

  public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS
    = GL2.GL_MAX_FRAGMENT_UNIFORM_BLOCKS;

  public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS
    = GL2.GL_MAX_COMBINED_UNIFORM_BLOCKS;

  public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS
    = GL2.GL_MAX_UNIFORM_BUFFER_BINDINGS;

  public static final int GL_MAX_UNIFORM_BLOCK_SIZE
    = GL2.GL_MAX_UNIFORM_BLOCK_SIZE;

  public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS
    = GL2.GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS;

  public static final int GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS
    = GL2.GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS;

  public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS
    = GL2.GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS;

  public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT
    = GL2.GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT;

  public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH
    = GL2.GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH;

  public static final int GL_ACTIVE_UNIFORM_BLOCKS
    = GL2.GL_ACTIVE_UNIFORM_BLOCKS;

  public static final int GL_UNIFORM_TYPE
    = GL2.GL_UNIFORM_TYPE;

  public static final int GL_UNIFORM_SIZE
    = GL2.GL_UNIFORM_SIZE;

  public static final int GL_UNIFORM_NAME_LENGTH
    = GL2.GL_UNIFORM_NAME_LENGTH;

  public static final int GL_UNIFORM_BLOCK_INDEX
    = GL2.GL_UNIFORM_BLOCK_INDEX;

  public static final int GL_UNIFORM_OFFSET
    = GL2.GL_UNIFORM_OFFSET;

  public static final int GL_UNIFORM_ARRAY_STRIDE
    = GL2.GL_UNIFORM_ARRAY_STRIDE;

  public static final int GL_UNIFORM_MATRIX_STRIDE
    = GL2.GL_UNIFORM_MATRIX_STRIDE;

  public static final int GL_UNIFORM_IS_ROW_MAJOR
    = GL2.GL_UNIFORM_IS_ROW_MAJOR;

  public static final int GL_UNIFORM_BLOCK_BINDING
    = GL2.GL_UNIFORM_BLOCK_BINDING;

  public static final int GL_UNIFORM_BLOCK_DATA_SIZE
    = GL2.GL_UNIFORM_BLOCK_DATA_SIZE;

  public static final int GL_UNIFORM_BLOCK_NAME_LENGTH
    = GL2.GL_UNIFORM_BLOCK_NAME_LENGTH;

  public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS
    = GL2.GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS;

  public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES
    = GL2.GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES;

  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER
    = GL2.GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER;

  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER
    = GL2.GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER;

  public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER
    = GL2.GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER;

  public static final int GL_COPY_READ_BUFFER
    = GL2.GL_COPY_READ_BUFFER;

  public static final int GL_COPY_WRITE_BUFFER
    = GL2.GL_COPY_WRITE_BUFFER;

  public static final int GL_DEPTH_CLAMP
    = GL2.GL_DEPTH_CLAMP;

  public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION
    = GL2.GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION;

  public static final int GL_FIRST_VERTEX_CONVENTION
    = GL2.GL_FIRST_VERTEX_CONVENTION;

  public static final int GL_LAST_VERTEX_CONVENTION
    = GL2.GL_LAST_VERTEX_CONVENTION;

  public static final int GL_PROVOKING_VERTEX
    = GL2.GL_PROVOKING_VERTEX;

  public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS
    = GL2.GL_TEXTURE_CUBE_MAP_SEAMLESS;

  public static final int GL_SAMPLE_POSITION
    = GL2.GL_SAMPLE_POSITION;

  public static final int GL_SAMPLE_MASK
    = GL2.GL_SAMPLE_MASK;

  public static final int GL_SAMPLE_MASK_VALUE
    = GL2.GL_SAMPLE_MASK_VALUE;

  public static final int GL_MAX_SAMPLE_MASK_WORDS
    = GL2.GL_MAX_SAMPLE_MASK_WORDS;

  public static final int GL_TEXTURE_2D_MULTISAMPLE
    = GL2.GL_TEXTURE_2D_MULTISAMPLE;

  public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE
    = GL2.GL_PROXY_TEXTURE_2D_MULTISAMPLE;

  public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY
    = GL2.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;

  public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY
    = GL2.GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY;

  public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE
    = GL2.GL_TEXTURE_BINDING_2D_MULTISAMPLE;

  public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY
    = GL2.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY;

  public static final int GL_TEXTURE_SAMPLES
    = GL2.GL_TEXTURE_SAMPLES;

  public static final int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS
    = GL2.GL_TEXTURE_FIXED_SAMPLE_LOCATIONS;

  public static final int GL_SAMPLER_2D_MULTISAMPLE
    = GL2.GL_SAMPLER_2D_MULTISAMPLE;

  public static final int GL_INT_SAMPLER_2D_MULTISAMPLE
    = GL2.GL_INT_SAMPLER_2D_MULTISAMPLE;

  public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE
    = GL2.GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE;

  public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY
    = GL2.GL_SAMPLER_2D_MULTISAMPLE_ARRAY;

  public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY
    = GL2.GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY;

  public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY
    = GL2.GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY;

  public static final int GL_MAX_COLOR_TEXTURE_SAMPLES
    = GL2.GL_MAX_COLOR_TEXTURE_SAMPLES;

  public static final int GL_MAX_DEPTH_TEXTURE_SAMPLES
    = GL2.GL_MAX_DEPTH_TEXTURE_SAMPLES;

  public static final int GL_MAX_INTEGER_SAMPLES
    = GL2.GL_MAX_INTEGER_SAMPLES;

  public static final int GL_SAMPLE_SHADING
    = GL2.GL_SAMPLE_SHADING;

  public static final int GL_MIN_SAMPLE_SHADING_VALUE
    = GL2.GL_MIN_SAMPLE_SHADING_VALUE;

  public static final int GL_TEXTURE_CUBE_MAP_ARRAY
    = GL2.GL_TEXTURE_CUBE_MAP_ARRAY;

  public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY
    = GL2.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY;

  public static final int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY
    = GL2.GL_PROXY_TEXTURE_CUBE_MAP_ARRAY;

  public static final int GL_SAMPLER_CUBE_MAP_ARRAY
    = GL2.GL_SAMPLER_CUBE_MAP_ARRAY;

  public static final int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW
    = GL2.GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW;

  public static final int GL_INT_SAMPLER_CUBE_MAP_ARRAY
    = GL2.GL_INT_SAMPLER_CUBE_MAP_ARRAY;

  public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY
    = GL2.GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY;

  public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET
    = GL2.GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET;

  public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET
    = GL2.GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET;

  public static final int GL_SHADER_INCLUDE_ARB
    = GL2.GL_SHADER_INCLUDE_ARB;

  public static final int GL_NAMED_STRING_LENGTH_ARB
    = GL2.GL_NAMED_STRING_LENGTH_ARB;

  public static final int GL_NAMED_STRING_TYPE_ARB
    = GL2.GL_NAMED_STRING_TYPE_ARB;

  public static final int GL_SRC1_COLOR
    = GL2.GL_SRC1_COLOR;

  public static final int GL_ONE_MINUS_SRC1_COLOR
    = GL2.GL_ONE_MINUS_SRC1_COLOR;

  public static final int GL_ONE_MINUS_SRC1_ALPHA
    = GL2.GL_ONE_MINUS_SRC1_ALPHA;

  public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS
    = GL2.GL_MAX_DUAL_SOURCE_DRAW_BUFFERS;

  public static final int GL_SAMPLER_BINDING
    = GL2.GL_SAMPLER_BINDING;

  public static final int GL_RGB10_A2UI
    = GL2.GL_RGB10_A2UI;

  public static final int GL_TEXTURE_SWIZZLE_R
    = GL2.GL_TEXTURE_SWIZZLE_R;

  public static final int GL_TEXTURE_SWIZZLE_G
    = GL2.GL_TEXTURE_SWIZZLE_G;

  public static final int GL_TEXTURE_SWIZZLE_B
    = GL2.GL_TEXTURE_SWIZZLE_B;

  public static final int GL_TEXTURE_SWIZZLE_A
    = GL2.GL_TEXTURE_SWIZZLE_A;

  public static final int GL_TEXTURE_SWIZZLE_RGBA
    = GL2.GL_TEXTURE_SWIZZLE_RGBA;

  public static final int GL_TIME_ELAPSED
    = GL2.GL_TIME_ELAPSED;

  public static final int GL_TIMESTAMP
    = GL2.GL_TIMESTAMP;

  public static final int GL_INT_2_10_10_10_REV
    = GL2.GL_INT_2_10_10_10_REV;

  public static final int GL_ACTIVE_SUBROUTINES
    = GL2.GL_ACTIVE_SUBROUTINES;

  public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS
    = GL2.GL_ACTIVE_SUBROUTINE_UNIFORMS;

  public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS
    = GL2.GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS;

  public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH
    = GL2.GL_ACTIVE_SUBROUTINE_MAX_LENGTH;

  public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH
    = GL2.GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH;

  public static final int GL_MAX_SUBROUTINES
    = GL2.GL_MAX_SUBROUTINES;

  public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS
    = GL2.GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS;

  public static final int GL_NUM_COMPATIBLE_SUBROUTINES
    = GL2.GL_NUM_COMPATIBLE_SUBROUTINES;

  public static final int GL_COMPATIBLE_SUBROUTINES
    = GL2.GL_COMPATIBLE_SUBROUTINES;

  public static final int GL_TRANSFORM_FEEDBACK
    = GL2.GL_TRANSFORM_FEEDBACK;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED;

  public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE
    = GL2.GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE;

  public static final int GL_TRANSFORM_FEEDBACK_BINDING
    = GL2.GL_TRANSFORM_FEEDBACK_BINDING;

  public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS
    = GL2.GL_MAX_TRANSFORM_FEEDBACK_BUFFERS;

  public static final int GL_MAX_VERTEX_STREAMS
    = GL2.GL_MAX_VERTEX_STREAMS;

  public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT
    = GL2.GL_PROGRAM_BINARY_RETRIEVABLE_HINT;

  public static final int GL_GEOMETRY_SHADER_BIT
    = GL2.GL_GEOMETRY_SHADER_BIT;

  public static final int GL_TESS_CONTROL_SHADER_BIT
    = GL2.GL_TESS_CONTROL_SHADER_BIT;

  public static final int GL_TESS_EVALUATION_SHADER_BIT
    = GL2.GL_TESS_EVALUATION_SHADER_BIT;

  public static final int GL_MAX_VIEWPORTS
    = GL2.GL_MAX_VIEWPORTS;

  public static final int GL_VIEWPORT_SUBPIXEL_BITS
    = GL2.GL_VIEWPORT_SUBPIXEL_BITS;

  public static final int GL_VIEWPORT_BOUNDS_RANGE
    = GL2.GL_VIEWPORT_BOUNDS_RANGE;

  public static final int GL_LAYER_PROVOKING_VERTEX
    = GL2.GL_LAYER_PROVOKING_VERTEX;

  public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX
    = GL2.GL_VIEWPORT_INDEX_PROVOKING_VERTEX;

  public static final int GL_UNDEFINED_VERTEX
    = GL2.GL_UNDEFINED_VERTEX;

  public static final int GL_SYNC_CL_EVENT_ARB
    = GL2.GL_SYNC_CL_EVENT_ARB;

  public static final int GL_SYNC_CL_EVENT_COMPLETE_ARB
    = GL2.GL_SYNC_CL_EVENT_COMPLETE_ARB;

  public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB
    = GL2.GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB;

  public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB
    = GL2.GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB;

  public static final int GL_DEBUG_CALLBACK_FUNCTION_ARB
    = GL2.GL_DEBUG_CALLBACK_FUNCTION_ARB;

  public static final int GL_DEBUG_CALLBACK_USER_PARAM_ARB
    = GL2.GL_DEBUG_CALLBACK_USER_PARAM_ARB;

  public static final int GL_DEBUG_SOURCE_API_ARB
    = GL2.GL_DEBUG_SOURCE_API_ARB;

  public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB
    = GL2.GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB;

  public static final int GL_DEBUG_SOURCE_SHADER_COMPILER_ARB
    = GL2.GL_DEBUG_SOURCE_SHADER_COMPILER_ARB;

  public static final int GL_DEBUG_SOURCE_THIRD_PARTY_ARB
    = GL2.GL_DEBUG_SOURCE_THIRD_PARTY_ARB;

  public static final int GL_DEBUG_SOURCE_APPLICATION_ARB
    = GL2.GL_DEBUG_SOURCE_APPLICATION_ARB;

  public static final int GL_DEBUG_SOURCE_OTHER_ARB
    = GL2.GL_DEBUG_SOURCE_OTHER_ARB;

  public static final int GL_DEBUG_TYPE_ERROR_ARB
    = GL2.GL_DEBUG_TYPE_ERROR_ARB;

  public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB
    = GL2.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB;

  public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB
    = GL2.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB;

  public static final int GL_DEBUG_TYPE_PORTABILITY_ARB
    = GL2.GL_DEBUG_TYPE_PORTABILITY_ARB;

  public static final int GL_DEBUG_TYPE_PERFORMANCE_ARB
    = GL2.GL_DEBUG_TYPE_PERFORMANCE_ARB;

  public static final int GL_DEBUG_TYPE_OTHER_ARB
    = GL2.GL_DEBUG_TYPE_OTHER_ARB;

  public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_ARB
    = GL2.GL_MAX_DEBUG_MESSAGE_LENGTH_ARB;

  public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_ARB
    = GL2.GL_MAX_DEBUG_LOGGED_MESSAGES_ARB;

  public static final int GL_DEBUG_LOGGED_MESSAGES_ARB
    = GL2.GL_DEBUG_LOGGED_MESSAGES_ARB;

  public static final int GL_DEBUG_SEVERITY_HIGH_ARB
    = GL2.GL_DEBUG_SEVERITY_HIGH_ARB;

  public static final int GL_DEBUG_SEVERITY_MEDIUM_ARB
    = GL2.GL_DEBUG_SEVERITY_MEDIUM_ARB;

  public static final int GL_DEBUG_SEVERITY_LOW_ARB
    = GL2.GL_DEBUG_SEVERITY_LOW_ARB;

  public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT
    = GL2.GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT;

  public static final int GL_UNPACK_COMPRESSED_BLOCK_WIDTH
    = GL2.GL_UNPACK_COMPRESSED_BLOCK_WIDTH;

  public static final int GL_UNPACK_COMPRESSED_BLOCK_HEIGHT
    = GL2.GL_UNPACK_COMPRESSED_BLOCK_HEIGHT;

  public static final int GL_UNPACK_COMPRESSED_BLOCK_DEPTH
    = GL2.GL_UNPACK_COMPRESSED_BLOCK_DEPTH;

  public static final int GL_UNPACK_COMPRESSED_BLOCK_SIZE
    = GL2.GL_UNPACK_COMPRESSED_BLOCK_SIZE;

  public static final int GL_PACK_COMPRESSED_BLOCK_WIDTH
    = GL2.GL_PACK_COMPRESSED_BLOCK_WIDTH;

  public static final int GL_PACK_COMPRESSED_BLOCK_HEIGHT
    = GL2.GL_PACK_COMPRESSED_BLOCK_HEIGHT;

  public static final int GL_PACK_COMPRESSED_BLOCK_DEPTH
    = GL2.GL_PACK_COMPRESSED_BLOCK_DEPTH;

  public static final int GL_PACK_COMPRESSED_BLOCK_SIZE
    = GL2.GL_PACK_COMPRESSED_BLOCK_SIZE;

  public static final int GL_NUM_SAMPLE_COUNTS
    = GL2.GL_NUM_SAMPLE_COUNTS;

  public static final int GL_MIN_MAP_BUFFER_ALIGNMENT
    = GL2.GL_MIN_MAP_BUFFER_ALIGNMENT;

  public static final int GL_ATOMIC_COUNTER_BUFFER
    = GL2.GL_ATOMIC_COUNTER_BUFFER;

  public static final int GL_ATOMIC_COUNTER_BUFFER_BINDING
    = GL2.GL_ATOMIC_COUNTER_BUFFER_BINDING;

  public static final int GL_ATOMIC_COUNTER_BUFFER_START
    = GL2.GL_ATOMIC_COUNTER_BUFFER_START;

  public static final int GL_ATOMIC_COUNTER_BUFFER_SIZE
    = GL2.GL_ATOMIC_COUNTER_BUFFER_SIZE;

  public static final int GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE
    = GL2.GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE;

  public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS
    = GL2.GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS;

  public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES
    = GL2.GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES;

  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER
    = GL2.GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER;

  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER
    = GL2.GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER;

  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER
    = GL2.GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER;

  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER
    = GL2.GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER;

  public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER
    = GL2.GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER;

  public static final int GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS
    = GL2.GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS;

  public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS
    = GL2.GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS;

  public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS
    = GL2.GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS;

  public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS
    = GL2.GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS;

  public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS
    = GL2.GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS;

  public static final int GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS
    = GL2.GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS;

  public static final int GL_MAX_VERTEX_ATOMIC_COUNTERS
    = GL2.GL_MAX_VERTEX_ATOMIC_COUNTERS;

  public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS
    = GL2.GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS;

  public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS
    = GL2.GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS;

  public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTERS
    = GL2.GL_MAX_GEOMETRY_ATOMIC_COUNTERS;

  public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTERS
    = GL2.GL_MAX_FRAGMENT_ATOMIC_COUNTERS;

  public static final int GL_MAX_COMBINED_ATOMIC_COUNTERS
    = GL2.GL_MAX_COMBINED_ATOMIC_COUNTERS;

  public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE
    = GL2.GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE;

  public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS
    = GL2.GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS;

  public static final int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS
    = GL2.GL_ACTIVE_ATOMIC_COUNTER_BUFFERS;

  public static final int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX
    = GL2.GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX;

  public static final int GL_UNSIGNED_INT_ATOMIC_COUNTER
    = GL2.GL_UNSIGNED_INT_ATOMIC_COUNTER;

  public static final int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT
    = GL2.GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT;

  public static final int GL_ELEMENT_ARRAY_BARRIER_BIT
    = GL2.GL_ELEMENT_ARRAY_BARRIER_BIT;

  public static final int GL_UNIFORM_BARRIER_BIT
    = GL2.GL_UNIFORM_BARRIER_BIT;

  public static final int GL_TEXTURE_FETCH_BARRIER_BIT
    = GL2.GL_TEXTURE_FETCH_BARRIER_BIT;

  public static final int GL_SHADER_IMAGE_ACCESS_BARRIER_BIT
    = GL2.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT;

  public static final int GL_COMMAND_BARRIER_BIT
    = GL2.GL_COMMAND_BARRIER_BIT;

  public static final int GL_PIXEL_BUFFER_BARRIER_BIT
    = GL2.GL_PIXEL_BUFFER_BARRIER_BIT;

  public static final int GL_TEXTURE_UPDATE_BARRIER_BIT
    = GL2.GL_TEXTURE_UPDATE_BARRIER_BIT;

  public static final int GL_BUFFER_UPDATE_BARRIER_BIT
    = GL2.GL_BUFFER_UPDATE_BARRIER_BIT;

  public static final int GL_FRAMEBUFFER_BARRIER_BIT
    = GL2.GL_FRAMEBUFFER_BARRIER_BIT;

  public static final int GL_TRANSFORM_FEEDBACK_BARRIER_BIT
    = GL2.GL_TRANSFORM_FEEDBACK_BARRIER_BIT;

  public static final int GL_ATOMIC_COUNTER_BARRIER_BIT
    = GL2.GL_ATOMIC_COUNTER_BARRIER_BIT;

  public static final int GL_MAX_IMAGE_UNITS
    = GL2.GL_MAX_IMAGE_UNITS;

  public static final int GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS
    = GL2.GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS;

  public static final int GL_IMAGE_BINDING_NAME
    = GL2.GL_IMAGE_BINDING_NAME;

  public static final int GL_IMAGE_BINDING_LEVEL
    = GL2.GL_IMAGE_BINDING_LEVEL;

  public static final int GL_IMAGE_BINDING_LAYERED
    = GL2.GL_IMAGE_BINDING_LAYERED;

  public static final int GL_IMAGE_BINDING_LAYER
    = GL2.GL_IMAGE_BINDING_LAYER;

  public static final int GL_IMAGE_BINDING_ACCESS
    = GL2.GL_IMAGE_BINDING_ACCESS;

  public static final int GL_IMAGE_1D
    = GL2.GL_IMAGE_1D;

  public static final int GL_IMAGE_2D
    = GL2.GL_IMAGE_2D;

  public static final int GL_IMAGE_3D
    = GL2.GL_IMAGE_3D;

  public static final int GL_IMAGE_2D_RECT
    = GL2.GL_IMAGE_2D_RECT;

  public static final int GL_IMAGE_CUBE
    = GL2.GL_IMAGE_CUBE;

  public static final int GL_IMAGE_BUFFER
    = GL2.GL_IMAGE_BUFFER;

  public static final int GL_IMAGE_1D_ARRAY
    = GL2.GL_IMAGE_1D_ARRAY;

  public static final int GL_IMAGE_2D_ARRAY
    = GL2.GL_IMAGE_2D_ARRAY;

  public static final int GL_IMAGE_CUBE_MAP_ARRAY
    = GL2.GL_IMAGE_CUBE_MAP_ARRAY;

  public static final int GL_IMAGE_2D_MULTISAMPLE
    = GL2.GL_IMAGE_2D_MULTISAMPLE;

  public static final int GL_IMAGE_2D_MULTISAMPLE_ARRAY
    = GL2.GL_IMAGE_2D_MULTISAMPLE_ARRAY;

  public static final int GL_INT_IMAGE_1D
    = GL2.GL_INT_IMAGE_1D;

  public static final int GL_INT_IMAGE_2D
    = GL2.GL_INT_IMAGE_2D;

  public static final int GL_INT_IMAGE_3D
    = GL2.GL_INT_IMAGE_3D;

  public static final int GL_INT_IMAGE_2D_RECT
    = GL2.GL_INT_IMAGE_2D_RECT;

  public static final int GL_INT_IMAGE_CUBE
    = GL2.GL_INT_IMAGE_CUBE;

  public static final int GL_INT_IMAGE_BUFFER
    = GL2.GL_INT_IMAGE_BUFFER;

  public static final int GL_INT_IMAGE_1D_ARRAY
    = GL2.GL_INT_IMAGE_1D_ARRAY;

  public static final int GL_INT_IMAGE_2D_ARRAY
    = GL2.GL_INT_IMAGE_2D_ARRAY;

  public static final int GL_INT_IMAGE_CUBE_MAP_ARRAY
    = GL2.GL_INT_IMAGE_CUBE_MAP_ARRAY;

  public static final int GL_INT_IMAGE_2D_MULTISAMPLE
    = GL2.GL_INT_IMAGE_2D_MULTISAMPLE;

  public static final int GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY
    = GL2.GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY;

  public static final int GL_UNSIGNED_INT_IMAGE_1D
    = GL2.GL_UNSIGNED_INT_IMAGE_1D;

  public static final int GL_UNSIGNED_INT_IMAGE_2D
    = GL2.GL_UNSIGNED_INT_IMAGE_2D;

  public static final int GL_UNSIGNED_INT_IMAGE_3D
    = GL2.GL_UNSIGNED_INT_IMAGE_3D;

  public static final int GL_UNSIGNED_INT_IMAGE_2D_RECT
    = GL2.GL_UNSIGNED_INT_IMAGE_2D_RECT;

  public static final int GL_UNSIGNED_INT_IMAGE_CUBE
    = GL2.GL_UNSIGNED_INT_IMAGE_CUBE;

  public static final int GL_UNSIGNED_INT_IMAGE_BUFFER
    = GL2.GL_UNSIGNED_INT_IMAGE_BUFFER;

  public static final int GL_UNSIGNED_INT_IMAGE_1D_ARRAY
    = GL2.GL_UNSIGNED_INT_IMAGE_1D_ARRAY;

  public static final int GL_UNSIGNED_INT_IMAGE_2D_ARRAY
    = GL2.GL_UNSIGNED_INT_IMAGE_2D_ARRAY;

  public static final int GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY
    = GL2.GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY;

  public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE
    = GL2.GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE;

  public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY
    = GL2.GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY;

  public static final int GL_MAX_IMAGE_SAMPLES
    = GL2.GL_MAX_IMAGE_SAMPLES;

  public static final int GL_IMAGE_BINDING_FORMAT
    = GL2.GL_IMAGE_BINDING_FORMAT;

  public static final int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE
    = GL2.GL_IMAGE_FORMAT_COMPATIBILITY_TYPE;

  public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE
    = GL2.GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE;

  public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_CLASS
    = GL2.GL_IMAGE_FORMAT_COMPATIBILITY_BY_CLASS;

  public static final int GL_MAX_VERTEX_IMAGE_UNIFORMS
    = GL2.GL_MAX_VERTEX_IMAGE_UNIFORMS;

  public static final int GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS
    = GL2.GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS;

  public static final int GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS
    = GL2.GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS;

  public static final int GL_MAX_GEOMETRY_IMAGE_UNIFORMS
    = GL2.GL_MAX_GEOMETRY_IMAGE_UNIFORMS;

  public static final int GL_MAX_FRAGMENT_IMAGE_UNIFORMS
    = GL2.GL_MAX_FRAGMENT_IMAGE_UNIFORMS;

  public static final int GL_MAX_COMBINED_IMAGE_UNIFORMS
    = GL2.GL_MAX_COMBINED_IMAGE_UNIFORMS;

  public static final int GL_SAMPLER_BUFFER_AMD
    = GL2.GL_SAMPLER_BUFFER_AMD;

  public static final int GL_INT_SAMPLER_BUFFER_AMD
    = GL2.GL_INT_SAMPLER_BUFFER_AMD;

  public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER_AMD
    = GL2.GL_UNSIGNED_INT_SAMPLER_BUFFER_AMD;

  public static final int GL_TESSELLATION_MODE_AMD
    = GL2.GL_TESSELLATION_MODE_AMD;

  public static final int GL_TESSELLATION_FACTOR_AMD
    = GL2.GL_TESSELLATION_FACTOR_AMD;

  public static final int GL_DISCRETE_AMD
    = GL2.GL_DISCRETE_AMD;

  public static final int GL_CONTINUOUS_AMD
    = GL2.GL_CONTINUOUS_AMD;

  public static final int GL_RED_SNORM
    = GL2.GL_RED_SNORM;

  public static final int GL_RG_SNORM
    = GL2.GL_RG_SNORM;

  public static final int GL_RGB_SNORM
    = GL2.GL_RGB_SNORM;

  public static final int GL_RGBA_SNORM
    = GL2.GL_RGBA_SNORM;

  public static final int GL_R8_SNORM
    = GL2.GL_R8_SNORM;

  public static final int GL_RG8_SNORM
    = GL2.GL_RG8_SNORM;

  public static final int GL_RGB8_SNORM
    = GL2.GL_RGB8_SNORM;

  public static final int GL_RGBA8_SNORM
    = GL2.GL_RGBA8_SNORM;

  public static final int GL_R16_SNORM
    = GL2.GL_R16_SNORM;

  public static final int GL_RG16_SNORM
    = GL2.GL_RG16_SNORM;

  public static final int GL_RGB16_SNORM
    = GL2.GL_RGB16_SNORM;

  public static final int GL_RGBA16_SNORM
    = GL2.GL_RGBA16_SNORM;

  public static final int GL_SIGNED_NORMALIZED
    = GL2.GL_SIGNED_NORMALIZED;

  public static final int GL_BUFFER_GPU_ADDRESS_NV
    = GL2.GL_BUFFER_GPU_ADDRESS_NV;

  public static final int GL_GPU_ADDRESS_NV
    = GL2.GL_GPU_ADDRESS_NV;

  public static final int GL_MAX_SHADER_BUFFER_ADDRESS_NV
    = GL2.GL_MAX_SHADER_BUFFER_ADDRESS_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV
    = GL2.GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV;

  public static final int GL_ELEMENT_ARRAY_UNIFIED_NV
    = GL2.GL_ELEMENT_ARRAY_UNIFIED_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY_ADDRESS_NV
    = GL2.GL_VERTEX_ATTRIB_ARRAY_ADDRESS_NV;

  public static final int GL_VERTEX_ARRAY_ADDRESS_NV
    = GL2.GL_VERTEX_ARRAY_ADDRESS_NV;

  public static final int GL_NORMAL_ARRAY_ADDRESS_NV
    = GL2.GL_NORMAL_ARRAY_ADDRESS_NV;

  public static final int GL_COLOR_ARRAY_ADDRESS_NV
    = GL2.GL_COLOR_ARRAY_ADDRESS_NV;

  public static final int GL_INDEX_ARRAY_ADDRESS_NV
    = GL2.GL_INDEX_ARRAY_ADDRESS_NV;

  public static final int GL_TEXTURE_COORD_ARRAY_ADDRESS_NV
    = GL2.GL_TEXTURE_COORD_ARRAY_ADDRESS_NV;

  public static final int GL_EDGE_FLAG_ARRAY_ADDRESS_NV
    = GL2.GL_EDGE_FLAG_ARRAY_ADDRESS_NV;

  public static final int GL_SECONDARY_COLOR_ARRAY_ADDRESS_NV
    = GL2.GL_SECONDARY_COLOR_ARRAY_ADDRESS_NV;

  public static final int GL_FOG_COORD_ARRAY_ADDRESS_NV
    = GL2.GL_FOG_COORD_ARRAY_ADDRESS_NV;

  public static final int GL_ELEMENT_ARRAY_ADDRESS_NV
    = GL2.GL_ELEMENT_ARRAY_ADDRESS_NV;

  public static final int GL_VERTEX_ATTRIB_ARRAY_LENGTH_NV
    = GL2.GL_VERTEX_ATTRIB_ARRAY_LENGTH_NV;

  public static final int GL_VERTEX_ARRAY_LENGTH_NV
    = GL2.GL_VERTEX_ARRAY_LENGTH_NV;

  public static final int GL_NORMAL_ARRAY_LENGTH_NV
    = GL2.GL_NORMAL_ARRAY_LENGTH_NV;

  public static final int GL_COLOR_ARRAY_LENGTH_NV
    = GL2.GL_COLOR_ARRAY_LENGTH_NV;

  public static final int GL_INDEX_ARRAY_LENGTH_NV
    = GL2.GL_INDEX_ARRAY_LENGTH_NV;

  public static final int GL_TEXTURE_COORD_ARRAY_LENGTH_NV
    = GL2.GL_TEXTURE_COORD_ARRAY_LENGTH_NV;

  public static final int GL_EDGE_FLAG_ARRAY_LENGTH_NV
    = GL2.GL_EDGE_FLAG_ARRAY_LENGTH_NV;

  public static final int GL_SECONDARY_COLOR_ARRAY_LENGTH_NV
    = GL2.GL_SECONDARY_COLOR_ARRAY_LENGTH_NV;

  public static final int GL_FOG_COORD_ARRAY_LENGTH_NV
    = GL2.GL_FOG_COORD_ARRAY_LENGTH_NV;

  public static final int GL_ELEMENT_ARRAY_LENGTH_NV
    = GL2.GL_ELEMENT_ARRAY_LENGTH_NV;

  public static final int GL_DRAW_INDIRECT_UNIFIED_NV
    = GL2.GL_DRAW_INDIRECT_UNIFIED_NV;

  public static final int GL_DRAW_INDIRECT_ADDRESS_NV
    = GL2.GL_DRAW_INDIRECT_ADDRESS_NV;

  public static final int GL_DRAW_INDIRECT_LENGTH_NV
    = GL2.GL_DRAW_INDIRECT_LENGTH_NV;

  public static final int GL_MAX_DEBUG_LOGGED_MESSAGES_AMD
    = GL2.GL_MAX_DEBUG_LOGGED_MESSAGES_AMD;

  public static final int GL_DEBUG_LOGGED_MESSAGES_AMD
    = GL2.GL_DEBUG_LOGGED_MESSAGES_AMD;

  public static final int GL_DEBUG_SEVERITY_HIGH_AMD
    = GL2.GL_DEBUG_SEVERITY_HIGH_AMD;

  public static final int GL_DEBUG_SEVERITY_MEDIUM_AMD
    = GL2.GL_DEBUG_SEVERITY_MEDIUM_AMD;

  public static final int GL_DEBUG_SEVERITY_LOW_AMD
    = GL2.GL_DEBUG_SEVERITY_LOW_AMD;

  public static final int GL_DEBUG_CATEGORY_API_ERROR_AMD
    = GL2.GL_DEBUG_CATEGORY_API_ERROR_AMD;

  public static final int GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD
    = GL2.GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD;

  public static final int GL_DEBUG_CATEGORY_DEPRECATION_AMD
    = GL2.GL_DEBUG_CATEGORY_DEPRECATION_AMD;

  public static final int GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD
    = GL2.GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD;

  public static final int GL_DEBUG_CATEGORY_PERFORMANCE_AMD
    = GL2.GL_DEBUG_CATEGORY_PERFORMANCE_AMD;

  public static final int GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD
    = GL2.GL_DEBUG_CATEGORY_SHADER_COMPILER_AMD;

  public static final int GL_DEBUG_CATEGORY_APPLICATION_AMD
    = GL2.GL_DEBUG_CATEGORY_APPLICATION_AMD;

  public static final int GL_DEBUG_CATEGORY_OTHER_AMD
    = GL2.GL_DEBUG_CATEGORY_OTHER_AMD;

  public static final int GL_DEPTH_CLAMP_NEAR_AMD
    = GL2.GL_DEPTH_CLAMP_NEAR_AMD;

  public static final int GL_DEPTH_CLAMP_FAR_AMD
    = GL2.GL_DEPTH_CLAMP_FAR_AMD;

  public static final int GL_TEXTURE_SRGB_DECODE_EXT
    = GL2.GL_TEXTURE_SRGB_DECODE_EXT;

  public static final int GL_DECODE_EXT
    = GL2.GL_DECODE_EXT;

  public static final int GL_SKIP_DECODE_EXT
    = GL2.GL_SKIP_DECODE_EXT;

  public static final int GL_TEXTURE_COVERAGE_SAMPLES_NV
    = GL2.GL_TEXTURE_COVERAGE_SAMPLES_NV;

  public static final int GL_TEXTURE_COLOR_SAMPLES_NV
    = GL2.GL_TEXTURE_COLOR_SAMPLES_NV;

  public static final int GL_FACTOR_MIN_AMD
    = GL2.GL_FACTOR_MIN_AMD;

  public static final int GL_FACTOR_MAX_AMD
    = GL2.GL_FACTOR_MAX_AMD;

  public static final int GL_SUBSAMPLE_DISTANCE_AMD
    = GL2.GL_SUBSAMPLE_DISTANCE_AMD;

  public static final int GL_SYNC_X11_FENCE_EXT
    = GL2.GL_SYNC_X11_FENCE_EXT;

  public static final int GL_SCALED_RESOLVE_FASTEST_EXT
    = GL2.GL_SCALED_RESOLVE_FASTEST_EXT;

  public static final int GL_SCALED_RESOLVE_NICEST_EXT
    = GL2.GL_SCALED_RESOLVE_NICEST_EXT;

  public static final int GL_EXTERNAL_VIRTUAL_MEMORY_BUFFER_AMD
    = GL2.GL_EXTERNAL_VIRTUAL_MEMORY_BUFFER_AMD;

  public static final int GL_SET_AMD
    = GL2.GL_SET_AMD;

  public static final int GL_REPLACE_VALUE_AMD
    = GL2.GL_REPLACE_VALUE_AMD;

  public static final int GL_STENCIL_OP_VALUE_AMD
    = GL2.GL_STENCIL_OP_VALUE_AMD;

  public static final int GL_STENCIL_BACK_OP_VALUE_AMD
    = GL2.GL_STENCIL_BACK_OP_VALUE_AMD;

  public static final int GL_TEXTURE_RECTANGLE
    = GL2.GL_TEXTURE_RECTANGLE;

  public static final int GL_TEXTURE_BINDING_RECTANGLE
    = GL2.GL_TEXTURE_BINDING_RECTANGLE;

  public static final int GL_PROXY_TEXTURE_RECTANGLE
    = GL2.GL_PROXY_TEXTURE_RECTANGLE;

  public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE
    = GL2.GL_MAX_RECTANGLE_TEXTURE_SIZE;

  public static final int GL_SAMPLER_2D_RECT
    = GL2.GL_SAMPLER_2D_RECT;

  public static final int GL_SAMPLER_2D_RECT_SHADOW
    = GL2.GL_SAMPLER_2D_RECT_SHADOW;

  public static final int GL_INVALID_INDEX
    = GL2.GL_INVALID_INDEX;

  public static final int GL_ALL_BARRIER_BITS
    = GL2.GL_ALL_BARRIER_BITS;

  public static void glActiveShaderProgram(
    int pipeline,
    int program) {
    gl().glActiveShaderProgram(
      pipeline,
      program);
  }

  public static void glBeginConditionalRender(
    int id,
    int mode) {
    gl().glBeginConditionalRender(
      id,
      mode);
  }

  public static void glBeginQueryIndexed(
    int target,
    int index,
    int id) {
    gl().glBeginQueryIndexed(
      target,
      index,
      id);
  }

  public static void glBeginTransformFeedback(
    int primitiveMode) {
    gl().glBeginTransformFeedback(
      primitiveMode);
  }

  public static void glBindBufferBase(
    int target,
    int index,
    int buffer) {
    gl().glBindBufferBase(
      target,
      index,
      buffer);
  }

  public static void glBindBufferRange(
    int target,
    int index,
    int buffer,
    long offset,
    long size) {
    gl().glBindBufferRange(
      target,
      index,
      buffer,
      offset,
      size);
  }

  public static void glBindFragDataLocation(
    int program,
    int color,
    String name) {
    gl().glBindFragDataLocation(
      program,
      color,
      name);
  }

  public static void glBindFragDataLocationIndexed(
    int program,
    int colorNumber,
    int index,
    String name) {
    gl().glBindFragDataLocationIndexed(
      program,
      colorNumber,
      index,
      name);
  }

  public static void glBindImageTexture(
    int unit,
    int texture,
    int level,
    boolean layered,
    int layer,
    int access,
    int format) {
    gl().glBindImageTexture(
      unit,
      texture,
      level,
      layered,
      layer,
      access,
      format);
  }

  public static void glBindProgramPipeline(
    int pipeline) {
    gl().glBindProgramPipeline(
      pipeline);
  }

  public static void glBindSampler(
    int unit,
    int sampler) {
    gl().glBindSampler(
      unit,
      sampler);
  }

  public static void glBindTransformFeedback(
    int target,
    int id) {
    gl().glBindTransformFeedback(
      target,
      id);
  }

  public static void glBindVertexArray(
    int array) {
    gl().glBindVertexArray(
      array);
  }

  public static void glBlendEquationSeparatei(
    int buf,
    int modeRGB,
    int modeAlpha) {
    gl().glBlendEquationSeparatei(
      buf,
      modeRGB,
      modeAlpha);
  }

  public static void glBlendEquationi(
    int buf,
    int mode) {
    gl().glBlendEquationi(
      buf,
      mode);
  }

  public static void glBlendFuncSeparatei(
    int buf,
    int srcRGB,
    int dstRGB,
    int srcAlpha,
    int dstAlpha) {
    gl().glBlendFuncSeparatei(
      buf,
      srcRGB,
      dstRGB,
      srcAlpha,
      dstAlpha);
  }

  public static void glBlendFunci(
    int buf,
    int src,
    int dst) {
    gl().glBlendFunci(
      buf,
      src,
      dst);
  }

  public static void glBlitFramebuffer(
    int srcX0,
    int srcY0,
    int srcX1,
    int srcY1,
    int dstX0,
    int dstY0,
    int dstX1,
    int dstY1,
    int mask,
    int filter) {
    gl().glBlitFramebuffer(
      srcX0,
      srcY0,
      srcX1,
      srcY1,
      dstX0,
      dstY0,
      dstX1,
      dstY1,
      mask,
      filter);
  }

  public static void glBufferAddressRangeNV(
    int pname,
    int index,
    long address,
    long length) {
    gl().glBufferAddressRangeNV(
      pname,
      index,
      address,
      length);
  }

  public static void glClampColor(
    int target,
    int clamp) {
    gl().glClampColor(
      target,
      clamp);
  }

  public static void glClearBufferfi(
    int buffer,
    int drawbuffer,
    float depth,
    int stencil) {
    gl().glClearBufferfi(
      buffer,
      drawbuffer,
      depth,
      stencil);
  }

  public static void glClearBufferfv(
    int buffer,
    int drawbuffer,
    FloatBuffer value) {
    gl().glClearBufferfv(
      buffer,
      drawbuffer,
      value);
  }

  public static void glClearBufferfv(
    int buffer,
    int drawbuffer,
    float[] value,
    int value_offset) {
    gl().glClearBufferfv(
      buffer,
      drawbuffer,
      value,
      value_offset);
  }

  public static void glClearBufferiv(
    int buffer,
    int drawbuffer,
    IntBuffer value) {
    gl().glClearBufferiv(
      buffer,
      drawbuffer,
      value);
  }

  public static void glClearBufferiv(
    int buffer,
    int drawbuffer,
    int[] value,
    int value_offset) {
    gl().glClearBufferiv(
      buffer,
      drawbuffer,
      value,
      value_offset);
  }

  public static void glClearBufferuiv(
    int buffer,
    int drawbuffer,
    IntBuffer value) {
    gl().glClearBufferuiv(
      buffer,
      drawbuffer,
      value);
  }

  public static void glClearBufferuiv(
    int buffer,
    int drawbuffer,
    int[] value,
    int value_offset) {
    gl().glClearBufferuiv(
      buffer,
      drawbuffer,
      value,
      value_offset);
  }

  public static void glColorFormatNV(
    int size,
    int type,
    int stride) {
    gl().glColorFormatNV(
      size,
      type,
      stride);
  }

  public static void glColorMaski(
    int index,
    boolean r,
    boolean g,
    boolean b,
    boolean a) {
    gl().glColorMaski(
      index,
      r,
      g,
      b,
      a);
  }

  public static void glColorP3ui(
    int type,
    int color) {
    gl().glColorP3ui(
      type,
      color);
  }

  public static void glColorP3uiv(
    int type,
    IntBuffer color) {
    gl().glColorP3uiv(
      type,
      color);
  }

  public static void glColorP3uiv(
    int type,
    int[] color,
    int color_offset) {
    gl().glColorP3uiv(
      type,
      color,
      color_offset);
  }

  public static void glColorP4ui(
    int type,
    int color) {
    gl().glColorP4ui(
      type,
      color);
  }

  public static void glColorP4uiv(
    int type,
    IntBuffer color) {
    gl().glColorP4uiv(
      type,
      color);
  }

  public static void glColorP4uiv(
    int type,
    int[] color,
    int color_offset) {
    gl().glColorP4uiv(
      type,
      color,
      color_offset);
  }

  public static void glCompileShaderIncludeARB(
    int shader,
    int count,
    String[] path,
    IntBuffer length) {
    gl().glCompileShaderIncludeARB(
      shader,
      count,
      path,
      length);
  }

  public static void glCompileShaderIncludeARB(
    int shader,
    int count,
    String[] path,
    int[] length,
    int length_offset) {
    gl().glCompileShaderIncludeARB(
      shader,
      count,
      path,
      length,
      length_offset);
  }

  public static void glCompressedTexImage1D(
    int target,
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

  public static void glCompressedTexImage1D(
    int target,
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

  public static void glCompressedTexSubImage1D(
    int target,
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

  public static void glCompressedTexSubImage1D(
    int target,
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

  public static void glCopyBufferSubData(
    int readTarget,
    int writeTarget,
    long readOffset,
    long writeOffset,
    long size) {
    gl().glCopyBufferSubData(
      readTarget,
      writeTarget,
      readOffset,
      writeOffset,
      size);
  }

  public static void glCopyTexImage1D(
    int target,
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

  public static void glCopyTexSubImage1D(
    int target,
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

  public static int glCreateShaderProgramv(
    int type,
    int count,
    PointerBuffer strings) {
    return gl().glCreateShaderProgramv(
      type,
      count,
      strings);
  }

  public static long glCreateSyncFromCLeventARB(
    Buffer context,
    Buffer event,
    int flags) {
    return gl().glCreateSyncFromCLeventARB(
      context,
      event,
      flags);
  }

  public static void glDebugMessageControlARB(
    int source,
    int type,
    int severity,
    int count,
    IntBuffer ids,
    boolean enabled) {
    gl().glDebugMessageControlARB(
      source,
      type,
      severity,
      count,
      ids,
      enabled);
  }

  public static void glDebugMessageControlARB(
    int source,
    int type,
    int severity,
    int count,
    int[] ids,
    int ids_offset,
    boolean enabled) {
    gl().glDebugMessageControlARB(
      source,
      type,
      severity,
      count,
      ids,
      ids_offset,
      enabled);
  }

  public static void glDebugMessageEnableAMD(
    int category,
    int severity,
    int count,
    IntBuffer ids,
    boolean enabled) {
    gl().glDebugMessageEnableAMD(
      category,
      severity,
      count,
      ids,
      enabled);
  }

  public static void glDebugMessageEnableAMD(
    int category,
    int severity,
    int count,
    int[] ids,
    int ids_offset,
    boolean enabled) {
    gl().glDebugMessageEnableAMD(
      category,
      severity,
      count,
      ids,
      ids_offset,
      enabled);
  }

  public static void glDebugMessageInsertAMD(
    int category,
    int severity,
    int id,
    int length,
    String buf) {
    gl().glDebugMessageInsertAMD(
      category,
      severity,
      id,
      length,
      buf);
  }

  public static void glDebugMessageInsertARB(
    int source,
    int type,
    int id,
    int severity,
    int length,
    String buf) {
    gl().glDebugMessageInsertARB(
      source,
      type,
      id,
      severity,
      length,
      buf);
  }

  public static void glDeleteNamedStringARB(
    int namelen,
    String name) {
    gl().glDeleteNamedStringARB(
      namelen,
      name);
  }

  public static void glDeleteProgramPipelines(
    int n,
    IntBuffer pipelines) {
    gl().glDeleteProgramPipelines(
      n,
      pipelines);
  }

  public static void glDeleteProgramPipelines(
    int n,
    int[] pipelines,
    int pipelines_offset) {
    gl().glDeleteProgramPipelines(
      n,
      pipelines,
      pipelines_offset);
  }

  public static void glDeleteSamplers(
    int count,
    IntBuffer samplers) {
    gl().glDeleteSamplers(
      count,
      samplers);
  }

  public static void glDeleteSamplers(
    int count,
    int[] samplers,
    int samplers_offset) {
    gl().glDeleteSamplers(
      count,
      samplers,
      samplers_offset);
  }

  public static void glDeleteTransformFeedbacks(
    int n,
    IntBuffer ids) {
    gl().glDeleteTransformFeedbacks(
      n,
      ids);
  }

  public static void glDeleteTransformFeedbacks(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glDeleteTransformFeedbacks(
      n,
      ids,
      ids_offset);
  }

  public static void glDeleteVertexArrays(
    int n,
    IntBuffer arrays) {
    gl().glDeleteVertexArrays(
      n,
      arrays);
  }

  public static void glDeleteVertexArrays(
    int n,
    int[] arrays,
    int arrays_offset) {
    gl().glDeleteVertexArrays(
      n,
      arrays,
      arrays_offset);
  }

  public static void glDepthRangeArrayv(
    int first,
    int count,
    DoubleBuffer v) {
    gl().glDepthRangeArrayv(
      first,
      count,
      v);
  }

  public static void glDepthRangeArrayv(
    int first,
    int count,
    double[] v,
    int v_offset) {
    gl().glDepthRangeArrayv(
      first,
      count,
      v,
      v_offset);
  }

  public static void glDepthRangeIndexed(
    int index,
    double n,
    double f) {
    gl().glDepthRangeIndexed(
      index,
      n,
      f);
  }

  public static void glDisablei(
    int target,
    int index) {
    gl().glDisablei(
      target,
      index);
  }

  public static void glDrawArraysInstanced(
    int mode,
    int first,
    int count,
    int primcount) {
    gl().glDrawArraysInstanced(
      mode,
      first,
      count,
      primcount);
  }

  public static void glDrawArraysInstancedBaseInstance(
    int mode,
    int first,
    int count,
    int primcount,
    int baseinstance) {
    gl().glDrawArraysInstancedBaseInstance(
      mode,
      first,
      count,
      primcount,
      baseinstance);
  }

  public static void glDrawBuffer(
    int mode) {
    gl().glDrawBuffer(
      mode);
  }

  public static void glDrawBuffers(
    int n,
    IntBuffer bufs) {
    gl().glDrawBuffers(
      n,
      bufs);
  }

  public static void glDrawBuffers(
    int n,
    int[] bufs,
    int bufs_offset) {
    gl().glDrawBuffers(
      n,
      bufs,
      bufs_offset);
  }

  public static void glDrawElementsBaseVertex(
    int mode,
    int count,
    int type,
    Buffer indices,
    int basevertex) {
    gl().glDrawElementsBaseVertex(
      mode,
      count,
      type,
      indices,
      basevertex);
  }

  public static void glDrawElementsInstanced(
    int mode,
    int count,
    int type,
    Buffer indices,
    int primcount) {
    gl().glDrawElementsInstanced(
      mode,
      count,
      type,
      indices,
      primcount);
  }

  public static void glDrawElementsInstancedBaseInstance(
    int mode,
    int count,
    int type,
    Buffer indices,
    int primcount,
    int baseinstance) {
    gl().glDrawElementsInstancedBaseInstance(
      mode,
      count,
      type,
      indices,
      primcount,
      baseinstance);
  }

  public static void glDrawElementsInstancedBaseVertex(
    int mode,
    int count,
    int type,
    Buffer indices,
    int primcount,
    int basevertex) {
    gl().glDrawElementsInstancedBaseVertex(
      mode,
      count,
      type,
      indices,
      primcount,
      basevertex);
  }

  public static void glDrawElementsInstancedBaseVertexBaseInstance(
    int mode,
    int count,
    int type,
    Buffer indices,
    int primcount,
    int basevertex,
    int baseinstance) {
    gl().glDrawElementsInstancedBaseVertexBaseInstance(
      mode,
      count,
      type,
      indices,
      primcount,
      basevertex,
      baseinstance);
  }

  public static void glDrawRangeElements(
    int mode,
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

  public static void glDrawRangeElements(
    int mode,
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

  public static void glDrawRangeElementsBaseVertex(
    int mode,
    int start,
    int end,
    int count,
    int type,
    Buffer indices,
    int basevertex) {
    gl().glDrawRangeElementsBaseVertex(
      mode,
      start,
      end,
      count,
      type,
      indices,
      basevertex);
  }

  public static void glDrawTransformFeedback(
    int mode,
    int id) {
    gl().glDrawTransformFeedback(
      mode,
      id);
  }

  public static void glDrawTransformFeedbackInstanced(
    int mode,
    int id,
    int primcount) {
    gl().glDrawTransformFeedbackInstanced(
      mode,
      id,
      primcount);
  }

  public static void glDrawTransformFeedbackStream(
    int mode,
    int id,
    int stream) {
    gl().glDrawTransformFeedbackStream(
      mode,
      id,
      stream);
  }

  public static void glDrawTransformFeedbackStreamInstanced(
    int mode,
    int id,
    int stream,
    int primcount) {
    gl().glDrawTransformFeedbackStreamInstanced(
      mode,
      id,
      stream,
      primcount);
  }

  public static void glEdgeFlagFormatNV(
    int stride) {
    gl().glEdgeFlagFormatNV(
      stride);
  }

  public static void glEnablei(
    int target,
    int index) {
    gl().glEnablei(
      target,
      index);
  }

  public static void glEndConditionalRender(
    ) {
    gl().glEndConditionalRender();
  }

  public static void glEndQueryIndexed(
    int target,
    int index) {
    gl().glEndQueryIndexed(
      target,
      index);
  }

  public static void glEndTransformFeedback(
    ) {
    gl().glEndTransformFeedback();
  }

  public static void glFlushMappedBufferRange(
    int target,
    long offset,
    long length) {
    gl().glFlushMappedBufferRange(
      target,
      offset,
      length);
  }

  public static void glFogCoordFormatNV(
    int type,
    int stride) {
    gl().glFogCoordFormatNV(
      type,
      stride);
  }

  public static void glFramebufferTexture1D(
    int target,
    int attachment,
    int textarget,
    int texture,
    int level) {
    gl().glFramebufferTexture1D(
      target,
      attachment,
      textarget,
      texture,
      level);
  }

  public static void glFramebufferTextureARB(
    int target,
    int attachment,
    int texture,
    int level) {
    gl().glFramebufferTextureARB(
      target,
      attachment,
      texture,
      level);
  }

  public static void glFramebufferTextureFaceARB(
    int target,
    int attachment,
    int texture,
    int level,
    int face) {
    gl().glFramebufferTextureFaceARB(
      target,
      attachment,
      texture,
      level,
      face);
  }

  public static void glFramebufferTextureLayer(
    int target,
    int attachment,
    int texture,
    int level,
    int layer) {
    gl().glFramebufferTextureLayer(
      target,
      attachment,
      texture,
      level,
      layer);
  }

  public static void glFramebufferTextureLayerARB(
    int target,
    int attachment,
    int texture,
    int level,
    int layer) {
    gl().glFramebufferTextureLayerARB(
      target,
      attachment,
      texture,
      level,
      layer);
  }

  public static void glGenProgramPipelines(
    int n,
    IntBuffer pipelines) {
    gl().glGenProgramPipelines(
      n,
      pipelines);
  }

  public static void glGenProgramPipelines(
    int n,
    int[] pipelines,
    int pipelines_offset) {
    gl().glGenProgramPipelines(
      n,
      pipelines,
      pipelines_offset);
  }

  public static void glGenSamplers(
    int count,
    IntBuffer samplers) {
    gl().glGenSamplers(
      count,
      samplers);
  }

  public static void glGenSamplers(
    int count,
    int[] samplers,
    int samplers_offset) {
    gl().glGenSamplers(
      count,
      samplers,
      samplers_offset);
  }

  public static void glGenTransformFeedbacks(
    int n,
    IntBuffer ids) {
    gl().glGenTransformFeedbacks(
      n,
      ids);
  }

  public static void glGenTransformFeedbacks(
    int n,
    int[] ids,
    int ids_offset) {
    gl().glGenTransformFeedbacks(
      n,
      ids,
      ids_offset);
  }

  public static void glGenVertexArrays(
    int n,
    IntBuffer arrays) {
    gl().glGenVertexArrays(
      n,
      arrays);
  }

  public static void glGenVertexArrays(
    int n,
    int[] arrays,
    int arrays_offset) {
    gl().glGenVertexArrays(
      n,
      arrays,
      arrays_offset);
  }

  public static void glGetActiveAtomicCounterBufferiv(
    int program,
    int bufferIndex,
    int pname,
    IntBuffer params) {
    gl().glGetActiveAtomicCounterBufferiv(
      program,
      bufferIndex,
      pname,
      params);
  }

  public static void glGetActiveAtomicCounterBufferiv(
    int program,
    int bufferIndex,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetActiveAtomicCounterBufferiv(
      program,
      bufferIndex,
      pname,
      params,
      params_offset);
  }

  public static void glGetActiveSubroutineName(
    int program,
    int shadertype,
    int index,
    int bufsize,
    IntBuffer length,
    ByteBuffer name) {
    gl().glGetActiveSubroutineName(
      program,
      shadertype,
      index,
      bufsize,
      length,
      name);
  }

  public static void glGetActiveSubroutineName(
    int program,
    int shadertype,
    int index,
    int bufsize,
    int[] length,
    int length_offset,
    byte[] name,
    int name_offset) {
    gl().glGetActiveSubroutineName(
      program,
      shadertype,
      index,
      bufsize,
      length,
      length_offset,
      name,
      name_offset);
  }

  public static void glGetActiveSubroutineUniformName(
    int program,
    int shadertype,
    int index,
    int bufsize,
    IntBuffer length,
    ByteBuffer name) {
    gl().glGetActiveSubroutineUniformName(
      program,
      shadertype,
      index,
      bufsize,
      length,
      name);
  }

  public static void glGetActiveSubroutineUniformName(
    int program,
    int shadertype,
    int index,
    int bufsize,
    int[] length,
    int length_offset,
    byte[] name,
    int name_offset) {
    gl().glGetActiveSubroutineUniformName(
      program,
      shadertype,
      index,
      bufsize,
      length,
      length_offset,
      name,
      name_offset);
  }

  public static void glGetActiveSubroutineUniformiv(
    int program,
    int shadertype,
    int index,
    int pname,
    IntBuffer values) {
    gl().glGetActiveSubroutineUniformiv(
      program,
      shadertype,
      index,
      pname,
      values);
  }

  public static void glGetActiveSubroutineUniformiv(
    int program,
    int shadertype,
    int index,
    int pname,
    int[] values,
    int values_offset) {
    gl().glGetActiveSubroutineUniformiv(
      program,
      shadertype,
      index,
      pname,
      values,
      values_offset);
  }

  public static void glGetActiveUniformBlockName(
    int program,
    int uniformBlockIndex,
    int bufSize,
    IntBuffer length,
    ByteBuffer uniformBlockName) {
    gl().glGetActiveUniformBlockName(
      program,
      uniformBlockIndex,
      bufSize,
      length,
      uniformBlockName);
  }

  public static void glGetActiveUniformBlockName(
    int program,
    int uniformBlockIndex,
    int bufSize,
    int[] length,
    int length_offset,
    byte[] uniformBlockName,
    int uniformBlockName_offset) {
    gl().glGetActiveUniformBlockName(
      program,
      uniformBlockIndex,
      bufSize,
      length,
      length_offset,
      uniformBlockName,
      uniformBlockName_offset);
  }

  public static void glGetActiveUniformBlockiv(
    int program,
    int uniformBlockIndex,
    int pname,
    IntBuffer params) {
    gl().glGetActiveUniformBlockiv(
      program,
      uniformBlockIndex,
      pname,
      params);
  }

  public static void glGetActiveUniformBlockiv(
    int program,
    int uniformBlockIndex,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetActiveUniformBlockiv(
      program,
      uniformBlockIndex,
      pname,
      params,
      params_offset);
  }

  public static void glGetActiveUniformName(
    int program,
    int uniformIndex,
    int bufSize,
    IntBuffer length,
    ByteBuffer uniformName) {
    gl().glGetActiveUniformName(
      program,
      uniformIndex,
      bufSize,
      length,
      uniformName);
  }

  public static void glGetActiveUniformName(
    int program,
    int uniformIndex,
    int bufSize,
    int[] length,
    int length_offset,
    byte[] uniformName,
    int uniformName_offset) {
    gl().glGetActiveUniformName(
      program,
      uniformIndex,
      bufSize,
      length,
      length_offset,
      uniformName,
      uniformName_offset);
  }

  public static void glGetActiveUniformsiv(
    int program,
    int uniformCount,
    IntBuffer uniformIndices,
    int pname,
    IntBuffer params) {
    gl().glGetActiveUniformsiv(
      program,
      uniformCount,
      uniformIndices,
      pname,
      params);
  }

  public static void glGetActiveUniformsiv(
    int program,
    int uniformCount,
    int[] uniformIndices,
    int uniformIndices_offset,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetActiveUniformsiv(
      program,
      uniformCount,
      uniformIndices,
      uniformIndices_offset,
      pname,
      params,
      params_offset);
  }

  public static void glGetBooleani_v(
    int target,
    int index,
    ByteBuffer data) {
    gl().glGetBooleani_v(
      target,
      index,
      data);
  }

  public static void glGetBooleani_v(
    int target,
    int index,
    byte[] data,
    int data_offset) {
    gl().glGetBooleani_v(
      target,
      index,
      data,
      data_offset);
  }

  public static void glGetBufferParameterui64vNV(
    int target,
    int pname,
    LongBuffer params) {
    gl().glGetBufferParameterui64vNV(
      target,
      pname,
      params);
  }

  public static void glGetBufferParameterui64vNV(
    int target,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetBufferParameterui64vNV(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetBufferSubData(
    int target,
    long offset,
    long size,
    Buffer data) {
    gl().glGetBufferSubData(
      target,
      offset,
      size,
      data);
  }

  public static void glGetCompressedTexImage(
    int target,
    int level,
    Buffer img) {
    gl().glGetCompressedTexImage(
      target,
      level,
      img);
  }

  public static void glGetCompressedTexImage(
    int target,
    int level,
    long img_buffer_offset) {
    gl().glGetCompressedTexImage(
      target,
      level,
      img_buffer_offset);
  }

  public static int glGetDebugMessageLogAMD(
    int count,
    int bufsize,
    IntBuffer categories,
    IntBuffer severities,
    IntBuffer ids,
    IntBuffer lengths,
    ByteBuffer message) {
    return gl().glGetDebugMessageLogAMD(
      count,
      bufsize,
      categories,
      severities,
      ids,
      lengths,
      message);
  }

  public static int glGetDebugMessageLogAMD(
    int count,
    int bufsize,
    int[] categories,
    int categories_offset,
    int[] severities,
    int severities_offset,
    int[] ids,
    int ids_offset,
    int[] lengths,
    int lengths_offset,
    byte[] message,
    int message_offset) {
    return gl().glGetDebugMessageLogAMD(
      count,
      bufsize,
      categories,
      categories_offset,
      severities,
      severities_offset,
      ids,
      ids_offset,
      lengths,
      lengths_offset,
      message,
      message_offset);
  }

  public static int glGetDebugMessageLogARB(
    int count,
    int bufsize,
    IntBuffer sources,
    IntBuffer types,
    IntBuffer ids,
    IntBuffer severities,
    IntBuffer lengths,
    ByteBuffer messageLog) {
    return gl().glGetDebugMessageLogARB(
      count,
      bufsize,
      sources,
      types,
      ids,
      severities,
      lengths,
      messageLog);
  }

  public static int glGetDebugMessageLogARB(
    int count,
    int bufsize,
    int[] sources,
    int sources_offset,
    int[] types,
    int types_offset,
    int[] ids,
    int ids_offset,
    int[] severities,
    int severities_offset,
    int[] lengths,
    int lengths_offset,
    byte[] messageLog,
    int messageLog_offset) {
    return gl().glGetDebugMessageLogARB(
      count,
      bufsize,
      sources,
      sources_offset,
      types,
      types_offset,
      ids,
      ids_offset,
      severities,
      severities_offset,
      lengths,
      lengths_offset,
      messageLog,
      messageLog_offset);
  }

  public static void glGetDoublei_v(
    int target,
    int index,
    DoubleBuffer data) {
    gl().glGetDoublei_v(
      target,
      index,
      data);
  }

  public static void glGetDoublei_v(
    int target,
    int index,
    double[] data,
    int data_offset) {
    gl().glGetDoublei_v(
      target,
      index,
      data,
      data_offset);
  }

  public static void glGetDoublev(
    int pname,
    DoubleBuffer params) {
    gl().glGetDoublev(
      pname,
      params);
  }

  public static void glGetDoublev(
    int pname,
    double[] params,
    int params_offset) {
    gl().glGetDoublev(
      pname,
      params,
      params_offset);
  }

  public static void glGetFloati_v(
    int target,
    int index,
    FloatBuffer data) {
    gl().glGetFloati_v(
      target,
      index,
      data);
  }

  public static void glGetFloati_v(
    int target,
    int index,
    float[] data,
    int data_offset) {
    gl().glGetFloati_v(
      target,
      index,
      data,
      data_offset);
  }

  public static int glGetFragDataIndex(
    int program,
    String name) {
    return gl().glGetFragDataIndex(
      program,
      name);
  }

  public static int glGetFragDataLocation(
    int program,
    String name) {
    return gl().glGetFragDataLocation(
      program,
      name);
  }

  public static void glGetIntegeri_v(
    int target,
    int index,
    IntBuffer data) {
    gl().glGetIntegeri_v(
      target,
      index,
      data);
  }

  public static void glGetIntegeri_v(
    int target,
    int index,
    int[] data,
    int data_offset) {
    gl().glGetIntegeri_v(
      target,
      index,
      data,
      data_offset);
  }

  public static void glGetIntegerui64i_vNV(
    int value,
    int index,
    LongBuffer result) {
    gl().glGetIntegerui64i_vNV(
      value,
      index,
      result);
  }

  public static void glGetIntegerui64i_vNV(
    int value,
    int index,
    long[] result,
    int result_offset) {
    gl().glGetIntegerui64i_vNV(
      value,
      index,
      result,
      result_offset);
  }

  public static void glGetIntegerui64vNV(
    int value,
    LongBuffer result) {
    gl().glGetIntegerui64vNV(
      value,
      result);
  }

  public static void glGetIntegerui64vNV(
    int value,
    long[] result,
    int result_offset) {
    gl().glGetIntegerui64vNV(
      value,
      result,
      result_offset);
  }

  public static void glGetInternalformativ(
    int target,
    int internalformat,
    int pname,
    int bufSize,
    IntBuffer params) {
    gl().glGetInternalformativ(
      target,
      internalformat,
      pname,
      bufSize,
      params);
  }

  public static void glGetInternalformativ(
    int target,
    int internalformat,
    int pname,
    int bufSize,
    int[] params,
    int params_offset) {
    gl().glGetInternalformativ(
      target,
      internalformat,
      pname,
      bufSize,
      params,
      params_offset);
  }

  public static void glGetMultisamplefv(
    int pname,
    int index,
    FloatBuffer val) {
    gl().glGetMultisamplefv(
      pname,
      index,
      val);
  }

  public static void glGetMultisamplefv(
    int pname,
    int index,
    float[] val,
    int val_offset) {
    gl().glGetMultisamplefv(
      pname,
      index,
      val,
      val_offset);
  }

  public static void glGetNamedBufferParameterui64vNV(
    int buffer,
    int pname,
    LongBuffer params) {
    gl().glGetNamedBufferParameterui64vNV(
      buffer,
      pname,
      params);
  }

  public static void glGetNamedBufferParameterui64vNV(
    int buffer,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetNamedBufferParameterui64vNV(
      buffer,
      pname,
      params,
      params_offset);
  }

  public static void glGetNamedStringARB(
    int namelen,
    String name,
    int bufSize,
    IntBuffer stringlen,
    ByteBuffer string) {
    gl().glGetNamedStringARB(
      namelen,
      name,
      bufSize,
      stringlen,
      string);
  }

  public static void glGetNamedStringARB(
    int namelen,
    String name,
    int bufSize,
    int[] stringlen,
    int stringlen_offset,
    byte[] string,
    int string_offset) {
    gl().glGetNamedStringARB(
      namelen,
      name,
      bufSize,
      stringlen,
      stringlen_offset,
      string,
      string_offset);
  }

  public static void glGetNamedStringivARB(
    int namelen,
    String name,
    int pname,
    IntBuffer params) {
    gl().glGetNamedStringivARB(
      namelen,
      name,
      pname,
      params);
  }

  public static void glGetNamedStringivARB(
    int namelen,
    String name,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetNamedStringivARB(
      namelen,
      name,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramPipelineInfoLog(
    int pipeline,
    int bufSize,
    IntBuffer length,
    ByteBuffer infoLog) {
    gl().glGetProgramPipelineInfoLog(
      pipeline,
      bufSize,
      length,
      infoLog);
  }

  public static void glGetProgramPipelineInfoLog(
    int pipeline,
    int bufSize,
    int[] length,
    int length_offset,
    byte[] infoLog,
    int infoLog_offset) {
    gl().glGetProgramPipelineInfoLog(
      pipeline,
      bufSize,
      length,
      length_offset,
      infoLog,
      infoLog_offset);
  }

  public static void glGetProgramPipelineiv(
    int pipeline,
    int pname,
    IntBuffer params) {
    gl().glGetProgramPipelineiv(
      pipeline,
      pname,
      params);
  }

  public static void glGetProgramPipelineiv(
    int pipeline,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetProgramPipelineiv(
      pipeline,
      pname,
      params,
      params_offset);
  }

  public static void glGetProgramStageiv(
    int program,
    int shadertype,
    int pname,
    IntBuffer values) {
    gl().glGetProgramStageiv(
      program,
      shadertype,
      pname,
      values);
  }

  public static void glGetProgramStageiv(
    int program,
    int shadertype,
    int pname,
    int[] values,
    int values_offset) {
    gl().glGetProgramStageiv(
      program,
      shadertype,
      pname,
      values,
      values_offset);
  }

  public static void glGetQueryIndexediv(
    int target,
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetQueryIndexediv(
      target,
      index,
      pname,
      params);
  }

  public static void glGetQueryIndexediv(
    int target,
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetQueryIndexediv(
      target,
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjecti64v(
    int id,
    int pname,
    LongBuffer params) {
    gl().glGetQueryObjecti64v(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjecti64v(
    int id,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetQueryObjecti64v(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectiv(
    int id,
    int pname,
    IntBuffer params) {
    gl().glGetQueryObjectiv(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjectiv(
    int id,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetQueryObjectiv(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetQueryObjectui64v(
    int id,
    int pname,
    LongBuffer params) {
    gl().glGetQueryObjectui64v(
      id,
      pname,
      params);
  }

  public static void glGetQueryObjectui64v(
    int id,
    int pname,
    long[] params,
    int params_offset) {
    gl().glGetQueryObjectui64v(
      id,
      pname,
      params,
      params_offset);
  }

  public static void glGetSamplerParameterIiv(
    int sampler,
    int pname,
    IntBuffer params) {
    gl().glGetSamplerParameterIiv(
      sampler,
      pname,
      params);
  }

  public static void glGetSamplerParameterIiv(
    int sampler,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetSamplerParameterIiv(
      sampler,
      pname,
      params,
      params_offset);
  }

  public static void glGetSamplerParameterIuiv(
    int sampler,
    int pname,
    IntBuffer params) {
    gl().glGetSamplerParameterIuiv(
      sampler,
      pname,
      params);
  }

  public static void glGetSamplerParameterIuiv(
    int sampler,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetSamplerParameterIuiv(
      sampler,
      pname,
      params,
      params_offset);
  }

  public static void glGetSamplerParameterfv(
    int sampler,
    int pname,
    FloatBuffer params) {
    gl().glGetSamplerParameterfv(
      sampler,
      pname,
      params);
  }

  public static void glGetSamplerParameterfv(
    int sampler,
    int pname,
    float[] params,
    int params_offset) {
    gl().glGetSamplerParameterfv(
      sampler,
      pname,
      params,
      params_offset);
  }

  public static void glGetSamplerParameteriv(
    int sampler,
    int pname,
    IntBuffer params) {
    gl().glGetSamplerParameteriv(
      sampler,
      pname,
      params);
  }

  public static void glGetSamplerParameteriv(
    int sampler,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetSamplerParameteriv(
      sampler,
      pname,
      params,
      params_offset);
  }

  public static String glGetStringi(
    int name,
    int index) {
    return gl().glGetStringi(
      name,
      index);
  }

  public static int glGetSubroutineIndex(
    int program,
    int shadertype,
    String name) {
    return gl().glGetSubroutineIndex(
      program,
      shadertype,
      name);
  }

  public static int glGetSubroutineUniformLocation(
    int program,
    int shadertype,
    String name) {
    return gl().glGetSubroutineUniformLocation(
      program,
      shadertype,
      name);
  }

  public static void glGetTexImage(
    int target,
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

  public static void glGetTexImage(
    int target,
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

  public static void glGetTexLevelParameterfv(
    int target,
    int level,
    int pname,
    FloatBuffer params) {
    gl().glGetTexLevelParameterfv(
      target,
      level,
      pname,
      params);
  }

  public static void glGetTexLevelParameterfv(
    int target,
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

  public static void glGetTexLevelParameteriv(
    int target,
    int level,
    int pname,
    IntBuffer params) {
    gl().glGetTexLevelParameteriv(
      target,
      level,
      pname,
      params);
  }

  public static void glGetTexLevelParameteriv(
    int target,
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

  public static void glGetTexParameterIiv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetTexParameterIiv(
      target,
      pname,
      params);
  }

  public static void glGetTexParameterIiv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTexParameterIiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTexParameterIuiv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glGetTexParameterIuiv(
      target,
      pname,
      params);
  }

  public static void glGetTexParameterIuiv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetTexParameterIuiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glGetTransformFeedbackVarying(
    int program,
    int index,
    int bufSize,
    IntBuffer length,
    IntBuffer size,
    IntBuffer type,
    ByteBuffer name) {
    gl().glGetTransformFeedbackVarying(
      program,
      index,
      bufSize,
      length,
      size,
      type,
      name);
  }

  public static void glGetTransformFeedbackVarying(
    int program,
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
    gl().glGetTransformFeedbackVarying(
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

  public static int glGetUniformBlockIndex(
    int program,
    String uniformBlockName) {
    return gl().glGetUniformBlockIndex(
      program,
      uniformBlockName);
  }

  public static void glGetUniformIndices(
    int program,
    int uniformCount,
    String[] uniformNames,
    IntBuffer uniformIndices) {
    gl().glGetUniformIndices(
      program,
      uniformCount,
      uniformNames,
      uniformIndices);
  }

  public static void glGetUniformIndices(
    int program,
    int uniformCount,
    String[] uniformNames,
    int[] uniformIndices,
    int uniformIndices_offset) {
    gl().glGetUniformIndices(
      program,
      uniformCount,
      uniformNames,
      uniformIndices,
      uniformIndices_offset);
  }

  public static void glGetUniformSubroutineuiv(
    int shadertype,
    int location,
    IntBuffer params) {
    gl().glGetUniformSubroutineuiv(
      shadertype,
      location,
      params);
  }

  public static void glGetUniformSubroutineuiv(
    int shadertype,
    int location,
    int[] params,
    int params_offset) {
    gl().glGetUniformSubroutineuiv(
      shadertype,
      location,
      params,
      params_offset);
  }

  public static void glGetUniformui64vNV(
    int program,
    int location,
    LongBuffer params) {
    gl().glGetUniformui64vNV(
      program,
      location,
      params);
  }

  public static void glGetUniformui64vNV(
    int program,
    int location,
    long[] params,
    int params_offset) {
    gl().glGetUniformui64vNV(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetUniformuiv(
    int program,
    int location,
    IntBuffer params) {
    gl().glGetUniformuiv(
      program,
      location,
      params);
  }

  public static void glGetUniformuiv(
    int program,
    int location,
    int[] params,
    int params_offset) {
    gl().glGetUniformuiv(
      program,
      location,
      params,
      params_offset);
  }

  public static void glGetVertexAttribIiv(
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetVertexAttribIiv(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribIiv(
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVertexAttribIiv(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribIuiv(
    int index,
    int pname,
    IntBuffer params) {
    gl().glGetVertexAttribIuiv(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribIuiv(
    int index,
    int pname,
    int[] params,
    int params_offset) {
    gl().glGetVertexAttribIuiv(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribLdv(
    int index,
    int pname,
    DoubleBuffer params) {
    gl().glGetVertexAttribLdv(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribLdv(
    int index,
    int pname,
    double[] params,
    int params_offset) {
    gl().glGetVertexAttribLdv(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetVertexAttribdv(
    int index,
    int pname,
    DoubleBuffer params) {
    gl().glGetVertexAttribdv(
      index,
      pname,
      params);
  }

  public static void glGetVertexAttribdv(
    int index,
    int pname,
    double[] params,
    int params_offset) {
    gl().glGetVertexAttribdv(
      index,
      pname,
      params,
      params_offset);
  }

  public static void glGetnColorTable(
    int target,
    int format,
    int type,
    int bufSize,
    Buffer table) {
    gl().glGetnColorTable(
      target,
      format,
      type,
      bufSize,
      table);
  }

  public static void glGetnCompressedTexImage(
    int target,
    int lod,
    int bufSize,
    Buffer img) {
    gl().glGetnCompressedTexImage(
      target,
      lod,
      bufSize,
      img);
  }

  public static void glGetnConvolutionFilter(
    int target,
    int format,
    int type,
    int bufSize,
    Buffer image) {
    gl().glGetnConvolutionFilter(
      target,
      format,
      type,
      bufSize,
      image);
  }

  public static void glGetnHistogram(
    int target,
    boolean reset,
    int format,
    int type,
    int bufSize,
    Buffer values) {
    gl().glGetnHistogram(
      target,
      reset,
      format,
      type,
      bufSize,
      values);
  }

  public static void glGetnMapdv(
    int target,
    int query,
    int bufSize,
    DoubleBuffer v) {
    gl().glGetnMapdv(
      target,
      query,
      bufSize,
      v);
  }

  public static void glGetnMapdv(
    int target,
    int query,
    int bufSize,
    double[] v,
    int v_offset) {
    gl().glGetnMapdv(
      target,
      query,
      bufSize,
      v,
      v_offset);
  }

  public static void glGetnMapfv(
    int target,
    int query,
    int bufSize,
    FloatBuffer v) {
    gl().glGetnMapfv(
      target,
      query,
      bufSize,
      v);
  }

  public static void glGetnMapfv(
    int target,
    int query,
    int bufSize,
    float[] v,
    int v_offset) {
    gl().glGetnMapfv(
      target,
      query,
      bufSize,
      v,
      v_offset);
  }

  public static void glGetnMapiv(
    int target,
    int query,
    int bufSize,
    IntBuffer v) {
    gl().glGetnMapiv(
      target,
      query,
      bufSize,
      v);
  }

  public static void glGetnMapiv(
    int target,
    int query,
    int bufSize,
    int[] v,
    int v_offset) {
    gl().glGetnMapiv(
      target,
      query,
      bufSize,
      v,
      v_offset);
  }

  public static void glGetnMinmax(
    int target,
    boolean reset,
    int format,
    int type,
    int bufSize,
    Buffer values) {
    gl().glGetnMinmax(
      target,
      reset,
      format,
      type,
      bufSize,
      values);
  }

  public static void glGetnPixelMapfv(
    int map,
    int bufSize,
    FloatBuffer values) {
    gl().glGetnPixelMapfv(
      map,
      bufSize,
      values);
  }

  public static void glGetnPixelMapfv(
    int map,
    int bufSize,
    float[] values,
    int values_offset) {
    gl().glGetnPixelMapfv(
      map,
      bufSize,
      values,
      values_offset);
  }

  public static void glGetnPixelMapuiv(
    int map,
    int bufSize,
    IntBuffer values) {
    gl().glGetnPixelMapuiv(
      map,
      bufSize,
      values);
  }

  public static void glGetnPixelMapuiv(
    int map,
    int bufSize,
    int[] values,
    int values_offset) {
    gl().glGetnPixelMapuiv(
      map,
      bufSize,
      values,
      values_offset);
  }

  public static void glGetnPixelMapusv(
    int map,
    int bufSize,
    ShortBuffer values) {
    gl().glGetnPixelMapusv(
      map,
      bufSize,
      values);
  }

  public static void glGetnPixelMapusv(
    int map,
    int bufSize,
    short[] values,
    int values_offset) {
    gl().glGetnPixelMapusv(
      map,
      bufSize,
      values,
      values_offset);
  }

  public static void glGetnPolygonStipple(
    int bufSize,
    ByteBuffer pattern) {
    gl().glGetnPolygonStipple(
      bufSize,
      pattern);
  }

  public static void glGetnPolygonStipple(
    int bufSize,
    byte[] pattern,
    int pattern_offset) {
    gl().glGetnPolygonStipple(
      bufSize,
      pattern,
      pattern_offset);
  }

  public static void glGetnSeparableFilter(
    int target,
    int format,
    int type,
    int rowBufSize,
    Buffer row,
    int columnBufSize,
    Buffer column,
    Buffer span) {
    gl().glGetnSeparableFilter(
      target,
      format,
      type,
      rowBufSize,
      row,
      columnBufSize,
      column,
      span);
  }

  public static void glGetnTexImage(
    int target,
    int level,
    int format,
    int type,
    int bufSize,
    Buffer img) {
    gl().glGetnTexImage(
      target,
      level,
      format,
      type,
      bufSize,
      img);
  }

  public static void glGetnUniformdv(
    int program,
    int location,
    int bufSize,
    DoubleBuffer params) {
    gl().glGetnUniformdv(
      program,
      location,
      bufSize,
      params);
  }

  public static void glGetnUniformdv(
    int program,
    int location,
    int bufSize,
    double[] params,
    int params_offset) {
    gl().glGetnUniformdv(
      program,
      location,
      bufSize,
      params,
      params_offset);
  }

  public static void glGetnUniformuiv(
    int program,
    int location,
    int bufSize,
    IntBuffer params) {
    gl().glGetnUniformuiv(
      program,
      location,
      bufSize,
      params);
  }

  public static void glGetnUniformuiv(
    int program,
    int location,
    int bufSize,
    int[] params,
    int params_offset) {
    gl().glGetnUniformuiv(
      program,
      location,
      bufSize,
      params,
      params_offset);
  }

  public static long glImportSyncEXT(
    int external_sync_type,
    long external_sync,
    int flags) {
    return gl().glImportSyncEXT(
      external_sync_type,
      external_sync,
      flags);
  }

  public static void glIndexFormatNV(
    int type,
    int stride) {
    gl().glIndexFormatNV(
      type,
      stride);
  }

  public static boolean glIsBufferResidentNV(
    int target) {
    return gl().glIsBufferResidentNV(
      target);
  }

  public static boolean glIsEnabledi(
    int target,
    int index) {
    return gl().glIsEnabledi(
      target,
      index);
  }

  public static boolean glIsNamedBufferResidentNV(
    int buffer) {
    return gl().glIsNamedBufferResidentNV(
      buffer);
  }

  public static boolean glIsNamedStringARB(
    int namelen,
    String name) {
    return gl().glIsNamedStringARB(
      namelen,
      name);
  }

  public static boolean glIsProgramPipeline(
    int pipeline) {
    return gl().glIsProgramPipeline(
      pipeline);
  }

  public static boolean glIsSampler(
    int sampler) {
    return gl().glIsSampler(
      sampler);
  }

  public static boolean glIsTransformFeedback(
    int id) {
    return gl().glIsTransformFeedback(
      id);
  }

  public static boolean glIsVertexArray(
    int array) {
    return gl().glIsVertexArray(
      array);
  }

  public static void glMakeBufferNonResidentNV(
    int target) {
    gl().glMakeBufferNonResidentNV(
      target);
  }

  public static void glMakeBufferResidentNV(
    int target,
    int access) {
    gl().glMakeBufferResidentNV(
      target,
      access);
  }

  public static void glMakeNamedBufferNonResidentNV(
    int buffer) {
    gl().glMakeNamedBufferNonResidentNV(
      buffer);
  }

  public static void glMakeNamedBufferResidentNV(
    int buffer,
    int access) {
    gl().glMakeNamedBufferResidentNV(
      buffer,
      access);
  }

  public static ByteBuffer glMapBufferRange(
    int target,
    long offset,
    long length,
    int access) {
    return gl().glMapBufferRange(
      target,
      offset,
      length,
      access);
  }

  public static void glMemoryBarrier(
    int barriers) {
    gl().glMemoryBarrier(
      barriers);
  }

  public static void glMinSampleShading(
    float value) {
    gl().glMinSampleShading(
      value);
  }

  public static void glMultiDrawArrays(
    int mode,
    IntBuffer first,
    IntBuffer count,
    int primcount) {
    gl().glMultiDrawArrays(
      mode,
      first,
      count,
      primcount);
  }

  public static void glMultiDrawArrays(
    int mode,
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

  public static void glMultiDrawArraysIndirectAMD(
    int mode,
    Buffer indirect,
    int primcount,
    int stride) {
    gl().glMultiDrawArraysIndirectAMD(
      mode,
      indirect,
      primcount,
      stride);
  }

  public static void glMultiDrawElements(
    int mode,
    IntBuffer count,
    int type,
    PointerBuffer indices,
    int primcount) {
    gl().glMultiDrawElements(
      mode,
      count,
      type,
      indices,
      primcount);
  }

  public static void glMultiDrawElements(
    int mode,
    int[] count,
    int count_offset,
    int type,
    PointerBuffer indices,
    int primcount) {
    gl().glMultiDrawElements(
      mode,
      count,
      count_offset,
      type,
      indices,
      primcount);
  }

  public static void glMultiDrawElementsIndirectAMD(
    int mode,
    int type,
    Buffer indirect,
    int primcount,
    int stride) {
    gl().glMultiDrawElementsIndirectAMD(
      mode,
      type,
      indirect,
      primcount,
      stride);
  }

  public static void glMultiTexCoordP1ui(
    int texture,
    int type,
    int coords) {
    gl().glMultiTexCoordP1ui(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP1uiv(
    int texture,
    int type,
    IntBuffer coords) {
    gl().glMultiTexCoordP1uiv(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP1uiv(
    int texture,
    int type,
    int[] coords,
    int coords_offset) {
    gl().glMultiTexCoordP1uiv(
      texture,
      type,
      coords,
      coords_offset);
  }

  public static void glMultiTexCoordP2ui(
    int texture,
    int type,
    int coords) {
    gl().glMultiTexCoordP2ui(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP2uiv(
    int texture,
    int type,
    IntBuffer coords) {
    gl().glMultiTexCoordP2uiv(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP2uiv(
    int texture,
    int type,
    int[] coords,
    int coords_offset) {
    gl().glMultiTexCoordP2uiv(
      texture,
      type,
      coords,
      coords_offset);
  }

  public static void glMultiTexCoordP3ui(
    int texture,
    int type,
    int coords) {
    gl().glMultiTexCoordP3ui(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP3uiv(
    int texture,
    int type,
    IntBuffer coords) {
    gl().glMultiTexCoordP3uiv(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP3uiv(
    int texture,
    int type,
    int[] coords,
    int coords_offset) {
    gl().glMultiTexCoordP3uiv(
      texture,
      type,
      coords,
      coords_offset);
  }

  public static void glMultiTexCoordP4ui(
    int texture,
    int type,
    int coords) {
    gl().glMultiTexCoordP4ui(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP4uiv(
    int texture,
    int type,
    IntBuffer coords) {
    gl().glMultiTexCoordP4uiv(
      texture,
      type,
      coords);
  }

  public static void glMultiTexCoordP4uiv(
    int texture,
    int type,
    int[] coords,
    int coords_offset) {
    gl().glMultiTexCoordP4uiv(
      texture,
      type,
      coords,
      coords_offset);
  }

  public static void glNamedStringARB(
    int type,
    int namelen,
    String name,
    int stringlen,
    String string) {
    gl().glNamedStringARB(
      type,
      namelen,
      name,
      stringlen,
      string);
  }

  public static void glNormalFormatNV(
    int type,
    int stride) {
    gl().glNormalFormatNV(
      type,
      stride);
  }

  public static void glNormalP3ui(
    int type,
    int coords) {
    gl().glNormalP3ui(
      type,
      coords);
  }

  public static void glNormalP3uiv(
    int type,
    IntBuffer coords) {
    gl().glNormalP3uiv(
      type,
      coords);
  }

  public static void glNormalP3uiv(
    int type,
    int[] coords,
    int coords_offset) {
    gl().glNormalP3uiv(
      type,
      coords,
      coords_offset);
  }

  public static void glPauseTransformFeedback(
    ) {
    gl().glPauseTransformFeedback();
  }

  public static void glPixelStoref(
    int pname,
    float param) {
    gl().glPixelStoref(
      pname,
      param);
  }

  public static void glPointParameteri(
    int pname,
    int param) {
    gl().glPointParameteri(
      pname,
      param);
  }

  public static void glPointParameteriv(
    int pname,
    IntBuffer params) {
    gl().glPointParameteriv(
      pname,
      params);
  }

  public static void glPointParameteriv(
    int pname,
    int[] params,
    int params_offset) {
    gl().glPointParameteriv(
      pname,
      params,
      params_offset);
  }

  public static void glPolygonMode(
    int face,
    int mode) {
    gl().glPolygonMode(
      face,
      mode);
  }

  public static void glPrimitiveRestartIndex(
    int index) {
    gl().glPrimitiveRestartIndex(
      index);
  }

  public static void glProgramParameteri(
    int program,
    int pname,
    int value) {
    gl().glProgramParameteri(
      program,
      pname,
      value);
  }

  public static void glProgramParameteriARB(
    int program,
    int pname,
    int value) {
    gl().glProgramParameteriARB(
      program,
      pname,
      value);
  }

  public static void glProgramUniform1d(
    int program,
    int location,
    double v0) {
    gl().glProgramUniform1d(
      program,
      location,
      v0);
  }

  public static void glProgramUniform1dv(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform1dv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1dv(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform1dv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform1f(
    int program,
    int location,
    float v0) {
    gl().glProgramUniform1f(
      program,
      location,
      v0);
  }

  public static void glProgramUniform1fv(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform1fv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1fv(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform1fv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform1i(
    int program,
    int location,
    int v0) {
    gl().glProgramUniform1i(
      program,
      location,
      v0);
  }

  public static void glProgramUniform1iv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform1iv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1iv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform1iv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform1ui(
    int program,
    int location,
    int v0) {
    gl().glProgramUniform1ui(
      program,
      location,
      v0);
  }

  public static void glProgramUniform1uiv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform1uiv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform1uiv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform1uiv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2d(
    int program,
    int location,
    double v0,
    double v1) {
    gl().glProgramUniform2d(
      program,
      location,
      v0,
      v1);
  }

  public static void glProgramUniform2dv(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform2dv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2dv(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform2dv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2f(
    int program,
    int location,
    float v0,
    float v1) {
    gl().glProgramUniform2f(
      program,
      location,
      v0,
      v1);
  }

  public static void glProgramUniform2fv(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform2fv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2fv(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform2fv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2i(
    int program,
    int location,
    int v0,
    int v1) {
    gl().glProgramUniform2i(
      program,
      location,
      v0,
      v1);
  }

  public static void glProgramUniform2iv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform2iv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2iv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform2iv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform2ui(
    int program,
    int location,
    int v0,
    int v1) {
    gl().glProgramUniform2ui(
      program,
      location,
      v0,
      v1);
  }

  public static void glProgramUniform2uiv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform2uiv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform2uiv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform2uiv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3d(
    int program,
    int location,
    double v0,
    double v1,
    double v2) {
    gl().glProgramUniform3d(
      program,
      location,
      v0,
      v1,
      v2);
  }

  public static void glProgramUniform3dv(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform3dv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3dv(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform3dv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3f(
    int program,
    int location,
    float v0,
    float v1,
    float v2) {
    gl().glProgramUniform3f(
      program,
      location,
      v0,
      v1,
      v2);
  }

  public static void glProgramUniform3fv(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform3fv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3fv(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform3fv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3i(
    int program,
    int location,
    int v0,
    int v1,
    int v2) {
    gl().glProgramUniform3i(
      program,
      location,
      v0,
      v1,
      v2);
  }

  public static void glProgramUniform3iv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform3iv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3iv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform3iv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform3ui(
    int program,
    int location,
    int v0,
    int v1,
    int v2) {
    gl().glProgramUniform3ui(
      program,
      location,
      v0,
      v1,
      v2);
  }

  public static void glProgramUniform3uiv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform3uiv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform3uiv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform3uiv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4d(
    int program,
    int location,
    double v0,
    double v1,
    double v2,
    double v3) {
    gl().glProgramUniform4d(
      program,
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glProgramUniform4dv(
    int program,
    int location,
    int count,
    DoubleBuffer value) {
    gl().glProgramUniform4dv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4dv(
    int program,
    int location,
    int count,
    double[] value,
    int value_offset) {
    gl().glProgramUniform4dv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4f(
    int program,
    int location,
    float v0,
    float v1,
    float v2,
    float v3) {
    gl().glProgramUniform4f(
      program,
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glProgramUniform4fv(
    int program,
    int location,
    int count,
    FloatBuffer value) {
    gl().glProgramUniform4fv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4fv(
    int program,
    int location,
    int count,
    float[] value,
    int value_offset) {
    gl().glProgramUniform4fv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4i(
    int program,
    int location,
    int v0,
    int v1,
    int v2,
    int v3) {
    gl().glProgramUniform4i(
      program,
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glProgramUniform4iv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform4iv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4iv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform4iv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniform4ui(
    int program,
    int location,
    int v0,
    int v1,
    int v2,
    int v3) {
    gl().glProgramUniform4ui(
      program,
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glProgramUniform4uiv(
    int program,
    int location,
    int count,
    IntBuffer value) {
    gl().glProgramUniform4uiv(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniform4uiv(
    int program,
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glProgramUniform4uiv(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix2dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix2fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x3dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix2x3dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x3dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x3dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x3fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix2x3fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x3fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x3fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x4dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix2x4dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x4dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x4dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix2x4fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix2x4fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix2x4fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix2x4fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix3dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix3fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x2dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix3x2dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x2dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x2dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x2fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix3x2fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x2fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x2fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x4dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix3x4dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x4dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x4dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix3x4fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix3x4fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix3x4fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix3x4fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix4dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix4fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x2dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix4x2dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x2dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x2dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x2fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix4x2fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x2fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x2fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x3dv(
    int program,
    int location,
    int count,
    boolean transpose,
    DoubleBuffer value) {
    gl().glProgramUniformMatrix4x3dv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x3dv(
    int program,
    int location,
    int count,
    boolean transpose,
    double[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x3dv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformMatrix4x3fv(
    int program,
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glProgramUniformMatrix4x3fv(
      program,
      location,
      count,
      transpose,
      value);
  }

  public static void glProgramUniformMatrix4x3fv(
    int program,
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glProgramUniformMatrix4x3fv(
      program,
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glProgramUniformui64NV(
    int program,
    int location,
    long value) {
    gl().glProgramUniformui64NV(
      program,
      location,
      value);
  }

  public static void glProgramUniformui64vNV(
    int program,
    int location,
    int count,
    LongBuffer value) {
    gl().glProgramUniformui64vNV(
      program,
      location,
      count,
      value);
  }

  public static void glProgramUniformui64vNV(
    int program,
    int location,
    int count,
    long[] value,
    int value_offset) {
    gl().glProgramUniformui64vNV(
      program,
      location,
      count,
      value,
      value_offset);
  }

  public static void glProvokingVertex(
    int mode) {
    gl().glProvokingVertex(
      mode);
  }

  public static void glQueryCounter(
    int id,
    int target) {
    gl().glQueryCounter(
      id,
      target);
  }

  public static void glReadBuffer(
    int mode) {
    gl().glReadBuffer(
      mode);
  }

  public static void glRenderbufferStorageMultisample(
    int target,
    int samples,
    int internalformat,
    int width,
    int height) {
    gl().glRenderbufferStorageMultisample(
      target,
      samples,
      internalformat,
      width,
      height);
  }

  public static void glResumeTransformFeedback(
    ) {
    gl().glResumeTransformFeedback();
  }

  public static void glSampleMaski(
    int index,
    int mask) {
    gl().glSampleMaski(
      index,
      mask);
  }

  public static void glSamplerParameterIiv(
    int sampler,
    int pname,
    IntBuffer param) {
    gl().glSamplerParameterIiv(
      sampler,
      pname,
      param);
  }

  public static void glSamplerParameterIiv(
    int sampler,
    int pname,
    int[] param,
    int param_offset) {
    gl().glSamplerParameterIiv(
      sampler,
      pname,
      param,
      param_offset);
  }

  public static void glSamplerParameterIuiv(
    int sampler,
    int pname,
    IntBuffer param) {
    gl().glSamplerParameterIuiv(
      sampler,
      pname,
      param);
  }

  public static void glSamplerParameterIuiv(
    int sampler,
    int pname,
    int[] param,
    int param_offset) {
    gl().glSamplerParameterIuiv(
      sampler,
      pname,
      param,
      param_offset);
  }

  public static void glSamplerParameterf(
    int sampler,
    int pname,
    float param) {
    gl().glSamplerParameterf(
      sampler,
      pname,
      param);
  }

  public static void glSamplerParameterfv(
    int sampler,
    int pname,
    FloatBuffer param) {
    gl().glSamplerParameterfv(
      sampler,
      pname,
      param);
  }

  public static void glSamplerParameterfv(
    int sampler,
    int pname,
    float[] param,
    int param_offset) {
    gl().glSamplerParameterfv(
      sampler,
      pname,
      param,
      param_offset);
  }

  public static void glSamplerParameteri(
    int sampler,
    int pname,
    int param) {
    gl().glSamplerParameteri(
      sampler,
      pname,
      param);
  }

  public static void glSamplerParameteriv(
    int sampler,
    int pname,
    IntBuffer param) {
    gl().glSamplerParameteriv(
      sampler,
      pname,
      param);
  }

  public static void glSamplerParameteriv(
    int sampler,
    int pname,
    int[] param,
    int param_offset) {
    gl().glSamplerParameteriv(
      sampler,
      pname,
      param,
      param_offset);
  }

  public static void glScissorArrayv(
    int first,
    int count,
    IntBuffer v) {
    gl().glScissorArrayv(
      first,
      count,
      v);
  }

  public static void glScissorArrayv(
    int first,
    int count,
    int[] v,
    int v_offset) {
    gl().glScissorArrayv(
      first,
      count,
      v,
      v_offset);
  }

  public static void glScissorIndexed(
    int index,
    int left,
    int bottom,
    int width,
    int height) {
    gl().glScissorIndexed(
      index,
      left,
      bottom,
      width,
      height);
  }

  public static void glScissorIndexedv(
    int index,
    IntBuffer v) {
    gl().glScissorIndexedv(
      index,
      v);
  }

  public static void glScissorIndexedv(
    int index,
    int[] v,
    int v_offset) {
    gl().glScissorIndexedv(
      index,
      v,
      v_offset);
  }

  public static void glSecondaryColorFormatNV(
    int size,
    int type,
    int stride) {
    gl().glSecondaryColorFormatNV(
      size,
      type,
      stride);
  }

  public static void glSecondaryColorP3ui(
    int type,
    int color) {
    gl().glSecondaryColorP3ui(
      type,
      color);
  }

  public static void glSecondaryColorP3uiv(
    int type,
    IntBuffer color) {
    gl().glSecondaryColorP3uiv(
      type,
      color);
  }

  public static void glSecondaryColorP3uiv(
    int type,
    int[] color,
    int color_offset) {
    gl().glSecondaryColorP3uiv(
      type,
      color,
      color_offset);
  }

  public static void glSetMultisamplefvAMD(
    int pname,
    int index,
    FloatBuffer val) {
    gl().glSetMultisamplefvAMD(
      pname,
      index,
      val);
  }

  public static void glSetMultisamplefvAMD(
    int pname,
    int index,
    float[] val,
    int val_offset) {
    gl().glSetMultisamplefvAMD(
      pname,
      index,
      val,
      val_offset);
  }

  public static void glStencilOpValueAMD(
    int face,
    int value) {
    gl().glStencilOpValueAMD(
      face,
      value);
  }

  public static void glTessellationFactorAMD(
    float factor) {
    gl().glTessellationFactorAMD(
      factor);
  }

  public static void glTessellationModeAMD(
    int mode) {
    gl().glTessellationModeAMD(
      mode);
  }

  public static void glTexBuffer(
    int target,
    int internalformat,
    int buffer) {
    gl().glTexBuffer(
      target,
      internalformat,
      buffer);
  }

  public static void glTexCoordFormatNV(
    int size,
    int type,
    int stride) {
    gl().glTexCoordFormatNV(
      size,
      type,
      stride);
  }

  public static void glTexCoordP1ui(
    int type,
    int coords) {
    gl().glTexCoordP1ui(
      type,
      coords);
  }

  public static void glTexCoordP1uiv(
    int type,
    IntBuffer coords) {
    gl().glTexCoordP1uiv(
      type,
      coords);
  }

  public static void glTexCoordP1uiv(
    int type,
    int[] coords,
    int coords_offset) {
    gl().glTexCoordP1uiv(
      type,
      coords,
      coords_offset);
  }

  public static void glTexCoordP2ui(
    int type,
    int coords) {
    gl().glTexCoordP2ui(
      type,
      coords);
  }

  public static void glTexCoordP2uiv(
    int type,
    IntBuffer coords) {
    gl().glTexCoordP2uiv(
      type,
      coords);
  }

  public static void glTexCoordP2uiv(
    int type,
    int[] coords,
    int coords_offset) {
    gl().glTexCoordP2uiv(
      type,
      coords,
      coords_offset);
  }

  public static void glTexCoordP3ui(
    int type,
    int coords) {
    gl().glTexCoordP3ui(
      type,
      coords);
  }

  public static void glTexCoordP3uiv(
    int type,
    IntBuffer coords) {
    gl().glTexCoordP3uiv(
      type,
      coords);
  }

  public static void glTexCoordP3uiv(
    int type,
    int[] coords,
    int coords_offset) {
    gl().glTexCoordP3uiv(
      type,
      coords,
      coords_offset);
  }

  public static void glTexCoordP4ui(
    int type,
    int coords) {
    gl().glTexCoordP4ui(
      type,
      coords);
  }

  public static void glTexCoordP4uiv(
    int type,
    IntBuffer coords) {
    gl().glTexCoordP4uiv(
      type,
      coords);
  }

  public static void glTexCoordP4uiv(
    int type,
    int[] coords,
    int coords_offset) {
    gl().glTexCoordP4uiv(
      type,
      coords,
      coords_offset);
  }

  public static void glTexImage1D(
    int target,
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

  public static void glTexImage1D(
    int target,
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

  public static void glTexImage2DMultisample(
    int target,
    int samples,
    int internalformat,
    int width,
    int height,
    boolean fixedsamplelocations) {
    gl().glTexImage2DMultisample(
      target,
      samples,
      internalformat,
      width,
      height,
      fixedsamplelocations);
  }

  public static void glTexImage2DMultisampleCoverageNV(
    int target,
    int coverageSamples,
    int colorSamples,
    int internalFormat,
    int width,
    int height,
    boolean fixedSampleLocations) {
    gl().glTexImage2DMultisampleCoverageNV(
      target,
      coverageSamples,
      colorSamples,
      internalFormat,
      width,
      height,
      fixedSampleLocations);
  }

  public static void glTexImage3DMultisample(
    int target,
    int samples,
    int internalformat,
    int width,
    int height,
    int depth,
    boolean fixedsamplelocations) {
    gl().glTexImage3DMultisample(
      target,
      samples,
      internalformat,
      width,
      height,
      depth,
      fixedsamplelocations);
  }

  public static void glTexImage3DMultisampleCoverageNV(
    int target,
    int coverageSamples,
    int colorSamples,
    int internalFormat,
    int width,
    int height,
    int depth,
    boolean fixedSampleLocations) {
    gl().glTexImage3DMultisampleCoverageNV(
      target,
      coverageSamples,
      colorSamples,
      internalFormat,
      width,
      height,
      depth,
      fixedSampleLocations);
  }

  public static void glTexParameterIiv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glTexParameterIiv(
      target,
      pname,
      params);
  }

  public static void glTexParameterIiv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTexParameterIiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexParameterIuiv(
    int target,
    int pname,
    IntBuffer params) {
    gl().glTexParameterIuiv(
      target,
      pname,
      params);
  }

  public static void glTexParameterIuiv(
    int target,
    int pname,
    int[] params,
    int params_offset) {
    gl().glTexParameterIuiv(
      target,
      pname,
      params,
      params_offset);
  }

  public static void glTexSubImage1D(
    int target,
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

  public static void glTexSubImage1D(
    int target,
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

  public static void glTextureImage2DMultisampleCoverageNV(
    int texture,
    int target,
    int coverageSamples,
    int colorSamples,
    int internalFormat,
    int width,
    int height,
    boolean fixedSampleLocations) {
    gl().glTextureImage2DMultisampleCoverageNV(
      texture,
      target,
      coverageSamples,
      colorSamples,
      internalFormat,
      width,
      height,
      fixedSampleLocations);
  }

  public static void glTextureImage2DMultisampleNV(
    int texture,
    int target,
    int samples,
    int internalFormat,
    int width,
    int height,
    boolean fixedSampleLocations) {
    gl().glTextureImage2DMultisampleNV(
      texture,
      target,
      samples,
      internalFormat,
      width,
      height,
      fixedSampleLocations);
  }

  public static void glTextureImage3DMultisampleCoverageNV(
    int texture,
    int target,
    int coverageSamples,
    int colorSamples,
    int internalFormat,
    int width,
    int height,
    int depth,
    boolean fixedSampleLocations) {
    gl().glTextureImage3DMultisampleCoverageNV(
      texture,
      target,
      coverageSamples,
      colorSamples,
      internalFormat,
      width,
      height,
      depth,
      fixedSampleLocations);
  }

  public static void glTextureImage3DMultisampleNV(
    int texture,
    int target,
    int samples,
    int internalFormat,
    int width,
    int height,
    int depth,
    boolean fixedSampleLocations) {
    gl().glTextureImage3DMultisampleNV(
      texture,
      target,
      samples,
      internalFormat,
      width,
      height,
      depth,
      fixedSampleLocations);
  }

  public static void glTransformFeedbackVaryings(
    int program,
    int count,
    String[] varyings,
    int bufferMode) {
    gl().glTransformFeedbackVaryings(
      program,
      count,
      varyings,
      bufferMode);
  }

  public static void glUniform1ui(
    int location,
    int v0) {
    gl().glUniform1ui(
      location,
      v0);
  }

  public static void glUniform1uiv(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform1uiv(
      location,
      count,
      value);
  }

  public static void glUniform1uiv(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform1uiv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform2ui(
    int location,
    int v0,
    int v1) {
    gl().glUniform2ui(
      location,
      v0,
      v1);
  }

  public static void glUniform2uiv(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform2uiv(
      location,
      count,
      value);
  }

  public static void glUniform2uiv(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform2uiv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform3ui(
    int location,
    int v0,
    int v1,
    int v2) {
    gl().glUniform3ui(
      location,
      v0,
      v1,
      v2);
  }

  public static void glUniform3uiv(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform3uiv(
      location,
      count,
      value);
  }

  public static void glUniform3uiv(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform3uiv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniform4ui(
    int location,
    int v0,
    int v1,
    int v2,
    int v3) {
    gl().glUniform4ui(
      location,
      v0,
      v1,
      v2,
      v3);
  }

  public static void glUniform4uiv(
    int location,
    int count,
    IntBuffer value) {
    gl().glUniform4uiv(
      location,
      count,
      value);
  }

  public static void glUniform4uiv(
    int location,
    int count,
    int[] value,
    int value_offset) {
    gl().glUniform4uiv(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUniformBlockBinding(
    int program,
    int uniformBlockIndex,
    int uniformBlockBinding) {
    gl().glUniformBlockBinding(
      program,
      uniformBlockIndex,
      uniformBlockBinding);
  }

  public static void glUniformMatrix2x3fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix2x3fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix2x3fv(
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glUniformMatrix2x3fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix2x4fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix2x4fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix2x4fv(
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glUniformMatrix2x4fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix3x2fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix3x2fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix3x2fv(
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glUniformMatrix3x2fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix3x4fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix3x4fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix3x4fv(
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glUniformMatrix3x4fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix4x2fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix4x2fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix4x2fv(
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glUniformMatrix4x2fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformMatrix4x3fv(
    int location,
    int count,
    boolean transpose,
    FloatBuffer value) {
    gl().glUniformMatrix4x3fv(
      location,
      count,
      transpose,
      value);
  }

  public static void glUniformMatrix4x3fv(
    int location,
    int count,
    boolean transpose,
    float[] value,
    int value_offset) {
    gl().glUniformMatrix4x3fv(
      location,
      count,
      transpose,
      value,
      value_offset);
  }

  public static void glUniformSubroutinesuiv(
    int shadertype,
    int count,
    IntBuffer indices) {
    gl().glUniformSubroutinesuiv(
      shadertype,
      count,
      indices);
  }

  public static void glUniformSubroutinesuiv(
    int shadertype,
    int count,
    int[] indices,
    int indices_offset) {
    gl().glUniformSubroutinesuiv(
      shadertype,
      count,
      indices,
      indices_offset);
  }

  public static void glUniformui64NV(
    int location,
    long value) {
    gl().glUniformui64NV(
      location,
      value);
  }

  public static void glUniformui64vNV(
    int location,
    int count,
    LongBuffer value) {
    gl().glUniformui64vNV(
      location,
      count,
      value);
  }

  public static void glUniformui64vNV(
    int location,
    int count,
    long[] value,
    int value_offset) {
    gl().glUniformui64vNV(
      location,
      count,
      value,
      value_offset);
  }

  public static void glUseProgramStages(
    int pipeline,
    int stages,
    int program) {
    gl().glUseProgramStages(
      pipeline,
      stages,
      program);
  }

  public static void glValidateProgramPipeline(
    int pipeline) {
    gl().glValidateProgramPipeline(
      pipeline);
  }

  public static void glVertexAttrib1d(
    int index,
    double x) {
    gl().glVertexAttrib1d(
      index,
      x);
  }

  public static void glVertexAttrib1dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib1dv(
      index,
      v);
  }

  public static void glVertexAttrib1dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib1dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib1s(
    int index,
    short x) {
    gl().glVertexAttrib1s(
      index,
      x);
  }

  public static void glVertexAttrib1sv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib1sv(
      index,
      v);
  }

  public static void glVertexAttrib1sv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib1sv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib2d(
    int index,
    double x,
    double y) {
    gl().glVertexAttrib2d(
      index,
      x,
      y);
  }

  public static void glVertexAttrib2dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib2dv(
      index,
      v);
  }

  public static void glVertexAttrib2dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib2dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib2s(
    int index,
    short x,
    short y) {
    gl().glVertexAttrib2s(
      index,
      x,
      y);
  }

  public static void glVertexAttrib2sv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib2sv(
      index,
      v);
  }

  public static void glVertexAttrib2sv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib2sv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib3d(
    int index,
    double x,
    double y,
    double z) {
    gl().glVertexAttrib3d(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttrib3dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib3dv(
      index,
      v);
  }

  public static void glVertexAttrib3dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib3dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib3s(
    int index,
    short x,
    short y,
    short z) {
    gl().glVertexAttrib3s(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttrib3sv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib3sv(
      index,
      v);
  }

  public static void glVertexAttrib3sv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib3sv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nbv(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4Nbv(
      index,
      v);
  }

  public static void glVertexAttrib4Nbv(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4Nbv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Niv(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4Niv(
      index,
      v);
  }

  public static void glVertexAttrib4Niv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4Niv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nsv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4Nsv(
      index,
      v);
  }

  public static void glVertexAttrib4Nsv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4Nsv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nub(
    int index,
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

  public static void glVertexAttrib4Nubv(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4Nubv(
      index,
      v);
  }

  public static void glVertexAttrib4Nubv(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4Nubv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nuiv(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4Nuiv(
      index,
      v);
  }

  public static void glVertexAttrib4Nuiv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4Nuiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4Nusv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4Nusv(
      index,
      v);
  }

  public static void glVertexAttrib4Nusv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4Nusv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4bv(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4bv(
      index,
      v);
  }

  public static void glVertexAttrib4bv(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4bv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4d(
    int index,
    double x,
    double y,
    double z,
    double w) {
    gl().glVertexAttrib4d(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttrib4dv(
      index,
      v);
  }

  public static void glVertexAttrib4dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttrib4dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4iv(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4iv(
      index,
      v);
  }

  public static void glVertexAttrib4iv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4iv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4s(
    int index,
    short x,
    short y,
    short z,
    short w) {
    gl().glVertexAttrib4s(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttrib4sv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4sv(
      index,
      v);
  }

  public static void glVertexAttrib4sv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4sv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4ubv(
    int index,
    ByteBuffer v) {
    gl().glVertexAttrib4ubv(
      index,
      v);
  }

  public static void glVertexAttrib4ubv(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttrib4ubv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4uiv(
    int index,
    IntBuffer v) {
    gl().glVertexAttrib4uiv(
      index,
      v);
  }

  public static void glVertexAttrib4uiv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttrib4uiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttrib4usv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttrib4usv(
      index,
      v);
  }

  public static void glVertexAttrib4usv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttrib4usv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribFormatNV(
    int index,
    int size,
    int type,
    boolean normalized,
    int stride) {
    gl().glVertexAttribFormatNV(
      index,
      size,
      type,
      normalized,
      stride);
  }

  public static void glVertexAttribI1i(
    int index,
    int x) {
    gl().glVertexAttribI1i(
      index,
      x);
  }

  public static void glVertexAttribI1iv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI1iv(
      index,
      v);
  }

  public static void glVertexAttribI1iv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI1iv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI1ui(
    int index,
    int x) {
    gl().glVertexAttribI1ui(
      index,
      x);
  }

  public static void glVertexAttribI1uiv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI1uiv(
      index,
      v);
  }

  public static void glVertexAttribI1uiv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI1uiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI2i(
    int index,
    int x,
    int y) {
    gl().glVertexAttribI2i(
      index,
      x,
      y);
  }

  public static void glVertexAttribI2iv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI2iv(
      index,
      v);
  }

  public static void glVertexAttribI2iv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI2iv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI2ui(
    int index,
    int x,
    int y) {
    gl().glVertexAttribI2ui(
      index,
      x,
      y);
  }

  public static void glVertexAttribI2uiv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI2uiv(
      index,
      v);
  }

  public static void glVertexAttribI2uiv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI2uiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI3i(
    int index,
    int x,
    int y,
    int z) {
    gl().glVertexAttribI3i(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttribI3iv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI3iv(
      index,
      v);
  }

  public static void glVertexAttribI3iv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI3iv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI3ui(
    int index,
    int x,
    int y,
    int z) {
    gl().glVertexAttribI3ui(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttribI3uiv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI3uiv(
      index,
      v);
  }

  public static void glVertexAttribI3uiv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI3uiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4bv(
    int index,
    ByteBuffer v) {
    gl().glVertexAttribI4bv(
      index,
      v);
  }

  public static void glVertexAttribI4bv(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttribI4bv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4i(
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glVertexAttribI4i(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttribI4iv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI4iv(
      index,
      v);
  }

  public static void glVertexAttribI4iv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI4iv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4sv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttribI4sv(
      index,
      v);
  }

  public static void glVertexAttribI4sv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttribI4sv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4ubv(
    int index,
    ByteBuffer v) {
    gl().glVertexAttribI4ubv(
      index,
      v);
  }

  public static void glVertexAttribI4ubv(
    int index,
    byte[] v,
    int v_offset) {
    gl().glVertexAttribI4ubv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4ui(
    int index,
    int x,
    int y,
    int z,
    int w) {
    gl().glVertexAttribI4ui(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttribI4uiv(
    int index,
    IntBuffer v) {
    gl().glVertexAttribI4uiv(
      index,
      v);
  }

  public static void glVertexAttribI4uiv(
    int index,
    int[] v,
    int v_offset) {
    gl().glVertexAttribI4uiv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribI4usv(
    int index,
    ShortBuffer v) {
    gl().glVertexAttribI4usv(
      index,
      v);
  }

  public static void glVertexAttribI4usv(
    int index,
    short[] v,
    int v_offset) {
    gl().glVertexAttribI4usv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribIFormatNV(
    int index,
    int size,
    int type,
    int stride) {
    gl().glVertexAttribIFormatNV(
      index,
      size,
      type,
      stride);
  }

  public static void glVertexAttribIPointer(
    int index,
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glVertexAttribIPointer(
      index,
      size,
      type,
      stride,
      pointer);
  }

  public static void glVertexAttribL1d(
    int index,
    double x) {
    gl().glVertexAttribL1d(
      index,
      x);
  }

  public static void glVertexAttribL1dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttribL1dv(
      index,
      v);
  }

  public static void glVertexAttribL1dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttribL1dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL2d(
    int index,
    double x,
    double y) {
    gl().glVertexAttribL2d(
      index,
      x,
      y);
  }

  public static void glVertexAttribL2dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttribL2dv(
      index,
      v);
  }

  public static void glVertexAttribL2dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttribL2dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL3d(
    int index,
    double x,
    double y,
    double z) {
    gl().glVertexAttribL3d(
      index,
      x,
      y,
      z);
  }

  public static void glVertexAttribL3dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttribL3dv(
      index,
      v);
  }

  public static void glVertexAttribL3dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttribL3dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribL4d(
    int index,
    double x,
    double y,
    double z,
    double w) {
    gl().glVertexAttribL4d(
      index,
      x,
      y,
      z,
      w);
  }

  public static void glVertexAttribL4dv(
    int index,
    DoubleBuffer v) {
    gl().glVertexAttribL4dv(
      index,
      v);
  }

  public static void glVertexAttribL4dv(
    int index,
    double[] v,
    int v_offset) {
    gl().glVertexAttribL4dv(
      index,
      v,
      v_offset);
  }

  public static void glVertexAttribLPointer(
    int index,
    int size,
    int type,
    int stride,
    Buffer pointer) {
    gl().glVertexAttribLPointer(
      index,
      size,
      type,
      stride,
      pointer);
  }

  public static void glVertexAttribP1ui(
    int index,
    int type,
    boolean normalized,
    int value) {
    gl().glVertexAttribP1ui(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP1uiv(
    int index,
    int type,
    boolean normalized,
    IntBuffer value) {
    gl().glVertexAttribP1uiv(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP1uiv(
    int index,
    int type,
    boolean normalized,
    int[] value,
    int value_offset) {
    gl().glVertexAttribP1uiv(
      index,
      type,
      normalized,
      value,
      value_offset);
  }

  public static void glVertexAttribP2ui(
    int index,
    int type,
    boolean normalized,
    int value) {
    gl().glVertexAttribP2ui(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP2uiv(
    int index,
    int type,
    boolean normalized,
    IntBuffer value) {
    gl().glVertexAttribP2uiv(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP2uiv(
    int index,
    int type,
    boolean normalized,
    int[] value,
    int value_offset) {
    gl().glVertexAttribP2uiv(
      index,
      type,
      normalized,
      value,
      value_offset);
  }

  public static void glVertexAttribP3ui(
    int index,
    int type,
    boolean normalized,
    int value) {
    gl().glVertexAttribP3ui(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP3uiv(
    int index,
    int type,
    boolean normalized,
    IntBuffer value) {
    gl().glVertexAttribP3uiv(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP3uiv(
    int index,
    int type,
    boolean normalized,
    int[] value,
    int value_offset) {
    gl().glVertexAttribP3uiv(
      index,
      type,
      normalized,
      value,
      value_offset);
  }

  public static void glVertexAttribP4ui(
    int index,
    int type,
    boolean normalized,
    int value) {
    gl().glVertexAttribP4ui(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP4uiv(
    int index,
    int type,
    boolean normalized,
    IntBuffer value) {
    gl().glVertexAttribP4uiv(
      index,
      type,
      normalized,
      value);
  }

  public static void glVertexAttribP4uiv(
    int index,
    int type,
    boolean normalized,
    int[] value,
    int value_offset) {
    gl().glVertexAttribP4uiv(
      index,
      type,
      normalized,
      value,
      value_offset);
  }

  public static void glVertexFormatNV(
    int size,
    int type,
    int stride) {
    gl().glVertexFormatNV(
      size,
      type,
      stride);
  }

  public static void glVertexP2ui(
    int type,
    int value) {
    gl().glVertexP2ui(
      type,
      value);
  }

  public static void glVertexP2uiv(
    int type,
    IntBuffer value) {
    gl().glVertexP2uiv(
      type,
      value);
  }

  public static void glVertexP2uiv(
    int type,
    int[] value,
    int value_offset) {
    gl().glVertexP2uiv(
      type,
      value,
      value_offset);
  }

  public static void glVertexP3ui(
    int type,
    int value) {
    gl().glVertexP3ui(
      type,
      value);
  }

  public static void glVertexP3uiv(
    int type,
    IntBuffer value) {
    gl().glVertexP3uiv(
      type,
      value);
  }

  public static void glVertexP3uiv(
    int type,
    int[] value,
    int value_offset) {
    gl().glVertexP3uiv(
      type,
      value,
      value_offset);
  }

  public static void glVertexP4ui(
    int type,
    int value) {
    gl().glVertexP4ui(
      type,
      value);
  }

  public static void glVertexP4uiv(
    int type,
    IntBuffer value) {
    gl().glVertexP4uiv(
      type,
      value);
  }

  public static void glVertexP4uiv(
    int type,
    int[] value,
    int value_offset) {
    gl().glVertexP4uiv(
      type,
      value,
      value_offset);
  }

  public static void glViewportArrayv(
    int first,
    int count,
    FloatBuffer v) {
    gl().glViewportArrayv(
      first,
      count,
      v);
  }

  public static void glViewportArrayv(
    int first,
    int count,
    float[] v,
    int v_offset) {
    gl().glViewportArrayv(
      first,
      count,
      v,
      v_offset);
  }

  public static void glViewportIndexedf(
    int index,
    float x,
    float y,
    float w,
    float h) {
    gl().glViewportIndexedf(
      index,
      x,
      y,
      w,
      h);
  }

  public static void glViewportIndexedfv(
    int index,
    FloatBuffer v) {
    gl().glViewportIndexedfv(
      index,
      v);
  }

  public static void glViewportIndexedfv(
    int index,
    float[] v,
    int v_offset) {
    gl().glViewportIndexedfv(
      index,
      v,
      v_offset);
  }

  public static ByteBuffer glAllocateMemoryNV(
    int arg0,
    float arg1,
    float arg2,
    float arg3) {
    return gl().glAllocateMemoryNV(
      arg0,
      arg1,
      arg2,
      arg3);
  }

  public static boolean isExtensionAvailable(String extensionName) {
    return gl().isExtensionAvailable(extensionName);
  }

  public static void setSwapInterval(int interval) {
    gl().setSwapInterval(interval);
  }

  private static GL2 gl() {
    return (GL2)GLContext.getCurrentGL();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Gl() {
  }
}
