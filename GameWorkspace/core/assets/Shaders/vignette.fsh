in vec4 v_color;
in vec2 v_texCoord0;
in vec4 fragColor;

uniform vec2 u_resolution;
uniform sampler2D u_sampler2D;
uniform float innerRadius;
uniform float intensity;

float outerRadius = 0.7;

void main() {
	vec4 color = texture2D(u_sampler2D, v_texCoord0) * v_color;
	vec2 relativePosition = gl_FragCoord.xy / u_resolution - .5;
	// relativePosition.x *= u_resolution.x / u_resolution.y;
	float len = length(relativePosition);
	float vignette = smoothstep(outerRadius, innerRadius, len);
	color.rgb = mix(color.rgb, color.rgb * vignette, intensity);

	gl_FragColor = color;
}